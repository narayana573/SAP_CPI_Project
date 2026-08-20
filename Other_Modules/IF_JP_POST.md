# IFlow Design Document (v2 — Simplified Pass-Through)

## IF_JEPOST_DATAIQ2ECC_ME

**Process:** Journal Entry Posting — DataIQ to SAP ECC (Middle East)
**Author:** Surya | **Project:** Boomi-to-CPI Migration | **Client:** LTIMindtree
**Version:** v2.0 | **Date:** 20-Aug-2026

**Change from v1:** CPI does **not** read, split, validate, or route by company code. The CSV file is picked up as-is and placed in the AL11 folder untouched. The ABAP program on SAP ECC reads the file and determines the company code and posting logic itself. This makes IFlow 1 a **pure file-relay (pass-through) integration**.

**Change from v2:** File arrival timing is irregular — payroll runs monthly, and DataIQ typically drops the file somewhere in the D-5 to D-3 window before month-end, with no fixed date/time. Polling strategy updated to a **Quartz Cron schedule active only during the expected window**, rather than continuous all-month polling. See Section 2A.

---

## 1. Business Scenario

DataIQ (Power Automate + AWS) prepares a standard General Entry CSV covering all 5 Middle East entities and drops it into an SFTP outbound folder. CPI's only job is to detect the file, move it to the SAP ECC AL11 inbound directory, and log the transaction — no content inspection is required in this flow. The ABAP background job on ECC reads the file, identifies company codes internally, and posts the JEs via BAPI.

---

## 2. Why Sender Polling (not Poll Enrich)

**Poll Enrich** is a content-enrichment EIP pattern — used mid-flow, after some other trigger (Timer, HTTPS call, etc.) has already started the integration, to pull in *additional* data as one input among several. It does not itself act as a flow trigger.

For a "watch a folder → relay the file" scenario, the **Sender SFTP Adapter in polling mode** is the correct and standard pattern because it natively provides:

| Capability | Sender SFTP Polling | Poll Enrich |
|---|---|---|
| Acts as flow trigger | Yes | No — needs Timer/other trigger first |
| File locking during read | Built-in | Manual handling needed |
| Move to archive/error folder | Built-in (post-processing action) | Must be scripted separately |
| Duplicate file prevention | Built-in | Must be scripted separately |
| Best suited for | Direct file-to-file relay (this case) | Pulling supplementary data mid-flow, multi-source enrichment |

**Recommendation:** Keep Sender SFTP Polling. Poll Enrich would only be worth introducing if a future requirement needs CPI to conditionally check for a control/trigger file, or combine data from more than one source before proceeding — not applicable here.

---

## 2A. Scheduling Strategy for Irregular Monthly File Arrival

**Problem:** File arrival has no fixed date/time — it lands somewhere between D-5 and D-3 of the payroll cycle (occasionally D-4 or other nearby days), and this only happens once a month. A flat "poll every 5 minutes, all month" approach works but is wasteful and makes it hard to define what "late" even means.

**Recommended approach: Quartz Cron Expression on the Sender SFTP Adapter's Schedule tab**

Instead of a simple recurring interval, configure the Sender SFTP Adapter's schedule to only activate during the expected window:

| Setting | Recommendation |
|---|---|
| Active window | Last 7 calendar days of the month (covers D-5 to D-3 with buffer on both sides) |
| Poll frequency within window | Every 15–20 minutes |
| Outside window | Adapter does not poll — no connections, no log noise |
| Schedule type | Quartz Cron Expression (CPI's Sender SFTP Adapter supports this natively under the Schedule configuration) |

Example Quartz cron (adjust exact days once the real cutoff is confirmed):
```
0 */15 * 24-31 * ?
```
*(polls every 15 minutes, on calendar days 24–31 of every month — refine once the exact expected window is confirmed with DataIQ/payroll team)*

**Why this is the standard pattern for monthly/irregular batch files:**
- Doesn't matter which specific day within the window the file lands — CPI picks it up on the next poll cycle regardless of D-5, D-4, or D-3
- Avoids running an integration touchpoint 25+ days a month for nothing
- Gives you a clean, defined window to build monitoring/alerting against

**Recommended safeguard — "file not received" alert:** Since this is a payroll-critical monthly process, don't rely on silent waiting. Add a check (e.g., a separate scheduled flow or CPI's own monitoring/alerting) that fires a notification if the expected file has **not** arrived by a defined cutoff — for example, end of day on D-2. This turns a potential silent miss into a proactive alert to the team well before the payroll deadline.

**Why not plain continuous polling, and why not Poll Enrich:**
- Continuous polling works but adds operational noise without benefit, given the arrival pattern is a known monthly cycle, not truly random
- Poll Enrich still doesn't apply here — it needs a separate trigger (Timer) to start the flow, which just reintroduces the same scheduling question one layer up. The Sender SFTP Adapter's native cron schedule already solves this directly.

---

## 3. High-Level Flow Diagram

