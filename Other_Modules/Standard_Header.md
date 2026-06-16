# SAP Cloud Integration – Important Headers & Exchange Properties

> **Source:** [SAP Help Portal – Headers and Exchange Properties Provided by the Integration Framework](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/headers-and-exchange-properties-provided-by-integration-framework)

-----

## Key Concepts

|Concept              |Scope                                     |Forwarded to Receiver?                       |
|---------------------|------------------------------------------|---------------------------------------------|
|**Header**           |Message metadata, transferred with message|✅ Yes (HTTP adapters convert to HTTP headers)|
|**Exchange Property**|iFlow-internal data container             |❌ No (scope limited to the iFlow)            |


> ⚠️ **Note:** If message headers exceed ~4–16 KB, the receiver may reject the call. Use a Content Modifier to delete unnecessary headers before sending.

-----

## 1. SAP-Specific Headers (Monitoring & Routing)

|Name                        |Type  |Description                                                                                                          |
|----------------------------|------|---------------------------------------------------------------------------------------------------------------------|
|`SAP_ApplicationID`         |Header|Custom application ID for message monitoring. Searchable in the Message Monitoring editor. Set via XPath or constant.|
|`SAP_Sender`                |Header|Identifies the sender system. Shown in the message processing log (MPL).                                             |
|`SAP_Receiver`              |Header|Identifies the receiver system. All values collected in MPL as comma-separated list if changed during processing.    |
|`SAP_MessageType`           |Header|Used to categorize messages in monitoring.                                                                           |
|`SAP_MessageProcessingLogID`|Header|Read-only. Contains the ID of the current message processing log (MPL).                                              |

-----

## 2. SAP-Specific Exchange Properties

|Name                   |Type    |Description                                                                                                                                                      |
|-----------------------|--------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
|`SAP_ReceiverOverwrite`|Property|Set to `true` to show only the **last** added `SAP_Receiver` value in the MPL (instead of accumulating all values). Useful to avoid noise in multicast scenarios.|

-----

## 3. HTTP / HTTPS Adapter Headers

|Name                     |Type  |Description                                                                                                                                                                                                        |
|-------------------------|------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|`CamelHttpUri`           |Header|Overrides the URI configured in the receiver endpoint. Use to **dynamically set the full URI** at runtime.                                                                                                         |
|`CamelHttpUrl`           |Header|The complete URL called (without query parameters). E.g., `https://host/http/hello`                                                                                                                                |
|`CamelHttpQuery`         |Header|The query string from the request URL. Use in receiver adapter to **dynamically append query parameters**. E.g., `abcd=1234`                                                                                       |
|`CamelHttpMethod`        |Header|HTTP method of the incoming request: `GET`, `POST`, `PUT`, `DELETE`, etc.                                                                                                                                          |
|`CamelHttpPath`          |Header|Dynamic URL path portion beyond the base endpoint. E.g., if endpoint is `/myEndpoint/*` and caller uses `/myEndpoint/abc/def`, value is `abc/def`. ⚠️ Must be deleted before outbound HTTP call to avoid URI errors.|
|`CamelServletContextPath`|Header|The static path as defined in the sender adapter address field. E.g., `/abcd/1234`                                                                                                                                 |
|`CamelHttpResponseCode`  |Header|Set manually to customize the HTTP response code returned to the sender.                                                                                                                                           |
|`Content-Type`           |Header|HTTP content type of the request body. E.g., `application/json`, `text/plain`, `text/html`. Can include charset: `text/html; charset=utf-8`                                                                        |

-----

## 4. Content Encoding

|Name              |Type    |Description                                                                                                                                                         |
|------------------|--------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|`CamelCharsetName`|Property|Specifies the character encoding for message processing (e.g., `iso-8859-1`, `UTF-8`). Used in Encoder steps and Content Modifier. Overrides default UTF-8 behavior.|

-----

## 5. Message Splitting Headers

