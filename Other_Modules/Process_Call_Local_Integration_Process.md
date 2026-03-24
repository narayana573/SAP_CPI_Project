In SAP Cloud Integration (SAP CPI), both **Process Calls** and **Local Integration Processes** are used to modularize integration flows, making them cleaner, reusable, and easier to maintain.

Think of a **Local Integration Process** as a "sub-routine" or a function, and a **Process Call** as the "trigger" that executes that function.

---

## 1. Local Integration Process

A Local Integration Process is a separate container within your main integration flow (i.e., the same `.iflw` file). It allows you to group multiple steps that perform a specific logical task.

* **Encapsulation:** Instead of having 50 steps in one long horizontal line, you can move specific logic (like complex mapping or exception handling) into a local process.
* **Reusability:** You can call the same local process multiple times from different points in the main flow.
* **Scope:** It is "local" to that specific artifact. You cannot call a local process in "Flow A" from "Flow B."

---

## 2. Process Call

The Process Call is the actual element you drag and drop into your **Main Integration Process**. Its only job is to route the message exchange to a Local Integration Process.

### Types of Process Calls

1. **Local Process Call:** Used to trigger a Local Integration Process within the same integration flow.
2. **External Process Call:** (Less common) Used to call another integration flow entirely, though this is usually handled via Request-Reply with protocols like ProcessDirect.

---

## Key Differences & Interaction

| Feature | Main Integration Process | Local Integration Process |
| --- | --- | --- |
| **Trigger** | Triggered by an External Sender (HTTPS, SFTP, etc.) | Triggered by a **Process Call** |
| **Usage** | High-level flow logic | Detailed sub-logic/reusable steps |
| **Quantity** | Only one per artifact | Can have multiple in one artifact |

### How the Data Flows

When a **Process Call** is reached:

1. The **Message Body** and **Headers/Properties** are passed into the Local Integration Process.
2. The Local Process executes its steps (e.g., a Groovy script or a Mapping).
3. Once the Local Process finishes, the message (with any changes made) is handed back to the Main Process to continue from the next step after the Process Call.

---

## Why use them?

* **Readability:** It prevents "spaghetti" flows.
* **Maintenance:** If you need to change a logic used in three different places, you only change it once in the Local Process.
* **Error Handling:** You can define specific "Exception Sub-flows" within a Local Integration Process to handle errors at a granular level.

Would you like me to show you how to pass specific headers between a Main Process and a Local Process?
