# SAP CPI – SFTP Sender & Receiver Configuration Notes

---

## SFTP Sender

### Read Lock Strategy

Controls how the adapter locks files during polling to avoid processing the same file concurrently.

| Strategy | Description |
|---|---|
| **None** | No locking. Suitable for single-consumer scenarios only. Risky in multi-node environments. |
| **Rename** | Renames the file before processing. Prevents other pollers from picking it up. |
| **Content Change** | Waits until file content stops changing (size/timestamp stable). Set **Read Lock Check Interval (ms)** to control how often stability is checked. Useful for files written incrementally. |
| **Done File Expected** | Waits for a companion "done" marker file before processing. Configure **Done File Name** (e.g., `${file:name}.done`). |

#### Empty File Handling (under Rename / Done File Expected)

| Option | Behavior |
|---|---|
| **Process Empty Files** | Passes empty files through the pipeline normally. |
| **Skip Empty Files** | Silently ignores zero-byte files. |
| **MPL With Post-Processing Only** | Creates a Message Processing Log (MPL) entry **without** running the integration flow; only post-processing steps (e.g., Delete/Move) are executed. Useful for audit trails on empty files without triggering business logic. |

---

### Scheduler / Poll Settings

| Property | Description |
|---|---|
| **Poll on One Worker Only** ✅ | Restricts polling to a single worker node. Prevents duplicate file picks in clustered deployments. |
| **Stop on Exception** ✅ | Halts polling if an error occurs during processing. Prevents cascading failures. |
| **Sorting** | Sort polled files by **File Size**. |
| **Sorting Order** | **Ascending** (smallest first) / **Descending** (largest first). |
| **Max. Messages per Poll** | Maximum number of files processed per poll cycle. Default: `20`. |
| **Lock Timeout (min)** | How long a file lock is held before auto-release. Default: `15 min`. Prevents permanent locks if a node crashes. |

---

### Directory & Performance

| Property | Description |
|---|---|
| **Change Directories Stepwise** ☐ | When disabled, navigates to the target directory in a single command. Enable only if the SFTP server requires step-by-step `cd` traversal. |
| **Include Subdirectories** ☐ | Disabled — polls the configured directory only, not subdirectories. |
| **Use Fast Exists Check** ✅ | Uses a lightweight `ls` command instead of `stat` to verify file existence. Improves performance on high-latency SFTP servers. |

---

### Post-Processing

| Option | Behavior |
|---|---|
| **Delete File** | Deletes the source file from the SFTP server after successful processing. Most common option. |
| Move File | Moves the file to an archive directory (appears when Move is selected). |
| Keep File | Leaves the file in place. Can cause re-processing; use with care. |

---

---

## SFTP Receiver

### Directory Handling

| Property | Description |
|---|---|
| **Change Directories Stepwise** | Navigates to the target path one directory level at a time. Required by some SFTP servers that reject absolute path jumps. |
| **Create Directories** | Automatically creates the target directory path on the SFTP server if it does not exist. |
| **Flatten File Names** | Strips directory separators from the file name, writing all files into a single flat directory regardless of source structure. |
| **Prevent Directory Traversal** | Blocks path traversal attacks (e.g., `../../etc/passwd`). Should always be enabled in production for security. |
| **Use Fast Exists Check** | Uses `ls` instead of `stat` to check if the target file already exists. Speeds up transfers on high-latency servers. Ensure the SFTP server supports this reliably before enabling. |

---

### Handling for Existing Files

| Option | Behavior |
|---|---|
| **Override** | Overwrites the existing file on the server. |
| **Append** | Appends the new content to the existing file. |
| **Fail** | Throws an error if the file already exists. Useful for strict deduplication. |
| **Ignore** | Silently skips the write if the file already exists. |

---

### Temporary File

Writes the file to a `.tmp` name first, then renames it to the final name upon completion. Prevents downstream consumers from picking up a partially written file.

| Property | Example Value |
|---|---|
| **Temporary File Name** | `${file:name}.tmp` |

> **How it works:** File is uploaded as `invoice.xml.tmp` → on successful write, renamed to `invoice.xml`. Consumers watching for `.xml` files will only see complete files.

---

*Context: SAP Cloud Integration (CPI) — SFTP Adapter configuration reference.*
