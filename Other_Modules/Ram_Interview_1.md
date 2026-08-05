```
## SAP CPI Interview 1

### Introduction

1. Tell me about yourself.

### Integration Patterns

2. What are the various integration patterns you have worked on?
3. What do you mean by integration patterns?
4. If multiple source systems are targeting the same SAP S/4HANA system, which integration pattern would you use?
5. Why would you use Parallel Multicast in that scenario?

### Synchronous vs Asynchronous

6. Have you worked on synchronous and asynchronous scenarios?
7. What is synchronous communication?
8. What is asynchronous communication?
9. What is the difference between synchronous and asynchronous communication?

### OData

10. If you have an OData service, how do you design an iFlow for it?
11. How do you configure the OData adapter in SAP CPI?
12. How comfortable are you with OData?
13. Have you implemented pagination in OData?
14. What is pagination?
15. Why do we use a Looping Process Call with pagination?
16. If there are 1,000 records, how do you determine when to stop fetching pages and ensure all records are processed?

### Enterprise Integration Patterns

17. Which Enterprise Integration Pattern (EIP) components have you worked on?
18. Explain Aggregator with an example.
19. Explain Join with an example.
20. What is the difference between Join and Aggregator?

### Externalized Parameters

21. Have you worked with Externalized Parameters?
22. How do you externalize parameters in an Integration Flow?
23. When should Externalized Parameters be used?

### JMS Adapter

24. When do you use a JMS Adapter?
25. Why do we use JMS?
26. Explain Guaranteed Delivery.
27. How do you technically decouple sender and receiver using JMS?
28. What happens if the receiver system is down?
29. How are messages stored in JMS?

### API Management

30. Have you worked on SAP API Management?
31. How do you monitor an API Proxy if it fails?
32. What is the purpose of an API Proxy?
33. Which API policies have you used?
34. What is Rate Limiting Policy?
35. What is Quota Policy?

### SFTP Adapter

36. What are the prerequisites for configuring an SFTP Adapter?
37. How do you establish an SFTP connection?
38. What Security Materials are required?
39. What is a Known Host Key?
40. How do you perform an SFTP Connectivity Test?
41. What authentication methods have you used in SFTP?
42. Have you used Username/Password Authentication?
43. Have you used SSH Key Authentication?

### Monitoring & Debugging

44. How do you identify whether an SFTP file has been picked up?
45. While testing an iFlow, how do you verify that the file was picked successfully?
46. Which monitoring tools do you use?
47. How do you debug an iFlow?

### Complex Scenario

48. Explain one complex integration scenario you have worked on.
49. Describe an end-to-end integration you developed.
50. What was the source system?
51. What was the target system?
52. What transformation logic did you implement?
53. What challenges did you face?
54. How did you resolve those challenges?

---

## Topics Covered in This Interview

- SAP CPI Introduction
- Enterprise Integration Patterns (EIP)
- Synchronous vs Asynchronous
- OData Adapter
- Pagination
- Looping Process Call
- Join & Aggregator
- Externalized Parameters
- JMS Adapter
- Guaranteed Delivery
- API Management
- SFTP Adapter
- Security Materials
- Monitoring
- Debugging
- Complex Integration Scenario
```



# SAP CPI Interview 1 — Answers

---

## Introduction

**1. Tell me about yourself.**

I'm an SAP CPI (Cloud Platform Integration) Consultant with hands-on experience designing, developing, and supporting integration flows on SAP Integration Suite. I've worked on adapters like OData, SFTP, SOAP, REST, IDoc, JMS, and Mail, along with Groovy scripting for custom transformations. My work spans building end-to-end integrations between SAP S/4HANA and third-party/cloud systems, handling both synchronous and asynchronous scenarios, implementing Enterprise Integration Patterns, and using API Management for exposing secure APIs. Currently, I'm working on a Boomi-to-CPI migration project, converting existing Boomi processes into CPI iFlows while ensuring functional parity and improved monitoring/error handling.

---

## Integration Patterns

**2. What are the various integration patterns you have worked on?**

