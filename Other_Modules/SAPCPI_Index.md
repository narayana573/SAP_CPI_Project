### **Integration Adapter Comparison**

| Adapter | Protocol/Type | Primary Use Case | Key Features |
| --- | --- | --- | --- |
| **Mail** | SMTP / IMAP / POP3 | Sending or receiving emails. | Supports attachments and encrypted communication. |
| **SOAP** | HTTP / XML | Connecting to legacy web services. | Uses WSDL for structured, contract-based communication. |
| **FTP** | FTP | Basic file transfers. | Standard file transfer; lacks native encryption. |
| **HTTPS** | HTTP / TLS | Generic REST APIs and web traffic. | The "universal" adapter for modern cloud connectivity. |
| **SFTP** | SSH File Transfer | Secure file exchange. | Preferred over FTP for secure, encrypted batch processing. |
| **OData** | REST / OData | SAP-to-SAP and modern web apps. | Supports CRUD operations with built-in filtering and pagination. |
| **IDOC** | SAP Proprietary | SAP ECC or S/4HANA integration. | High-volume asynchronous business document exchange. |
| **JMS** | Java Messaging Service | Asynchronous messaging / Queuing. | Guarantees delivery and decouples systems using queues. |
| **Salesforce** | REST / SOAP | Connecting to Salesforce CRM. | Simplifies authentication and object mapping for Salesforce. |
| **SalesforcePubSub** | gRPC / Event-driven | Real-time Salesforce events. | High-scale, low-latency event streaming (replaces Streaming API). |
| **SuccessFactors** | OData / SOAP | SAP SuccessFactors (HR) integration. | Specialized for HR data entities and simplified login. |
| **AMQP** | Advanced Message Queuing | Cloud-to-Cloud messaging. | Interoperable protocol used by Azure Service Bus and RabbitMQ. |

---



---

# ✅ **SAP CPI Adapter Notes (Interview-Ready)**

---

## **1. Mail Adapter**

**Purpose:** Send and receive emails
**Protocols:** SMTP (send), IMAP/POP3 (receive)

**Key Points for Interviews**

* Used for notification, alerting, and sending reports.
* Supports attachments (PDF, XML, CSV) and encrypted channels (TLS).
* Can poll mailboxes periodically using IMAP/POP3.
* Useful when integrating with non-API legacy systems that rely on email workflows.

---

## **2. SOAP Adapter**

**Purpose:** Integrate with legacy SOAP/XML services
**Protocol:** HTTP-based SOAP

**Key Points**

* Supports **WSDL-based** contract definitions.
* Handles SOAP 1.1 and 1.2.
* Allows WS-Security (UsernameToken, Digital Signature, Encryption).
* Ideal for older SAP and non-SAP enterprise systems still using XML contracts.

---

## **3. FTP Adapter**

**Purpose:** Basic file transfer
**Protocol:** FTP

**Key Points**

* No encryption → generally NOT secure.
* Rarely recommended unless required by legacy systems.
* Migration recommendation → use **SFTP** adapter instead.

---

## **4. HTTPS Adapter**

**Purpose:** REST APIs, generic HTTP calls
**Protocol:** HTTP over TLS

**Key Points**

* The **most versatile adapter** in CPI.
* Supports JSON, XML, binary payloads.
* Used for outbound REST calls and inbound API endpoints.
* Supports OAuth2, Basic Auth, Certificates, Custom Headers.

---

## **5. SFTP Adapter**
The SFTP receiver adapter connects an SAP Cloud Platform tenant to a remote system
using the SSH File Transfer protocol to write files to the system. SSH File Transfer protocol is
also referred to as Secure File Transfer protocol (or SFTP). 

If you have configured a receiver SFTP adapter, message processing is performed as
follows at runtime: The tenant sends a request to an SFTP server (think of this as the receiver
system), and the data flow is in the same direction, from the tenant to the SFTP server. In
other words, the tenant writes files to the SFTP server (from where the communication
partner can read them).

**Purpose:** Secure file exchange
**Protocol:** SSH File Transfer

**Key Points**

* Encrypted end-to-end → preferred over FTP.
* Supports:

  * File polling
  * Archiving
  * File locking
* Uses **SSH key-based authentication**.
* Common for bank interfaces, payroll files, batch processing.

---

## **6. OData Adapter**

**Purpose:** SAP and modern API integration
**Protocol:** REST + OData standard

