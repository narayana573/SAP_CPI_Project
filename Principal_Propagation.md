# 🔐 SAP CPI — Cloud Connector, FM Whitelisting & Principal Propagation

### *Simple, Easy Notes for SAP CPI Developers*

-----

## 🗺️ Big Picture — How Everything Connects

Before diving in, understand **where each topic fits**:

```
┌─────────────────────────────────────────────────────────────────┐
│                     SAP BTP / CPI (Cloud)                       │
│                    iFlow → RFC Receiver Adapter                  │
└──────────────────────────┬──────────────────────────────────────┘
                           │  Secure Tunnel
                           │  (HTTPS / Port 443 only — outbound)
┌──────────────────────────▼──────────────────────────────────────┐
│                  SAP CLOUD CONNECTOR (SCC)                      │   ← TOPIC 1: Setup
│              (Installed on On-Premise Network)                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │         FM WHITELIST (Access Control)                   │   │   ← TOPIC 2: FM Whitelisting
│  │   Only allowed FMs/BAPIs can be called from cloud       │   │
│  └─────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │         PRINCIPAL PROPAGATION                           │   │   ← TOPIC 3: Principal Propagation
│  │   Forwards real user identity to backend                │   │
│  └─────────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│              SAP On-Premise Backend (S/4HANA / ECC)             │
│                   BAPI / RFC executes here                       │
└─────────────────────────────────────────────────────────────────┘
```

-----

-----

# 📦 TOPIC 1 — SAP Cloud Connector (SCC) Setup

-----

## 🤔 What is the Cloud Connector?

> The Cloud Connector is a **secure bridge** between SAP BTP (cloud) and your on-premise SAP systems.

### Simple Analogy:

Your company has a **secure office building** (on-premise SAP). You work remotely (cloud / CPI). The Cloud Connector is the **secure VPN tunnel + security guard** that:

- Lets **only authorized cloud apps** reach specific on-premise systems
- Uses **outbound-only connections** from on-premise — no firewall port opening needed
- Acts as a **gatekeeper** — controls which systems and resources are accessible

-----

## 🔑 Key Concepts Before Setup

|Term              |Plain English Meaning                                               |
|------------------|--------------------------------------------------------------------|
|**Subaccount**    |Your CPI’s home address in BTP                                      |
|**Location ID**   |A label for SCC when you have multiple SCCs                         |
|**Virtual Host**  |A fake/alias hostname that cloud uses (hides real internal hostname)|
|**Internal Host** |The real SAP system hostname (e.g., `s4hana-prod.company.com`)      |
|**Access Control**|The whitelist of what cloud apps can access                         |
|**Resource**      |A specific BAPI, RFC, URL path, or service exposed through SCC      |

-----

## 🛠️ Cloud Connector Setup — Step by Step

### 🔷 Step 1: Install Cloud Connector

- Download from SAP Support Portal (SAP Cloud Connector installer)
- Install on a server **inside your corporate network** (on-premise side)
- SCC runs as a local web application — access it via browser:
  
  ```
  https://localhost:8443
  ```
- Default login: `Administrator` / `manage`
- ⚠️ **Change the password immediately** after first login!

-----

### 🔷 Step 2: Connect SCC to your BTP Subaccount

After login into SCC Admin Console:

1. Click **“Add Subaccount”**
1. Fill in:

|Field              |What to Enter                                               |
|-------------------|------------------------------------------------------------|
|**Region**         |Your BTP region (e.g., `cf.eu10.hana.ondemand.com`)         |
|**Subaccount Name**|Your BTP Subaccount ID (found in BTP Cockpit)               |
|**Display Name**   |Friendly label for this connection                          |
|**Subaccount User**|BTP user email                                              |
|**Password**       |BTP user password                                           |
|**Location ID**    |Optional — use if you have multiple SCCs for same subaccount|

1. Click **Save** → SCC will connect to BTP
1. You’ll see **Status: Connected** with a green indicator ✅

> 💡 **Verify in BTP Cockpit:** Go to BTP Cockpit → Connectivity → Cloud Connectors — you should see your SCC listed there.

-----

