# SAP CPI (SAP Integration Suite) — Standard Headers Reference

> **Unmissable Reference:** All standard Apache Camel and SAP-native headers for SAP Cloud Platform Integration (CPI) palette functions, organized by behavioral domain.

-----

## Access Syntax Quick Reference

|Context                                    |Syntax                                               |
|-------------------------------------------|-----------------------------------------------------|
|**Content Modifier / Condition Expression**|`${header.HeaderName}`                               |
|**Groovy Script**                          |`exchange.getIn().getHeader("HeaderName")`           |
|**JavaScript**                             |`var val = exchange.getIn().getHeader("HeaderName");`|
|**XPath Expression**                       |`in:header('HeaderName')`                            |
|**XSLT**                                   |Pass via `<xsl:param>` from Camel Exchange           |


> ⚠️ **Properties vs. Headers:** Properties (`${property.Name}`) persist across the full message flow but are **not** propagated to external targets. Headers are transmitted with the message and **are** visible to downstream adapters and receivers.

-----

## Domain 1 — Core Integration Framework & MPL Headers

> These SAP-native headers are set by the runtime engine and relate to the **Message Processing Log (MPL)** and envelope metadata.

|Header Name                 |Type    |Set By             |Description                                                                                                                    |
|----------------------------|--------|-------------------|-------------------------------------------------------------------------------------------------------------------------------|
|`SAP_MessageProcessingLogID`|`String`|Runtime (auto)     |Unique ID for the current message processing log entry. Use to correlate logs in the Operations Monitor.                       |
|`SAP_ApplicationID`         |`String`|Developer          |Custom business key (e.g., PO number, invoice ID) written to the MPL for searchability in monitoring. Set via Content Modifier.|
|`SAP_Sender`                |`String`|Runtime / Developer|Logical sender system name. Populated from iFlow configuration; can be overridden.                                             |
|`SAP_Receiver`              |`String`|Runtime / Developer|Logical receiver system name. Populated from iFlow configuration; can be overridden.                                           |
|`SAP_MessageType`           |`String`|Developer          |Descriptive label for the message type (e.g., `SalesOrder`, `GoodsReceipt`). Written to the MPL.                               |
|`SAP_CorrelationId`         |`String`|Runtime (auto)     |Correlation ID linking related messages across multiple iFlow runs (e.g., in async patterns).                                  |
|`SAP_PregeneratedMsgId`     |`String`|Developer          |Pre-assigned unique message ID; useful for idempotent scenarios to prevent duplicate processing.                               |

-----

## Domain 2 — HTTP/HTTPS Protocols & REST Adapters

> Headers used with the **HTTPS Sender Adapter**, **HTTP Receiver Adapter**, and **OData adapters**.

|Header Name            |Type     |Set By                    |Description                                                                                                    |
|-----------------------|---------|--------------------------|---------------------------------------------------------------------------------------------------------------|
|`CamelHttpMethod`      |`String` |Sender adapter / Developer|HTTP verb of the inbound request or outbound call: `GET`, `POST`, `PUT`, `DELETE`, `PATCH`.                    |
|`CamelHttpQuery`       |`String` |Sender adapter            |Raw query string from the inbound URL (e.g., `id=123&type=order`). Does not include `?`.                       |
|`CamelHttpUri`         |`String` |Sender adapter            |Relative URI path of the inbound HTTP request (e.g., `/orders/create`).                                        |
|`CamelHttpUrl`         |`String` |Sender adapter            |Full absolute URL of the inbound request (e.g., `https://host/api/orders`).                                    |
|`CamelHttpResponseCode`|`Integer`|HTTP Receiver adapter     |HTTP status code returned by the external endpoint (e.g., `200`, `400`, `500`). Available post-call.           |
|`CamelHttpResponseText`|`String` |HTTP Receiver adapter     |HTTP status reason phrase returned (e.g., `OK`, `Bad Request`). Available post-call.                           |
|`Content-Type`         |`String` |Developer / Adapter       |MIME type of the message body (e.g., `application/json`, `application/xml`, `text/plain`).                     |
|`Accept`               |`String` |Developer                 |Expected response content type sent to HTTP endpoints.                                                         |
|`X-CSRF-Token`         |`String` |Developer / OData adapter |Anti-CSRF token. Send `Fetch` on GET to retrieve; echo the returned value on modifying calls (POST/PUT/DELETE).|
|`Authorization`        |`String` |Developer                 |HTTP Authorization header value (e.g., `Bearer <token>`, `Basic <base64>`). Handle with care — avoid logging.  |
|`CamelHttpPath`        |`String` |Sender adapter            |Path suffix appended dynamically to the base URL during outbound calls.                                        |

-----

## Domain 3 — Messaging Protocols & Specialized Adapters

### 3a — IDoc Adapter Headers