|Name                |Type  |Description                                                                                                  |
|--------------------|------|-------------------------------------------------------------------------------------------------------------|
|`CamelSplitIndex`   |Header|Counter for split messages, starting from `0`. Increments for each split Exchange.                           |
|`CamelSplitSize`    |Header|Total number of split items. In stream-based splitting, only available for the **last** (completed) Exchange.|
|`CamelSplitComplete`|Header|Boolean. Indicates whether the current Exchange is the **last** item in a split.                             |

-----

## 6. Aggregator Step Headers

|Name                        |Type  |Description                                                                                                                       |
|----------------------------|------|----------------------------------------------------------------------------------------------------------------------------------|
|`CamelAggregatedCompletedBy`|Header|Indicates **why** aggregation completed. Values: `timeout` (completion timeout reached) or `predicate` (completion condition met).|

-----

## 7. SFTP Adapter Headers

|Name               |Type  |Description                                                                                                                                                       |
|-------------------|------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|`CamelFileName`    |Header|Overrides the file name and directory on the SFTP receiver. Use to **dynamically set the output file name**. If not set, the Exchange ID is used as the file name.|
|`CamelFileNameOnly`|Header|(Sender) Contains only the file name, without the directory path.                                                                                                 |
|`CamelFileParent`  |Header|(Sender) Technical header containing the parent directory of the file.                                                                                            |

-----

## 8. Mail Adapter Headers

|Name          |Type  |Description                                               |
|--------------|------|----------------------------------------------------------|
|`Subject`     |Header|Subject line of the outgoing e-mail.                      |
|`To`          |Header|Recipient e-mail address.                                 |
|`Cc`          |Header|Additional recipient e-mail address.                      |
|`From`        |Header|Sender e-mail address.                                    |
|`Date`        |Header|Date and time the e-mail was sent.                        |
|`Content-Type`|Header|Format of the e-mail body: `text/html` or `text/plain`.   |
|`Message-ID`  |Header|Unique ID assigned to the e-mail by the mail system.      |
|`Reply-To`    |Header|Message ID of the original message this e-mail replies to.|
|`Archived-At` |Header|Link to the archived form of the e-mail.                  |

-----

## 9. JMS Adapter Headers

|Name          |Type  |Description                                   |
|--------------|------|----------------------------------------------|
|`JMSTimestamp`|Header|Timestamp of when the JMS message was created.|

-----

## 10. SOAP / IDoc Adapter Headers

|Name                      |Type  |Description                                                                                        |
|--------------------------|------|---------------------------------------------------------------------------------------------------|
|`SOAPAction`              |Header|Standard Web Services specification header. Used to indicate the intended action of a SOAP request.|
|`SapAuthenticatedUserName`|Header|Username of the client calling the iFlow. **Not set** if client certificate authentication is used.|
|`SapIDocType`             |Header|IDoc type parsed from the XML response by the IDoc adapter. E.g., `WPDTAX01`.                      |

-----

## Quick Reference: Header vs. Property

```
Header  → Travels with the message → Visible to receiver systems
Property → Stays inside the iFlow  → Used for internal logic only
```

### Common Usage Patterns

- **Dynamic endpoint URL:** Set `CamelHttpUri` or `CamelHttpQuery` in a Content Modifier before the receiver adapter.
- **Dynamic file naming (SFTP):** Set `CamelFileName` using an expression like `file_${exchangeId}_${property.CamelSplitIndex}`.
- **Custom monitoring:** Set `SAP_ApplicationID`, `SAP_Sender`, `SAP_Receiver` in a Content Modifier at the start of the iFlow.
- **Character encoding:** Set `CamelCharsetName` property + matching `Content-Type` header together for consistent encoding.
- **Split processing:** Use `CamelSplitIndex` and `CamelSplitComplete` to track progress across split messages.

-----

*Reference: SAP Cloud Integration – Integration Framework Documentation | Last reviewed: June 2026*
