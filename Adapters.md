# 📘 SAP CPI Adapters — Interview Notes

> **Complete reference for SAP Integration Suite / Cloud Platform Integration (CPI) Adapters**

---

## 📑 Table of Contents

1. [OData Adapter](#1-odata-adapter)
2. [Advanced Event Mesh (AEM)](#2-advanced-event-mesh-aem)
3. [JMS Adapter](#3-jms-adapter)
4. [RFC Adapter](#4-rfc-adapter)
5. [SOAP Adapter (SOAP 1.x vs SAP RM)](#5-soap-adapter-soap-1x-vs-sap-rm)
6. [IDoc Adapter](#6-idoc-adapter)
7. [SFTP Adapter](#7-sftp-adapter)
8. [HTTPS / HTTP Adapter](#8-https--http-adapter)
9. [Quick Comparison Summary](#9-quick-comparison-summary)

---

## 1. OData Adapter

### What is OData?
- **OData (Open Data Protocol)** is a REST-based protocol built on HTTP/HTTPS, used to expose and consume data as RESTful APIs.
- Built on standards: **HTTP, AtomPub, JSON**.
- Commonly used for **SAP Gateway**, **S/4HANA**, and **SuccessFactors** integrations.

### OData Versions
| Version | Notes |
|---------|-------|
| OData V2 | XML/JSON, widely used in SAP ECC/S4 Gateway |
| OData V4 | JSON-first, improved query capabilities |

### Key Operations (CRUD over HTTP)
| HTTP Method | OData Operation | Description |
|-------------|----------------|-------------|
| GET | Read | Fetch entity or collection |
| POST | Create | Create new entity |
| PUT / PATCH | Update | Full / partial update |
| DELETE | Delete | Delete entity |

### Sender Adapter (OData)
- Exposes CPI as an **OData service endpoint**.
- External systems call CPI via OData URL.
- Supports **GET, POST, PUT, DELETE**.

### Receiver Adapter (OData)
- CPI calls an **external OData service** (e.g., S/4HANA).
- Configured with: Service URL, Operation (Query/Read/Create/Update/Delete), Entity set, Query options (`$filter`, `$select`, `$expand`).

### Key Configuration Properties
| Property | Description |
|----------|-------------|
| Address | Base URL of OData service |
| Resource Path | Entity set path (e.g., `/SalesOrderSet`) |
| Query Options | `$filter`, `$top`, `$skip`, `$expand` |
| Authentication | Basic, OAuth2, Client Certificate |
| CSRF Token | Required for POST/PUT/DELETE |

### Interview Tips
- OData uses **$metadata** endpoint to describe the data model.
- **CSRF token** must be fetched first (via GET with `X-CSRF-Token: Fetch`) before write operations.
- CPI can **paginate** large result sets using `$top` and `$skip`.

![OData Architecture](https://github.com/user-attachments/assets/6af395dc-ccaf-4b67-90c5-41ce834d78f7)
![OData Configuration](https://github.com/user-attachments/assets/43762665-fb1f-4d1d-9c89-63979726148d)
![OData Query Options](https://github.com/user-attachments/assets/cbee14e0-8e91-40e5-a03e-771bc4941a96)
![OData Flow Overview](https://github.com/user-attachments/assets/12adb404-f2b5-4f62-afd0-eab7e4cf19c8)

---

## 2. Advanced Event Mesh (AEM)

### What is Advanced Event Mesh?
- **SAP Advanced Event Mesh (AEM)** is a fully managed event streaming and management platform.
- Based on **Solace PubSub+** technology.
- Enables **event-driven architecture (EDA)** across hybrid and multi-cloud environments.
- Supports protocols: **AMQP, MQTT, JMS, REST, WebSocket, SMF**.

### Core Concepts
| Concept | Description |
|---------|-------------|
| **Event Broker** | Central hub that routes events between producers and consumers |
| **Topic** | Hierarchical address for routing messages (e.g., `order/created/EU`) |
| **Queue** | Persistent storage for messages; ensures guaranteed delivery |
| **Event Mesh** | Network of interconnected brokers across regions/clouds |
| **Publisher** | Produces/sends events to a topic |
| **Subscriber** | Consumes events from a topic or queue |

# SAP Event Mesh vs. Advanced Event Mesh (AEM) — Quick Notes

---

## 🔍 What Are They?

| | **SAP Event Mesh** | **SAP Integration Suite — Advanced Event Mesh (AEM)** |
|---|---|---|
| **Nature** | Lightweight event broker | Enterprise-grade event management platform |
| **Best For** | Simple SAP integrations | Complex, large-scale event-driven architectures |
| **Pricing** | Usage-based (cost-effective) | Enterprise licensing |

---

## ⚡ Key Capabilities at a Glance

### SAP Event Mesh
- ✅ Basic event streaming & integration
- ✅ Native SAP BTP deployment
- ✅ Great for SAP-to-SAP messaging
- ❌ Limited governance & lifecycle management
- ❌ Smaller payload & storage support

### Advanced Event Mesh (AEM)
- ✅ Full **event lifecycle governance** (design → publish → discover)
- ✅ **Multi-cloud** deployment — AWS, Azure, GCP, on-prem, edge
- ✅ **Message replay** for debugging & new consumer onboarding
- ✅ **Event transactions** for data consistency
- ✅ Large payload & high-volume storage
- ✅ Enterprise compliance & control

---

## 📦 Technical Limits Comparison

| Feature | Event Mesh | Advanced Event Mesh |
|---|---|---|
| **Max Message Size** | 1 MB | 30 MB |
| **Storage Capacity** | 10 GB | 6 TB |
| **Deployment** | SAP BTP only | Multi-cloud + on-prem + edge |
| **Event Governance** | ❌ Basic | ✅ Full lifecycle |
| **Message Replay** | ❌ | ✅ |
| **Transactions** | ❌ | ✅ |

---

## 🧭 Decision Guide — Which One to Choose?

| Scenario | Choose |
|---|---|
| Simple SAP-to-SAP event flows | **Event Mesh** |
| Budget-sensitive / starter projects | **Event Mesh** |
| Complex enterprise event architectures | **AEM** |
| Need multi-cloud or on-prem deployment | **AEM** |
| Require compliance & event governance | **AEM** |
| Large payloads or high data volumes | **AEM** |
| Event reuse, discovery, collaboration | **AEM** |

---


> **Event Mesh** = Lightweight, SAP-native, cost-effective. Ideal for simple integrations.
>
> **AEM** = Powerhouse, multi-cloud, governed. Ideal for enterprise-scale event-driven systems.

---

*Part of SAP Integration Suite | Event-Driven Architecture Series*

### AEM vs JMS (Quick Comparison)
| Feature | AEM | JMS |
|---------|-----|-----|
| Protocol | AMQP, MQTT, REST, SMF | JMS (javax.jms) |
| Topology | Multi-cloud mesh | Local/CPI-managed queue |
| Use Case | Event-driven, real-time streaming | Point-to-point, reliable async |
| Persistence | Yes (queues + replay) | Yes (within CPI) |
| Message Replay | ✔ Yes | ❌ No |

### CPI Integration with AEM
- CPI connects to AEM via the **Advanced Event Mesh sender/receiver adapter**.
- **Sender**: CPI subscribes to an AEM topic/queue to receive events.
- **Receiver**: CPI publishes events to AEM topics/queues.

### Key Use Cases
- Real-time event propagation between SAP and non-SAP systems.
- Decoupling microservices in an event-driven architecture.
- Fan-out messaging (one event → multiple consumers).
- Cross-region event replication.

### Interview Tips
- AEM supports **dynamic topic routing** — topics can be hierarchical and wildcard subscriptions are possible.
- Unlike JMS (which is CPI-internal), AEM is **external, multi-cloud**.
- Supports **message replay** — consumers can re-read past events.

![AEM Overview](https://github.com/user-attachments/assets/62852361-1429-44c4-b3f7-3a55080f2cda)

---

## 3. JMS Adapter

### What is JMS?
- **Java Message Service (JMS)** is an API for sending messages between two or more clients.
- In SAP CPI, JMS uses an **internal message broker** (no external broker needed).
- Enables **asynchronous, decoupled** communication within CPI iFlows.

### Common Use Cases
- **Decouple** sender and receiver iFlows (async processing).
- **Retry handling** — messages stay in queue on failure.
- **Parallel processing** — multiple consumers reading from same queue.
- **Error handling** — dead-letter queue pattern.

### Sender Adapter (JMS)
- CPI **reads** messages from a JMS queue.
- Can be combined with **Polling interval** or triggered when message arrives.
- Key Properties:
  - Queue Name
  - Retry Interval
  - Max Retry Count

### Receiver Adapter (JMS)
- CPI **writes** messages to a JMS queue.
- Key Properties:
  - Queue Name
  - Retain Payload Header
  - Expiration

### Quality of Service (QoS)
| QoS Level | Description |
|-----------|-------------|
| **AT LEAST ONCE** | Message delivered one or more times (possible duplicates) |
| **EXACTLY ONCE** | Message delivered exactly once (uses duplicate check) |
| **EXACTLY ONCE IN ORDER (EOIO)** | Delivered once, in sequence |

### JMS Queue Management
- Queues are managed in **CPI Operations Cockpit** (Message Queue Monitor).
- Messages can be **retried**, **deleted**, or **moved** via UI.
- CPI has a **JMS resource quota** — number of queues and messages is limited by license.

### Key Interview Questions
- **Q: What happens if the JMS queue is full?**  
  A: New messages are rejected; sender iFlow will fail with a queue capacity error.
- **Q: How to implement retry with JMS?**  
  A: Configure Max Retries + Dead Letter Queue (DLQ) in the JMS sender adapter.
- **Q: Difference between JMS and AEM?**  
  A: JMS is CPI-internal; AEM is external/multi-cloud with advanced routing and replay.

📖 Reference: [JMS Sender Adapter](https://help.sap.com/docs/integration-suite/sap-integration-suite/configure-jms-sender-adapter) | [JMS Receiver Adapter](https://help.sap.com/docs/integration-suite/sap-integration-suite/configure-jms-receiver-adapter)

---

## 4. RFC Adapter

### What is RFC?
- **Remote Function Call (RFC)** is SAP's proprietary protocol for calling function modules in SAP ABAP systems.
- Used to integrate CPI with **SAP ECC**, **S/4HANA**, **BW**, etc.
- CPI supports **RFC Receiver Adapter** only (not sender).

### Types of RFC
| Type | Description |
|------|-------------|
| **sRFC** (Synchronous RFC) | Caller waits for response |
| **aRFC** (Asynchronous RFC) | Caller does not wait |
| **tRFC** (Transactional RFC) | Guaranteed once delivery |
| **qRFC** (Queued RFC) | tRFC with ordering (EOIO) |

### RFC Receiver Adapter in CPI
- CPI acts as **client**, calling a **BAPI or Function Module** in SAP.
- Uses **SAP JCo (Java Connector)** under the hood.
- Requires **On-Premise connectivity** via **Cloud Connector**.

### Configuration Properties
| Property | Description |
|----------|-------------|
| Target System | SAP system alias configured in Cloud Connector |
| Function Module / BAPI | Name of the RFC-enabled function |
| Input Parameters | Mapped from CPI message |
| Output Parameters | Result mapped back to CPI message |

### Cloud Connector Requirement
- RFC calls go through **SAP Cloud Connector** (SCC) which creates a secure tunnel between CPI (cloud) and on-premise SAP system.
- SCC must expose the SAP system as a **back-end resource**.

### Common BAPIs Used
- `BAPI_SALESORDER_CREATEFROMDAT2` — Create Sales Order
- `BAPI_MATERIAL_GETLIST` — Get Material List
- `RFC_READ_TABLE` — Read any SAP table

### Interview Tips
- RFC Adapter is **receiver only** in CPI — CPI always calls SAP, not the other way.
- For SAP-to-CPI calls, use **SOAP** or **HTTP** adapter, not RFC.
- RFC requires **SAP JCo libraries** and **Cloud Connector** configuration.

📖 Reference: [RFC Receiver Adapter](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/rfc-receiver-adapter)

---

## 5. SOAP Adapter (SOAP 1.x vs SAP RM)

### What is SOAP?
- **SOAP (Simple Object Access Protocol)** is an XML-based messaging protocol over HTTP/HTTPS.
- Uses **WSDL** to describe the service contract.
- CPI supports both **SOAP 1.x** and **SAP RM (Reliable Messaging)** variants.

---

### ✅ 1. Message Pattern

#### SOAP 1.x
- Supports both **Synchronous (request-reply)** and **Asynchronous (one-way)**.
- Works like a standard SOAP web service adapter.

#### SAP RM
- Designed mainly for **asynchronous one-way** messaging.
- Focuses on *reliable* delivery rather than synchronous response.
- Common for **fire-and-forget** messaging with guaranteed arrival.

---

### ✅ 2. Reliability & Ordering

#### SAP RM
- Complies with **WS-Reliable Messaging (WS-RM)** standards.
- Guarantees:
  - Message delivery
  - No duplicates
  - Correct sequence/order
- Retries automatically if messages fail.

#### SOAP 1.x
- No built-in reliable messaging.
- No guarantee of sequence ordering or exactly-once delivery.
- Reliability must be handled at integration/application level.

---

### ✅ 3. Usage Scenarios

#### SOAP RM — When to Use
- Communicating with SAP backend systems requiring WS-RM.
- Integrating with **ECC**, **S/4HANA**, **PI/PO**, or legacy SAP stacks.
- When business cannot tolerate message loss (financial, logistics).

**Typical use:** IDocs over SOAP RM, PI-style async messages, ALE-style messaging.

#### SOAP 1.x — When to Use
- Integrations with **third-party web services**.
- Standard SOAP API consumption.
- When a system provides a typical WSDL with request/response structures.

**Typical use:** Calling CRM APIs, vendor SOAP services, payment services.

---

### ✅ 4. WSDL & Operation Support

| Feature | SOAP 1.x | SAP RM |
|---------|----------|--------|
| Request-Reply | ✔ Yes | Limited |
| One-Way | ✔ Yes | ✔ Yes (primary) |
| Complex WSDLs | ✔ Full | Limited |
| Multi-operation WSDL | ✔ Yes | ❌ Restricted |

---

### ✅ 5. Technical Behavior

#### SAP RM
- Creates a **sequence ID** for each message exchange.
- Maintains a session to enforce order.
- Retries delivery if the receiving system is down.
- Ensures **QoS = Exactly Once In Order (EOIO)**.

#### SOAP 1.x
- Works like a simple SOAP transport.
- No retry mechanism unless configured separately.

---

### ⚡ Summary Table

| Feature | SOAP 1.x | SAP RM |
|---------|----------|--------|
| Message Pattern | Sync + Async | Async (mostly) |
| Reliability | No built-in RM | WS-Reliable Messaging |
| Order Guarantee | ❌ No | ✔ Yes |
| Duplicate Avoidance | ❌ No | ✔ Yes |
| WSDL Support | Full & flexible | Limited (mostly one-way) |
| Best For | Third-party SOAP APIs | SAP ECC/S4HANA/PI |
| Delivery QoS | Best effort | Exactly Once (EO) / In Order (EOIO) |

![SOAP Adapter Types](https://github.com/user-attachments/assets/1bad345f-8a88-4fd4-b478-bec01805bdc6)
![SOAP Configuration](https://github.com/user-attachments/assets/2c21db9b-8252-4250-baa4-f27a4e1dbfaf)
![SOAP vs SAP RM Comparison](https://github.com/user-attachments/assets/fbb699ca-ffce-488b-8576-e5d5677b2d5b)

---

## 6. IDoc Adapter

### What is IDoc?
- **IDoc (Intermediate Document)** is SAP's standard data container format for asynchronous inter-system communication.
- Used for exchanging business documents (orders, deliveries, invoices) between SAP systems and third parties.
- Each IDoc has a **Control Record**, **Data Records**, and **Status Records**.

### IDoc Structure
```
IDoc
├── Control Record (Header: sender, receiver, message type)
├── Data Records (Segments: actual business data)
└── Status Records (Processing status)
```

### Key Terminology
| Term | Description |
|------|-------------|
| **Message Type** | Business context (e.g., ORDERS, DESADV, INVOIC) |
| **Basic Type** | IDoc structure/template (e.g., ORDERS05) |
| **Extension** | Custom fields added to Basic Type |
| **Partner Profile** | Configuration for sending/receiving IDocs |
| **Port** | Communication channel (ALE port, file port) |

### IDoc Adapter in CPI
- CPI supports **IDoc Sender** and **IDoc Receiver** adapters.
- Used for integrating CPI with **SAP ECC/S4HANA** via ALE/IDoc.

#### IDoc Sender (SAP → CPI)
- SAP system sends IDoc to CPI endpoint.
- CPI processes and forwards to target system.

#### IDoc Receiver (CPI → SAP)
- CPI sends IDoc to SAP system.
- SAP posts IDoc and triggers business processing.

### Common IDoc Message Types
| Message Type | Business Scenario |
|-------------|-------------------|
| ORDERS | Purchase Order |
| ORDRSP | Order Response |
| DESADV | Delivery/Despatch Advice |
| INVOIC | Invoice |
| MATMAS | Material Master |
| DEBMAS | Customer Master |

### Interview Tips
- IDoc is always **asynchronous**.
- IDoc uses **tRFC/qRFC** under the hood for reliable delivery.
- In CPI, IDocs are converted to/from **XML** for processing.
- Use **SOAP RM adapter** when IDocs are sent over SOAP (WS-based IDoc).

📖 Reference: [IDoc Module](https://github.com/narayana573/SAP_CPI_Project/blob/main/Other_Modules/IDOC.md)

---

## 7. SFTP Adapter

### What is SFTP?
- **SFTP (SSH File Transfer Protocol)** is a secure protocol for transferring files over SSH.
- Used for file-based integration patterns (batch, bulk data exchange).
- Commonly used with legacy systems, banks, EDI partners.

### SFTP Sender Adapter
- CPI **polls** an SFTP server for new files at a configured interval.
- Downloads files for processing.

#### Key Configuration Properties
| Property | Description |
|----------|-------------|
| Host | SFTP server hostname/IP |
| Port | Default: 22 |
| Credential Name | SSH username + password or private key |
| Directory | Remote folder path to poll |
| File Pattern | Filter files (e.g., `*.csv`, `order_*.xml`) |
| Polling Interval | How often CPI checks for new files |
| Post-Processing | Delete / Archive / Move file after processing |
| Idempotent Check | Avoid reprocessing same file |

### SFTP Receiver Adapter
- CPI **writes/uploads** processed files to an SFTP server.

#### Key Configuration Properties
| Property | Description |
|----------|-------------|
| Host / Port | SFTP server details |
| Directory | Target upload folder |
| File Name | Static or dynamic (use headers/properties) |
| Duplicate Handling | Overwrite / Append / Throw Error |
| Create Directories | Auto-create missing folders |

### Authentication Types
| Type | Description |
|------|-------------|
| Username/Password | Basic credentials |
| Public Key (RSA) | SSH key-pair authentication (more secure) |
| Known Hosts File | Verify server identity |

### Post-Processing Options (Sender)
| Option | Behavior |
|--------|----------|
| Delete | File deleted after successful processing |
| Archive | Moved to archive folder |
| Move | Moved to another directory |
| Keep | File stays in source folder |

### Interview Tips
- SFTP adapter uses **polling** — it checks the directory at set intervals.
- Use **idempotent file name** option to avoid duplicate processing.
- Dynamic file names: Use `${header.CamelFileName}` or `${property.fileName}` expressions.
- SFTP connects through **Cloud Connector** for on-premise SFTP servers.
<img width="477" height="592" alt="image" src="https://github.com/user-attachments/assets/372f5a71-66ef-4205-997b-8c37e0b42585" />
<img width="752" height="386" alt="image" src="https://github.com/user-attachments/assets/7448b3dd-772c-488b-a3f0-eea33cb9a7c7" />
<img width="687" height="667" alt="image" src="https://github.com/user-attachments/assets/44acd808-fa02-4c31-855b-d6e38b6580ba" />


---

## 8. HTTPS / HTTP Adapter

### What is HTTPS Adapter?
- **HTTPS (HTTP Sender)** adapter exposes CPI integration flow as a **REST/HTTP endpoint**.
- External systems call CPI via HTTPS URL.
- Supports: **GET, POST, PUT, DELETE, PATCH**.

### HTTP Sender Adapter
- Exposes CPI iFlow as an **inbound HTTP(S) endpoint**.
- External systems (Postman, apps, cloud services) send requests to CPI.

#### Key Configuration Properties
| Property | Description |
|----------|-------------|
| Address | Relative path of the endpoint (e.g., `/orders`) |
| Authorization | Role-based (iFlows require `ESBMessaging.send` role) |
| CSRF Token Protection | Optional, for REST clients |
| Message Exchange Pattern | Request-Reply or One-Way |

### HTTP Receiver Adapter
- CPI calls an **external HTTP(S) service**.
- Sends HTTP requests to third-party REST APIs.

#### Key Configuration Properties
| Property | Description |
|----------|-------------|
| URL | Target service URL |
| Method | GET / POST / PUT / DELETE / PATCH |
| Authentication | None, Basic, OAuth2, Client Certificate |
| Headers | Custom HTTP headers |
| Query Parameters | URL query string params |
| Timeout | Connection + response timeout |

### Authentication Options
| Type | Use Case |
|------|----------|
| Basic Authentication | Username/password |
| OAuth 2.0 | Token-based (recommended for modern APIs) |
| Client Certificate | Mutual TLS (mTLS) |
| None | Open/public endpoints |

### HTTP vs HTTPS Adapter
| Feature | HTTP | HTTPS |
|---------|------|-------|
| Security | Unencrypted | TLS encrypted |
| Recommended? | ❌ Not for production | ✔ Always preferred |
| Certificate | Not needed | SSL/TLS cert required |

### CPI Endpoint URL Format
```
https://<tenant>.it-cpi001.cfapps.sap.hana.ondemand.com/http/<your-relative-path>
```

### Interview Tips
- HTTPS sender requires the client to have the `ESBMessaging.send` role assigned.
- Use **OAuth2** (Client Credentials) for system-to-system calls.
- HTTP adapter can call any REST API — SAP, Salesforce, external services.
- Response handling: Use **Content Modifier** or **Message Mapping** to process response payload.

![HTTPS Adapter Overview](https://github.com/user-attachments/assets/b230293f-a2e8-4813-aa04-a7bd097b8425)

---

## 9. Quick Comparison Summary

| Adapter | Direction | Protocol | Sync/Async | Best For |
|---------|-----------|----------|-----------|----------|
| **OData** | Sender + Receiver | HTTP/HTTPS + OData | Both | SAP Gateway, S/4HANA APIs |
| **AEM** | Sender + Receiver | AMQP, MQTT, REST | Async | Event-driven, multi-cloud |
| **JMS** | Sender + Receiver | JMS (internal) | Async | Decouple iFlows, retry handling |
| **RFC** | Receiver only | RFC (SAP JCo) | Both | BAPI/FM calls to SAP on-prem |
| **SOAP 1.x** | Sender + Receiver | SOAP over HTTP | Both | Third-party SOAP web services |
| **SAP RM** | Sender + Receiver | SOAP + WS-RM | Async | SAP ECC/S4 reliable messaging |
| **IDoc** | Sender + Receiver | IDoc/ALE | Async | SAP-to-SAP document exchange |
| **SFTP** | Sender + Receiver | SFTP (SSH) | Async (polling) | File-based batch integrations |
| **HTTPS/HTTP** | Sender + Receiver | HTTP/HTTPS | Both | REST APIs, generic web services |

---

## 🎯 Common Interview Questions

**Q1: When would you use JMS over AEM?**  
A: JMS is for CPI-internal async decoupling (lightweight, no extra cost). AEM is for enterprise event-driven architecture across multiple systems/clouds with advanced routing and replay.

**Q2: What is the difference between SOAP 1.x and SAP RM adapter?**  
A: SOAP 1.x is for standard SOAP web services (sync + async). SAP RM adds WS-Reliable Messaging for guaranteed, ordered, duplicate-free delivery — used for SAP backend integrations.

**Q3: Why use RFC adapter instead of SOAP for SAP integration?**  
A: RFC provides direct function module/BAPI calls to SAP ABAP systems without needing a web service. More suitable for tightly coupled SAP-SAP scenarios via Cloud Connector.

**Q4: How does SFTP sender handle large files?**  
A: CPI streams the file content. For very large files, use **Chunk Processing** with the Splitter step to process records in batches.

**Q5: How is IDoc different from SOAP?**  
A: IDoc is SAP's own document format for ALE-based messaging. SOAP is a general XML web service protocol. IDocs can be sent over SOAP RM (hybrid approach in newer SAP releases).

**Q6: What is CSRF token and when is it needed?**  
A: CSRF (Cross-Site Request Forgery) token is required by SAP OData services for write operations (POST/PUT/DELETE). First, do a GET with `X-CSRF-Token: Fetch`, then use the returned token in subsequent write requests.

---
# 🔥 SAP CPI — AMQP Adapter Interview Notes

> **Protocol:** AMQP 1.0 | Connects CPI to **external** message brokers

---

## What is AMQP?

- **Advanced Message Queuing Protocol** — open standard for async messaging.
- CPI supports **AMQP 1.0 only** (⚠️ RabbitMQ uses 0.9.1 by default — needs plugin).
- Used to connect CPI to **external** brokers: Azure Service Bus, SAP Event Mesh, Solace, ActiveMQ.
- Unlike **JMS** (CPI-internal), AMQP talks to **outside systems**.

---

## Adapter Directions

| Adapter | Role | Flow |
|---------|------|------|
| **Sender** | CPI **reads/consumes** from broker | Broker → CPI |
| **Receiver** | CPI **writes/produces** to broker | CPI → Broker |

---

## Transport Protocols — TCP vs WebSocket

| Feature | TCP | WebSocket |
|---------|-----|-----------|
| Port | 5671 (TLS) / 5672 (plain) | **443** |
| Firewall | ❌ Needs port open | ✔ Uses HTTPS port |
| Auth | SASL, Client Certificate | SASL, **OAuth2** |
| TLS | Optional (checkbox) | Default (WSS) |
| SAP Event Mesh | ❌ | ✔ Recommended |
| Azure Service Bus | ✔ TLS over TCP | ✔ |
| On-Premise (CC) | ✔ | ✔ (CC mapping = TCP) |

> 💡 **WebSocket = port 443** — preferred for cloud brokers (no firewall issues).  
> 💡 **OAuth2** is only available with **WebSocket** transport.  
> 💡 **Client Certificate** is only available with **TCP** transport.

---

## WebSocket Configuration (SAP Event Mesh Example)

```
Transport Protocol : WebSocket
Host               : enterprise-messaging-messaging-gateway.cfapps.<region>.hana.ondemand.com
Port               : 443
Path               : /protocols/amqp10ws
Authentication     : OAuth2 Client Credentials
Credential Name    : <alias of deployed OAuth2 credential>
Queue Name         : queue:<your-queue-name>
```

**How to set up OAuth2 for Event Mesh:**
1. Get the service key from your BTP Event Mesh instance.
2. Find the `amqp10ws` section → copy Host, Port, Path.
3. In CPI → `Monitor → Security Material → Add OAuth2 Client Credentials`.
4. Use `clientid`, `clientsecret`, `tokenendpoint` from the service key.
5. Reference the credential alias in the adapter's **Credential Name** field.

---

## Key Configuration Properties

### Connection Tab
| Property | Description |
|----------|-------------|
| Host | Broker hostname |
| Port | Broker port |
| Proxy Type | `Internet` (direct) / `On-Premise` (Cloud Connector) |
| Path | *(WebSocket only)* e.g. `/protocols/amqp10ws` |
| Connect with TLS | Enable TLS (TCP only; auto-on for Client Cert) |
| Location ID | *(On-Premise only)* Cloud Connector Location ID |
| Authentication | SASL / OAuth2 / Client Certificate / None |
| Credential Name | Alias of deployed credentials |

### Processing Tab — Sender
| Property | Description |
|----------|-------------|
| Queue Name | `queue:<n>` for Event Mesh; plain name for others |
| No. of Processes | Parallel workers (default: 1, max: 99) |
| Max Prefetch Messages | Messages pre-fetched per worker (1–100) |
| Max. Number of Retries | Retries before failure status sent (default: 0) |
| Delivery Status After Max Retries | `REJECTED` → DLQ / `MODIFIED_FAILED_UNDELIVERABLE` |

### Processing Tab — Receiver
| Property | Description |
|----------|-------------|
| Queue / Topic Name | Destination on the broker |
| Delivery | `Persistent` (survives restart) / `Non-Persistent` (faster, lossy) |

---

## Queue vs Topic Syntax

| Type | Syntax | Adapter |
|------|--------|---------|
| Queue | `queue:<name>` | Sender + Receiver |
| Topic | `topic:<name>` | Receiver only |
| Topic Subscription | `<topic>/subscriptions/<sub>` | Sender only (Azure SB) |

> ⚠️ Sender adapter **cannot consume directly from a topic** — must use a queue or topic subscription.

---

## Delivery Status & DLQ

```
Message fails in CPI
      ↓
Retries exhausted (Max. Retries reached)
      ↓
CPI sends → REJECTED → Broker routes to Dead Letter Queue (DLQ)
```

- **DLQ must be configured on the broker side** — CPI only sends the AMQP outcome.
- `MODIFIED_FAILED_UNDELIVERABLE` is **not supported** on SAP Event Mesh & Solace — use `REJECTED`.

---

## Monitoring

| Monitor | What It Shows |
|---------|--------------|
| **Poll Status** (Integration Content) | AMQP Sender consumption status |
| **Message Processing Monitor** | Errors from AMQP Receiver |
| **Broker's own tool** | Queue depth, DLQ — ❌ NOT in CPI |

> 💡 Use **separate iFlows per queue** to avoid mixed MPL logs.

---

## Supported Brokers (Quick Ref)

| Broker | Transport | Auth | Queue Prefix |
|--------|-----------|------|-------------|
| SAP Event Mesh | WebSocket (443) | OAuth2 | `queue:` |
| Azure Service Bus | TCP TLS (5671) | SASL | None |
| Solace PubSub+ | TCP / WebSocket | SASL / Client Cert | `queue:` |
| ActiveMQ / Artemis | TCP / WebSocket | SASL | None |
| Apache Qpid | TCP / WebSocket | SASL | None |

---

## Interview Q&A

**Q: Difference between AMQP and JMS?**  
A: JMS is CPI-internal (no external broker needed). AMQP connects to external brokers like Azure Service Bus or Solace.

**Q: Why use WebSocket over TCP?**  
A: WebSocket uses port **443** — no firewall changes needed. Also required for **OAuth2** authentication (e.g., SAP Event Mesh).

**Q: Which AMQP version does CPI support?**  
A: **AMQP 1.0 only**. RabbitMQ defaults to 0.9.1 — needs the AMQP 1.0 plugin.

**Q: How does retry + DLQ work?**  
A: Set `Max. Number of Retries` in the sender adapter. Once exceeded, CPI sends `REJECTED` to the broker which routes the message to the configured DLQ.

**Q: How to connect to SAP Event Mesh via AMQP?**  
A: WebSocket transport, port 443, path `/protocols/amqp10ws`, OAuth2 Client Credentials, queue name prefixed with `queue:`.

**Q: Can the sender adapter consume directly from a topic?**  
A: No — must use a queue or topic subscription. Topics are only supported in the **Receiver** adapter.

---

*SAP Integration Suite — AMQP Adapter Quick Reference*
*Last Updated: 2025 | SAP Integration Suite — CPI Adapter Interview Reference*


# SAP Cloud Integration (CPI) – Top 15 Adapters


---

| # | Adapter Name | Transport Protocol | Message Call | Direction |
|---|---|---|---|---|
| 1 | **HTTPS / HTTP** | HTTP / HTTPS | Synchronous / Asynchronous | Sender & Receiver |
| 2 | **SOAP** | HTTP / HTTPS | Synchronous (Request-Reply) | Sender & Receiver |
| 3 | **IDoc** | HTTP / RFC | Asynchronous | Sender & Receiver |
| 4 | **OData** | HTTP / HTTPS (OData V2 / V4) | Synchronous (CRUD) | Sender & Receiver |
| 5 | **SFTP** | SSH / SFTP (SSH v2) | Asynchronous (File-based Polling) | Sender & Receiver |
| 6 | **ProcessDirect** | Internal (In-Memory) | Synchronous | Sender & Receiver |
| 7 | **RFC** | RFC / TCP (via Cloud Connector) | Synchronous (Request-Reply) | Receiver |
| 8 | **JMS** | JMS (Internal Embedded Message Broker) | Asynchronous (Queue-based) | Sender & Receiver |
| 9 | **JDBC** | JDBC / TCP | Synchronous | Receiver |
| 10 | **Mail** | SMTP / IMAP / POP3 | Asynchronous | Sender & Receiver |
| 11 | **SuccessFactors (SFSF)** | HTTP / HTTPS (OData / SOAP) | Synchronous / Asynchronous | Sender & Receiver |
| 12 | **AMQP** | AMQP 1.0 (TCP / WebSocket) | Asynchronous (Event/Messaging) | Sender & Receiver |
| 13 | **Advanced Event Mesh (AEM)** | SMF – Solace Message Format (TCP) | Asynchronous (Event-Driven / Pub-Sub) | Sender & Receiver |
| 14 | **AS2** | HTTP / HTTPS | Asynchronous (EDI / B2B) | Sender & Receiver |
| 15 | **XI (SAP PI/PO)** | HTTP / HTTPS | Asynchronous | Sender & Receiver |

---

## Key Notes

| Term | Explanation |
|---|---|
| **Sender** | Adapter acts as inbound — receives/triggers messages *into* CPI |
| **Receiver** | Adapter acts as outbound — CPI sends messages *out* to target system |
| **Sender & Receiver** | Adapter supports both directions |
| **Synchronous** | Request-Reply pattern; caller waits for an immediate response |
| **Asynchronous** | Fire-and-forget or event-driven; no immediate response required |

---