### 🔷 Step 3: Add Your On-Premise SAP System (System Mapping)

This tells SCC which SAP backend to expose:

1. In SCC, go to **Cloud to On-Premise → Access Control**
1. Click **”+”** to add a new system mapping
1. Fill in:

|Field             |Value                                                     |
|------------------|----------------------------------------------------------|
|**Back-end Type** |`ABAP System`                                             |
|**Protocol**      |`RFC` (for BAPI/RFC calls) or `HTTPS` (for OData/REST)    |
|**Internal Host** |Real hostname of SAP system (e.g., `s4hana.company.local`)|
|**Internal Port** |SAP Gateway port (e.g., `3300` for RFC, `44300` for HTTPS)|
|**Virtual Host**  |Alias name (e.g., `my-s4hana`) — this is what CPI uses    |
|**Virtual Port**  |Alias port (e.g., `sapgw00`)                              |
|**Principal Type**|`None` (basic auth) OR `Principal Propagation` (SSO)      |

1. Click **Finish** → System appears in the list
1. Status should show **“Reachable”** ✅

> ⚠️ **If Status = “Not Reachable”:** Check firewall, hostname, port, and that SAP system is running.

-----

### 🔷 Step 4: Add Resources (FM Whitelist — see Topic 2)

After adding the system, you must **explicitly allow** which BAPIs/FMs can be called. *(Covered in detail in Topic 2 below)*

-----

### 🔷 Step 5: Create RFC Destination in BTP Cockpit

This is the “address book” CPI uses to find your SAP system:

1. Go to **BTP Cockpit → Connectivity → Destinations**
1. Click **New Destination**
1. Fill in:

```
Name:          S4HANA_RFC_DEST       ← Used in CPI iFlow
Type:          RFC
Proxy Type:    OnPremise
Location ID:   (leave blank or match your SCC Location ID)
User:          RFC_USER              ← SAP technical user
Password:      ********
```

1. Add **Additional Properties**:

|Property Key       |Value      |Meaning              |
|-------------------|-----------|---------------------|
|`jco.client.ashost`|`my-s4hana`|Virtual Host from SCC|
|`jco.client.sysnr` |`00`       |SAP System Number    |
|`jco.client.client`|`100`      |SAP Client           |
|`jco.client.lang`  |`EN`       |Language             |

1. Click **Save** → Click **Check Connection** → Should show ✅ Connection OK

-----

### 🔷 Step 6: Use Destination in CPI iFlow

In your iFlow:

1. Add **Request-Reply** step
1. Set Receiver Adapter: **RFC**
1. In Connection tab:
- Destination Name: `S4HANA_RFC_DEST` ← matches BTP Destination name
1. In Processing tab:
- Enter BAPI/FM name

-----

## ✅ Cloud Connector Setup Summary

```
1. Install SCC on on-premise server → Access at https://localhost:8443
2. Connect SCC to BTP Subaccount → Status: Connected
3. Add System Mapping → Virtual Host → Internal Host → Status: Reachable
4. Add FM Resources (Whitelist) → Only listed FMs can be called
5. Create RFC Destination in BTP Cockpit → jco.client.* properties
6. Reference Destination in CPI iFlow RFC Adapter
```

-----

-----

# 📋 TOPIC 2 — FM Whitelisting in Cloud Connector

-----

## 🤔 What is FM Whitelisting?

> FM Whitelisting = **The security guard’s allowed-entry list** at the SCC gate.

Even if your SCC is connected and SAP system is reachable, **no BAPI or Function Module can be called** unless you explicitly add it to the whitelist (called “Resources” in SCC).

### Why This Security Exists:

- Prevents unauthorized access to ALL FMs in your SAP system
- Forces developers to consciously declare which functions are exposed
- One of the most common causes of errors in CPI-RFC integration!

> ⚠️ **Most Common CPI Error:**
> 
> ```
> "Expose the function module in your Cloud Connector in case it was a valid request"
> ```
> 
> **This means the FM is NOT whitelisted!**

-----

## 📍 Where to Whitelist — In SCC Admin Console

**Path:** Cloud to On-Premise → Access Control → Select your System → Resources tab → Click “+”

-----

