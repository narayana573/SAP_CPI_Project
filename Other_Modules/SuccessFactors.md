# SAP CPI & SuccessFactors Integration — Interview Preparation Notes

---


### Adapters to Know
- **HTTP / HTTPS Adapter** — REST-based integration
- **OData Adapter** — SAP S/4HANA, SuccessFactors API
- **SOAP Adapter** — Legacy SAP ECC / BAPIs
- **SFTP Adapter** — File-based integration
- **SuccessFactors Adapter** — Native SF OData V2/V4
- **IDOC Adapter** — SAP-to-SAP IDoc messaging
- **JDBC Adapter** — Database integration
- **Mail Adapter** — Email notification

### Security in CPI
- **OAuth 2.0** (Client Credentials, Authorization Code) — used for SuccessFactors
- **Basic Authentication** — username/password
- **Certificate-based Authentication** — mutual TLS
- **Keystore Management** — stores certificates, keys, OAuth credentials
- **Security Artifact** — user credentials, OAuth config deployed separately



---

## 3. SuccessFactors (SF) Modules — What Are They, Really?

> 💡 **Think of SuccessFactors as a big HR software suite** — like a set of apps that together manage everything about an employee from the day they apply for a job to the day they leave the company. Each "module" handles one part of that journey.

---

### 🗂️ The Modules — Plain English Guide

| Module | Short Name | What It Does (Simply Put) |
|--------|-----------|--------------------------|
| **`Employee Central`** | `EC` | The "heart" of SF — stores every employee's basic info: who they are, what role they hold, which department they belong to, and their salary details |
| **`Recruiting Management`** | `RCM` | Manages job postings, job applications, interviews, and sending out offer letters — basically the "hiring" app |
| **`Onboarding`** | `ONB` | Once someone is hired, this module handles the paperwork, welcome tasks, and getting them set up before Day 1 |
| **`Performance & Goals`** | `PMGM` | Where employees set their yearly targets and managers rate their performance — like a digital appraisal system |
| **`Learning Management System`** | `LMS` | Manages training programs, courses, certifications, and tracks who completed which training |
| **`Compensation`** | `COMP` | Handles salary revisions, bonuses, and pay planning — used during appraisal cycles |
| **`Succession & Development`** | `SDP` | Identifies future leaders and builds a "backup plan" if key people leave — talent pipeline management |
| **`Workforce Analytics`** | `WFA` | Gives HR managers dashboards and reports — like headcount, attrition rates, diversity numbers |
| **`Time & Attendance`** | `TM` | Tracks employee working hours, leaves, absences, and holiday calendars |

---

### 🏗️ Employee Central — The Core of Everything

> 💡 **Think of `Employee Central` (`EC`) like a filing cabinet** at the HR office. Every employee has a folder. Inside that folder are different sections — personal details, job details, address, salary, etc. Each section is called a **"Data Object"**.

#### Key Data Objects Inside EC (What Each "Section" Holds)

| Data Object | What It Stores | Real-World Analogy |
|-------------|---------------|-------------------|
| **`Person`** | The unique identity of a human being — their permanent ID in the system | Like an Aadhar card number — never changes even if the job changes |
| **`Employment`** | The employment relationship — when they joined, employment type (full-time/contract) | Like a joining letter tied to a specific company |
| **`Job Information`** | Current role, department, location, reporting manager, job title | Like the "current designation" section on a business card |
| **`Personal Information`** | Legal name, date of birth, gender, nationality | Like a passport details page |
| **`Address Information`** | Home address, correspondence address | Residential proof in documents |
| **`Compensation Information`** | Current salary, pay grade, currency | The CTC details in an offer letter |
| **`Position`** | The "seat" or "slot" in the org chart — separate from the person sitting in it | Like a job vacancy — it exists even if no one is in it |

---

### 🔗 How These Objects Relate to Each Other

> 💡 **Simple analogy:** A `Person` is like a human being. That human can have one or more `Employment` records (e.g., joined, left, rejoined). Under each employment, you have their `Job Info`, `Salary`, `Personal Details`, etc.

