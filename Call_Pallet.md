## **Call Pallet**
---



### **External Call**

* **Content Enricher:** 
```
The content enricher adds the content of a payload with the original message in the course of an integration process.
This process converts the two separate messages into a single enhanced payload.
This feature enables you to make external calls during the course of an integration process to obtain additional data, if any.

Consider the first message in the integration flow as the original message
and the message obtained by making an external call during the integration process as the lookup message.
You can choose between two strategies to enrich these two payloads as a single message:

Combine

Enrich
https://help.sap.com/docs/cloud-integration/sap-cloud-integration/define-content-enricher

```	
	* **Poll Enrich:** 
	https://help.sap.com/docs/integration-suite/sap-integration-suite/define-poll-enrich
	
	* **Request Reply:** 
	https://help.sap.com/docs/integration-suite/sap-integration-suite/define-request-reply
	
	* **Send:** 
	https://help.sap.com/docs/cloud-integration/sap-cloud-integration/define-send-step

### **Local Call**

    * **Idempotent Process Call:** 
Idempotency helps ensure data consistency and integrity, where messages or operations might be retried,
causing duplicate messages, due to network glitches, system failures, or any other issues. 
It helps to maintain data consistency and integrity in complex integration scenarios.

https://community.sap.com/t5/technology-blog-posts-by-sap/step-by-step-for-implementing-idempotent-process-call-sap-cloud-integration/ba-p/13543161
https://community.sap.com/t5/technology-blog-posts-by-members/sap-cloud-integration-idempotent-process-call/ba-p/13580854

	
    * **Looping Process Call:**
A Looping Process Call in SAP Cloud Integration (CPI) is a palette component used
to repeatedly execute a local integration process based on defined conditions,
typically used for paginated API calls, processing bulk data in chunks, or retrying operations. 
It improves performance and reduces memory strain by executing a local process multiple times, 
with a conditional expression determining when the loop terminates. 

https://help.sap.com/docs/cloud-integration/sap-cloud-integration/define-looping-process-call
https://community.sap.com/t5/enterprise-resource-planning-blog-posts-by-members/sap-cloud-integration-looping-process-call/ba-p/13598555

	
    * **Process Call:** 
---