## ➕ How to Add a Resource (Whitelist an FM)

1. In SCC Admin Console → **Cloud to On-Premise → Access Control**
1. Select your mapped SAP system
1. In the lower panel, click **”+”** under Resources
1. Fill in:

|Field                   |Option                                      |When to Use                        |
|------------------------|--------------------------------------------|-----------------------------------|
|**Function Module Name**|Exact name: `BAPI_SALESORDER_CREATEFROMDAT2`|Single specific BAPI               |
|**Function Module Name**|Prefix: `BAPI_SALESORDER_`                  |All BAPIs starting with this prefix|
|**Function Module Name**|Prefix: `BAPI_`                             |ALL standard BAPIs                 |
|**Function Module Name**|Prefix: `Z_`                                |ALL custom Z FMs                   |
|**Enabled**             |✅ Check this                                |Must be enabled                    |

1. Click **Save**

-----

## 📋 Recommended FMs to Whitelist for CPI (RFC Adapter)

When using the **RFC Receiver Adapter with BAPI**, always whitelist these as a minimum:

|Function Module                                        |Why It Must Be Whitelisted                                                                    |
|-------------------------------------------------------|----------------------------------------------------------------------------------------------|
|`BAPI_TRANSACTION_COMMIT`                              |**Mandatory** — Called automatically when “Send Confirm Transaction” is enabled in RFC Adapter|
|`BAPI_TRANSACTION_ROLLBACK`                            |**Mandatory** — Called automatically on error                                                 |
|Your main BAPI (e.g., `BAPI_SALESORDER_CREATEFROMDAT2`)|Your actual business function                                                                 |
|`RFC_PING`                                             |Optional — useful for connection testing                                                      |
|`RFC_GET_SYSTEM_INFO`                                  |Optional — useful for testing connectivity                                                    |


> 💡 **Pro Tip:** If you enable “Send Confirm Transaction” in the CPI RFC Adapter but forget to whitelist `BAPI_TRANSACTION_COMMIT` in SCC, you’ll get a **denial error** even if your main BAPI is whitelisted!

-----

## 🏷️ Naming Policy Options — Exact vs Prefix

|Naming Policy   |Example Entry                   |What It Exposes                           |
|----------------|--------------------------------|------------------------------------------|
|**Exact Name**  |`BAPI_SALESORDER_CREATEFROMDAT2`|Only this one BAPI                        |
|**Prefix**      |`BAPI_SALESORDER_`              |All BAPIs starting with `BAPI_SALESORDER_`|
|**Prefix**      |`BAPI_`                         |All standard BAPIs                        |
|**Prefix**      |`Z_MY_PROJECT_`                 |All custom FMs for your project           |
|**Wildcard `*`**|`*`                             |ALL FMs — ⛔ Never use in production!      |


> ⚠️ **Security Warning:** Using `*` (wildcard) exposes ALL Function Modules in your SAP system to the cloud. This is a **critical security risk**. Always use specific names or scoped prefixes.

-----

## 🔍 Common Whitelist Errors & Fixes

|Error Message                                         |Cause                                      |Fix                                           |
|------------------------------------------------------|-------------------------------------------|----------------------------------------------|
|`Expose the function module in your Cloud Connector`  |FM not in whitelist                        |Add FM name or prefix to SCC Resources        |
|`Denying request for resource BAPI_TRANSACTION_COMMIT`|Commit FM not whitelisted                  |Add `BAPI_TRANSACTION_COMMIT` to SCC resources|
|`Error Getting Function`                              |FM exists in SAP but not whitelisted in SCC|Add exact FM name in SCC whitelist            |
|Works in Dev but not in Prod                          |Different SCC configurations per landscape |Check whitelist in Production SCC separately  |

-----

## 📌 Whitelist Strategy for CPI Projects

```
Recommended Approach:
──────────────────────
✅ Add specific BAPI names for each integration
✅ Add BAPI_TRANSACTION_COMMIT and BAPI_TRANSACTION_ROLLBACK always
✅ Use prefix (e.g., BAPI_CUSTOMER_) for a group of related BAPIs
✅ For Z FMs — use Z_PROJECTNAME_ prefix

❌ NEVER use * wildcard in production
❌ Don't add entire BAPI_ prefix unless all BAPIs should be accessible
```

