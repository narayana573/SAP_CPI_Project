
---

# ✅ **SAP Cloud Integration (CPI) – 100% Official Palette Reference **

*(Every category + every flow step exactly as seen in the Cloud Integration Web UI)*

---

# **1. Participant**

Defines process boundaries.

* **Sender**
* **Receiver**

---

# **2. Process**

Structural process elements.

* **Integration Process** (main pool)
* **Local Integration Process**
* **Exception Subprocess**
* **Looping Process Call** *(this also appears under Call, but the icon is here as well)*

---

# **3. Event**

Process start/stop triggers.

### **Start Events**

* Timer Start Event
* Message Start Event
* Error Start Event

### **Intermediate Events**

* Timer Intermediate Event
* Message Intermediate Event

### **End Events**

* Message End
* Error End
* Escalation End
* Terminate Message End

---

# **4. Mapping**

Data structure transformations.

* **Message Mapping (.mmap)**
* **XSLT Mapping**
* **Operation Mapping** (from ESR)
* **Value Mapping**
* **Enhanced Message Mapping (BETA)** *(new UI)*

---

# **5. Transformation**

Payload and header manipulation.

### **Content & Property Handling**

* **Content Modifier**
* **Write Variables** (Properties/Headers/Exchange)
* **Content Deletion** *(removes body/attachments)*

### **Converters**

* XML → JSON
* JSON → XML
* CSV → XML
* XML → CSV

### **Transformation Tools**

* **Data Transform** *(Excel-like rules, new tool)*
* **Filter** (XPath, JSONPath)
* **Message Digest** (hashing – MD5, SHA-256)
* **Encoder** (Base64, GZip, Zip)
* **Decoder** (Base64, GZip, Zip)

### **Scripts**

* **Groovy Script**
* **JavaScript** (ECMAScript 6)

---

# **6. Call**

### **External Calls**

* **Request-Reply**
* **Send** (Fire-and-Forget)

### **Internal Calls**

* **Process Call** (Local or Global)
* **Local Integration Process Call**
* **Looping Process Call**

### **Adapters inside Call Category (Important in CPI UI)**

These appear when configuring Request-Reply/Send, but not as palette icons:

* HTTP
* SOAP
* OData
* SFTP
* AS2
* AMQP
* JMS
  *(Included because interviewers often ask this.)*

---

# **7. Routing**

Controlling message flow paths.

* **Router** (Content-based routing)
* **Multicast** (Parallel / Sequential)
* **Splitter**

  * General Splitter
  * Iterating Splitter
  * JSON Splitter
  * IDoc Splitter
* **Aggregator**
* **Gather**
* **Exception Subprocess Trigger** *(SYSTEM adds this automatically)*

---

# **8. Security**

Crypto-level processing.

* **PGP Encryptor / Decryptor**
* **PKCS7 Encryptor / Decryptor**
* **XML Signer / Verifier**
* **PGP Signer / Verifier**

*(Note: PKCS7 signing is not available; only encryption/decryption.)*

---

# **9. Persistence**

Persist or retrieve data.

* **Data Store Operations**

  * Put
  * Get
  * Select
  * Delete
* **Write Variables** (Global / Local / Exchange)
* **Message Store**
* **Trace Variables** *(appears in monitor settings, but adding for completeness)*

---

# **10. Validator**

Schema validation tools.

* **XML Validator** (XSD)
* **JSON Validator** (JSON Schema)

---

# ⭐ **11. (NEW 2024-25) – Helper Tools / Utility Category**

These appear in the newest palette and are often missed in interviews:

* **Exception Throw** (throws controlled Error)
* **Raise Message Event**
* **Resource Call** (fetches a resource from Resources tab)
* **Message Chunk Handling** *(rare, but present)*
* **OData Create/Merge** *(inside OData adapter configuration)*

---
