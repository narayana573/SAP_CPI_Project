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

https://community.sap.com/t5/technology-blog-posts-by-members/join-amp-gather-in-sap-cloud-platform-integration/ba-p/13983494
```

### **Aggregator**

### **Multicast**
* **Parallel Multicast**
* **Sequential Multicast**
### **Router**
### **Splitter**
* **EDI Splitter**
* **General Splitter**
* **IDoc Splitter**
* **Iterating Splitter**
* **PKCS#7/CMS Splitter**
* **Tar Splitter**
* **Zip Splitter**

---