**Key Points**

* Supports CRUD: GET, POST, PUT, PATCH, DELETE.
* Handles pagination (skip, top), filters, and queries.
* Used heavily for SAP SuccessFactors, S/4HANA, and custom Fiori apps.
* Supports metadata import from service URL.

---

## **7. IDoc Adapter**

**Purpose:** Integrate with SAP ECC/S4 systems
**Protocol:** SAP IDoc Framework (ALE/EDI)

**Key Points**

* High-volume asynchronous communication.
* Requires SAP Cloud Connector for on-premise connectivity.
* Supports both **XML IDoc** and **flat IDoc** in CPI.
* Robust for business documents like MATMAS, DEBMAS, ORDERS.

---

## **8. JMS Adapter**

**Purpose:** Messaging queues (asynchronous)
**Protocol:** JMS

**Key Points**

* Used for decoupled, guaranteed-delivery scenarios.
* Supports publish/subscribe and queue-based messaging.
* Recommended for:

  * High throughput asynchronous flows
  * Retry/resilience patterns
* Integrates with SAP Event Mesh / Message Broker services.

---

## **9. Salesforce Adapter**

**Purpose:** Connect to Salesforce CRM
**Protocol:** REST / SOAP

**Key Points**

* Provides simplified authentication mechanisms.
* Prebuilt connectivity objects for Salesforce SObjects.
* Ideal for pushing Leads, Accounts, Opportunities between SAP systems and Salesforce.

---

## **10. Salesforce PubSub Adapter**

**Purpose:** Real-time event streaming from Salesforce
**Protocol:** gRPC

**Key Points**

* Replaces older Streaming API.
* Near real-time scaling (thousands of events/sec).
* Used to subscribe/publish platform events, CDC events, and custom events.
* Ideal for event-driven architecture.

---

## **11. SuccessFactors Adapter**

**Purpose:** Integrate with SAP SuccessFactors HR suite
**Protocol:** OData + SOAP (for older APIs)

**Key Points**

* Simplifies SF OData authentication.
* Provides safer retry mechanisms around SuccessFactors rate limits.
* Used for Employee Central, Recruiting, Learning, and Compensation data.

---

## **12. AMQP Adapter**

**Purpose:** Cloud messaging (Azure, RabbitMQ, etc.)
**Protocol:** AMQP

**Key Points**

* Used for enterprise cloud-to-cloud messaging.
* Works with Azure Service Bus, RabbitMQ.
* Supports durable queues and high reliability.
* Key for decoupled microservice architectures.
*
* ProcessDirect
Use ProcessDirect adapter (sender and receiver) to establish fast and direct
communication between integration flows by reducing latency and network overhead
provided both of them are available within a same tenant.
Deployment of the ProcessDirect adapter must support N:1 cardinality, where N
(producer) → 1 (consumer). The Address mentioned in ProcessDirect configuration settings
must match for producer and consumer integration flows at any given point.
ProcessDirect Receiver Adapter: If the ProcessDirect adapter is used to send data to other
integration flows, the integration flow is considered as a producer integration flow. In this
case, the integration flow has a receiver ProcessDirect adapter.
ProcessDirect Sender adapter: If the ProcessDirect adapter is used to consume data from
other integration flows, the integration flow is considered as a consumer integration flow. In
this case, the integration flow has a sender ProcessDirect adapter.



---

---

# 🚀 **SAP CPI Adapters — In-Depth Technical Notes (Interview Grade)**

---

# **1. Mail Adapter (SMTP / IMAP / POP3)**

### **Use Case**

Send/receive emails containing business documents, notifications, reports.

---

## **📌 Internal Working**

### **Sender (Inbound from Mailbox)**

* CPI polls a mailbox periodically using:

  * **IMAP** → recommended (supports flags & seen/unseen)
  * **POP3** → downloads mail and optionally deletes it

### **Receiver (Outbound Email)**

* Uses **SMTP** protocol.
* CPI constructs the MIME message with:

  * Subject
  * From / To / CC / BCC
  * Body (Text/HTML)
  * Attachments (MIME multipart)

---

## **🔧 Important Configuration Parameters**

### **Sender**

* Mailbox host, port (993 for IMAPS)
* Polling interval (cron-like schedule)
* Folder (Inbox / Custom folder)
* Mark as read/Delete after processing
* Search criteria (UNSEEN, SUBJECT filter)

