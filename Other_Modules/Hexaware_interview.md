Hexaware Interview Questions:

1. Can you introduce yourself shortly?
2. Are you currently in a support project or an implementation project?
3. What kind of implementation are you doing?


**Answer:**
I’m mainly working on **end-to-end integration implementations** using SAP CPI that include:

* **Cloud-to-Cloud Integrations:**
  Connecting SAP cloud applications like SuccessFactors, Ariba, or C4C with external cloud systems such as Salesforce or ServiceNow.

* **On-Premise to Cloud Integrations:**
  Integrating SAP ECC/S/4HANA systems with cloud applications through **SAP Cloud Connector** and **RFC/IDoc/OData adapters**.

* **B2B/EDI Integrations:**
  Implementing **EDIFACT / ANSI X12** based B2B flows using **AS2 and SFTP** adapters, including partner onboarding and message validation.

* **API Management & Governance:**
  Creating and managing **API proxies**, applying policies like OAuth, API keys, and rate limiting, and monitoring traffic in **SAP API Management**.




4. What is your source system?
5. What kind of adapters or APIs do you use to connect with the source systems?
6. Which adapter are you going to use on the receiver system side?
7. Can you explain one of the integration flows from your implementations?
8. For the different regions, are you using a single point of receiver or multiple receiver points?
9. Does each receiver system use the same adapter or different adapters?
10. What kind of authentications are you using?
11. Is there any mapping involved in your flow?
12. What type of mapping will you do if, for example, you need to map country codes (like "ind" to "in" for India)?

**Answer:**
For scenarios like converting `"ind"` to `"in"`, I will use a **Value Mapping** in SAP CPI.
Value Mapping helps maintain a table of source → target values and is ideal for country codes, currency codes, region codes, etc. It also supports multiple agencies and can be reused across integration flows.


13. What type of files do you use in message mapping?

**Answer:**
In Message Mapping, we typically use **XSD files** (XML Schema Definitions) for both **source** and **target** structures.
Sometimes we also use **WSDL files** if the service definition is provided in WSDL format.

**So mainly:**

* **XSD files** → Most common
* **WSDL files** → When the integration scenario provides a service definition

14. Have you previously used any Groovy script for any scenario?

**Answer:**
Yes, I’ve used Groovy scripting in multiple scenarios. Some examples include:

* **Dynamic Header/Property Setting:**
  Setting message headers or properties at runtime based on input data.

* **Payload Manipulation:**
  Parsing and modifying XML/JSON payloads—like removing unwanted nodes, adding fields, or formatting dates.

* **Custom Error Handling:**
  Creating detailed error messages, logging, and throwing custom exceptions.

* **String/Number Transformations:**
  Converting values (like dates, country codes, or ID formats) when graphical mapping isn’t sufficient.

* **Routing Logic:**
  Implementing conditions based on payload values to choose the correct receiver.

15. Do you have any experience with API Management?
16. Do you currently have an SAP CPI account with you?
17. What is the ProcessDirect adapter used for?
18. Why do we want to connect two different flows using ProcessDirect?
19. Does using ProcessDirect improve your integration performance?
20. For ProcessDirect, do you only connect two iFlows, or can you use more?
21. If your integration fails, what kind of actions are you going to take immediately?

**Answer:**
If an integration fails, I take the following steps immediately:

1. **Check the CPI Monitor:**
   Look at the *Message Processing Logs* to identify where exactly the flow failed—sender, mapping, adapter call, or receiver.

2. **Analyze the Error Stack/Trace:**
   Open the error details to understand whether it’s a payload issue, mapping error, script exception, or connectivity failure.

3. **Validate the Payload:**
   Download and check the payload for missing fields, wrong formats, or invalid values.

4. **Check Connectivity:**
   If it’s a receiver failure, test the endpoint (SFTP, API, SOAP, OData) and verify credentials, certificates, or firewall/network issues.

5. **Retry Manually (if possible):**
   Reprocess the same payload after fixing the issue.

6. **Inform Stakeholders:**
   Update the respective team (functional, third-party, BASIS, or backend team) with clear findings.

7. **Fix the Root Cause:**
   Based on the issue—correct the mapping, update configuration, fix script, adjust adapter settings, or update value mapping.

8. **Add Alerts if Needed:**
   If it’s a recurring issue, set up *alerting*, *exception handling*, or *improved logging* to prevent future failures.

--


22. Where do you check the logs to see where it failed?
23. Have you faced any flow failures in the past and provided solutions to production systems? Can you explain a scenario?
24. If an integration is scheduled for 10:00 but the receiving system is down, what action or process happens in CPI?

**Answer:**
If the receiving system is down at the scheduled time (10:00 AM), CPI **will still trigger the integration** as per the schedule.
But when CPI tries to send the message to the receiver, the call **fails** and the message goes into **Failed** state in the *Message Processing Monitor*.

CPI **does NOT automatically retry** the message unless a retry mechanism (JMS queue or custom retry logic) is built.

So the flow behaves like this:

1. **Scheduler triggers the iFlow at 10:00 AM.**
2. CPI processes the flow normally until the receiver call.
3. Since the target system is down → **Receiver adapter throws an error**.
4. Message goes into **Failed** status.
5. You must either:

   * Manually **retry** the message, or
   * Use a **JMS-based retry mechanism** if it’s implemented.
---

25. How do you get information/alerts from CPI if the system is down or the connection fails?
26. Have you configured mail notifications? What things do you need to configure them?
27. What kind of information is stored in the security materials?
28. What type of authentication are you using for the mail/SMTP adapter?
29. What are the roles of PGP keys?
30. Who provides the PGP keys?
31. How do you run a connectivity test for SFTP or SSH before using the iFlow?
32. What is the port number you are using for SMTP?
33. Have you previously used the exception subprocess?
34. What is the use of the Base64 encoder?
35. Can you explain something about the Request-Reply step?