|Header Name           |Type    |Set By      |Description                                                      |
|----------------------|--------|------------|-----------------------------------------------------------------|
|`SapIdocType`         |`String`|IDoc adapter|Basic IDoc type (e.g., `ORDERS05`, `DEBMAS07`).                  |
|`SapIdocTypeExtension`|`String`|IDoc adapter|Enhancement/extension type for the IDoc, if applicable.          |
|`SapIdocNumber`       |`String`|IDoc adapter|Unique IDoc document number assigned by SAP.                     |
|`SapIdocMesType`      |`String`|IDoc adapter|Message type associated with the IDoc (e.g., `ORDERS`, `DEBMAS`).|
|`SapIdocPartnerNumber`|`String`|IDoc adapter|Partner (sender or receiver) number from the IDoc control record.|
|`SapIdocPartnerType`  |`String`|IDoc adapter|Partner type (e.g., `LS` for logical system, `KU` for customer). |

### 3b — SOAP Adapter Headers

|Header Name      |Type    |Set By                  |Description                                                              |
|-----------------|--------|------------------------|-------------------------------------------------------------------------|
|`CamelSoapAction`|`String`|SOAP adapter / Developer|SOAP Action URI. Required for SOAP 1.1; maps to `SOAPAction` HTTP header.|
|`SOAPAction`     |`String`|SOAP Sender adapter     |Raw value of the `SOAPAction` HTTP header from inbound SOAP request.     |

### 3c — XI / Proxy Adapter Headers

|Header Name          |Type    |Set By    |Description                                                                               |
|---------------------|--------|----------|------------------------------------------------------------------------------------------|
|`SapMessageIdEx`     |`String`|XI adapter|Unique message ID used in SAP XI/PI protocol envelope. Critical for exactly-once delivery.|
|`SapQualityOfService`|`String`|XI adapter|Quality of service indicator: `ExactlyOnce` or `BestEffort`.                              |
|`SapPlainText`       |`String`|XI adapter|Indicates whether the XI message is plain text (`true`/`false`).                          |

### 3d — AS2 Adapter Headers

|Header Name   |Type    |Set By            |Description                                    |
|--------------|--------|------------------|-----------------------------------------------|
|`AS2From`     |`String`|AS2 Sender adapter|AS2 sender ID from the inbound AS2 message.    |
|`AS2To`       |`String`|AS2 Sender adapter|AS2 receiver ID from the inbound AS2 message.  |
|`AS2MessageID`|`String`|AS2 adapter       |Unique message ID from the AS2 protocol header.|
|`AS2Subject`  |`String`|AS2 Sender adapter|Subject line of the inbound AS2 message.       |

-----

## Domain 4 — File & Transmission Protocols (SFTP / FTP / Poll)

> Headers set by **SFTP Sender**, **FTP Sender**, and **Poll-based adapters**.

|Header Name            |Type    |Set By                  |Description                                                                                                                                |
|-----------------------|--------|------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
|`CamelFileName`        |`String`|File/SFTP adapter       |Name of the file being processed (e.g., `orders_20240601.xml`). Can also be set to control the **output filename** in a File/SFTP Receiver.|
|`CamelFileNameConsumed`|`String`|File/SFTP Sender adapter|Full path and name of the source file that was consumed/read.                                                                              |
|`CamelFileAbsolutePath`|`String`|File/SFTP Sender adapter|Absolute path of the consumed file on the remote server.                                                                                   |
|`CamelFilePath`        |`String`|File/SFTP Sender adapter|Relative path of the consumed file (relative to the configured root directory).                                                            |
|`CamelFileParent`      |`String`|File/SFTP Sender adapter|Parent directory of the consumed file.                                                                                                     |
|`CamelFileSize`        |`Long`  |File/SFTP Sender adapter|Size of the consumed file in bytes.                                                                                                        |
|`CamelFileLastModified`|`Long`  |File/SFTP Sender adapter|Last-modified timestamp of the file (milliseconds since epoch).                                                                            |
|`CamelFileHost`        |`String`|SFTP/FTP Sender adapter |Hostname of the remote SFTP/FTP server from which the file was retrieved.                                                                  |


> 💡 **Tip:** Set `CamelFileName` in a Content Modifier before a File/SFTP Receiver to dynamically control the output file name using expressions like `orders_${date:now:yyyyMMdd_HHmmss}.xml`.

-----

## Domain 5 — Advanced Structural Flow Patterns

### 5a — Splitter Headers

|Header Name         |Type      |Set By       |Description                                                                          |
|--------------------|----------|-------------|-------------------------------------------------------------------------------------|
|`CamelSplitIndex`   |`Integer` |Splitter step|Zero-based index of the current split item (0 = first item).                         |
|`CamelSplitSize`    |`Integer` |Splitter step|Total number of items produced by the split (available when `Streaming` is disabled).|
|`CamelSplitComplete`|`Boolean` |Splitter step|`true` when processing the **last** split item. Use to trigger post-split logic.     |
|`CamelSplitParent`  |`Exchange`|Splitter step|Reference to the parent exchange before splitting. Useful in aggregation callbacks.  |

