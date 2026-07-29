# JMS Adapter in SAP Cloud Integration — Complete Notes

*A layman's guide covering the JMS Adapter, Sender configuration, Receiver configuration, and Resource Limits/Optimization*

---

## 1. What is JMS, in Plain English?

Think of **JMS (Java Message Service)** as a **post office for messages between computer systems**.

- Instead of System A calling System B directly and *waiting* for a reply (like a phone call), System A drops a "letter" (message) into a **mailbox (queue)**.
- System B picks up that letter **whenever it is ready**.
- If System B is busy, sick (down), or slow, the letter just **waits safely in the mailbox** — it is not lost.

This is called **asynchronous messaging** — "asynchronous" simply means "not happening at the same time." The sender doesn't have to wait around for the receiver to respond.

In **SAP Cloud Integration (formerly SAP CPI / Cloud Platform Integration)**, the **JMS Adapter** is the component you use inside an **Integration Flow (iFlow)** to talk to this "mailbox" system.

---

## 2. Why Do We Need the JMS Adapter? (The Problem It Solves)

Imagine a scenario:

1. A sender system pushes an order message into SAP Cloud Integration.
2. SAP Cloud Integration must then send it onward to a receiver system (say, an ERP system).
3. What if the ERP system is temporarily unreachable — network hiccup, maintenance window, overload?

**Without JMS:** The message could simply fail, and the sender system would need to notice the failure and resend it manually. That's extra work and risk of message loss.

**With JMS:** SAP Cloud Integration stores the message safely in a **queue** and **automatically retries** sending it until it succeeds (or until retries are exhausted). The original sender does not need to know or do anything — SAP Cloud Integration handles the retry itself.

This concept is called **asynchronous decoupling** — the inbound (receiving) and outbound (sending) parts of message processing are "decoupled" (separated) by a queue sitting in between, so a problem on one side doesn't immediately affect the other side.

