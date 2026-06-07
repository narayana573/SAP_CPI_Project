# SAP CPI — Palette Functions: Complete Interview Reference Guide

> **Validated against SAP Help Portal — Cloud Integration (SAP Integration Suite)**
> All palette names, operations, and behaviors conform to official SAP documentation.


---

# SAP Cloud Integration — Palette Functions Master Reference

## Table of Contents

1. [Process (Participant Shapes)](#1-process)
2. [Events](#2-events)
3. [Mapping](#3-mapping)
4. [Transformation](#4-transformation)
5. [Call](#5-call)
6. [Routing](#6-routing)
7. [Security](#7-security)
8. [Persistence](#8-persistence)
9. [Validator](#9-validator)
10. [Interview Questions — All Levels](#10-interview-questions)

---

## 1. Process

> **Definition:** Process shapes define the container/scope of the integration logic in an iFlow (Integration Flow).

| Shape | Description |
|---|---|
| **Integration Process** | The main container for all integration logic. Every iFlow has at least one Integration Process. |
| **Exception Subprocess** | A special BPMN construct for centralized runtime error handling. Triggered automatically when an exception occurs in the main process. Only one allowed per iFlow. |
| **Local Integration Process** | A reusable sub-flow scoped to the same iFlow. Called via Process Call step. Used to modularize and reuse logic within one iFlow. |

### Integration Process

The Integration Process is the top-level execution container. All message processing steps, adapters, and routing logic are placed inside it. It connects a Sender channel to a Receiver channel and orchestrates the entire message flow.

**Use Case:** Standard end-to-end integration flow (e.g., SOAP → Mapping → REST).

---

### Exception Subprocess

The Exception Subprocess provides a **global try-catch block** for the entire iFlow. When any runtime exception occurs in the main flow (adapter failure, mapping error, script exception, or explicitly thrown error), execution is automatically redirected here.

**Key facts:**
- Only **one** Exception Subprocess is allowed per iFlow.
- Contains an **Error Start Event** at its beginning.
- Main process terminates once exception is raised.
- Commonly used for: centralized logging, alert notifications, custom error response to sender.

**Error context headers available inside Exception Subprocess:**

| Header | Description |
|---|---|
| `CamelExceptionCaught` | The Java exception object |
| `CamelFailureRouteId` | The route/step where failure occurred |

**Groovy snippet for error extraction:**
```groovy
def ex = message.getProperty("CamelExceptionCaught")
message.setBody("Error: " + ex.getMessage())
return message
```

**Exception Subprocess Flow:**
```
Sender → Integration Process
             |
             ├── Step A (success) → Step B → Receiver
             |
             └── (any error) ──► Exception Subprocess
                                       |
                              ┌────────┴────────┐
                           Logging          Alerting
                                       |
                               Custom Error Response
                                       |
                               Error End / End Event
```

**Best Practices:**
- Always log a Correlation ID for traceability.
- Mask sensitive data in error payloads.
- Send meaningful HTTP status codes (e.g., 503, 400).
- Never expose internal stack traces to consumers.
- Route technical vs. business errors differently.

---

### Local Integration Process (LIP)

A Local Integration Process (LIP) is a **reusable sub-flow within the same iFlow**. It improves readability and reduces duplication of common logic such as validation, enrichment, or pre-processing.

**LIP vs. Process Call (Cross-iFlow):**

| Feature | Local Integration Process | Process Call (Cross-iFlow) |
|---|---|---|
| Scope | Same iFlow only | Across multiple iFlows |
| Reusability | Local | Global/shared |
| Performance | Slightly better (intra-flow) | Minor overhead (inter-flow) |
| Use case | Common steps within one iFlow | Shared services across landscapes |

**Key behaviors:**
- LIP executes **synchronously** — main flow waits for it to complete.
- LIP shares the **same message exchange** (body, headers, properties).
- Unhandled exceptions in LIP propagate to the main flow's Exception Subprocess.
- LIP **cannot** be reused across different iFlows or packages.

---

## 2. Events

> **Definition:** Events define how an iFlow starts, ends, or handles specific conditions such as errors, timers, and escalations.

```
Start Events ──────────────────────────────────────
  Message Start Event   → Inbound adapter trigger
  Timer Start Event     → Schedule-based trigger
  None Start Event      → Logical start of process

End Events ────────────────────────────────────────
  Message End Event     → Sends data to receiver
  None End Event        → Ends processing silently
  Error End Event       → Throws an error explicitly
  Escalation End Event  → Raises a non-fatal escalation
  Terminate End Event   → Kills all parallel branches

Error Handling ────────────────────────────────────
  Error Start Event     → Entry point of Exception Subprocess
```

### Event Quick Reference

| Event | Purpose | Key Interview Point |
|---|---|---|
| **Message Start Event** | Triggered by inbound message (HTTP, SFTP, SOAP, etc.) | Only one trigger per iFlow |
| **Timer Start Event** | Scheduled execution (cron expression) | Used for batch, polling jobs |
| **None Start Event** | Marks logical start of sub-process | Does not bind to an adapter |
| **Message End Event** | Sends processed message to receiver | Multiple allowed for conditional routing |
| **None End Event** | Ends processing; no message sent | Used for internal/fire-and-forget flows |
| **Error Start Event** | Entry of Exception Subprocess | Triggered automatically on runtime error |
| **Error End Event** | Explicitly raises an error | Used for business validation failures |
| **Escalation End Event** | Raises an escalation condition | Does not hard-fail the flow; propagates upward |
| **Terminate End Event** | Forcefully stops all active branches | Used for fatal rule violations |

**Architecture Note — Error End vs Escalation End:**
- **Error End Event:** Stops current path with failure → control moves to Exception Subprocess.
- **Escalation End Event:** Raises an abnormal condition (e.g., threshold breach, partial data) — typically handled by an Escalation Event Subprocess if defined, otherwise the iFlow may terminate depending on configuration.

---

## 3. Mapping

> **Definition:** Mapping steps transform message structure and values between source and target formats.

| Step | Official Name | Description |
|---|---|---|
| **Message Mapping** | Message Mapping | Graphical source-to-target XML/JSON mapping with built-in function library |
| **XSLT Mapping** | XSLT Mapping | Custom transformation using XSLT stylesheets |
| **Value Mapping** | (Used inside Message Mapping) | Lookup-table-based code conversion between systems |

> ⚠️ **"Operation Mapping" and "ID Mapping" are NOT standalone palette steps** in Cloud Integration Web UI. These are legacy PI/PO concepts. The correct steps are **Message Mapping** and **Value Mapping**.

### Message Mapping — Function Categories

#### Node Functions

Control how target nodes are created from source node contexts.

| Function | Purpose | Example |
|---|---|---|
| `UseOneAsMany` | Repeats one source value for multiple target nodes | Header `OrderID` → copy into every Item |
| `RemoveContexts` | Flattens hierarchy by removing extra context levels | Nested nodes → flat list |
| `CollapseContexts` | Groups multiple values into a single context | Aggregation before mapping |
| `SplitByValue` | Splits when a specific value appears | Split on status change |
| `SplitBySeparator` | Splits based on a delimiter | `A,B,C` → `A`, `B`, `C` |
| `RemoveEmptyValues` | Filters out null/blank nodes from target | Prevent empty XML elements |
| `Exists` | Checks if a node is present | Guard optional fields |

**Rapid-fire:**
- Repeat header to items? → `UseOneAsMany`
- Flatten hierarchy? → `RemoveContexts`
- Group values? → `CollapseContexts`
- Remove empty nodes? → `RemoveEmptyValues`
- Split delimited values? → `SplitBySeparator`

#### Boolean Functions

Return `true` or `false` for conditional mapping logic.

| Function | Purpose |
|---|---|
| `Equals` | Check equality of two values |
| `GreaterThan` / `LessThan` | Compare numeric values or dates |
| `And` / `Or` / `Not` | Logical operators for compound conditions |
| `IsEmpty` | Check if a node has no value |
| `Exists` | Check if a node is present at all |
| `IfThenElse` | Map value conditionally |
| `IfWithoutElse` | Map only when condition is true |

**Distinction:** `Exists` checks *presence*; `IsEmpty` checks *content*.

#### Arithmetic Functions

Perform mathematical calculations on numeric fields at runtime.

| Function | Use Case |
|---|---|
| `Add` / `Subtract` | `BaseAmount + Tax`; `Gross – Discount` |
| `Multiply` / `Divide` | `Quantity × UnitPrice`; unit conversion |
| `Round` / `Ceiling` / `Floor` | Financial rounding (use explicitly) |
| `Abs` | Convert negative to positive value |

> ⚠️ Always guard `Divide` with a `Equals(Divisor, 0)` check using `IfThenElse` to prevent divide-by-zero runtime errors.

#### Fixed Value Mapping vs Value Mapping

| Aspect | Fixed Value Mapping | Value Mapping |
|---|---|---|
| Nature | Static/Hardcoded | Dynamic/Lookup-based |
| Depends on source value | No | Yes |
| Maintenance | Requires iFlow redeployment | No redeployment needed |
| Use case | Constants, default flags | Country codes, status, enumerations |
| Scalability | Poor for many values | Excellent for large code lists |

---

## 4. Transformation

> **Definition:** Transformation steps modify, convert, or inspect the message payload during processing.

| Step | Purpose |
|---|---|
| **Content Modifier** | Set/modify headers, exchange properties, and simple body content |
| **Converter** | Transform payload format (XML ↔ JSON ↔ CSV) |
| **Decoder** | Decode encoded content (e.g., Base64) to readable format |
| **EDI Extractor** | Parse EDI messages (EDIFACT/X12) into XML |
| **Encoder** | Encode content (e.g., Base64) for transmission |
| **Filter** | Allow or block messages based on condition |
| **Message Digest** | Generate cryptographic hash (MD5, SHA-256, etc.) |
| **Script** | Execute Groovy or JavaScript custom logic |
| **XML Modifier** | Add, delete, or update XML nodes/attributes via XPath |

---

### 4.1 Content Modifier

Enriches message **headers**, **Exchange Properties**, and **body** without writing code. Executes at runtime.

**Three modifiable areas:**

| Area | Description | Scope |
|---|---|---|
| Headers | Sent to receiver adapters (HTTP headers, etc.) | Travels with message |
| Exchange Properties | Internal flow control — routing, conditions, reuse | Not sent externally |
| Body | Simple payload construction via templates and expressions | Replaces current body |

**Header vs Exchange Property:**
- **Header:** Passed to downstream adapters and receiver systems.
- **Property:** Internal to the iFlow; used for routing conditions and reuse.

**Common use cases:**
- Set static header: `X-Client-Id = ABC123`
- Extract XPath value from payload: `${xpath(/Order/ID)}`
- Set timestamp or Correlation ID for traceability
- Build small JSON/XML request body for REST calls
- Reference secure parameters (never hardcode credentials)

> 🔑 For complex logic (loops, conditional restructuring, multi-record transformations) — use **Groovy Script**, not Content Modifier.

---

### 4.2 Converter

Changes payload **format** without changing business data values. Does not transform structure or field values.

**Supported conversions:**

| From | To |
|---|---|
| XML | JSON |
| XML | CSV |
| JSON | XML |
| CSV | XML |

**Placement best practice:** Convert inbound formats to a canonical format (usually XML) immediately after the sender adapter. Apply outbound conversion just before the receiver adapter.

**Converter vs. Message Mapping:**
- **Converter:** Changes format (JSON ↔ XML ↔ CSV)
- **Message Mapping:** Changes structure and field values

> For custom/non-standard parsing, use Groovy Script. For standard format conversions, always prefer the Converter step.

---

### 4.3 Encoder / Decoder

| Step | Action |
|---|---|
| **Encoder** | Converts readable payload → encoded format (Base64, MIME, ZIP) |
| **Decoder** | Converts encoded content → readable/original format |

**Design pattern for encoded payloads:**
```
Inbound:  Decoder → Converter/Mapping → Processing
Outbound: Processing → Mapping → Encoder → Receiver Adapter
```

> Never log decoded sensitive payloads in plain text. Encode only at the boundary.

---

### 4.4 EDI Extractor

Parses EDI flat-file formats (**EDIFACT**, **ANSI X12**) into structured XML so that CPI's standard message mapping and routing steps can process the data.

**Why it's needed:** EDI is segment-based; CPI mapping works on XML/JSON. EDI Extractor bridges this gap.

**Requirements:** Partner agreements, Message Implementation Guidelines (MIGs), proper separator configuration.

**Typical inbound EDI flow:**
```
SFTP/AS2 → EDI Extractor → Message Mapping → ERP/Backend
```

> If EDI structure is invalid or does not match the MIG, EDI Extractor throws a runtime exception — handle via Exception Subprocess.

---

### 4.5 Filter

Conditionally **allows or blocks** a message from continuing in the flow. Binary decision step (pass or stop). Does not modify the payload.

**Supported condition types:**
- XPath on XML payload
- JSONPath on JSON payload
- `${header.name}` — header-based
- `${property.name}` — exchange property-based

**Filter vs Router:**

| Filter | Router |
|---|---|
| Single pass/stop decision | Multiple conditional branches |
| True/False only | IF–ELSE with n branches |
| Best for binary validation | Best for content-based routing |

**Performance tip:** Avoid complex XPath on large payloads inside Filter. Pre-calculate conditions upstream (via Content Modifier or Groovy) and store in Exchange Properties. Evaluate the property in Filter.

---

### 4.6 Message Digest

Generates a **cryptographic hash** of the message or selected fields. Does **not** modify the payload. Result is stored in a header or exchange property.

**Supported algorithms:** MD5, SHA-1, SHA-256, SHA-512

> Use **SHA-256 or higher** for security-sensitive scenarios. MD5 is considered cryptographically weak.

**Use cases:**
- Duplicate detection (hash business key → compare with stored hashes)
- Data integrity verification (send hash in header; receiver recomputes and compares)
- Input for digital signatures

**Idempotency pattern:**
```
Generate Digest on key fields → Check persistence → Duplicate? → Drop
                                                  → New?       → Process and Store
```

---

### 4.7 Script (Groovy / JavaScript)

Executes custom code logic within the iFlow. Used where standard palette steps are insufficient.

**When to use Groovy:**
- Complex transformations with loops or multi-conditions
- Dynamic payload construction (optional fields, derived values)
- Advanced error handling and custom exception logic
- Composite enrichment from headers, properties, and payload combined

**Core Groovy API:**
```groovy
// Read body, headers, properties
def body = message.getBody(String)
def headers = message.getHeaders()
def props = message.getProperties()

// Set header/property
message.setHeader("X-Processed-By", "CPI")
message.setProperty("isHighValue", true)

// Modify body
message.setBody(body.replace("OLD", "NEW"))
return message
```

**Common snippets:**

```groovy
// Extract XML value (XPath-like)
import groovy.xml.XmlSlurper
def xml = new XmlSlurper().parseText(message.getBody(String))
def orderId = xml.Order.ID.text()
message.setProperty("OrderID", orderId)

// Generate Correlation ID
import java.util.UUID
message.setHeader("CorrelationID", UUID.randomUUID().toString())

// Error handling
try {
    def body = message.getBody(String)
    if (!body) throw new Exception("Empty payload")
} catch (Exception e) {
    message.setProperty("GroovyError", e.getMessage())
    throw e  // propagates to Exception Subprocess
}
```

**Best practices:**
- Wrap logic in `try-catch` blocks
- Avoid parsing large payloads multiple times
- Externalize business rules via properties/parameters
- Keep scripts modular and well-commented

---

### 4.8 XML Modifier

Makes **structural changes** to XML payloads (add, delete, replace nodes/attributes) using XPath expressions — without building a full Message Mapping.

**Supported operations:** Add node, Delete node, Replace value, Add attribute, Delete attribute

**XML Modifier vs Content Modifier:**

| XML Modifier | Content Modifier |
|---|---|
| Changes XML node structure | Changes headers, properties, body template |
| XPath-targeted | Header/property/constant assignment |
| For structural XML edits | For enrichment and metadata |

**Use case examples:**
- Inject a `<Header>` element with timestamp and correlation ID that the receiver requires
- Remove an element the receiver rejects (e.g., `<InternalDebugFlag>`)
- Rename an element (`<custId>` → `<customerId>`)

> For complex conditional or iterative XML construction → use **Groovy Script** or **Message Mapping**.

---

## 5. Call

> **Definition:** Call steps invoke external systems or local sub-processes during iFlow execution.

### 5.1 External Call

| Step | Communication Type | Replaces Body |
|---|---|---|
| **Content Enricher** | Synchronous (via Request-Reply) | No — merges with original |
| **Poll Enrich** | Pull-based (polling receiver) | No — merges with original |
| **Request-Reply** | Synchronous | Yes — response replaces body |
| **Send** | Asynchronous (fire-and-forget) | No |

---

#### Content Enricher

Fetches additional data from an external system and **merges** it with the original message. Does not replace the payload (unless configured to do so).

**When to use:** Incoming payload is incomplete and needs data from a secondary system (e.g., adding customer address to an order message).

**Configuration options:**
- Enrichment Type: Combine, Replace
- Aggregation Strategy: XPath or Groovy-based
- Timeout configuration

**Content Enricher vs Request-Reply:**

| Feature | Content Enricher | Request-Reply |
|---|---|---|
| Body after call | Original + enrichment merged | Response replaces body |
| Use case | Add missing data | Real-time API call |
| Aggregation strategy | Yes | No |

---

#### Request-Reply

A **synchronous** communication step. Sends a message to an external system and **waits for a response** before continuing the flow. The response **replaces** the current message body.

**Adapters supported:** HTTP, OData, SOAP, IDoc, RFC, JDBC, and others.

**Body preservation pattern (before Request-Reply):**
```
Content Modifier (save body → property) → Request-Reply → use property if needed
```

**Request-Reply vs Send:**

| Feature | Request-Reply | Send |
|---|---|---|
| Communication | Synchronous | Asynchronous |
| Waits for response | Yes | No |
| Replaces body | Yes | No |
| Use case | Real-time API/validation | Fire-and-forget |
| Performance impact | Higher (blocks flow) | Low (non-blocking) |

**Timeout handling:** Configure timeout in the receiver adapter. On timeout → exception thrown → handled by Exception Subprocess.

---

#### Poll Enrich

**Pulls** data from a receiver system at runtime and merges it with the current message. Unlike Request-Reply (push/on-demand), Poll Enrich is designed for polling-based data access.

**When to use Poll Enrich vs Request-Reply:**

| Scenario | Use |
|---|---|
| Data is available on-demand, receiver responds to calls | Request-Reply |
| Data must be fetched by polling (snapshot, periodic) | Poll Enrich |
| Reference/master data from polling endpoints | Poll Enrich |

**Poll Enrich vs Content Enricher:**

| Poll Enrich | Content Enricher |
|---|---|
| Pull-based (CPI polls receiver) | Call-based (CPI sends request) |
| Suited for snapshot/periodic data | Suited for transactional lookups |
| Often used for master data | Used for per-message enrichment |

---

#### Send

Asynchronous **fire-and-forget** delivery step. Sends the message to the receiver and continues immediately without waiting for a response.

**When to use:**
- No response is required from the receiver
- High-throughput, event-based flows
- Decoupling sender and receiver
- Sending IDocs to SAP S/4HANA

> Any response from the receiver is **ignored** by CPI when using Send. If a response is required, use Request-Reply.

---

### 5.2 Local Call

| Step | Description |
|---|---|
| **Process Call** | Calls a Local Integration Process once for modularization |
| **Idempotent Process Call** | Prevents duplicate processing using a unique key repository |
| **Looping Process Call** | Repeatedly calls a Local Integration Process until a loop condition is met |

---

#### Process Call

Invokes a **Local Integration Process** from within the main iFlow. Execution is synchronous — main flow waits for the called process to return. Message body, headers, and properties are shared and modifications in the called process are reflected back.

**Use cases:** Centralized validation, reusable authentication token handling, payload normalization, common error formatting.

**Process Call vs External Request-Reply:**

| Process Call | Request-Reply |
|---|---|
| Calls local sub-flow (internal) | Calls external system |
| No network latency | Network + backend latency |
| For reuse/modularity | For integration |

---

#### Idempotent Process Call

Ensures a message is **processed exactly once** by checking a unique key against an internal **Idempotent Repository** before executing the sub-process.

**Flow:**
```
Extract unique key → Check repository
    ├── Key exists → Skip (duplicate detected)
    └── Key missing → Execute process → Store key in repository
```

**Configuration:** Define a key expression (e.g., `${property.OrderID}`) and a retention period.

**Idempotent Process Call vs Data Store:**

| Idempotent Process Call | Data Store |
|---|---|
| Prevents duplicate processing | Stores message data |
| Lightweight key lookup | Stores full payload |
| Automatic retention | Manual retention management |

> ⚠️ If the flow fails after the key is stored, the key remains — meaning the message won't reprocess automatically. Design carefully around this.

---

#### Looping Process Call

Repeatedly invokes a Local Integration Process until a **loop condition** evaluates to false.

**Primary use cases:**
- Consuming **paginated APIs** (page = 1, 2, 3 until `hasMore = false`)
- Sending **large payloads in chunks** (e.g., 50,000 records → batches of 1,000)
- Controlled retries with business conditions

**Loop flow:**
```
Main iFlow
  └─► Looping Process Call
          └─► Local iFlow (one iteration)
                  ├── Process page/chunk
                  ├── Set loop condition property
                  └── Return to loop
          └─► [condition = false] → Exit → Continue main flow
```

**Looping Process Call vs Process Call:**

| Feature | Process Call | Looping Process Call |
|---|---|---|
| Executions | Once | Repeated |
| Loop condition | None | Required |
| Use case | Modularization | Pagination / batching |
| State tracking | Stateless | Stateful (via properties) |

> ⚠️ **Always define a hard stop / maximum iteration count** to prevent infinite loops. Monitor tenant resource usage with looping flows.

---

## 6. Routing

> **Definition:** Routing steps control how messages are distributed, split, combined, or directed within an iFlow.

| Step | Pattern | Purpose |
|---|---|---|
| **Aggregator** | Aggregator EIP | Combine multiple related messages into one |
| **Gather** | Fan-in (Multicast counterpart) | Collect and merge branches from Multicast |
| **Join** | Synchronization | Wait for all parallel branches without merging |
| **Multicast** | Fan-out (Parallel / Sequential) | Send same message to multiple branches |
| **Router** | Content-Based Router EIP | Route to one branch based on conditions |
| **Splitter** | Splitter EIP | Divide one message into multiple messages |

---

### 6.1 Aggregator

Collects multiple related messages and **combines them into a single message** based on a **correlation key** and a **completion condition**.

**Three required configurations:**
1. **Correlation Key** — identifies which messages belong together (e.g., `OrderID`)
2. **Aggregation Strategy** — defines how messages are merged (XPath, concatenation, Groovy)
3. **Completion Condition** — determines when to release the aggregated result (count, timeout, flag)

**Aggregator vs Gather:**

| Aggregator | Gather |
|---|---|
| Works independently | Works only with Multicast |
| Dynamic message count | Fixed branch count |
| Requires correlation + completion | Implicit correlation via Multicast |
| Stateful, can timeout | Short-lived collection |

> ⚠️ **Always define a timeout** to prevent messages getting stuck in the aggregator waiting indefinitely.

---

### 6.2 Gather

The **fan-in** counterpart to Multicast. Waits for all branches initiated by a Multicast step to complete, then **combines their outputs** into a single message before continuing.

**Gather vs Join:**

| Gather | Join |
|---|---|
| Synchronizes + merges payloads | Synchronizes only (no merge) |
| Requires aggregation strategy | No aggregation needed |
| Used for data orchestration | Used for execution control |

**Gather vs Aggregator:**

| Gather | Aggregator |
|---|---|
| Fixed, known branch count | Dynamic, unknown message count |
| Implicit correlation (via Multicast) | Requires explicit correlation key |

---

### 6.3 Join

Waits for all parallel branches from a Multicast to complete before allowing the main flow to continue. Does **not** merge payloads — purely for **execution synchronization**.

**When to use Join (not Gather):**
- Parallel branches perform side effects (logging, cache update, monitoring)
- No payload merging needed
- Main flow should resume only after all branches complete

**Join flow diagram:**
```
Multicast ──┬── Branch 1 (Logging) ──────┐
            ├── Branch 2 (Monitoring) ───► Join ──► Continue
            └── Branch 3 (Cache Update) ─┘
```

---

### 6.4 Multicast

**Fans out** a message to multiple processing branches. Each branch receives a **copy** of the original message.

**Types:**

| Type | Behavior |
|---|---|
| **Parallel Multicast** | All branches execute simultaneously → best for performance |
| **Sequential Multicast** | Branches execute one by one → use when order matters |

**Multicast vs Splitter:**

| Multicast | Splitter |
|---|---|
| Sends **same payload** to multiple branches | Divides payload into **different parts** |
| Fan-out of identical copies | Fan-out of distinct fragments |
| Used for parallel processing | Used for record-level processing |

**Common pattern:**
```
Parallel Multicast → Branch 1: Pricing API (Request-Reply)
                  → Branch 2: Tax API (Request-Reply)
                  → Branch 3: Availability API (Request-Reply)
                  → Gather → Merge responses → Continue
```

---

### 6.5 Router

Routes a message to **one** of multiple conditional branches based on defined conditions. Works on the **first-match principle** — the first matching condition wins.

**Supported condition types:**
- XPath: `/Order/Amount > 10000`
- Exchange Property: `${property.OrderType} = 'EXPORT'`
- Header: `${header.Env} = 'PROD'`

**Router vs Filter:**

| Router | Filter |
|---|---|
| Routes to one of multiple branches | Allows or blocks (pass/stop) |
| IF–ELSE decision | TRUE/FALSE only |
| Multiple conditions | Single condition |

> ⚠️ **Always configure a Default route.** Without it, unmatched messages are silently dropped.

---

### 6.6 Splitter

Divides a **single message** into **multiple individual messages**, each processed independently downstream.

**Types of Splitter:**

| Type | Description | Use Case |
|---|---|---|
| **General Splitter** | XPath/JSONPath-based split on repeating nodes | Large XML/JSON batch payloads |
| **Iterating Splitter** | Always sequential processing | When processing order matters |
| **EDI/PKCS#7 Splitter** | EDI-specific format splitting | EDI interchange with multiple transactions |

**General vs Iterating Splitter:**

| General Splitter | Iterating Splitter |
|---|---|
| Can process in parallel | Always sequential |
| Better performance for large batches | Controlled, ordered processing |

**Common pattern:**
```
Large Order (100 items)
  └─► Splitter (XPath: /Order/Items/Item)
          └─► Item 1 → Backend Call
          └─► Item 2 → Backend Call
          ...
          └─► Item 100 → Backend Call
                └─► Aggregator (if recombination needed)
```

> Headers and properties are **copied** to each split message. Changes inside a split are local to that split instance.

---

## 7. Security

> **Definition:** Security steps protect message confidentiality, integrity, and authenticity using cryptographic operations.

| Step | Key Used | Purpose |
|---|---|---|
| **Encryptor** | Receiver's Public Key | Encrypt payload for confidentiality |
| **Decryptor** | Own Private Key | Decrypt received encrypted payload |
| **Signer** | Own Private Key | Digitally sign payload for integrity/authentication |
| **Verifier** | Sender's Public Key | Verify digital signature |

**Supported formats/algorithms:**
- **PGP** (encryption and signing)
- **PKCS#7 / CMS** (encryption and signing)
- **XML Digital Signature**

> ⚠️ **"PKCS#7 Signing" is not a separate palette step** — PKCS#7 is a signing *algorithm/format* available within the **Signer** step.

**Encryption vs Signing:**

| Aspect | Encryptor / Decryptor | Signer / Verifier |
|---|---|---|
| Goal | Confidentiality (data hidden) | Integrity + Authenticity (data visible but protected) |
| Encryption uses | Receiver's public key | — |
| Decryption uses | Own private key | — |
| Signing uses | — | Sender's private key |
| Verification uses | — | Sender's public key |

**Best practice sequence when both are required:**
```
Sign first → Then Encrypt
(Sign → Encrypt is the recommended order for combined security)
```

**Certificate management:** All keys and certificates are stored in:
`Monitor → Manage Security → Keystore`

**Secure file transfer scenario (to bank via SFTP):**
```
Payload → Signer (CPI private key) → Encryptor (bank's public key) → SFTP Adapter → Bank
```

---

## 8. Persistence

> **Definition:** Persistence steps store and retrieve data during or between iFlow executions.

| Step | Persistence Type | Scope | Use Case |
|---|---|---|---|
| **Data Store Operations** | Persistent (database-backed) | Cross-iFlow, Cross-execution | Retry, async decoupling, audit storage |
| **Persist** | Short-term trace storage | Single iFlow execution | Message tracing/debugging |
| **Write Variables** | In-memory (exchange-scope) | Current iFlow execution only | Intermediate value storage, routing |

---

### 8.1 Data Store Operations

Provides **persistent, database-backed storage** for messages during integration processing. Used for asynchronous patterns, retry logic, and cross-iFlow data sharing.

**Available operations:**

| Operation | Description |
|---|---|
| **Write** | Store a message with Entry ID and retention period |
| **Get** | Retrieve a single entry by Entry ID (does not delete) |
| **Select** | Retrieve multiple entries based on conditions |
| **Delete** | Remove a specific entry by Entry ID |

> ⚠️ The correct operation name is **Write** (not "Put"). Confirm this in the SAP Cloud Integration UI.

**Write operation parameters:**
- Data Store Name
- Entry ID (unique identifier)
- Overwrite: Yes/No (if No and entry exists → processing fails)
- Retention Period

**Data Store vs JMS Queue:**

| Data Store | JMS Queue |
|---|---|
| Persistent storage | Messaging middleware |
| Manual/pull-based retrieval | Automatic consumption |
| Custom retry logic | Built-in async communication |
| Suitable for store-and-forward | Suitable for event-driven integration |

**Data Store Operations — Monitoring:**
`Monitor → Manage Stores → Data Stores`

**Best practices:**
- Always define a retention period
- Delete entries after successful processing to prevent accumulation
- Avoid storing very large payloads
- Use meaningful Data Store names (e.g., `FailedOrders_DS`)

---

### 8.2 Persist

A lightweight step used for **message tracing and debugging** during iFlow execution. Stores a snapshot of the current message in the monitoring trace. Not intended for production data persistence or cross-iFlow access.

---

### 8.3 Write Variables

Stores **simple values** (strings, expressions, XPath results) as named variables scoped to the **current iFlow execution**. Not persisted after execution ends.

**Configuration:**
- Variable Name
- Data Type
- Source Type: XPath, Constant, Expression

**Write Variables vs Data Store vs Exchange Properties:**

| Feature | Write Variables | Exchange Properties | Data Store |
|---|---|---|---|
| Scope | Current iFlow execution | Current iFlow execution | Persistent, cross-iFlow |
| Persistence | No | No | Yes |
| Access in Groovy | `message.getProperty("varName")` | `message.getProperty("propName")` | Via Data Store Get step |
| Use case | Intermediate extraction | Routing, reuse | Retry, async, audit |

> At runtime, Write Variables behave similarly to Exchange Properties and are accessible in Groovy using `message.getProperty("variableName")`.

---

## 9. Validator

> **Definition:** Validator steps ensure messages conform to expected structural standards before further processing.

| Step | Validates Against | Failure Behavior |
|---|---|---|
| **XML Validator** | XSD (XML Schema Definition) | Throws validation exception |
| **EDI Validator** | EDI schemas (X12, EDIFACT) | Throws validation exception |

**Best practice placement:** Immediately after the sender adapter, before mapping or transformation.

### XML vs EDI Validator

| Aspect | XML Validator | EDI Validator |
|---|---|---|
| Validates | XML against XSD | EDI against EDI schema |
| Schema file | XSD file (uploaded to iFlow Resources) | EDI schema / MIG |
| Input format | XML payload | EDI (X12 / EDIFACT) payload |
| Validation type | Element-based (hierarchy, types) | Segment-based (structure, qualifiers) |
| Business rules | No — structural only | No — structural only |

> Neither XML Validator nor EDI Validator validates **business rules** — use Groovy, Router conditions, or mapping logic for that.

**XSD upload path:** `Resources → Add → XSD file`, then reference in XML Validator step.

---

## 10. Interview Questions

---

### 10.1 Process (Integration Process, Exception Subprocess, LIP)

#### Basic Level

**Q1. What is the difference between Integration Process, Exception Subprocess, and Local Integration Process?**
**A:** Integration Process is the main execution container. Exception Subprocess handles runtime errors globally (one per iFlow). Local Integration Process is a reusable sub-flow within the same iFlow.

**Q2. How many Exception Subprocesses can an iFlow have?**
**A:** Exactly **one** per iFlow.

**Q3. What triggers the Exception Subprocess?**
**A:** Any unhandled runtime exception — adapter failure, mapping error, Groovy exception, or explicitly thrown error via Error End Event.

**Q4. Can a Local Integration Process be reused across different iFlows?**
**A:** No. LIP is scoped to the same iFlow. For cross-iFlow reuse, use a Process Call to a separate integration flow.

**Q5. What header contains the actual exception object in the Exception Subprocess?**
**A:** `CamelExceptionCaught`

#### Medium Level

**Q6. What happens to the main flow when the Exception Subprocess is triggered?**
**A:** The main flow terminates. Execution transfers exclusively to the Exception Subprocess.

**Q7. What data is shared between the main iFlow and a Local Integration Process?**
**A:** The full message exchange — body, headers, and exchange properties — is shared. Any changes in the LIP reflect back in the main flow.

**Q8. Can a Local Integration Process call another Local Integration Process?**
**A:** Yes, nesting is technically possible, but minimize depth for readability and maintainability.

#### Advanced Level

**Q9. How do you design a centralized error-handling framework across 10 iFlows?**
**A:** Each iFlow has an Exception Subprocess that uses a Process Call to invoke a shared "Error Handler iFlow." The shared iFlow handles logging, alerting, and formatting centrally.

**Q10. Can business errors be handled in the Exception Subprocess?**
**A:** Yes — if you explicitly throw them using an **Error End Event** or `throw new Exception(...)` in Groovy, control passes to the Exception Subprocess.

---

### 10.2 Events

#### Basic Level

**Q1. What is the difference between Message Start Event and Timer Start Event?**
**A:** Message Start Event triggers on inbound message receipt (event-driven). Timer Start Event triggers on a schedule (batch/periodic).

**Q2. What is the difference between End Event and Message End Event?**
**A:** End Event terminates processing internally (no message sent). Message End Event sends the payload to a receiver system.

**Q3. Can an iFlow have multiple Message End Events?**
**A:** Yes — for routing to different receivers based on conditions.

**Q4. What is the Terminate End Event used for?**
**A:** It immediately stops **all** active parallel branches. Used for fatal rule violations, security failures, or corrupt payload detection.

**Q5. What is the difference between Error End Event and Escalation End Event?**
**A:** Error End Event stops the current path with failure and triggers the Exception Subprocess. Escalation End Event raises an abnormal condition (propagated upward) without hard-failing the flow.

#### Medium Level

**Q6. When would you use Error End Event vs throwing an exception in Groovy?**
**A:** Both route to Exception Subprocess. Error End Event is the clean BPMN-standard approach; Groovy exception is used when the error condition is determined programmatically inside a script.

**Q7. Can Error Start Event be triggered manually?**
**A:** No — it is triggered automatically when a runtime error occurs. You can trigger the flow path deliberately by using Error End Event or Groovy `throw`.

---

### 10.3 Mapping

#### Basic Level

**Q1. What is the difference between Message Mapping and XSLT Mapping?**
**A:** Message Mapping is graphical and provides a built-in function library. XSLT Mapping uses custom stylesheets for complex transformations.

**Q2. What is the purpose of Value Mapping in SAP CPI?**
**A:** To dynamically convert codes between systems (e.g., country codes, status codes) using a centrally maintained lookup table — without redeploying the iFlow.

**Q3. What does `UseOneAsMany` do?**
**A:** Repeats one source value for multiple target nodes (e.g., header `OrderID` → copied into every `Item`).

**Q4. What is the difference between `RemoveContexts` and `CollapseContexts`?**
**A:** `RemoveContexts` flattens the hierarchy; `CollapseContexts` groups multiple values into a single context.

**Q5. What is Fixed Value Mapping and when do you use it?**
**A:** Assigns a hardcoded constant to a target field (e.g., `SourceSystem = "CPI"`). Used only for static, unchanging values.

#### Medium Level

**Q6. What is the difference between `Exists` and `IsEmpty`?**
**A:** `Exists` checks if a node is present in the payload. `IsEmpty` checks if a node has no value (is blank/null).

**Q7. How do you prevent divide-by-zero errors in arithmetic mapping functions?**
**A:** Guard the `Divide` function using `Equals(Divisor, 0)` with `IfThenElse` to skip division when divisor is zero.

**Q8. When would you use Value Mapping over Fixed Value Mapping?**
**A:** When different source codes must map to different target codes dynamically (country codes, status enumerations). Value Mapping is centrally maintained and requires no iFlow redeployment when codes change.

#### Advanced Level

**Q9. Is "Operation Mapping" a palette step in SAP Cloud Integration?**
**A:** No. Operation Mapping is a legacy SAP PI/PO concept. In Cloud Integration, the palette step is **Message Mapping**. Mentioning "Operation Mapping" as a CPI palette step is incorrect.

**Q10. What happens if a source value is not found in Value Mapping at runtime?**
**A:** The target field may be empty or the mapping throws an error, depending on configuration. Best practice: always define a fallback/default value or handle the "not found" case explicitly.

---

### 10.4 Transformation

#### Basic Level

**Q1. What is the difference between Converter and Message Mapping?**
**A:** Converter changes the payload **format** (JSON ↔ XML ↔ CSV). Message Mapping changes the payload **structure and field values**.

**Q2. What does the Encoder step do?**
**A:** Converts readable payload content into an encoded representation (e.g., Base64) for transmission.

**Q3. What does the Filter step do in CPI?**
**A:** It conditionally allows or blocks a message from continuing in the flow based on a defined condition (XPath, header, property).

**Q4. Can the Filter step modify the message payload?**
**A:** No. Filter is purely a decision step.

**Q5. What is the purpose of the XML Modifier step?**
**A:** To make targeted structural changes to an XML payload (add, delete, or update nodes/attributes) using XPath, without requiring a full Message Mapping.

#### Medium Level

**Q6. How is EDI Extractor different from the Converter?**
**A:** EDI Extractor parses EDI-specific formats (EDIFACT/X12) into XML. The standard Converter handles JSON, XML, and CSV format conversions — it cannot parse EDI.

**Q7. When should you use Groovy Script instead of Content Modifier?**
**A:** When the logic involves loops, complex conditions, multi-record transformations, dynamic JSON/XML construction, or heavy restructuring of payloads.

**Q8. Why should you use SHA-256 instead of MD5 in Message Digest?**
**A:** MD5 is cryptographically weak and considered compromised for security purposes. SHA-256 provides stronger collision resistance for integrity and signing scenarios.

#### Advanced Level

**Q9. Describe the recommended pattern when a sender sends Base64-encoded JSON and the receiver expects XML.**
**A:** `Decoder (Base64 → JSON) → Converter (JSON → XML) → Message Mapping → Receiver Adapter`

**Q10. What is the best practice for using Filter on large XML payloads?**
**A:** Pre-calculate the condition upstream using Content Modifier or Groovy and store the result in an Exchange Property. Filter on the property value instead of evaluating complex XPath on the large payload.

---

### 10.5 Call

#### Basic Level

**Q1. What is the difference between Request-Reply and Send?**
**A:** Request-Reply is synchronous — it sends a request and waits for a response which replaces the body. Send is asynchronous fire-and-forget — it delivers the message and ignores any response.

**Q2. What is the Idempotent Process Call used for?**
**A:** To prevent duplicate processing by checking a unique key against an internal repository. If the key already exists, the sub-process is skipped.

**Q3. What happens to the message body after Request-Reply?**
**A:** The response from the receiver **replaces** the existing message body.

**Q4. When would you use Poll Enrich instead of Request-Reply?**
**A:** When the external system only supports polling (cannot receive on-demand requests) or when you need to fetch snapshot/reference data periodically.

**Q5. What is a Looping Process Call used for?**
**A:** For repeatedly calling a Local Integration Process until a defined loop condition is false — typically for paginated API consumption or chunked batch processing.

#### Medium Level

**Q6. How do you preserve the original message body when using Request-Reply?**
**A:** Before the Request-Reply step, use a Content Modifier to store the original body in an Exchange Property. Reference it afterward as needed.

**Q7. What is the risk of not defining an exit condition in Looping Process Call?**
**A:** An infinite loop — the flow will run indefinitely, exhausting CPI tenant resources.

**Q8. How does Process Call differ from a Local Integration Process?**
**A:** Local Integration Process is the container (the called sub-flow). Process Call is the **invocation step** in the palette that calls a Local Integration Process.

#### Advanced Level

**Q9. Can Request-Reply be used inside an asynchronous iFlow?**
**A:** Yes. Even in asynchronous iFlows, you can use Request-Reply for specific synchronous calls within the flow. The outer flow remains asynchronous; the Request-Reply step creates a synchronous sub-call.

**Q10. What is the difference between Idempotent Process Call and Data Store for duplicate prevention?**
**A:** Idempotent Process Call is lightweight, stores only the unique key with a retention period, and is purpose-built for deduplication. Data Store stores full message payloads, is heavier, and is designed for persistence, retry, and async decoupling — not optimized for deduplication checks.

---

### 10.6 Routing

#### Basic Level

**Q1. What is the difference between Splitter and Multicast?**
**A:** Splitter divides one message into multiple different fragments for individual processing. Multicast sends the same message copy to multiple branches for parallel/sequential processing.

**Q2. What is the difference between Gather and Join?**
**A:** Gather synchronizes branches AND merges their payloads. Join only synchronizes (waits for all branches) without merging payloads.

**Q3. What condition does Router use for first-match behavior?**
**A:** Router evaluates conditions sequentially and executes the **first matching** branch. If no condition matches and no default route is defined, the message is dropped.

**Q4. When should you use Aggregator instead of Gather?**
**A:** When the number of messages to combine is dynamic or unknown, or when messages arrive independently (not from a Multicast). Gather is used only with Multicast for fixed, known branch counts.

**Q5. What is the difference between General Splitter and Iterating Splitter?**
**A:** General Splitter can support parallel processing of split messages. Iterating Splitter always processes sequentially — used when processing order matters.

#### Medium Level

**Q6. How do you design a parallel API call to three services and combine results?**
**A:** Use Parallel Multicast (3 branches, each with a Request-Reply to one API), then use Gather to collect all 3 responses and merge them using XPath or Groovy.

**Q7. What happens if one branch fails in a Parallel Multicast before the Join/Gather?**
**A:** By default, the entire flow fails. Best practice: add an Exception Subprocess inside each branch to handle failures gracefully and provide fallback/default responses.

**Q8. How do you recombine split messages after per-item processing?**
**A:** Use Aggregator after the Splitter with:
- Correlation Key = a unique batch/message identifier (e.g., OrderID)
- Completion Condition = number of split items
- Aggregation Strategy = merge logic

#### Advanced Level

**Q9. What can happen if the Aggregator correlation key is incorrectly configured?**
**A:** Messages from different business objects may be grouped together, causing data corruption or incorrect aggregated payloads — a critical production risk.

**Q10. How does Sequential Multicast differ from Parallel Multicast in terms of performance?**
**A:** Sequential Multicast executes branches one at a time; total time = sum of all branch durations. Parallel Multicast executes all branches simultaneously; total time = slowest branch duration. Parallel is preferred when branches are independent and performance matters.

---

### 10.7 Security

#### Basic Level

**Q1. What key is used in the Encryptor step?**
**A:** The **receiver's public key** (asymmetric encryption — only the receiver can decrypt with their private key).

**Q2. What key is used in the Signer step?**
**A:** The **sender's private key** (only the sender can sign with it; anyone with the public key can verify).

**Q3. What is the difference between encryption and digital signing?**
**A:** Encryption ensures confidentiality (data is hidden). Signing ensures integrity and authenticity (data is visible but tamper-evident and origin-verified).

**Q4. What signing formats are supported by the Signer step?**
**A:** PGP Signing, XML Digital Signature, PKCS#7.

**Q5. Where are certificates and keys stored in SAP CPI?**
**A:** In `Monitor → Manage Security → Keystore`.

#### Medium Level

**Q6. What is the recommended sequence when you need to both sign and encrypt a message?**
**A:** **Sign first, then Encrypt.** This ensures the signature is over the original plaintext and both integrity and confidentiality are maintained correctly.

**Q7. What happens if the private key is missing during decryption?**
**A:** The Decryptor step throws a keystore/decryption error and message processing fails.

**Q8. Can CPI handle PGP-encrypted inbound messages?**
**A:** Yes. Use the Decryptor step with PGP configured and the appropriate private key stored in the Keystore.

#### Advanced Level

**Q9. Design a scenario where a bank file must be sent securely from CPI.**
**A:** Sign the payload using CPI's private key (Signer step) → Encrypt using the bank's public key (Encryptor step) → Send via SFTP adapter. The bank decrypts with their private key and verifies the signature using CPI's public certificate.

**Q10. What is the difference between PKCS#7 encryption and PGP encryption in CPI?**
**A:** Both are asymmetric encryption formats. PKCS#7 (CMS) is X.509 certificate-based and common in enterprise B2B scenarios. PGP uses PGP key pairs and is common in file-based and legacy B2B exchanges. The choice depends on the partner's requirements.

---

### 10.8 Persistence

#### Basic Level

**Q1. What are the four Data Store operations in SAP CPI?**
**A:** **Write**, **Get**, **Select**, **Delete**. (Note: the operation is called "Write", not "Put".)

**Q2. What is the difference between Get and Select in Data Store?**
**A:** Get retrieves a **single** entry by a specific Entry ID. Select retrieves **multiple** entries based on conditions (creation time, headers, filters).

**Q3. Can Data Store be accessed across multiple iFlows?**
**A:** Yes, if both iFlows use the same Data Store name and have the required permissions.

**Q4. What happens when the retention period of a Data Store entry expires?**
**A:** The entry is automatically deleted by the CPI platform.

**Q5. What is the scope of Write Variables?**
**A:** Write Variables are scoped to the **current iFlow execution** only. They are not persisted after the iFlow run completes.

#### Medium Level

**Q6. How do you implement retry logic using Data Store?**
**A:**
1. Write failed message to Data Store
2. Schedule a Timer-triggered iFlow
3. Use Select to fetch failed entries
4. Reprocess each entry
5. Delete entry on success

**Q7. What is the difference between Data Store and Write Variables?**
**A:** Data Store is persistent and accessible across iFlows and executions. Write Variables are in-memory, scoped to the current execution, and lost when the iFlow ends.

**Q8. How do you access a Write Variable value in a Groovy script?**
**A:** `def varValue = message.getProperty("variableName")` — at runtime, Write Variables behave similarly to Exchange Properties.

#### Advanced Level

**Q9. What are the limitations of Data Store compared to JMS Queue?**
**A:** Data Store requires manual retrieval (pull-based), has no built-in message consumption mechanism, lacks complex querying capability, and is not designed for high-throughput event streaming. JMS provides automatic push-based consumption, ordering guarantees, and is better suited for asynchronous decoupling at scale.

**Q10. What is the risk of not deleting Data Store entries after processing?**
**A:** Data accumulation, storage quota exhaustion (CPI tenant limits), potential duplicate processing if the same entry is fetched again, and increased monitoring noise.

---

### 10.9 Validator

#### Basic Level

**Q1. What is the XML Validator used for?**
**A:** To validate an XML message against an XSD schema definition, ensuring correct structure, mandatory elements, data types, and namespaces are present.

**Q2. Where do you upload the XSD file for XML Validator?**
**A:** In the iFlow Resources section: `Resources → Add → XSD file`, then reference it in the XML Validator step.

**Q3. What happens if XML validation fails?**
**A:** The iFlow stops processing and throws a validation exception — handle via Exception Subprocess.

**Q4. What EDI standards are supported by the EDI Validator?**
**A:** ANSI X12 and EDIFACT. TRADACOMS is supported in limited scenarios.

**Q5. Can XML Validator validate business rules?**
**A:** No. XML Validator validates structural compliance only. Business rules require Groovy, Router conditions, or mapping logic.

#### Medium Level

**Q6. What is the difference between EDI Validator and XML Validator?**
**A:** EDI Validator validates EDI formats (X12/EDIFACT) against EDI schemas using segment-based validation. XML Validator validates XML against XSD using element-based validation.

**Q7. Where should the Validator be placed in an iFlow?**
**A:** Immediately after the sender adapter, before any mapping or transformation — to fail fast on invalid input.

**Q8. What are common causes of XML validation failure?**
**A:** Missing required elements, invalid data types, namespace mismatches, incorrect element hierarchy, or XSD and payload being out of sync.

#### Advanced Level

**Q9. How would you design a robust validation framework for high-volume B2B integrations?**
**A:**
1. Validate at the entry point (Validator step after adapter)
2. Capture validation errors in Exception Subprocess
3. Store failed payload in Data Store
4. Send structured notification to support/partner
5. Enable reprocessing mechanism via Data Store + Timer iFlow

**Q10. Can XML Validator validate namespaces?**
**A:** Yes. Namespace mismatches are detected by the XML Validator and cause a validation failure. XSD must correctly define and match the namespaces present in the payload.

---

## Quick Architecture Reference

### Palette Summary — All Steps

```
1. PROCESS
   ├── Integration Process
   ├── Exception Subprocess
   └── Local Integration Process

2. EVENTS
   ├── Message Start Event / Timer Start Event / None Start Event
   ├── Message End Event / None End Event
   ├── Error End Event / Escalation End Event / Terminate End Event
   └── Error Start Event (inside Exception Subprocess)

3. MAPPING
   ├── Message Mapping (+ Value Mapping inside)
   └── XSLT Mapping

4. TRANSFORMATION
   ├── Content Modifier
   ├── Converter (XML↔JSON↔CSV)
   ├── Encoder / Decoder
   ├── EDI Extractor
   ├── Filter
   ├── Message Digest
   ├── Script (Groovy / JavaScript)
   └── XML Modifier

5. CALL
   ├── External: Content Enricher | Poll Enrich | Request-Reply | Send
   └── Local:   Process Call | Idempotent Process Call | Looping Process Call

6. ROUTING
   ├── Aggregator
   ├── Gather
   ├── Join
   ├── Multicast (Parallel / Sequential)
   ├── Router
   └── Splitter (General / Iterating)

7. SECURITY
   ├── Encryptor / Decryptor (PGP, PKCS#7)
   └── Signer / Verifier (PGP, XML Sig, PKCS#7)

8. PERSISTENCE
   ├── Data Store Operations (Write | Get | Select | Delete)
   ├── Persist
   └── Write Variables

9. VALIDATOR
   ├── XML Validator (XSD-based)
   └── EDI Validator (X12 / EDIFACT)
```

---

### Critical Distinctions for Interviews

| Pair | Key Differentiator |
|---|---|
| Request-Reply vs Send | Sync vs Async; body replaced vs ignored |
| Content Enricher vs Request-Reply | Merge original vs replace body |
| Gather vs Join | Merge payloads vs synchronize only |
| Gather vs Aggregator | Fixed branches vs dynamic message count |
| Splitter vs Multicast | Different fragments vs same copy |
| General vs Iterating Splitter | Parallel possible vs always sequential |
| Data Store vs Write Variables | Persistent cross-iFlow vs in-memory current-execution |
| Filter vs Router | Pass/stop vs multi-path routing |
| Encryptor vs Signer | Confidentiality vs Integrity/Authenticity |
| XML Modifier vs Content Modifier | XML node structure vs headers/properties/body template |
| Message Mapping vs Converter | Structure+values transformation vs format-only conversion |
| Process Call vs Looping Process Call | Once vs repeated with condition |
| Idempotent Process Call vs Data Store | Key-based deduplication vs full payload persistence |

---

*This guide is validated against SAP Help Portal — SAP Integration Suite (Cloud Integration). All palette names, step labels, and operational behaviors reflect the official SAP Cloud Integration Web UI.*