```
┌─────────────┐     SFTP Poll      ┌───────────────────────────────┐     SFTP Put      ┌───────────────────────────┐
│   DataIQ    │ ─────────────────► │        SAP CPI                 │ ─────────────────►│   SAP ECC – AL11            │
│ (Power      │  CSV file dropped  │                                │  Same file,        │   /JE_INBOUND_ECC_ME/       │
│ Automate +  │  in outbound folder│  1. Sender SFTP Adapter        │  untouched          │                             │
│ AWS)        │                    │  2. Content Modifier (log)     │  content            │  ABAP Background Job reads  │
└─────────────┘                    │  3. Exception Subprocess       │                     │  file, parses company code, │
                                    │  4. Receiver SFTP Adapter      │                     │  posts JE via BAPI          │
                                    └───────────────────────────────┘                     └─────────────┬───────────────┘
                                                                                                          │
                                                                                          Output: Success/Failure file
                                                                                          → separate AL11 outbound folder
                                                                                          (covered in IFlow 2)
```

---

## 4. Detailed Step-by-Step IFlow Design

### Step 0 — Sender Channel: SFTP Adapter

| Parameter | Value |
|---|---|
| Adapter Type | SFTP |
| Connection | Host/port of DataIQ SFTP server (to be shared by DataIQ team) |
| Source Directory | `/dataiq/outbound/je/` |
| File Name Pattern | `JE_ME_*.csv` (confirm exact pattern with DataIQ team) |
| Schedule | Quartz Cron — active only during D-5 to D-3 window (+ buffer), polling every 15–20 min within it. See Section 2A. |
| Post-Processing | Move file to `/dataiq/outbound/je/archive/` after successful pickup |
| Error Handling | Move to `/dataiq/outbound/je/error/` if pickup fails |
| Authentication | SSH key-based (preferred) |

---

### Step 1 — Content Modifier: Capture Metadata (logging only)

No business-field parsing here — just exchange properties for traceability:

| Property Name | Source | Purpose |
|---|---|---|
| `p_FileName` | `${file:name}` | Original file name for MPL tracing |
| `p_Timestamp` | `${date:now:yyyyMMddHHmmss}` | Used if renaming on write |
| `p_SourceSystem` | Fixed value `DATAIQ` | Monitoring/logging tag |

The file **body/payload is not touched** — it passes through as binary/text content unchanged.

---

### Step 2 — Exception Subprocess

Since there's no content validation, exceptions here are purely **transport-level**:

| Scenario | Action |
|---|---|
| Source file unreadable/corrupted at pickup | Log to MPL, move to error folder, alert |
| Receiver (AL11/SFTP) unreachable | Retry (3 attempts, exponential backoff) → park in JMS queue or Data Store for reprocessing if still failing |

---

### Step 3 — Receiver Channel: SFTP Adapter to AL11

| Parameter | Value |
|---|---|
| Adapter Type | SFTP |
| Target Directory | `/JE_INBOUND_ECC_ME/` (confirm exact AL11 path with Basis team) |
| File Name | Keep original name, or apply `JE_ME_<yyyyMMddHHmmss>.csv` if DataIQ's naming isn't unique enough to prevent overwrite |
| File Write Mode | Create new file (do not overwrite) |
| Authentication | SSH key-based, dedicated CPI service account |

**Important:** Confirm with Basis/ABAP team whether AL11 is exposed directly via SFTP on the app server, or via an intermediary/proxy — affects adapter config and firewall whitelisting.

---

## 5. Naming Conventions

| Artifact | Convention | Example |
|---|---|---|
| Package | `PKG_FI_JEPOSTING_ME` | |
| IFlow | `IF_JEPOST_DATAIQ2ECC_ME` | |
| Sender Channel | `SFTP_SND_DATAIQ_JEOUTBOUND` | |
| Receiver Channel | `SFTP_RCV_ECC_JEINBOUND_ME` | |
| Content Modifier | `CM_SetProperties_JEPost` | |
| Exception Subprocess | `EX_JEPost_ErrorHandling` | |
| Output file (to AL11) | Original name, or `JE_ME_<yyyyMMddHHmmss>.csv` | `JE_ME_20260820143000.csv` |
| Archive folder (source) | `/dataiq/outbound/je/archive/` | |
| Error folder (source) | `/dataiq/outbound/je/error/` | |

---

## 6. What Was Removed From v1 (and why)

| v1 Step | Status | Reason |
|---|---|---|
| Splitter | Removed | No need to split by company code — ABAP handles all entities from one file |
| Field-level Validation | Removed | Not CPI's responsibility; ABAP program validates on read |
| Router (5 entities) | Removed | ABAP program identifies company code internally |
| Message Mapping | Removed | File is passed through as-is, no transformation |
| Value Mapping (Company Code lookup) | Removed | No longer needed without routing |

---

## 7. Open Items to Confirm

- [ ] Exact AL11 folder path and SFTP access method (direct vs proxy)
- [ ] Whether original DataIQ file name is guaranteed unique, or CPI should rename on write
- [ ] File naming pattern on DataIQ's outbound side
- [ ] SLA/polling frequency requirement
- [ ] Credential/service account setup for both SFTP connections
- [ ] Whether ABAP job expects one combined file (all 5 entities) or separate files — confirm this is truly one file per drop
- [ ] Exact expected arrival window each month (confirm D-5/D-4/D-3 pattern with payroll team, or if it varies further) to finalize the cron schedule
- [ ] Cutoff time/day for the "file not received" alert (recommend D-2 EOD, but confirm against payroll deadline)

---

*This document covers IFlow 1 (JE Posting, pass-through) only. IFlow 2 (JE Status Update, ECC → DataIQ) is documented separately.*