### Key point on the message broker
The JMS Adapter uses a **message broker provided by SAP** (built on **SAP Event Mesh** technology). It does **NOT** connect to external/customer-owned message brokers (like a company's own IBM MQ or ActiveMQ server — those need a *different*, dedicated adapter, e.g., the IBM MQ Adapter).

### Licensing note
- The JMS Adapter is available on **all SAP Integration Suite editions except the Basic edition**.

---

## 3. The Two Halves of the JMS Adapter

The JMS Adapter comes in two flavors, used at two different points of a message's journey:

| Adapter Type | Role | Simple Analogy |
|---|---|---|
| **JMS Sender Adapter** | **Consumes** (reads/picks up) messages *from* a JMS queue to continue processing them in the iFlow | The postal worker taking a letter *out of* the mailbox to deliver it |
| **JMS Receiver Adapter** | **Produces** (writes/stores) messages *into* a JMS queue for later processing | Someone *putting* a letter *into* the mailbox |

A typical resilient pattern:

```
Sender System → (HTTP/other adapter) → JMS Receiver Adapter → [QUEUE] → JMS Sender Adapter → Receiver System
```

The message lands in the queue via the **Receiver adapter step**, waits there, and then a **separate iFlow (or the same one split by the queue) uses the Sender adapter** to pick it up and continue processing — including retries if the final destination isn't reachable.

---

## 4. How Messages Are Processed: Order Matters (or Doesn't)

This is one of the most important — and often confusing — concepts. When multiple messages sit in a queue, **in what order do they come out?** SAP Cloud Integration gives you two choices, called **Access Types**:

### 4.1 Non-Exclusive Access (the "default," parallel style)
- Multiple worker nodes (think: multiple postal workers) can pick up messages from the **same queue at the same time**.
- This means messages can be processed **in parallel**, which is great for **speed and throughput**.
- **Downside:** There is **no guarantee of order**. Message #5 might finish processing before Message #3.
- Use this when the **order of messages does not matter** to your business process.
- Controlled by a setting called **"Number of Concurrent Processes"** — this decides how many messages can be picked up and processed at the same time.

### 4.2 Exclusive Access (the "strict line," sequential style)
- Only **one consumer** (one worker) is allowed to access the queue at any given time.
- This **guarantees** that messages are processed in the **exact order** they were received — first in, first out, strictly.
- **Downside/Trade-off:**
  - Since only one process handles messages at a time, this **limits scalability** and **reduces throughput** (things move slower overall) compared to Non-Exclusive.
  - If **one message fails/errors out**, it will **block all the messages behind it** in the queue (like a traffic jam — nobody can pass until the stuck car is towed away).
- Use this only when **order is critical** to your business logic (e.g., processing financial transactions in sequence).
- This access type is a **newer feature**, introduced starting with a Cloud Integration release around **March 2025 (increment 2501)**. Before that, only Non-Exclusive queues existed.

**Simple rule of thumb:**
> Need speed and order doesn't matter? → **Non-Exclusive**
> Need strict order and can tolerate slower throughput? → **Exclusive**

---

## 5. Configuring the JMS Sender Adapter (Consuming Messages)

The **Sender Adapter** is placed at the **start** of an integration flow (or the start of the "second half" of a split flow) to **pull messages out of a queue** for processing.

### Key configuration areas (Connection Tab and beyond):

- **Queue Name**: The name of the mailbox you are reading from. This must match the queue name used when the message was originally placed there (usually by a Receiver adapter elsewhere).
- **Access Type**: Choose **Non-Exclusive** or **Exclusive**, as explained in Section 4.
- **Number of Concurrent Processes**: Relevant for Non-Exclusive access — controls how many messages can be picked up and worked on simultaneously by different worker nodes.
- **Deployment behavior**: When you **deploy** an integration flow that has a JMS Sender Adapter, SAP Cloud Integration:
  1. Creates the JMS queue (if it doesn't already exist), based on the name you specified.
  2. Opens a **consumer connection** from your tenant to the SAP-managed message broker, so it can start listening for and picking up messages.

### Things to remember
- If you update the adapter version later (SAP occasionally releases new adapter features), you may need to manually select **"Update Version"** on the adapter shape to get the latest capabilities.
- Make sure you are using the correct **Runtime Profile** — using the wrong one can prevent certain adapter features from showing up or working properly.

---

## 6. Configuring the JMS Receiver Adapter (Producing/Storing Messages)

The **Receiver Adapter** is placed **after** the point in the flow where you want to "park" a message safely before onward processing.

### What it does
- Takes the message currently being processed in the iFlow and **writes it into a JMS queue** (creates the queue on first deployment, just like the Sender side).
- This is the mechanism that enables the **"store now, retry later"** resilience pattern described in Section 2.

### Key configuration areas
- **Queue Name**: Same idea as the sender side — this is the mailbox name the message will be dropped into. A Sender Adapter elsewhere will need to reference this exact same queue name to pick the messages back up.
- **Access Type**: Non-Exclusive vs Exclusive — same concepts as Section 4, since it affects how the *corresponding sender* will later read this queue.
- Like the sender adapter, deployment triggers **queue creation** and connection setup to the SAP-managed broker.

### Real-world use case pattern
1. **Sender system → HTTPS Adapter (Sender) → [processing/validation] → JMS Receiver Adapter → Queue.**
2. Separately, a **JMS Sender Adapter** reads from that same queue and forwards the message on to the true final destination (ERP, SuccessFactors, a partner system, etc.).
3. If step 3's destination is down, the message just waits/retries in the queue — no message loss, no manual resend needed.

---

## 7. Standard JMS Message Headers & Exchange Properties

When a message flows through the JMS Adapter, SAP Cloud Integration attaches/reads certain **headers** (metadata riding alongside the actual message content — like the "To/From/Date" fields on an envelope, separate from the letter inside).

### Important limitation to remember
> **The JMS adapter only stores *simple* data type headers** — meaning **primitive data types** (like numbers, booleans) **or plain strings**. It does **not** support complex/object-type headers. If you try to pass a complex object as a header into a JMS queue, it will not be stored/carried correctly.

### Commonly used Standard JMS / Integration Framework Headers

| Header / Property | Meaning (Layman's Terms) |
|---|---|
| `JMSCorrelationID` | A tracking ID used to "link" a response message back to its original request message — like a reference number on a support ticket. |
| `JMSMessageID` | A unique ID automatically assigned to each message by the messaging system — like a tracking number on a parcel. |
| `JMSTimestamp` | The date/time the message was handed to the messaging system. |
| `JMSDeliveryMode` | Tells the broker whether the message should be **Persistent** (survives a system restart — like writing it on paper) or **Non-Persistent** (may be lost on restart — like a sticky note). |
| `JMSPriority` | A number indicating how urgently the message should be handled (higher = more urgent), if the broker supports priority queues. |
| `JMSRedelivered` | A flag (true/false) telling you whether this message is being delivered again because a previous delivery attempt didn't complete successfully. |
| `JMSExpiration` | An optional "expiry date/time" after which the broker may discard the message if it hasn't been processed. |
| `CamelJmsDestinationName` | (Integration Framework/Camel-related) The name of the queue/destination the message is associated with. |
| `SAP_MessageProcessingLogID` | SAP Cloud Integration's internal ID used for tracking the message in Monitoring / Message Processing Logs (MPL). |

*Note: Exact header names available can vary slightly by adapter version — always check the **"Headers and Exchange Properties Provided by the Integration Framework"** reference in SAP Help for the most current, authoritative list when building your flow.*

---

## 8. Transactions — Why Every JMS Message Needs One

- **Processing JMS messages always requires a transaction.** A transaction is like a "safety net" — it ensures that either:
  - The message is **fully picked up (or fully stored) and processing is confirmed complete**, **OR**
  - If something goes wrong midway, the action is **rolled back** — the message is *not* lost and *not* half-processed.
- Both the **Sender** (consuming) and **Receiver** (producing/providing) sides rely on this transactional handling to guarantee message safety.

### Known limitations with transactions
There are certain restrictions when the JMS adapter is combined with these specific iFlow steps in the **same transaction**:
- **Data Store Operations** steps
- **Aggregator** steps
- **Global Variables**

In short: combining JMS with these features needs extra care, since their transactional behavior may not fully align with the JMS adapter's transaction. (See SAP Community blog: *"Cloud Integration – How to configure Transaction Handling in Integration Flow"* for deeper guidance.)

---

## 9. JMS Resource Limits & How to Optimize Usage

Every tenant gets a **JMS messaging instance** — think of it as a **limited-size warehouse** for all your queues. It's not infinite, so SAP puts **caps (limits)** on it.

### What is limited?
The JMS messaging instance is shared by **multiple adapter types** that all use asynchronous, queue-based messaging:
- **JMS Adapter**
- **AS2 Adapter**
- **AS4 Adapter**
- **XI Adapter**

So if you have many iFlows using any of these adapters, they are all drawing from the **same pool of resources**.

### What resources are capped?
- **Number of queues** you can create
- **Storage capacity** (how much message data can sit in queues at once)
- **Number of connections** to the broker

These limits differ depending on your license/edition:
- **SAP Integration Suite** (any service plan)
- **SAP Cloud Integration (Enterprise Edition)** — existing/legacy licenses
- **SAP BTP Event Mesh** — has its own default/max queue capacity limits as well

Each plan/edition has its own **default queue capacity** and a **maximum capacity** you can request/scale up to (there is a ceiling — you can't increase forever).

### How is one queue's "cost" calculated?
Resources such as **number of transactions** and **message volume** can be calculated based on how many queues you have and how they're used. In other words: **more queues + more messages = more resource consumption**, so design efficiently rather than creating a queue for every tiny use case.

### When are queues created / removed?
- A queue is **created** the moment the **first integration flow that uses it is deployed**.
- Understanding this helps avoid "surprise" queue creation eating into your limits — every unique queue name referenced across your deployed iFlows counts.

### Monitoring: "Manage Queues"
- SAP Cloud Integration provides a monitor called **"Manage Queues"**, where you can see the current health/load of your queues.
- If you notice **critical resource situations** here (e.g., approaching your limits), you should **optimize your use of transactions** — for example, by:
  - Reducing the number of separate queues where possible (consolidate similar use cases).
  - Reviewing whether Exclusive access (single consumer, slower) vs Non-Exclusive (parallel, faster) is the right fit — Exclusive queues behave differently in terms of resource consumption, and older sizing recommendations (written for Non-Exclusive queues) may **not directly apply** to Exclusive queues.
  - Cleaning up / undeploying iFlows (and their queues) that are no longer needed.

### Runtime Nodes (a quick vocabulary note)
- **Runtime nodes** = also called **"worker nodes."** These are the actual compute units in SAP Cloud Integration's backend that do the work of processing messages (including pulling from JMS queues). More available runtime nodes generally means better ability to handle **Non-Exclusive** (parallel) queue processing.

---

## 10. Retry Behavior — What Happens When Delivery Fails

- If the final receiver system is unavailable, the JMS adapter **automatically retries** delivery.
- **Default retry interval:** approximately **1 minute** between attempts.
- **Alternative option — Exponential Back-off:** Instead of a fixed 1-minute gap, the wait time **doubles after each failed attempt** (e.g., 1 min, 2 min, 4 min, 8 min...). This is useful to avoid hammering an already-struggling receiver system with constant retries.
- The adapter keeps retrying until either:
  - The receiver becomes reachable and the message is delivered successfully, **or**
  - The delivery attempts are **exhausted / time out**, at which point manual intervention (e.g., via Message Processing Monitoring) is typically needed.

---

## 11. Quick-Reference Summary Table

| Concept | Plain-English Meaning |
|---|---|
| JMS Adapter | A "mailbox" mechanism for reliable, asynchronous (not-instant) messaging between steps or systems |
| Sender Adapter | Reads/picks up messages **from** a queue |
| Receiver Adapter | Writes/stores messages **into** a queue |
| Non-Exclusive Access | Multiple workers process messages in parallel — fast, but order not guaranteed |
| Exclusive Access | One worker at a time — guarantees order, but slower and one stuck message blocks the rest |
| Number of Concurrent Processes | How many parallel workers are allowed (Non-Exclusive mode) |
| Message Broker | SAP-managed backend (based on SAP Event Mesh) that actually stores the queues — cannot be swapped for a customer's own broker |
| Transaction | The "all-or-nothing" safety wrapper ensuring a message isn't lost or half-processed |
| JMS Resource Limits | Caps on number of queues, storage, and connections per tenant/license edition |
| Manage Queues Monitor | The dashboard to check queue health and resource pressure |
| Retry / Exponential Back-off | Automatic re-attempts at fixed or increasing intervals when delivery fails |

---

## 12. Source References (SAP Help Portal)

1. JMS Adapter (Overview): https://help.sap.com/docs/cloud-integration/sap-cloud-integration/jms-adapter
2. Configure the JMS Sender Adapter: https://help.sap.com/docs/cloud-integration/sap-cloud-integration/configure-jms-sender-adapter
3. Configure the JMS Receiver Adapter: https://help.sap.com/docs/cloud-integration/sap-cloud-integration/configure-jms-receiver-adapter
4. JMS Resource Limits and Optimizing their Usage: https://help.sap.com/docs/cloud-integration/sap-cloud-integration/jms-resource-limits-and-optimizing-their-usage

*Tip: Since SAP Help Portal content evolves, always cross-check the live pages above for the newest parameter names, screenshots, and any newly introduced features (such as Exclusive Access, which was only added in 2025).*
