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
