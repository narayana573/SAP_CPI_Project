# 🔥 **JMS**
https://help.sap.com/docs/integration-suite/sap-integration-suite/configure-jms-sender-adapter

https://help.sap.com/docs/integration-suite/sap-integration-suite/configure-jms-receiver-adapter

# 🔥 **SOAP**
---
# ✅ **1. Message Pattern**

### **SAP RM**

* Designed mainly for **asynchronous one-way** messaging.
* Focuses on *reliable* delivery rather than immediate synchronous response.
* Common when sender expects **fire-and-forget** messaging with guaranteed arrival.

### **SOAP 1.x**

* Supports both:

  * **Synchronous (request-reply)**
  * **Asynchronous (one-way)**
* Works like a *regular*, generic SOAP web service adapter.

---

# ✅ **2. Reliability & Ordering**

### **SAP RM**

* Built to comply with **WS-Reliable Messaging (WS-RM)** standards.
* Guarantees:

  * Message delivery
  * No duplicates
  * Correct sequence/order
* Retries automatically if messages fail.

### **SOAP 1.x**

* No built-in reliable messaging.
* No guarantee of:

  * Sequence ordering
  * Exactly-once delivery
* If reliability is needed, it must be handled at the integration or application level.

---

# ✅ **3. Usage Scenarios**

### **SOAP RM — When to Use**

✔ Communicating with SAP backend systems requiring WS-RM
✔ Integrating with **ECC**, **S/4HANA**, **PI/PO**, or legacy SAP stacks
✔ When business cannot tolerate message loss (financial, logistics, updates)

**Typical use:** IDocs over SOAP RM, PI-style async messages, ALE-style messaging.

### **SOAP 1.x — When to Use**

✔ Integrations with **third-party web services**
✔ Standard SOAP API consumption
✔ When a system provides a typical WSDL with request/response structures

**Typical use:** Calling CRM APIs, vendor SOAP services, payment services, etc.

---

# ✅ **4. WSDL & Operation Support**

### **SAP RM**

* Generally supports **one-way operations** only.
* Request-reply support is limited or not supported depending on scenario.
* WSDLs with multiple operations or complex patterns may not work.

### **SOAP 1.x**

* Fully compatible with:

  * Request-reply operations
  * Document-literal WSDLs
  * Complex schemas
* Easier to integrate with standard web service providers.

---

# ✅ **5. Technical Behavior**

### **SAP RM**

* Creates a **sequence ID** for each message exchange.
* Maintains a session to enforce order.
* Retries delivery if the receiving system is down.
* Ensures **QoS = Exactly Once In Order (EOIO).**

### **SOAP 1.x**

* Works like a simple SOAP transport.
* Delivers messages as they come.
* No retry mechanism unless configured separately.

---

# ⚡ Summary Table (Perfect for Interviews)

| Feature             | SOAP 1.x              | SAP RM                             |
| ------------------- | --------------------- | ----------------------------------- |
| Message Pattern     | Sync + Async          | Async (mostly)                      |
| Reliability         | No built-in RM        | WS-Reliable Messaging               |
| Order Guarantee     | ❌ No                  | ✔ Yes                               |
| Duplicate Avoidance | ❌ No                  | ✔ Yes                               |
| WSDL Support        | Full & flexible       | Limited (mostly one-way)            |
| Best For            | Third-party SOAP APIs | SAP ECC/S4/HANA/PI                  |
| Delivery QoS        | Best effort           | Exactly Once (EO) / In Order (EOIO) |

<img width="232" height="135" alt="image" src="https://github.com/user-attachments/assets/1bad345f-8a88-4fd4-b478-bec01805bdc6" />
<img width="492" height="421" alt="image" src="https://github.com/user-attachments/assets/2c21db9b-8252-4250-baa4-f27a4e1dbfaf" />
<img width="1070" height="887" alt="image" src="https://github.com/user-attachments/assets/fbb699ca-ffce-488b-8576-e5d5677b2d5b"/>              



---

# 🔥 **IDOC**
https://github.com/narayana573/SAP_CPI_Project/blob/main/Other_Modules/IDOC.md

# 🔥 **SFTP**
<img width="1402" height="728" alt="image" src="https://github.com/user-attachments/assets/843700b8-7fd0-40aa-92cc-69f756c8eada" />

# 🔥 **HTTPS**
<img width="580" height="346" alt="image" src="https://github.com/user-attachments/assets/b230293f-a2e8-4813-aa04-a7bd097b8425" />