-----

-----

# 👤 TOPIC 3 — Principal Propagation

-----

## 🤔 What is Principal Propagation?

> **Principal Propagation = Forwarding the real user’s identity from cloud to on-premise SAP**

### The Problem It Solves:

**Without Principal Propagation:**

```
User A (John) ──▶ CPI iFlow ──▶ SCC ──▶ SAP Backend
                                         Logs in as: RFC_TECHUSER
                                         (All users look the same!)
```

**With Principal Propagation:**

```
User A (John) ──▶ CPI iFlow ──▶ SCC ──▶ SAP Backend
                                         Logs in as: JOHN
                                         (SAP knows it's John!)
                                         (John's authorizations apply!)
```

### Simple Analogy:

Without PP → You use a **master key card** to enter a hotel. Everyone gets the same access.
With PP → You use **your personal key card**. You can only access your own room and permitted areas.

-----

## 🎯 When Do You Need Principal Propagation?

|Scenario                                                             |Need PP?             |
|---------------------------------------------------------------------|---------------------|
|CPI calls BAPI with a fixed technical user                           |❌ No — use Basic Auth|
|End-user triggers iFlow and SAP must apply that user’s authorizations|✅ Yes                |
|Audit trail in SAP must show the real user’s name                    |✅ Yes                |
|SAP data must be filtered by user’s authorization objects            |✅ Yes                |
|Background / scheduled jobs calling SAP                              |❌ No — use Basic Auth|
|SSO (Single Sign-On) across cloud and on-premise                     |✅ Yes                |

-----

## 🔄 How Principal Propagation Works — Step by Step Flow

```
1. User logs into sender app with their identity (e.g., john@company.com)
        │
        ▼
2. User's identity converted to JWT Token (JSON Web Token) or SAML assertion
   on SAP BTP / IAS (Identity Authentication Service)
        │
        ▼
3. CPI iFlow receives the request WITH the user's token
        │
        ▼
4. CPI passes the token to the Cloud Connector via the RFC Receiver Adapter
   (with Principal Propagation enabled in the BTP Destination)
        │
        ▼
5. Cloud Connector reads the user info from the token
   Creates a short-lived X.509 Certificate with the user's identity
   (The certificate says: "This request is from JOHN")
        │
        ▼
6. Cloud Connector sends the request to SAP backend with this X.509 certificate
   in the HTTP header: SSL_CLIENT_CERT
        │
        ▼
7. SAP Backend (ICM / SNC layer) reads the certificate
   Maps the cloud user identity to a local SAP user
   (e.g., john@company.com → SAP user: JOHN)
        │
        ▼
8. BAPI/RFC executes as JOHN — with JOHN's authorizations!
```

-----

## 🏗️ Three Certificates Needed — Simplified

Principal Propagation requires **3 certificates** to establish trust. Think of them as:

|Certificate                     |Role                                       |Simple Meaning                                |
|--------------------------------|-------------------------------------------|----------------------------------------------|
|**System Certificate**          |Identity card of the Cloud Connector itself|“I am the Cloud Connector — trust me”         |
|**CA Certificate (Local CA)**   |Certificate-signing authority in SCC       |“I (SCC) sign and vouch for user certificates”|
|**Short-lived User Certificate**|Temporary ID for each user                 |“This request is from JOHN — valid for 1 hour”|


> 💡 **For PoC/Dev:** Use **self-signed certificates** (SCC can generate these — easier)
> 💡 **For Production:** Use **CA-signed certificates** from your corporate CA or a trusted Certificate Authority

-----

## ⚙️ Principal Propagation Setup — Step by Step

### 🔷 Step 1: Configure System Certificate in SCC

1. SCC Admin Console → **Configuration → On Premise tab**
1. Under **System Certificate** → Click **“Create and import a self-signed certificate”**
1. Fill in:
- **Common Name (CN):** SCC server name (e.g., `scc-server.company.com`)
- **Organization, Country** etc.
1. Click **Create** → Certificate is generated
1. **Download** the certificate (`.der` format)
- This will be uploaded to your SAP backend in STRUST

