# SAP CPI Adapters – Complete Interview Notes

> **Quick Tip:** When choosing an adapter, remember the **3 P's** — **Protocol** (REST/SOAP/SFTP?), **Performance** (large file vs. real-time?), **Persistence** (queued or direct?).

---

## 1. Common Adapters

| Adapter | Used to Connect With | Communication Type | Supported Authentication Types |
|---|---|---|---|
| **HTTP** (REST & HTTPS) | APIs, Web Apps, External Systems | Synchronous | Basic Auth, OAuth 2.0 (Client Credentials / Auth Code / JWT Bearer), Client Certificate (Two-Way SSL), API Key (Header or Query Param) |
| **SFTP** | File Servers, Legacy Systems | Asynchronous | SSH Public Key (Recommended), Username/Password |
| **IDoc** | SAP ECC, S/4HANA (On-Prem) | Asynchronous | Basic Auth, Client Certificate (via Cloud Connector) |
| **SOAP** | Web Services (SAP & Non-SAP) | Synchronous | Basic Auth, Client Certificate (Mutual TLS), WS-Security (UsernameToken, X.509 Token) |
| **JMS** | Queue-based / Enterprise Messaging | Asynchronous | Internal Tenant-Managed (no external auth needed) |

---

## 2. SAP-Specific Adapters

| Adapter | Used to Connect With | Communication Type | Supported Authentication Types |
|---|---|---|---|
| **SuccessFactors** | SAP SuccessFactors HR Systems | Sync & Async | OAuth 2.0 (SAML Bearer – Recommended), Basic Auth |
| **Ariba** | SAP Ariba (Procurement) | Asynchronous | Shared Secret, Certificate, OAuth |
| **Concur** | SAP Concur (Travel & Expense) | Sync & Async | OAuth 2.0, Basic Auth |
| **OData (V2/V4)** | S/4HANA, SAP APIs | Synchronous | Basic Auth, OAuth 2.0, Client Certificate, SAML |

---

## 3. Messaging & Event Adapters

| Adapter | Used to Connect With | Communication Type | Supported Authentication Types |
|---|---|---|---|
| **AMQP** | Message Brokers (RabbitMQ, Azure Service Bus) | Asynchronous | SASL (Plain / Anonymous / External), OAuth 2.0 |
| **MQTT** | IoT Devices, Sensors | Asynchronous | Basic Auth, Client Certificate |
| **JMS** | Enterprise Messaging Systems | Asynchronous | Internal Tenant-Managed |
| **SAP Event Mesh** | Event-Driven SAP Integrations | Asynchronous | OAuth 2.0 (Client Credentials) |

---

## 4. Cloud & External System Adapters

| Adapter | Used to Connect With | Communication Type | Supported Authentication Types |
|---|---|---|---|
| **HTTP** (REST/HTTPS APIs) | Third-party APIs, Cloud Applications | Synchronous | Basic Auth, OAuth 2.0, Client Certificate, API Key |
| **AS2** (B2B/EDI) | Partner Systems (Secure Data Exchange) | Asynchronous | Digital Certificates (Signing & Encryption), MDN (Message Disposition Notification) |
| **SOAP** | External Enterprise Systems | Synchronous | Basic Auth, Client Certificate, WS-Security |

---

## 5. Special / Utility Components *(Important Distinction)*

| Component | Type | Used For | Key Details | Communication |
|---|---|---|---|---|
| **ProcessDirect** | Adapter | iFlow-to-iFlow communication (same tenant) | Fastest, no network overhead, no external auth required | Sync & Async |
| **Data Store** | ❌ Not an Adapter | Temporary/long-term data persistence within iFlows | Stores/retrieves data across different iFlow executions | — |
| **Mail** | Adapter | Email alerts, notifications (SMTP/IMAP/POP3) | Outbound only; supports Gmail/Outlook via OAuth 2.0 | Asynchronous |

---

## 6. Authentication Types – Quick Reference by Adapter

