Interview questions:

**1. Bristol Stone Interview (Ravi Kumar)**
1. Could you give a brief introduction about yourself?
2. Before working in CPI, what technology did you work on?
3. Have you worked on any other middleware like the older versions of PI/PO?
4. Can you explain a scenario where you used Event Mesh technology, and what adapters were used?
5. After the event mesh reads or captures a trigger from a broker, what process do you follow?
6. How is the connection created between the VPN and Event Mesh?
7. Can you explain a scenario where you used the ProcessDirect adapter?
8. What is your best design to implement exception handling and retry mechanisms for synchronous interfaces?
9. How would you optimize the use of JMS queues if you have 100+ interfaces but limited queue availability?
10. How do you handle exceptions and retries for a completely synchronous scenario?
11. If you are fetching data from an API via a schedule and sending it to another API, how do you manage a retry if the receiver API fails?
12. What kind of authentication methods have you used?
13. How do you set up SSH key authentication for SFTP, and how are the keys managed between systems?
14. What other components of the BTP Integration Suite have you worked with besides Event Mesh?
15. What is the type of project you are currently working on, and how many team members are there?
16. Do you have any experience working on migration projects (e.g., PI/PO to BTP)?
17. Are you currently serving a notice period or have you joined another company?

**2. Cap Gemini L1 Interview (Ravi Kumar)** *(Inferred from candidate responses)*
1. Could you explain an end-to-end complex integration scenario you recently developed?
2. How does the exception subprocess trigger and notify the concerned team if a specific branch of your integration fails?
3. How does encryption and decryption work, and who provides the public/private keys if CPI is the sender?

**3. Cap Gemini (27th March) Interview (Ravi Kumar)**
1. Can you show a government ID card beside your face for verification?
2. Can you please give a brief introduction?
3. What other adapters have you worked on?
4. What are the ALE/IDoc configuration steps required in the SAP system to register CPI to send documents?
5. How do you route messages if multiple IDocs come into the system?
6. What is the difference between a Message Header and an Exchange Property?
7. Which one do you prefer to use more, and why?
8. Is it possible to use a POST operation to send more than one payload at a time using the OData adapter?
9. How can you design a retry mechanism without using a second interface or JMS queues to avoid load on CPI?
10. How can you filter out duplicate messages entering CPI?
11. How should you design a flow to fetch a large amount of records (e.g., 10,000-20,000) from an OData service without overloading CPI?
12. What condition needs to be given for pagination?
13. Have you worked with Groovy scripts, and can you understand/debug existing code without extensive AI help?
14. What is a filter step and what are its value types?
15. How do you design an automated, consolidated error report for the past 24 hours to be sent as an email attachment?
16. Have you worked with Data Store operations?
17. Have you worked with Client Certificate authentication?
18. How do you connect to an SFTP server to place files securely?

**4. Cap Gemini Interview (Ravi Kumar)**
1. What is your total experience, and how much is relevant to SAP CPI?
2. In your 9 years of experience, how much was in PI/PO?
3. Can you explain one complex scenario you have worked on?
4. Do you have any idea about encryption, and which key is used to encrypt a file in CPI?
5. When using the Gather step, is it mandatory to use a Join step before it?
6. In an OData adapter, is it possible to update multiple entities in a single call?
7. If you receive an input CSV file with multiple records and one fails, how do you handle it so the remaining records process successfully?
8. How do you handle a CSV to XML conversion if one of the fields contains an extra comma in the description?
9. In which cases do we go with Event Mesh?
10. Why do we use API Management?
11. What is a rate limiting policy?
12. What is your notice period?
13. Are you flexible with a hybrid work policy (3 days from office) and both development/support roles?

**5. Cognizant L1 Interview (Ravi Kumar)**
1. Can you summarize your integration knowledge, years of experience, and the projects you have worked on?
2. Can you elaborate on your project role and summarize an end-to-end scenario?
3. What type of authentication have you used for the Mail adapter?
4. Whose credentials are used for the Mail adapter (a technical user or personal credentials)?
5. What is the alternative approach to JMS for a retry mechanism?
6. Can you elaborate on the configuration steps to use Data Store operations for retries?
7. Is it practically possible/good to use Data Store operations if there are 200 failing interfaces creating thousands of queues?
8. Is there any other approach to delay or retry without using queues?
9. Can you elaborate on your work with API Management and Event Mesh?
10. What other tools or adapters have you worked with (like Open Connectors)?
11. Which mapping do you think is better: standard graphical mapping or XSLT/Groovy?
12. What is the drawback between the pulser and circular approaches?
13. Which AI tools have you used (e.g., Copilot, ChatGPT), and are they available in your tenant?
14. Is there anything else you want to highlight before we close the call?

