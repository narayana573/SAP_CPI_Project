## Routing Pallet

## **Join**
```
The Join step is used to bring together messages from different routes or branches within a single integration flow.
It acts as a synchronization point, ensuring that all messages from specified routes have arrived before proceeding further.

 Key Features:
**Synchronization:** Ensures that all messages from the specified routes are available before proceeding.
**No Message Modification:** The Join step itself does not modify the content of the messages. It simply brings them together.
**Routing Flexibility:** Messages can be routed to different branches based on conditions or other criteria.
```


### **Gather**
```
The Gather step merges multiple messages into a single message.
This is useful when you need to combine data from different sources or create a summary message.

Key Features:

Message Combination: Merges multiple messages into a single message.
Combination Strategies: Offers various strategies for combining messages,
                       such as appending, concatenating, or creating a collection.
Conditional Gathering: Can be configured to gather messages based on specific conditions.
In Gather steps, there are two tabs:

General: You can give any meaningful name.
Aggregator Strategy: In this tab, several options are present to perform operations.
Incoming Format:

You can select which format to apply; here, four formats are available:

Any: If you want to combine any incoming messages independent of their format.
Plaintext: If messages from different routes are in plain text format.
XML (Different Format) If messages from different routes are in different XML formats.
XML (Same Format) If messages from different routes are in the same XML format.

Gather Configuration: Select the Gather component from the Message Routing tab
and set the Incoming Format to XML (Different Format) or XML (Same Format).
Aggregation Algorithm: Use the "Combine" option to merge the message bodies into a single, structured XML message.
Resulting Payload: The output is typically wrapped in multimap:Messages with subsequent multimap:Message1,
 multimap:Message2, etc., tags to differentiate the sources.
Removing Multimap Tags: If a clean XML is needed, use a Groovy script or
 Content Modifier to remove the multimap namespace and wrapping tags after the Gather step.

https://community.sap.com/t5/technology-blog-posts-by-members/join-amp-gather-in-sap-cloud-platform-integration/ba-p/13983494
```

### **Aggregator**

### **Multicast**
* **Parallel Multicast**
   >  You can send copies to all routes at once using Parallel Multicast
   > 
   > if one branch fail other branch will not have impact
* **Sequential Multicast**
  > Sequential Multicast, you can change the order in which the message should be sent to the Sequential Multicast branches
  > 
  > if one branch is failed from there it will break the process.
  <img width="1152" height="2191" alt="multicast" src="https://github.com/user-attachments/assets/4b46d2bc-44eb-4c50-bb21-9405456d4b2a" />




### **Router**
```
A router works based on the conditions provided. It determines what data needs to reach the target or receiver.
When we use a router palette, we need to provide conditions for the router. Based on these conditions,
the process occurs, and the data reaches the receiver.

"In a router, we have two parts:

General: In the General section, if desired, we can assign meaningful names.
Processing: In the Processing section, the actual processing occurs. This is where we define the conditions.
In the Processing section, there are several fields.

Expression Type: Here, we need to specify the type, whether it is XML or non-XML,
Condition: Here we define the condition for the process.
Default: we use this to send the default data, when the conditions are not satisfying the given condition
         then default option comes to play.
```
### **Splitter**
* **EDI Splitter**
* **General Splitter**
```
General Splitter:

The General Splitter splits a composite message comprising N messages into N
individual messages, each containing one message with the enveloping elements of the
composite message. We use the term enveloping elements to refer to the elements above
and including the split point. Note elements that follow the one which is indicated as split
point in the original message (but on the same level), aren’t counted as enveloping
elements. They will not be part of the resulting messages.
```
<img width="745" height="922" alt="image" src="https://github.com/user-attachments/assets/0cf4eddf-47bd-4018-9399-907368e0bd34" />


* **Iterating Splitter**
```
The Iterating Splitter splits a composite message into a series of messages without copying
the enveloping elements of the composite message.
```
<img width="877" height="837" alt="image" src="https://github.com/user-attachments/assets/a779ff1f-b567-4468-a760-6a9958afbebd" />


* **IDoc Splitter**
```
Splits a composite IDoc messages into a series of individual IDoc messages with the
enveloping elements of the composite IDoc message.
```
<img width="757" height="267" alt="image" src="https://github.com/user-attachments/assets/3a2b3ffd-ae52-4cff-af6d-ada693b7f4c3" />


* **PKCS#7/CMS Splitter**
* **Tar Splitter**
* **Zip Splitter**

---