-----

### 🔷 Step 2: Configure CA Certificate in SCC

1. Still in **Configuration → On Premise tab**
1. Under **CA Certificate** → Click **“Create and import a self-signed certificate”**
1. Fill in the details (similar to System Certificate)
1. Click **Create**
1. **Download** the CA certificate (`.der` format)
- This is also uploaded to SAP backend in STRUST

> 💡 The CA Certificate is what SCC uses to **sign** the short-lived user certificates. SAP backend needs it to **verify** those user certificates.

-----

### 🔷 Step 3: Upload Certificates to SAP Backend (STRUST)

In your SAP on-premise system:

1. Go to TCode: **STRUST** (Trust Manager)
1. Select **SSL Client (Anonymous)** or **SNC SAPCryptolib** node
1. **Import** the SCC System Certificate and CA Certificate:
- Double-click the SSL node → **Import Certificate** → Upload the `.der` file
- Click **Add to Certificate List**
- Click **Save**

-----

### 🔷 Step 4: Configure User Mapping in SAP Backend

SAP must know how to map the cloud user identity to a local SAP user:

1. Go to TCode: **CERTRULE** (Certificate Mapping Rules) or **SM59**
1. Create a mapping rule that says:
- If certificate attribute = `email` matches → map to SAP user with that email
- Example: `john@company.com` → SAP user `JOHN`
1. Ensure the SAP user exists with the **same email** as the BTP/IAS user

-----

### 🔷 Step 5: Configure Principal Propagation in SCC (Cloud to On-Premise)

1. SCC Admin Console → **Cloud to On-Premise → Principal Propagation tab**
1. Click **Synchronize** — SCC fetches the trusted Identity Providers from BTP
1. Select the Identity Provider (IAS or SAP ID Service)
1. Set the **attribute to use for user mapping** (e.g., `email` or `login name`)
1. Click **Save**

-----

### 🔷 Step 6: Set Principal Type in System Mapping

1. SCC → Cloud to On-Premise → Access Control
1. Edit your SAP System mapping
1. Set **Principal Type** = `Principal Propagation`
1. Save

-----

### 🔷 Step 7: Update BTP Destination for Principal Propagation

In BTP Cockpit → Connectivity → Destinations:

1. Open your RFC Destination
1. Add/Change the **Authentication** field:

|Property          |Value                 |
|------------------|----------------------|
|**Authentication**|`PrincipalPropagation`|
|**Proxy Type**    |`OnPremise`           |

1. Save → Test connection

-----

### 🔷 Step 8: iFlow Configuration (CPI Side)

In your CPI iFlow:

- Ensure the **incoming request carries the user’s identity** (JWT or SAML token)
- RFC Receiver Adapter will automatically use Principal Propagation if the destination is configured for it
- No special coding needed in the iFlow itself — it’s handled by the adapter + destination

-----

## ⚠️ Common Principal Propagation Issues & Fixes

|Problem                                   |Likely Cause                                      |Fix                                                                      |
|------------------------------------------|--------------------------------------------------|-------------------------------------------------------------------------|
|`User not mapped` error                   |No CERTRULE mapping or user email mismatch        |Check CERTRULE in SAP, ensure emails match in BTP and SAP                |
|`Certificate not trusted`                 |System cert not uploaded to STRUST                |Import SCC System + CA cert into STRUST correctly                        |
|`Authentication failed`                   |ICM not restarted after STRUST changes            |Restart ICM via TCode: `SMICM → Administration → Hard Shut Down → Global`|
|`PP works in Dev, fails in Prod`          |Separate SCC instances have different certificates|Redo certificate setup in each landscape separately                      |
|User gets generic data instead of personal|PP not enabled in BTP Destination                 |Change Authentication to `PrincipalPropagation` in Destination           |

-----

## 📊 Principal Propagation vs Basic Authentication — When to Use What