### 5b — Aggregator Headers

|Header Name                 |Type     |Set By                |Description                                                                                            |
|----------------------------|---------|----------------------|-------------------------------------------------------------------------------------------------------|
|`CamelAggregatedSize`       |`Integer`|Aggregator step       |Number of exchanges that have been aggregated into the current grouped message.                        |
|`CamelAggregatedTimeout`    |`Boolean`|Aggregator step       |`true` if the aggregation completed due to a timeout (rather than a completion condition being met).   |
|`CamelAggregatedCompletedBy`|`String` |Aggregator step       |Indicates what triggered completion: `size`, `timeout`, `predicate`, or `consumer`.                    |
|`CamelCorrelationId`        |`String` |Developer / Aggregator|Correlation key used to group messages within the Aggregator. Typically set before the Aggregator step.|

### 5c — Loop Headers

|Header Name     |Type     |Set By   |Description                                            |
|----------------|---------|---------|-------------------------------------------------------|
|`CamelLoopIndex`|`Integer`|Loop step|Zero-based iteration counter of the current loop cycle.|
|`CamelLoopSize` |`Integer`|Loop step|Total number of configured loop iterations.            |

-----

## Domain 6 — Exception Management Headers

> Headers populated in **Exception Sub-Processes** and error handlers.

|Header Name               |Type       |Set By               |Description                                                                                                  |
|--------------------------|-----------|---------------------|-------------------------------------------------------------------------------------------------------------|
|`CamelExceptionCaught`    |`Exception`|Exception Sub-Process|The Java `Exception` object caught. Access `.getMessage()`, `.getClass().getName()`, `.getCause()` in Groovy.|
|`CamelExceptionMessage`   |`String`   |Exception Sub-Process|String message from the caught exception. Equivalent to `CamelExceptionCaught.getMessage()`.                 |
|`CamelFailureEndpoint`    |`String`   |Dead Letter Channel  |URI of the endpoint that caused the failure (available in DLC error handlers).                               |
|`CamelFailureRouteId`     |`String`   |Exception Sub-Process|ID of the Camel route in which the failure occurred.                                                         |
|`SAP_ErrorModelStepID`    |`String`   |Runtime              |The iFlow step ID where the error was thrown. Useful for targeted error handling logic.                      |
|`SAP_MPL_ErrorInformation`|`String`   |Runtime              |Serialized error information recorded in the MPL at the point of failure.                                    |


> 💡 **Groovy Exception Access Example:**
> 
> ```groovy
> def ex = message.getHeaders().get("CamelExceptionCaught")
> def errMsg = ex?.getMessage() ?: "No exception details"
> message.setProperty("ErrorDetail", errMsg)
> ```

-----

## Framework Engineering Notes

### Header Lifecycle & Scope

|Behavior                                  |Detail                                                                                                    |
|------------------------------------------|----------------------------------------------------------------------------------------------------------|
|**Headers are per-exchange**              |Headers exist on the Camel `Exchange` and travel with the message through all steps.                      |
|**Headers propagate to receivers**        |Unlike properties, headers ARE forwarded to external targets (HTTP, SFTP, etc.) unless explicitly removed.|
|**Properties do NOT propagate externally**|Use `${property.X}` for internal flow state that should not leak to downstream systems.                   |
|**Header removal**                        |Use a Content Modifier → *Remove Header* tab, or Groovy: `message.getHeaders().remove("HeaderName")`.     |
|**Case sensitivity**                      |Header names ARE case-sensitive in Camel expressions. `CamelFileName` ≠ `camelfilename`.                  |

### Setting vs. Reading Headers

```groovy
// Groovy — Read a header
def method = message.getHeaders().get("CamelHttpMethod")

// Groovy — Set a header
message.setHeader("SAP_ApplicationID", "PO-2024-00123")

// Groovy — Remove a header (prevent leaking to receiver)
message.getHeaders().remove("Authorization")
```

```xpath
<!-- XPath — Read a header in a Router condition -->
${header.CamelHttpMethod} = 'POST'
```

### Common Patterns

|Use Case                         |Recommended Header(s)                                    |
|---------------------------------|---------------------------------------------------------|
|Monitor message in Operations    |`SAP_ApplicationID`, `SAP_MessageType`                   |
|Dynamic outbound filename        |`CamelFileName` (set before File Receiver)               |
|Detect last split item           |`CamelSplitComplete = true`                              |
|Handle HTTP errors gracefully    |`CamelHttpResponseCode` (check in Router after HTTP call)|
|Prevent duplicate IDoc processing|`SapMessageIdEx` (XI exactly-once QoS)                   |
|CSRF-protected OData write       |`X-CSRF-Token` (fetch then echo pattern)                 |
|Error notification with detail   |`CamelExceptionMessage`, `SAP_ErrorModelStepID`          |

-----

*Reference covers SAP Integration Suite (Cloud Integration) — Apache Camel 3.x runtime. Header availability may vary by adapter version.*