Content-Based Router, Splitter (General and Iterating), Gather, Aggregator, Join, Multicast, Parallel Multicast, Content Enricher, Message Filter, Router, and Process Direct for internal iFlow-to-iFlow calls.

**3. What do you mean by integration patterns?**

Integration patterns (Enterprise Integration Patterns/EIPs) are standard, reusable design solutions for common problems in message-based integration — like how to route, split, combine, filter, or enrich messages as they move between systems. SAP CPI provides these as pre-built palette steps so we don't have to hand-code that logic.

**4. If multiple source systems are targeting the same SAP S/4HANA system, which integration pattern would you use?**

I'd use **Multicast** (or **Parallel Multicast** depending on whether the branches need to run independently) if a single message needs to be sent to S/4HANA along with other target systems simultaneously. But if the question is about multiple *source* systems each independently sending data into S/4HANA, then typically each source has its own iFlow with a **Content-Based Router** to route to the right processing logic within S/4HANA (e.g., different IDoc types or OData entities), and I'd centralize common logic using **Process Direct** so multiple iFlows can call a shared S/4HANA-posting sub-flow instead of duplicating logic.

**5. Why would you use Parallel Multicast in that scenario?**

Parallel Multicast lets the message be sent to multiple receivers/branches at the same time instead of sequentially, which reduces overall processing time. It's useful when the branches are independent of each other (no dependency on one branch's outcome for another) and you want better throughput — for example, sending the same payload to S/4HANA and simultaneously logging it or sending a notification, without waiting for one call to finish before starting the next.

---

## Synchronous vs Asynchronous

**6. Have you worked on synchronous and asynchronous scenarios?**

Yes, I've implemented both. Synchronous, for example, in OData/SOAP-based real-time lookups where the caller waits for an immediate response, and asynchronous for scenarios like SFTP file-based integrations or JMS-queued postings where the sender doesn't need to wait for downstream processing to complete.

**7. What is synchronous communication?**

The sender sends a request and waits (blocks) until it receives a response from the receiver before continuing. Request and response happen within the same connection/transaction — e.g., a real-time OData GET call.

**8. What is asynchronous communication?**

The sender sends a message and does not wait for a response; it continues its own processing. The message may be picked up and processed later, often through a queue (like JMS) or a scheduled poll (like SFTP), decoupling sender and receiver in time.

**9. What is the difference between synchronous and asynchronous communication?**

| Aspect | Synchronous | Asynchronous |
|---|---|---|
| Sender behavior | Waits for response | Doesn't wait |
| Coupling | Tightly coupled in time | Decoupled in time |
| Use case | Real-time lookups, validations | File transfers, batch, guaranteed delivery |
| Failure handling | Immediate error visibility | Requires retry/queue-based recovery |
| Example adapters | OData, SOAP, HTTP | JMS, SFTP (poll), Mail |

---

## OData

**10. If you have an OData service, how do you design an iFlow for it?**