**6. ERGO Interview (Ravi Kumar)**
1. Can we have a brief introduction about yourself?
2. Can you explain a complex scenario where you faced difficulties while building an end-to-end flow?
3. What are the different types of local calls in SAP CPI?
4. What is an Idempotent Process Call?
5. Have you heard about Message Digest?
6. If you use a Sequential Multicast instead of Parallel, what would be the possible outcome in your flow?
7. In Splitter configuration, if there is no value in "Grouping", what would be the output?
8. If an interface needs to trigger based on an order creation in S/4HANA, what mechanism can you design?
9. How can we design this trigger through Event Mesh, and what configuration/adapter is needed?
10. How do you handle large payloads in CPI?
11. Can you explain the retry mechanism using JMS queues?
12. How can we tackle the issue of messages failing during scheduled SAP maintenance?
13. Have you worked on Groovy scripts, and can you share an example?
14. SAP CPI is based on which framework?
15. What is the total storage of SAP CPI?
16. Which pallet function are you most familiar with?
17. Can you explain how encryption and decryption work in SAP CPI?
18. Have you worked on signing and verification?
19. Can you give the configuration details of the SFTP adapter?
20. How do you change the file name dynamically at the receiver side?
21. If you don't want to use a Content Modifier to change the file name, how else can it be achieved?
22. How good are you with mapping, and have you used point-to-point or node functions?
23. Can you explain the node function "useOneAsMany", and how many inputs it requires?
24. Have you heard about the get headers function in mapping?
25. Can you give a brief idea about API Management and how it works?
26. What is Event Mesh, and have you faced any difficulties using it?
27. Out of your 5 years of experience, what was the most difficult issue you faced?
28. What effort estimation would you give to a client for a greenfield project with 40 interfaces?
29. Have you managed projects?
30. Have you worked on any migration projects (PI/PO to CPI), and how would you estimate effort/connectivity for it?
31. Is your current company mode hybrid, and what is the location? Are you ready to work 5 days from the office in Mumbai?

**7. Hexaware L1 Interview (Ravi Kumar)** *(Inferred from candidate responses)*
1. What are the best practices for building an iFlow when the CPI tenant has limited memory capacity?
2. How do you use SFTP post-processing to avoid picking up duplicate files?
3. What are the configuration steps required on the SAP side (like ALE/IDoc) to send documents to CPI?
4. Which mapping method do you prefer to use first, and why?
5. What is the difference between the Poll Enrich and Content Enricher steps?
6. What is the difference between the 'removeContext' and 'collapseContext' node functions?
7. How do you use the 'useOneAsMany' node function?
8. How do you connect CPI to an on-premise application?
9. Can you explain a recent end-to-end complex integration scenario you developed?
10. How did you design the error handling mechanism for this complex scenario?

**8. Infosys 2 Interview (Ravi Kumar)** *(Inferred from candidate responses)*
1. Can you explain a recent complex end-to-end integration scenario you developed?
2. How do you implement a retry mechanism if the receiver system is down?
3. Have you worked on SAP-to-SAP integration scenarios, and what adapters did you use?
4. Do you have experience with API Management, and how have you used it?
5. How would you rate your SAP CPI skills out of 10?
6. When do you use XSLT mapping instead of standard graphical mapping?
7. Can you explain the 'useOneAsMany' node function?
8. What other node functions do you frequently use in message mapping?
9. What types of authentication mechanisms have you configured for HTTP/OData connections?
10. How do you connect CPI to an on-premise application?
11. What steps are required to configure the SAP Cloud Connector?
12. Have you worked in support projects?
13. Which CPI adapters have you worked with?
14. How and why do you use the ProcessDirect adapter?
15. How do you handle complex technical scenarios or requirements that you haven't worked on before?

**9. WhatsApp Audio Interview (Surya)**
1. Can you introduce yourself shortly?
2. Are you currently in a support project or an implementation project?
3. What kind of implementation are you doing?
4. What is your source system?
5. What kind of adapters or APIs do you use to connect with the source systems?
6. Which adapter are you going to use on the receiver system side?
7. Can you explain one of the integration flows from your implementations?
8. For the different regions, are you using a single point of receiver or multiple receiver points?
9. Does each receiver system use the same adapter or different adapters?
10. What kind of authentications are you using?
11. Is there any mapping involved in your flow?
12. What type of mapping will you do if, for example, you need to map country codes?
13. What type of files do you use in message mapping?
14. Have you previously used any Groovy script for any scenario?
15. Do you have any experience with API Management?
16. Do you currently have an SAP CPI account with you?
17. What is the ProcessDirect adapter used for?
18. Why do we want to connect two different flows using ProcessDirect?
19. Does using ProcessDirect improve your integration performance?
20. For ProcessDirect, do you only connect two iFlows, or can you use more?
21. If your integration fails, what kind of actions are you going to take immediately?
22. Where do you check the logs to see where it failed?
23. Have you faced any flow failures in the past and provided solutions to production systems? Can you explain a scenario?
24. If an integration is scheduled for 10:00 but the receiving system is down, what action or process happens in CPI?
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