```
👤 Person  (has a permanent unique ID — PersonIdExternal)
    └── 💼 Employment  (tied to a userId — one per company relationship)
              ├── 🏢 Job Information      → role, department, manager
              ├── 💰 Compensation Info    → salary, pay grade
              ├── 📋 Personal Information → name, DOB, gender
              └── 🕐 Time Information     → work schedule, time zone
```

---

## 4. SuccessFactors APIs — How Systems "Talk" to SF

> 💡 **What is an API?** Imagine SF is a restaurant kitchen. An **`API`** (Application Programming Interface) is the waiter — you place an order (send a request), and the waiter brings back food (returns data). You don't go into the kitchen yourself; the waiter handles it.

---

### 📡 OData V2 API — The Main Way to Read/Write SF Data

- **`OData`** stands for **Open Data Protocol** — it's a standard way to read and update data over the internet
- Think of it like a structured web address (URL) you call to get or send employee data
- Base address looks like: `https://your-sf-system.com/odata/v2/`
- You can do **`CRUD`** operations:
  - **`C`reate** → Add a new employee record
  - **`R`ead** → Fetch existing employee data
  - **`U`pdate** → Modify existing data
  - **`D`elete** → Remove a record

**Real Example — Fetching an Employee's Job Info:**
```
GET /odata/v2/EmpJob?$filter=userId eq 'EMP001'&$format=json
```
> 🗣️ In plain English: *"Give me the job details of the employee whose ID is EMP001, and send it back as JSON format."*