### **Receiver**

* SMTP server, port
* TLS enable
* Authentication (Basic / OAuth)
* Content-Type: `text/plain`, `text/html`, `multipart/mixed`

---

## **🔐 Security**

* TLS encryption
* OAuth 2.0 for Gmail/Office365
* SMTP relay requires firewall allow-listing

---

## **⚠️ Limitations**

* Large emails (>20 MB) can cause timeouts
* No retry for polling
* Attachments processed as binary → must convert manually in Groovy

---

---

# **2. SOAP Adapter (HTTP + SOAP/XML)**

### **Use Case**

Legacy SOAP services, SAP systems, enterprise XML-based integrations.

---

## **📌 Internal Working**

### **Sender**

* Exposes an endpoint with:

  * SOAP envelope validation
  * WSDL binding
  * Operation determination

### **Receiver**

* Constructs SOAP envelope automatically OR allows passthrough mode.

---

## **🔧 Key Configuration Parameters**

### **Sender**

* WSDL import
* Choose operation
* SOAP version (1.1 or 1.2)
* WS-Security (Username/Password, Digital Signature)

### **Receiver**

* Envelope handling:
  ✔ Wrap payload
  ✔ Suppress SOAP envelope
* Authentication:

  * Basic, Certificate, OAuth
* SOAP Action header

---

## **🔐 Security**

* WS-Security (signing/encryption)
* TLS mutual certificate auth

---

## **⚠️ Limitations**

* No automatic retry unless configured via JMS
* SOAP envelope sizes can be large
* Limited support for attachments (need MIME steps)

---

---

# **3. FTP Adapter (FTP Protocol)**

### **Use Case**

Legacy, non-secure file-based integration.

---

## **📌 Internal Working**

* Raw file transfer (clear-text)
* Sender polls directory for:

  * New files
  * Specific file patterns
* Receiver writes file with same or dynamic naming

---

## **🔧 Parameters**

* File path, pattern
* Polling interval
* After processing: delete/move/archive
* Transfer mode: binary / text

---

## **⚠️ Limitations**

* NO encryption
* NO key-based authentication
* NOT recommended for cloud integration

---

---

# **4. HTTPS Adapter (Universal REST Adapter)**

### **Use Case**

All REST API, webhooks, JSON/XML integrations.

---

## **📌 Internal Working**

### **Sender**

* CPI exposes an HTTPS endpoint
* Accepts:

  * JSON
  * XML
  * Text
  * Binary
* Request headers forwarded as properties

### **Receiver**

* Builds HTTP request dynamically:

  * Method: GET/POST/PUT/PATCH/DELETE
  * Custom headers
  * Query parameters
  * Body supported: JSON/XML/binary

---

## **🔧 Configuration**

### **Sender**

* Allowed methods (POST, GET)
* CSRF handling
* Authorization: API Key, OAuth, Basic

### **Receiver**

* Method selection
* Timeout
* Error-handling (Accept non-200 response)
* Authentication:

  * Basic
  * OAuth2.0 Client Credentials
  * Certificate

---

## **🔐 Security**

* TLS encryption (CPI-managed)
* Cloud Connector for on-prem calls
* Truststore/keystore management

---

## **⚠️ Limitations**

* No built-in throttling
* Must handle pagination manually in Groovy

---

---

# **5. SFTP Adapter (SSH Secure File Transfer)**

### **Use Case**

Secure file-based batch processing.

---

## **📌 Internal Working**

### **Sender**

* Connects over SSH
* Polls directory using SFTP commands:

  * `ls`, `get`, `rm`, `mv`

### **Receiver**

* Uploads file using

  * `put` (write)
  * `chmod` operations (optional)

---

## **🔧 Configuration**

* Hostname, port 22
* Directory path
* Authentication:

  * Username + SSH private key
  * No password-only authentication allowed
* File Charset
* Delete or archive after reading
* File mask (expression like *.xml)

---

## **🔐 Security**

* Private key-based authentication
* SSH tunnel
* Strict host-key checking

---

## **⚠️ Limitations**

* No resume for partially transferred files
* High latency if polling interval is too short

---

---

# **6. OData Adapter (REST + OData Protocol)**

### **Use Case**

SAP SuccessFactors, S/4HANA, Fiori apps, SAP Data Services.

---

## **📌 Internal Working**

Uses OData v2/v4 protocol (XML/JSON formats).
Supports query options:

