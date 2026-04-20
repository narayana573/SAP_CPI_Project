
```text
[Sender: Generics Payload]
       |
       v
========================================================================
 MAIN INTEGRATION PROCESS
========================================================================
[Content Modifier]  ---> (Stores MO logs for optimization)
       |
       v
[Parallel Multicast] ---> (Splits payload to run 3 branches simultaneously)
       |
       +-------------------------------------------------+
       |                                                 |
       v                                                 v
[Process Call 1]                                  [Process Call 2] ... (and Process Call 3)
       |
========================================================================
 GLOBAL EXCEPTION SUBPROCESS: Handles overall main flow errors
========================================================================


Detailed view of the 3 Local Integration Processes (Branches):

🟢 BRANCH 1: ORDERS BRANCH
  [Splitter] ------------> Splits payload based on XPath (6 order records)
       |
  [Content Modifier] ----> Stores 'Order ID' and 'Source Payload' in properties
       |
  [Request-Reply] -------> Calls external OData API dynamically using Order ID
       |
  [Content Modifier] ----> Combines the OData Response + Original Source Payload 
       |
  [Receiver] ------------> Sends combined data to Blackline SFTP
       |
  *[Exception Subprocess]* -> Local error handling for Orders branch


🔵 BRANCH 2: SFTP BRANCH
  [Poll Enrich] ---------> Connects via SFTP Adapter to fetch all files (using '*') 
       |
  [Router] --------------> Condition: "poll enrich message found = true"
       |
       +---> Route 1 (True) -----> [Receiver] Sends files to Blackline Server
       |
       +---> Route 2 (False) ----> [Mail Adapter] Sends alert to team that files are missing
       |
  *[Exception Subprocess]* -> Local error handling for SFTP branch


🟣 BRANCH 3: CUSTOMERS BRANCH
  [Content Modifier] ----> Stores 'Customer ID' in properties
       |
  [Request-Reply] -------> Calls external OData API dynamically using Customer ID
       |
  [Router] --------------> Condition: "ABC = 123" (to pick only the 1st customer record)
       |
       +---> Route 1 (True) -----> [Receiver] Sends the 1st record to Blackline Server
       |
       +---> Route 2 (False) ----> Default fallback (Ignored)
       |
  *[Exception Subprocess]* -> Local error handling for Customers branch
```

**Key Highlights of this Architecture:**
*   **Modularity:** Instead of building everything in one massive flow, the main flow simply uses **Process Calls** to trigger three isolated Local Integration Processes.
*   **Robust Error Handling:** It features localized **Exception Subprocesses** inside each of the three branches, plus a **Global Exception Subprocess** in the main flow.
*   **Dynamic API Calls:** It heavily utilizes **Content Modifiers** to store properties and payload data temporarily to perform dynamic **Request-Reply** calls to an external OData API.
