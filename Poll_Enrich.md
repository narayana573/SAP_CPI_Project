# SAP Cloud Integration: Poll Enrich Pattern Overview

The **Poll Enrich** pattern in SAP Cloud Integration (CPI) allows an integration flow to read data from an external source—most commonly an **SFTP** server—in the middle of a process. This is used to enrich, replace, or combine the current message payload with external file content.

---

### 1. Key Components & Configuration Steps

To implement a Poll Enrich step, the following components are typically used:

* **Sender/Trigger**: An adapter (e.g., HTTPS, Timer, or SOAP) initiates the integration flow.
* **Content Modifier (Optional)**: Used to define headers or exchange properties that can be used for dynamic file paths or names.
* **Poll Enrich Step**: An "External Call" element where you configure how the new data merges with the existing data.
* **Aggregation Algorithm**: 
    * **Replace**: The original payload is completely overwritten by the content of the polled file.
    * **Concatenate**: The file content is appended to the end of the existing payload (useful for flat files).
    * **Combine XML**: Merges two XML structures under a common root tag.
* **SFTP Adapter**: Configured *inside* the Poll Enrich step to define the connection details, directory, and file name.

---

### 2. Example Scenario: Replace Payload with SFTP File

**Goal**: An HTTP call triggers the flow, but the final output must be the content of a specific configuration file stored on an SFTP server.

#### Configuration Details:
1.  **Trigger**: Configure an HTTPS Sender adapter to receive the initial request.
2.  **Poll Enrich Step**:
    * **Aggregation Algorithm**: Set to `Replace`.
3.  **SFTP Adapter (within Poll Enrich)**:
    * **Directory**: `/incoming/config/`
    * **File Name**: `settings.xml`
    * **Post-Processing**: Set to `Delete` or `Archive` if the file should only be processed once.

**Result**: The initial data sent via the HTTP request is discarded, and the message body now contains the contents of `settings.xml`.

---

### 3. Key Considerations

* **Dynamic Polling**: You can use Camel Simple Expression language (e.g., `${property.FileName}`) in the SFTP adapter configuration to poll different files based on the incoming request.
* **Multiple Files**: By default, Poll Enrich fetches **one file** per execution. To process multiple files from a directory, the Poll Enrich step must be placed inside a **Looping Process** or a **Local Integration Process**.
* **Post-processing**: Remember to configure the SFTP adapter's post-processing settings to prevent the same file from being polled repeatedly in a loop, unless the "Keep File" option is explicitly required for static lookups.
* **Error Handling**: If the file does not exist at the specified path, the Poll Enrich step will typically return an empty body or cause an error depending on the adapter configuration; it is best practice to use an **Exception Subprocess** to handle missing files.

---

<img width="685" height="622" alt="image" src="https://github.com/user-attachments/assets/00f6507b-1393-46ae-859a-fc0167d6ea25" />

<img width="808" height="626" alt="image" src="https://github.com/user-attachments/assets/67137813-c8aa-4dae-b69a-7be694ae9aac" />