I start by identifying the entity set and operation (GET/POST/PUT/DELETE) needed. On the sender side, I configure the trigger (timer, or a sender adapter if it's inbound). Then I add a **Request-Reply** step with the OData adapter configured as receiver, specifying the OData service URL, entity set, query options (filters, select, expand), and authentication. I add mapping/transformation steps before and after as needed to convert the source payload into the OData-expected structure and to handle the response. If large volumes are expected, I add pagination handling using a Looping Process Call.

**11. How do you configure the OData adapter in SAP CPI?**

In the Receiver channel: select adapter type as OData V2/V4, provide the Address (service root URL + entity set), select the Query type (Query/Create/Update/Delete/Function Import), define Resource Path, Query Options (filter, top, skip, select, expand), and configure authentication (Basic, OAuth2 Client Credentials, or Principal Propagation) along with the Credential Name pointing to a Security Material.

**12. How comfortable are you with OData?**

I'm quite comfortable — I've configured OData adapters for both querying (GET with filters/expand) and creating/updating records (POST/PUT), handled metadata-driven mapping, and dealt with OData-specific error responses and batch requests.

**13. Have you implemented pagination in OData?**

Yes, using the `$top` and `$skip` query options combined with a **Looping Process Call** to iterate through pages until all records are fetched.

**14. What is pagination?**

Pagination is the technique of retrieving large result sets in smaller, fixed-size chunks (pages) instead of all at once — using parameters like `$top` (page size) and `$skip` (offset) in OData — to avoid timeouts, memory issues, and performance degradation.

**15. Why do we use a Looping Process Call with pagination?**

Because the total number of records isn't known upfront in many cases, a Looping Process Call lets the iFlow repeatedly call the OData service, incrementing the `$skip` value each time, until a stopping condition is met (e.g., an empty page or reaching the total count). It keeps the looping logic modular and reusable as a sub-process.

**16. If there are 1,000 records, how do you determine when to stop fetching pages and ensure all records are processed?**

Two common approaches:
- Use `$inlinecount=allpages` (OData V2) or `$count=true` (V4) to get the total record count upfront, then calculate the number of iterations needed (e.g., 1000 records / 100 per page = 10 iterations), and loop exactly that many times.
- Alternatively, keep looping and checking the response size each time — if the returned page has fewer records than the page size (or zero records), that signals the last page, and the loop terminates.

I typically prefer the count-based approach since it's more deterministic and avoids an extra "empty" call at the end.

---

## Enterprise Integration Patterns

**17. Which Enterprise Integration Pattern (EIP) components have you worked on?**

Splitter (General/Iterating), Gather, Aggregator, Join, Multicast, Content Enricher, Content-Based Router, Message Filter, and Process Direct.

**18. Explain Aggregator with an example.**

Aggregator combines multiple related messages (usually produced by a Splitter) back into a single message, based on a correlation ID and a completion condition (e.g., number of messages expected, or a timeout). Example: An order is split into individual line items for parallel processing; the Aggregator collects all processed line-item responses and combines them back into one consolidated order-confirmation message before sending it to S/4HANA.

**19. Explain Join with an example.**

Join is used to combine two related messages/branches within the same message flow — typically pausing one branch until a corresponding branch (via Multicast/Parallel Multicast) completes, then merging their payloads. Example: A Parallel Multicast sends the payload to both S/4HANA (to fetch material master data) and a third-party pricing API simultaneously; a Join step waits for both responses and merges them into a single combined payload for further processing.

**20. What is the difference between Join and Aggregator?**

| Aspect | Join | Aggregator |
|---|---|---|
| Used with | Multicast/Parallel Multicast branches | Splitter output messages |
| Purpose | Merge parallel branches of the *same* original message | Combine multiple *split* sub-messages into one |
| Correlation | Based on message ID from the multicast | Based on correlation ID + completion condition |
| Typical scenario | Combining responses from parallel calls | Recombining split line items/batches |

---

## Externalized Parameters

**21. Have you worked with Externalized Parameters?**

Yes, I use them regularly to avoid hardcoding values across Dev/QA/Prod environments.

**22. How do you externalize parameters in an Integration Flow?**

In each configurable adapter/step field, I click the externalization icon and mark the field as externalized, giving it a parameter name and default value. This generates entries in the iFlow's Configure screen, where values can be set per transport (Dev/Test/Prod) without touching the iFlow design itself.

**23. When should Externalized Parameters be used?**

For anything environment-specific or subject to change without a design-time redeploy — endpoint URLs, credential names, file paths/directories, polling intervals, filter values, and any business-configurable thresholds (like batch size or retry counts).

---

## JMS Adapter

**24. When do you use a JMS Adapter?**

When I need guaranteed, asynchronous, decoupled message delivery between sender and receiver — especially for high-volume processing, or when the receiver system may be temporarily unavailable and messages shouldn't be lost.

**25. Why do we use JMS?**

To decouple sender and receiver in time, ensure guaranteed delivery even if the receiver is down, support asynchronous/parallel processing, and enable retry without impacting the source system.

**26. Explain Guaranteed Delivery.**

Guaranteed Delivery means once a message is written into the JMS queue, it's persisted (stored durably) so it won't be lost even if CPI restarts or the receiver is unavailable. The message stays in the queue until it's successfully processed, and if processing fails, it can be retried or moved to a dead-letter-like state, ensuring no data loss.

**27. How do you technically decouple sender and receiver using JMS?**

I split the integration into two iFlows (or two flow steps) connected via a JMS queue: the first iFlow receives the message and writes it to the JMS queue (sender side, decoupled from real-time receiver availability); a second iFlow/sender channel picks the message off the queue and delivers it to the actual target system. This way, the source system gets an immediate acknowledgment once the message hits the queue, independent of the target system's availability.

**28. What happens if the receiver system is down?**

The message remains safely persisted in the JMS queue. CPI will retry delivery based on the configured retry interval/count. The sender system already received its acknowledgment when the message was queued, so it's unaffected. Once the receiver comes back up, queued messages are delivered in order.

**29. How are messages stored in JMS?**

Messages are stored in a persistent, durable queue within the CPI tenant's JMS broker, in the order they were received (FIFO). Each message retains its payload and headers/properties until it is consumed or expires per the configured retention/retry policy.

---

## API Management

**30. Have you worked on SAP API Management?**

Yes, for exposing CPI iFlows as managed, secured APIs to external/internal consumers.

**31. How do you monitor an API Proxy if it fails?**

I check the API Management Analytics/Monitor dashboard for error trends, and drill into the Trace tool to see the exact request/response flow through each policy step where the failure occurred. I also check the backend (CPI iFlow) message monitoring to correlate whether the failure originated in the proxy layer or the backend integration itself.

**32. What is the purpose of an API Proxy?**

An API Proxy acts as a managed façade in front of the backend service — it decouples the consumer-facing API contract from the actual backend implementation, and lets you apply cross-cutting policies (security, rate limiting, quota, logging, transformation) without changing the backend.

**33. Which API policies have you used?**

Quota, Spike Arrest/Rate Limiting, Verify API Key, OAuth/Verify Access Token, CORS, Assign Message, Extract Variables, and Raise Fault for custom error handling.

**34. What is Rate Limiting Policy?**

It controls how many requests a consumer can make within a very short time window (e.g., per second) — mainly to protect the backend from sudden traffic spikes ("spike arrest"), regardless of total volume over a longer period.

**35. What is Quota Policy?**

It limits the total number of API calls a consumer/app can make over a longer defined period (e.g., 1,000 calls per day/month), used for usage governance and enforcing subscription/plan-based limits, as opposed to short-burst protection.

---

## SFTP Adapter

**36. What are the prerequisites for configuring an SFTP Adapter?**

Host/port details of the SFTP server, valid credentials (username/password or SSH key), the correct directory path, required Security Materials deployed in CPI (User Credentials or SSH Key pair, and Known Host key if host key verification is enabled), and network connectivity/whitelisting confirmed between CPI and the SFTP server.

**37. How do you establish an SFTP connection?**

Configure the sender/receiver channel with adapter type SFTP, provide the server address and port, directory and file name pattern, and reference the deployed Security Material (credentials or SSH key) for authentication. Then run a Connectivity Test from the channel configuration to validate the connection before deploying.

**38. What Security Materials are required?**

User Credentials (username/password) for basic auth, or an SSH Key pair (Private Key) for key-based auth, and optionally a Known Host key artifact if strict host key checking is enabled.

**39. What is a Known Host Key?**

It's the SSH public key fingerprint of the SFTP server, uploaded into CPI as a Security Material, used to verify the server's identity during connection — protecting against man-in-the-middle attacks by ensuring CPI only connects to the trusted, expected server.

**40. How do you perform an SFTP Connectivity Test?**

In the channel configuration (sender or receiver), there's a "Check Connection" / connectivity test button that attempts to establish the SFTP session using the configured credentials/keys and reports success or a specific error (auth failure, host unreachable, invalid path, etc.) without needing to deploy the full iFlow.

**41. What authentication methods have you used in SFTP?**

Both Username/Password and SSH Key-based authentication.

**42. Have you used Username/Password Authentication?**

Yes — configured as a User Credentials Security Material referenced in the SFTP channel, straightforward for servers that support basic auth.

**43. Have you used SSH Key Authentication?**

Yes — generated/obtained a key pair, uploaded the private key as an SSH Key Security Material in CPI, and ensured the corresponding public key was registered on the SFTP server, which is generally more secure than password-based auth.

---

## Monitoring & Debugging

**44. How do you identify whether an SFTP file has been picked up?**

I check the Message Processing Monitor in CPI (Monitor Message Processing / Integrations and APIs), filter by the iFlow name and time range, and confirm a message instance was triggered corresponding to the file. I can also check the SFTP server directly (or an archive/processed folder if configured) to see if the file was moved/renamed after pickup, which confirms successful consumption.

**45. While testing an iFlow, how do you verify that the file was picked successfully?**

I check the Message Processing Log for a "Completed" status with the correct timestamp, inspect the message payload in the trace to confirm it matches the source file content, and verify the file was archived/moved (if the channel is configured to do so) or check for absence in the source directory (if configured to delete after read).

**46. Which monitoring tools do you use?**

SAP CPI's Monitor Message Processing (Integrations and APIs cockpit), Message Processing Log with trace-level detail, Alerting configuration for proactive failure notifications, and API Management's Analytics dashboard for proxy-level monitoring.

**47. How do you debug an iFlow?**

I enable trace-level logging on the message processing run, inspect each step's input/output payload and headers in the trace, use Groovy script logging (`MessageLog`) for custom debug points, and if needed, use local testing via the "Local Deployment" or simulate individual mapping steps in isolation before deploying to test/QA.

---

## Complex Scenario

**48. Explain one complex integration scenario you have worked on.**

I'll walk through a scenario I've worked with recently: an AI/agentic architecture involving DataIKU, AWS, and SAP S/4HANA for a payroll journal entry process. The flow needed to reliably pass processed payroll data from an AWS-based/DataIKU pipeline into S/4HANA for journal entry posting, with guaranteed delivery since payroll postings can't be lost or duplicated.

**49. Describe an end-to-end integration you developed.**

The source system pushes processed payroll journal entry data (from AWS/DataIKU) to CPI via a REST/HTTP call. CPI validates and transforms the payload into the format required for posting (mapping fields to the S/4HANA journal entry structure), writes the message to a JMS queue for guaranteed delivery and decoupling, and a second flow step picks it from the queue and posts it to S/4HANA (via OData or IDoc, depending on the target API). Success/failure acknowledgments are logged and, on failure, the message is retried from the queue without needing the source system to resend.

**50. What was the source system?**

An AWS/DataIKU-based processing pipeline generating payroll journal entry data.

**51. What was the target system?**

SAP S/4HANA (posting to the relevant journal entry/finance module).

**52. What transformation logic did you implement?**

Mapping the source JSON/CSV payroll structure into the target OData/IDoc schema — field-level mapping (employee ID, cost center, GL account, amount, currency), data type conversions, and validation checks (e.g., mandatory field checks, amount sign conventions) using Groovy scripts and Message Mapping/graphical mapping steps.

**53. What challenges did you face?**

Ensuring no duplicate postings if the source retried a call (idempotency), handling S/4HANA downtime windows without losing messages, and reconciling error scenarios where partial batches failed validation midway.

**54. How did you resolve those challenges?**

Used JMS for guaranteed delivery and decoupling from S/4HANA availability, implemented idempotency checks using a unique reference ID for correlation, and used a Splitter with an Aggregator to process batch line items individually while still tracking overall batch completion — so partial failures could be isolated, logged, and reprocessed without reposting successful items.

---

## Topics Covered
SAP CPI Introduction · Enterprise Integration Patterns (EIP) · Synchronous vs Asynchronous · OData Adapter · Pagination · Looping Process Call · Join & Aggregator · Externalized Parameters · JMS Adapter · Guaranteed Delivery · API Management · SFTP Adapter · Security Materials · Monitoring · Debugging · Complex Integration Scenario