* `$filter`
* `$expand`
* `$select`
* `$top/$skip`
* `$orderby`

---

## **🔧 Configuration**

* Service URL
* Metadata fetch (EDMX)
* Entity set selection
* Batch processing support
* Query parameter building
* Pagination handling

---

## **🔐 Security**

* OAuth2.0 (SFSF)
* Basic Authentication
* Certificate authentication via SCC

---

## **⚠️ Limitations**

* API throttling (SuccessFactors rate limits)
* Must implement pagination logic manually

---

---

# **7. IDoc Adapter (SAP Proprietary ALE)**

### **Use Case**

SAP ECC/S/4HANA IDoc sending/receiving.

---

## **📌 Internal Working**

### **Sender (IDoc → CPI)**

* IDoc XML created in SAP system
* Delivered to CPI via SAP Cloud Connector
* IDoc Number and Control Segment routed as headers

### **Receiver (CPI → SAP)**

* CPI sends IDoc to SAP via tRFC
* Can process:

  * XML IDoc
  * Flat IDoc format

---

## **🔧 Configuration**

* SAP System ID
* IDoc type & extension
* RFC destination (through Cloud Connector)
* XML/Flat format selection

---

## **🔐 Security**

* SAP Cloud Connector
* SNC encryption
* SAP user authorization (WE30, WE20)

---

## **⚠️ Limitations**

* Not synchronous
* No retries unless JMS added

---

---

# **8. JMS Adapter (Queue Messaging)**

### **Use Case**

Guaranteed delivery, decoupling, asynchronous patterns.

---

## **📌 Internal Working**

Implements messaging using:

* Queues (point-to-point)
* Topics (publish-subscribe)

CPI exposes JMS API internally.

---

## **🔧 Configuration**

* JMS Queue name
* QoS:

  * At least once
  * Exactly once
* Timeout
* Max concurrency
* Retry intervals

---

## **🔐 Security**

* Uses SAP Cloud Integration Runtime
* Role-based access

---

## **⚠️ Limitations**

* JMS quota affects performance
* Large messages require data store instead of JMS

---

---

# **9. Salesforce Adapter (REST/SOAP)**

### **Use Case**

Integrate SAP with Salesforce CRM.

---

## **📌 Internal Working**

* Prebuilt connectivity to Salesforce Objects
* Authentication supported:

  * OAuth2 Username-Password
  * OAuth2 JWT Bearer Flow

---

## **🔧 Configuration**

* OAuth credentials
* API version
* Object (Account, Contact, Lead)
* Operation (Query, Upsert, Insert, Delete)

---

## **⚠️ Limitations**

* SOQL has governor limits
* Requires handling pagination (nextRecordsUrl)

---

---

# **10. Salesforce Pub/Sub Adapter (gRPC + Eventing)**

### **Use Case**

Real-time streaming of Salesforce events.

---

## **📌 Internal Working**

* gRPC connection to Salesforce Pub/Sub API
* Subscribes to:

  * CDC events
  * Platform events
  * Change events
* Pull-based event consumption with offsets

---

## **🔧 Configuration**

* Topic name
* Replay ID (start position)
* Acknowledgment settings

---

## **⚠️ Limitations**

* gRPC needs high throughput
* Large events require data shaping

---

---

# **11. SuccessFactors Adapter (SFAPI + OData)**

### **Use Case**

Employee Central, Talent, LMS, Payroll integrations.

---

## **📌 Internal Working**

Supports both:

* SOAP-based SFAPI
* OData v2

Handles SFSF-specific:

* Session timeout
* Re-login
* Rate limiting
* OData pagination

---

## **🔧 Configuration**

* SFAPI user credential
* Company ID
* Entity selection
* Query filters

---

## **⚠️ Limitations**

* SFSF daily API limits
* Failures due to SF blackout windows

---

---

# **12. AMQP Adapter (Cloud Messaging)**

### **Use Case**

Azure Service Bus, RabbitMQ, event-driven architecture.

---

## **📌 Internal Working**

* Connects to AMQP brokers via TLS
* Supports:

  * Queues
  * Topics
  * Subscriptions

---

## **🔧 Configuration**

* Broker URL
* SAS key / shared key
* Queue/topic name
* Prefetch count
* Durable subscription

---

## **⚠️ Limitations**

* AMQP session timeout handling must be tuned

---

---