**Commonly Used OData Entities (Tables you'll query):**
| Entity Name | What It Represents |
|-------------|-------------------|
| `PerPerson` | Basic person record |
| `EmpEmployment` | Employment details |
| `EmpJob` | Job information (role, department) |
| `EmpCompensation` | Salary and pay details |
| `User` | Login/system user record |

---

### 📡 OData V4 API — The Newer Version

- Upgraded version of `OData V2` — more powerful filtering, better performance
- Used in newer SF modules
- Supports **`Delta Queries`** natively — fetch only records that changed

---

### 📡 SFAPI — The Old Way (Legacy SOAP API)

> 💡 **`SFAPI`** is the older method of communicating with SuccessFactors. Instead of a simple web URL, it uses **`SOAP`** — a more complex, envelope-style XML message format. Think of it as sending a formal letter vs. sending a quick text (OData).

- Still used for some older SF modules
- Operations: `query`, `upsert` (insert or update), `insert`, `update`, `delete`
- Being gradually replaced by OData APIs

---

### 📦 Compound Employee API — The "Everything in One Box" API

> 💡 **Normal APIs fetch one type of data at a time.** The **`Compound Employee API`** is special — it bundles together all employee data (personal info + job info + compensation + address) into **one single response**. Like getting an employee's complete HR file in one go instead of opening each drawer separately.

- Returns data in **`XML`** format
- Perfect for **bulk replication** — sending thousands of employees to SAP HCM or S/4HANA
- Much more efficient than making 5 separate API calls per employee

---

### 🔐 How Authentication Works — OAuth 2.0 SAML Bearer Flow

> 💡 **Authentication = Proving you are who you say you are.** Before any system can read/write SF data, it must prove its identity. SF uses **`OAuth 2.0`** for this — a secure "token-based" login system. Here's how it works step by step, in plain English:

```
Step 1️⃣ → Your system creates a special "identity proof" called a SAML Assertion
           (Think: a signed letter saying "I am System X, here's my digital signature")

Step 2️⃣ → Send this to SF's token endpoint
           SF verifies the signature and hands back an Access Token
           (Think: SF checking your ID and giving you a visitor badge)

Step 3️⃣ → Use this Access Token in every API call
           (Think: showing your visitor badge at every door you open)
```

| Term | Plain English Meaning |
|------|-----------------------|
| **`OAuth 2.0`** | An industry-standard secure login framework used between systems |
| **`SAML Assertion`** | A digitally signed XML document that proves the identity of the calling system |
| **`Access Token`** | A temporary password given after login — must be included in every API request |
| **`Bearer Token`** | The format used to send the access token: `Authorization: Bearer <token>` |
| **`Token Endpoint`** | The SF URL where you exchange your SAML assertion for an access token |

---

## 5. SAP CPI ↔ SuccessFactors Integration Patterns

### Common Integration Scenarios

| Scenario | Direction | Frequency | Method |
|----------|-----------|-----------|--------|
| New Hire Replication | SF → SAP HCM/S4 | Event / Schedule | OData + IDoc/BAPI |
| Org Structure Sync | SF → SAP | Scheduled | Compound API |
| Payroll Data Push | SAP → SF | Scheduled | OData Write |
| Job Posting Sync | SAP → SF RCM | Event | HTTP/OData |
| Cost Center Sync | SAP → SF EC | Scheduled | OData Upsert |
| Rehire Detection | SF → SAP | Event | Delta Query |
| Learning Completions | SF LMS → External | Scheduled | OData Read |

### Pre-Packaged Integration Content
SAP provides **Integration Packages** on SAP Business Accelerator Hub:
- **SAP SuccessFactors Employee Central to SAP ERP**: replication of EC data to SAP HR
- **SAP ERP to SuccessFactors**: cost centers, org units to SF
- These are iFlow templates — customizable for customer requirements

### Typical New Hire Flow (SF EC → SAP HCM)
```
SF EC (New Hire Event)
  → CPI Trigger (Timer / Event-based)
    → SF OData Call (Compound Employee API)
      → XML/JSON Mapping (SF → IDoc PERNR format)
        → IDOC Send to SAP HCM
          → SAP Infotype Creation (0000, 0001, 0002...)
```

---

## 6. APIs, Web Services & Middleware Concepts

### REST vs SOAP
| | REST | SOAP |
|--|------|------|
| Protocol | HTTP | HTTP/HTTPS/SMTP |
| Format | JSON / XML | XML only |
| Standard | Architectural style | Protocol |
| Used in SF | OData V2/V4 (REST-based) | SFAPI (legacy) |
| Security | OAuth, API Keys | WS-Security |

### Key HTTP Methods
- **GET** — Read data
- **POST** — Create new record
- **PUT** — Full update
- **PATCH** — Partial update
- **DELETE** — Remove record

### JSON vs XML
- SuccessFactors OData returns JSON or XML (use `$format=json`)
- SAP IDoc is XML-based
- Mappings in CPI convert between formats

### Middleware Concepts
- **Message Queue**: Decouples sender and receiver (JMS in CPI)
- **Event-driven Integration**: Trigger on data change (webhooks, polling delta)
- **Delta Processing**: Only send records that changed since last run (using `lastModifiedDateTime`)
- **Idempotency**: Ensure same message processed once even if received multiple times

### Key CPI Middleware Patterns
- **Splitter**: Split large payload into individual messages
- **Aggregator**: Combine multiple messages into one
- **Multicast**: Send same message to multiple receivers
- **Request-Reply**: Synchronous call and response
- **Content-Based Router**: Route based on message content

---

## 7. SF Data Flows — Deep Dive (Plain English)

### 🔄 The Employee Lifecycle — What Happens at Each Stage

> 💡 Think of this as the **complete journey of an employee** — from applying for a job to eventually leaving. Each stage is handled by a different SF module, and every change triggers data that must flow to SAP systems via CPI.

```
📋 Apply      → 🎯 Hire       → 🚪 Join        → 🔄 Transfer   → ⬆️ Promote    → 🚶 Leave
   RCM            RCM/ONB          EC                EC               EC              EC
   └──────────────────────────── CPI sends data to SAP HCM / S4HANA at each stage ──────┘
```

**What triggers an integration at each stage:**

| Stage | SF Module | What data moves to SAP |
|-------|-----------|------------------------|
| Job offer accepted | `RCM` → `ONB` | Candidate becomes a pre-hire record |
| Employee joins (Day 1) | `ONB` → `EC` | Full employee record created in EC |
| Transfer to new dept | `EC` | Updated `Job Information` sent to SAP |
| Salary revision | `EC` (Compensation) | New salary pushed to SAP Payroll |
| Resignation / Termination | `EC` | End-of-employment record sent to SAP |

---

### ⏱️ Delta Query — "Only Tell Me What Changed"

> 💡 Imagine you have 50,000 employees. You don't want to re-send all 50,000 records every night — that's wasteful. A **`Delta Query`** says: *"Only give me employees whose data changed since yesterday."*

**How it works in SF OData:**
```
GET /odata/v2/EmpJob?$filter=lastModifiedDateTime gt datetime'2024-01-01T00:00:00'
```
> 🗣️ Plain English: *"Give me all job records that were modified after 1st January 2024."*

- After each run, save the **timestamp** of the last successful sync
- Next run, use that timestamp as the filter — you only get the **delta** (the changes)
- In CPI, this timestamp is stored as a **`Global Variable`** or in an external store

---

### 📅 Effective Dating — "Changes Don't Happen Immediately"

> 💡 In real HR, a promotion might be decided today but officially effective from the 1st of next month. **`Effective Dating`** in EC means every record change is tagged with the date it becomes "active" — the system holds past, present, and future records simultaneously.

**Why this matters for integration:**
- When you query SF, you need to say *"give me the record as of today"* using the **`asOfDate`** parameter
- If you don't specify, you may get a future-dated or expired record
- When replicating to SAP, you must decide: send only current record, or send full history?

| Concept | Plain English |
|---------|--------------|
| **`Effective Date`** | The date from which a change becomes officially active |
| **`asOfDate`** | A filter parameter to query what the record looked like on a specific date |
| **Future-dated record** | A change saved today but effective from a future date |
| **Historical record** | A past version of data that has since been superseded |

---

## 8. Technical Concepts — Quick Reference

### Groovy Scripting in CPI
```groovy
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body = message.getBody(String.class)
    def headers = message.getHeaders()
    def properties = message.getProperties()
    
    // Modify body
    message.setBody(body.replaceAll("OLD", "NEW"))
    
    // Set property
    message.setProperty("customProp", "value")
    
    return message
}
```

### XPath in CPI (used in Router conditions)
```xpath
/ns0:EmployeeData/PersonalInfo/FirstName = 'John'
```

### XSLT Mapping (basic)
```xml
<xsl:template match="/">
  <Employee>
    <n><xsl:value-of select="//FirstName"/></n>
  </Employee>
</xsl:template>
```

### CPI Externalized Parameters
- Variables that can be changed at runtime without redeploying iFlow
- Configured in **Externalized Parameters** tab
- Useful for environment-specific values (host URLs, credentials)

---

## 9. Monitoring & Operations

### Message Processing Log (MPL)
- Found in **Operations → Monitor → Message Processing**
- States: Completed, Failed, Retry, Discarded
- Contains: message ID, timestamps, trace logs (if enabled)

### Trace Level
- Enable **Trace** on iFlow for detailed step-by-step message inspection
- Useful for debugging but should not be left on in production

### Alerting
- Configure alerts in CPI Operations
- Send email notifications on failure

### Common Failure Causes
| Error | Cause | Fix |
|-------|-------|-----|
| Authentication failed | Wrong OAuth credentials | Refresh token / update keystore |
| 404 Not Found | Wrong OData entity URL | Verify endpoint |
| Mapping failed | Unexpected null/empty field | Add null checks in mapping |
| Connection timeout | Network/firewall | Check host/port, whitelist IP |
| CSRF token error | Missing CSRF fetch step | Add GET for CSRF token before POST |

---

## 10. Interview Tips & Scenarios

### Behavioral / Scenario Questions

**"Walk me through a CPI-SF integration you've built."**
- Describe the business need (e.g., new hire replication)
- Explain the iFlow design steps
- Mention the adapter/API used
- Describe any transformation/mapping logic
- Explain monitoring and error handling

**"How would you handle high-volume employee data replication?"**
- Use **Compound Employee API** for bulk extraction
- Implement **Splitter** to process records individually
- Use **JMS Queue** for reliable async processing
- Schedule during off-peak hours

**"How do you secure integrations in CPI?"**
- OAuth 2.0 credentials stored in **Security Artifacts**
- HTTPS endpoints only
- Certificate-based mutual auth for SAP-to-SAP
- Never hardcode credentials in iFlows

**"How do you manage environment-specific configuration?"**
- Use **Externalized Parameters** for dev/QA/prod configs
- Separate **Integration Packages** per landscape or use configuration maps

**"What is CSRF token handling in SF OData?"**
- SF OData requires CSRF token for write operations (POST/PUT/DELETE)
- Step 1: Send GET with header `X-CSRF-Token: Fetch` → receive token
- Step 2: Include token in header `X-CSRF-Token: <received_value>` in write request
- In CPI: use a Request-Reply step before the write call

---

## 11. 📖 Key Terms Glossary — Plain English Definitions

> All technical terms are `highlighted` for easy identification.

| Term | Plain English Definition |
|------|--------------------------|
| `iFlow` | A visual "recipe" in SAP CPI that defines step-by-step how data moves from one system to another |
| `Adapter` | A plug that connects CPI to an external system — like a universal travel adapter for different countries' sockets |
| `OData` | A standard web-based language for asking a system to give or update data — like a universal ordering format for APIs |
| `OData V2 / V4` | Two versions of the OData standard; V4 is newer and more powerful |
| `MPL` | Message Processing Log — the "diary" CPI keeps of every message it handled, including errors |
| `EC` | Employee Central — the core HR module in SF where all employee master data lives |
| `SFAPI` | The older, SOAP-based way to communicate with SF — being replaced by OData |
| `Compound Employee API` | An SF API that returns all data about an employee (personal, job, salary) in one single call |
| `Effective Dating` | An EC concept where every data change has a start date — you can store past, present, and future records at the same time |
| `Delta Query` | A smart query that says "only give me records that changed since last time I asked" — avoids full data dumps |
| `lastModifiedDateTime` | A timestamp field in SF used to filter only recently changed records |
| `asOfDate` | A parameter in SF queries to get data as it was on a specific date |
| `JMS` | Java Message Service — a queue system that holds messages temporarily so they are not lost if the receiver is busy |
| `CSRF Token` | A security code SF requires before allowing any data-write operation — prevents unauthorized changes |
| `Keystore` | A secure digital vault inside CPI where certificates and OAuth credentials are stored |
| `OAuth 2.0` | A modern, secure way for one system to prove its identity to another without sharing passwords |
| `SAML Assertion` | A digitally signed XML document used to prove identity during OAuth login with SF |
| `Access Token` | A temporary "visitor pass" given after successful login — must be sent with every API request |
| `Bearer Token` | The format for sending an access token in an API request header |
| `IDoc` | Intermediate Document — SAP's own format for passing structured business data between SAP systems |
| `BAPI` | Business API — a standard SAP function that can be called from outside to perform business operations |
| `PersonIdExternal` | The unique, permanent ID assigned to a person in SF — never changes even if they change jobs or rejoin |
| `userId` | The system login ID of an employee in SF — tied to their employment record |
| `Position` | An organizational "slot" in the company structure — it exists independently of who fills it |
| `CRUD` | Create, Read, Update, Delete — the four basic data operations any API can perform |
| `JSON` | A lightweight, human-readable data format commonly used in REST/OData API responses |
| `XML` | A structured data format using tags — used in SOAP, IDoc, and SF Compound API responses |

---

## 12. Recommended Preparation Checklist

- [ ] Be able to explain an iFlow end-to-end from scratch
- [ ] Know the SuccessFactors OData V2 entity model (`EmpJob`, `EmpEmployment`, `PerPerson`)
- [ ] Understand `OAuth 2.0` SAML Bearer flow for SF authentication
- [ ] Know how `CSRF token` handling works for SF write operations
- [ ] Be able to write basic Groovy for message transformation
- [ ] Understand delta query patterns using `lastModifiedDateTime`
- [ ] Know the difference between synchronous and asynchronous integration
- [ ] Know at least 2–3 common SF integration scenarios (new hire, org sync, payroll)
- [ ] Understand how to monitor and troubleshoot failed messages in CPI
- [ ] Know Splitter, Multicast, Router, Aggregator patterns

---

*Prepared for: SAP CPI & SuccessFactors Integration Role*
*Topics: CPI iFlows · SF OData APIs · Middleware Patterns · SF Modules · Authentication*