|Aspect                   |Basic Authentication                                  |Principal Propagation                                       |
|-------------------------|------------------------------------------------------|------------------------------------------------------------|
|**Who logs in to SAP?**  |Fixed technical user (e.g., `RFC_USER`)               |Real end-user (e.g., `JOHN`)                                |
|**SAP Authorizations**   |Same for all — technical user’s authorizations        |Per-user — each person’s own authorizations                 |
|**Audit Log in SAP**     |Shows technical user name                             |Shows real user name                                        |
|**Setup Complexity**     |✅ Simple                                              |⚠️ Complex (certs, mapping, STRUST)                          |
|**Best For**             |Background jobs, scheduled processes, system-to-system|User-triggered apps, SSO scenarios, data visibility per user|
|**CPI BTP Destination**  |`Authentication = BasicAuthentication`                |`Authentication = PrincipalPropagation`                     |
|**Certificates Required**|❌ No                                                  |✅ Yes (3 certificates)                                      |

-----

-----

# 🔄 All Three Topics Together — The Complete Flow

```
USER (john@company.com) triggers a request
         │
         ▼
SAP CPI iFlow receives it
  ─ RFC Receiver Adapter reads Destination Name
  ─ Destination = S4HANA_RFC_DEST (BTP Cockpit)
         │
         ▼
BTP Destination: Authentication = PrincipalPropagation
  ─ User's JWT token forwarded
         │  Secure Tunnel (HTTPS outbound)
         ▼
CLOUD CONNECTOR (SCC)
  ─ Receives request + user token
  ─ Checks: Is this FM in the WHITELIST? ────────── FM Whitelisting check!
      ├── YES → Continue
      └── NO  → Error: "Expose the function module..."
  ─ Principal Propagation:
      Creates short-lived X.509 cert for JOHN
      Sends to SAP backend with SSL_CLIENT_CERT header
         │
         ▼
SAP ON-PREMISE BACKEND
  ─ ICM reads X.509 cert
  ─ Maps john@company.com → SAP user JOHN (via CERTRULE)
  ─ BAPI executes AS JOHN
  ─ JOHN's authorizations apply
  ─ Returns result
         │
         ▼
BACK through SCC → CPI → User receives response
```

-----

# 🎯 Quick Reference Cheat Sheet

## Cloud Connector Setup

|Step|Action                      |Where                                          |
|----|----------------------------|-----------------------------------------------|
|1   |Install SCC                 |On-premise server                              |
|2   |Connect to BTP Subaccount   |SCC Admin Console → Add Subaccount             |
|3   |Add System Mapping          |Cloud to On-Premise → + → Virtual/Internal Host|
|4   |Add FM Resources (Whitelist)|Select System → Resources → +                  |
|5   |Create RFC Destination      |BTP Cockpit → Connectivity → Destinations      |
|6   |Use Destination in iFlow    |RFC Receiver Adapter → Destination Name        |

## FM Whitelisting — Must-Have FMs for BAPI calls

|FM to Whitelist            |Why                                                  |
|---------------------------|-----------------------------------------------------|
|Your main BAPI             |The actual business function                         |
|`BAPI_TRANSACTION_COMMIT`  |Auto-called by RFC Adapter (Send Confirm Transaction)|
|`BAPI_TRANSACTION_ROLLBACK`|Auto-called on error                                 |
|`RFC_PING`                 |Connection testing                                   |

## Principal Propagation — Key Setup Points

|Step|Action                          |TCode/Location                        |
|----|--------------------------------|--------------------------------------|
|1   |Create System Certificate       |SCC → Configuration → On Premise      |
|2   |Create CA Certificate           |SCC → Configuration → On Premise      |
|3   |Upload certs to SAP             |STRUST                                |
|4   |Map cloud user to SAP user      |CERTRULE                              |
|5   |Enable PP in SCC system mapping |Principal Type = Principal Propagation|
|6   |Set BTP Destination auth        |Authentication = PrincipalPropagation |
|7   |Restart ICM after STRUST changes|SMICM                                 |

-----

*Notes for SAP CPI Role | SAP Cloud Connector + Principal Propagation + FM Whitelisting*
*Key TCodes: STRUST · CERTRULE · SMICM · SM59 · SE37 · BAPI*
*SCC URL: <https://localhost:8443> | BTP Cockpit: <https://cockpit.btp.cloud.sap>*