| Authentication Type | Adapters That Support It |
|---|---|
| **Basic Auth** (Username + Password) | HTTP, SFTP, IDoc, SOAP, OData, SuccessFactors, Concur, Mail |
| **OAuth 2.0** (Client Credentials) | HTTP, OData, SuccessFactors, Ariba, Concur, Event Mesh, AMQP, Mail |
| **Client Certificate** (Mutual TLS / Two-Way SSL) | HTTP, SOAP, IDoc, SFTP (rare), OData, AMQP, MQTT |
| **SSH Public Key** | SFTP (Preferred & most secure) |
| **WS-Security** (UsernameToken / X.509) | SOAP only |
| **SAML Bearer** | SuccessFactors, OData |
| **API Key** (Header/Query Param) | HTTP |
| **Digital Certificate** (Signing/Encryption) | AS2 (B2B/EDI) |
| **Shared Secret** | Ariba |
| **Internal/Tenant-Managed** | JMS, ProcessDirect |

---

## 7. Real-Time Scenarios

| Scenario | Adapters Used | Flow | Communication Type |
|---|---|---|---|
| **File Integration** | SFTP + IDoc | SFTP → CPI → IDoc → SAP S/4HANA | Asynchronous |
| **API Integration** | HTTP | HTTP (REST API) → CPI → SAP or Third-party System | Synchronous |
| **Event-Driven Integration** | Event Mesh + OData | SAP Event Mesh → CPI → OData → S/4HANA | Asynchronous |
| **B2B / EDI Integration** | AS2 | AS2 (B2B/EDI) → CPI → SAP/Backend System | Asynchronous |
| **Messaging Integration** | JMS / AMQP / MQTT | Broker → CPI → SAP/Non-SAP Backend | Asynchronous |
| **iFlow-to-iFlow** | ProcessDirect | iFlow A → ProcessDirect → iFlow B | Sync & Async |

---

## 8. Key Concepts to Remember

### Synchronous vs. Asynchronous
- **Synchronous (Real-time):** Sender waits for response → HTTP, OData, SOAP, ProcessDirect
- **Asynchronous (Background):** Sender sends and doesn't wait → SFTP, IDoc, JMS, AMQP, MQTT, Event Mesh, AS2

### Critical Distinctions
| Concept | Explanation |
|---|---|
| **No REST Adapter** | There is no separate REST adapter in SAP CPI. RESTful services are handled using the **HTTP adapter** |
| **ProcessDirect vs HTTP** | Use ProcessDirect for internal iFlow-to-iFlow calls — faster, more secure, no additional network overhead |
| **JMS vs Data Store** | JMS = guaranteed delivery with retries (use when target may be offline). Data Store = persistence across iFlow executions |
| **SuccessFactors Auth** | Always prefer OAuth 2.0 SAML Bearer over Basic Auth for SuccessFactors |
| **AS2 Non-Repudiation** | MDN (Message Disposition Notification) in AS2 ensures non-repudiation for B2B partners |

### Communication Type Legend
| Symbol | Meaning |
|---|---|
| 🔄 Synchronous | Real-time / Waits for response |
| ⏳ Asynchronous | Background / No immediate response |

---

## 9. Interview Quick Tips

1. **HTTP handles REST** – Never say "REST Adapter" in an interview. It's the HTTP adapter.
2. **SFTP prefers SSH Keys** – Password auth is supported but less secure; always recommend SSH public key.
3. **IDoc = SAP-to-SAP** – Primarily for SAP ECC/S/4HANA integrations; asynchronous by nature.
4. **JMS for decoupling** – If a target system may go down, JMS queues ensure message retries without failing the sender.
5. **OData for S/4HANA APIs** – Modern SAP APIs expose OData; use V4 for newer systems.
6. **ProcessDirect = fastest internal call** – No authentication, no network latency.
7. **Data Store ≠ Adapter** – A common trick question; Data Store is a utility component, not an adapter.
8. **Event Mesh = OAuth 2.0** – Always uses Client Credentials grant type.
9. **AS2 = B2B/EDI security** – Relies on digital certificates for signing, encryption, and non-repudiation via MDN.
10. **WS-Security = SOAP only** – UsernameToken and X.509 tokens are exclusive to the SOAP adapter.

---

*Notes compiled from SAP CPI Complete Adapter Guide | Interview Preparation Reference*
