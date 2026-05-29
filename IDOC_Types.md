Here is the updated, definitive list including **Warehouse Management (WM) / Extended Warehouse Management (EWM)** IDoc types, as well as critical missed areas like **Plant Maintenance (PM)**, **Human Resources (HR)**, and **Quality Management (QM)**.
## 1. Warehouse Management (WM) & Extended Warehouse Management (EWM)
These IDocs bridge SAP ERP/EWM with automated conveyor systems, forklift terminals, automated storage systems (ASRS), or external 3PL systems to handle internal stock execution.
| IDoc Type | Message Type | Direction | Business Process (Inbound / Outbound Context) |
|---|---|---|---|
| **WMTOID01** | WMTORD | **Outbound** | Sends **Transfer Orders (TO)** or **Warehouse Tasks (WT)** from SAP to an external warehouse system for execution. |
| **WMTOID01** | WMTORD | **Inbound** | Receives movement/put-away commands from sub-systems to create and execute tasks in SAP. |
| **WMTCID01** | WMTOCO | **Inbound** | Sent by external automation to **Confirm Warehouse Tasks/Transfer Orders** once the physical stock is moved. |
| **WMIVID01** | WMIVND | **Inbound** | Imports **Physical Inventory counts** taken via external scanner devices into SAP WM. |
| **WMSUID01** | WMSUVE | **Inbound / Outbound** | Communicates **Storage Unit (Pallet/HU) Movements** between SAP and automated forklifts. |
| **SHP_IBDLV_SAVE_REPLICA02** | SHP_IBDLV_SAVE_REPLICA | **Inbound** | Replicates an **Inbound Delivery** from an external ERP into a Decentralized EWM system. |
| **SHP_OBDLV_SAVE_REPLICA02** | SHP_OBDLV_SAVE_REPLICA | **Inbound** | Replicates an **Outbound Delivery Order** into a Decentralized EWM system to prepare for picking. |
## 2. Sales & Distribution (SD) & Logistics Execution (LE)
| IDoc Type | Message Type | Direction | Business Process (Inbound / Outbound Context) |
|---|---|---|---|
| **ORDERS05** | ORDERS | **Inbound** | Receives a Purchase Order from a customer to automatically create a **Sales Order** in SAP. |
| **ORDERS05** | ORDERS | **Outbound** | Sends a **Purchase Order** created in SAP to an external vendor. |
| **ORDRSP05** | ORDRSP | **Outbound** | Sends a **Sales Order Confirmation** to the customer. |
| **ORDRSP05** | ORDRSP | **Inbound** | Receives an **Order Acknowledgement / Confirmation** from an external supplier. |
| **DESADV01** | DESADV | **Outbound** | Sends an **Advanced Shipping Notice (ASN)** / Delivery Note to the customer when goods leave the warehouse. |
| **DESADV01** | DESADV | **Inbound** | Receives a **Shipping Notification** from a supplier regarding an incoming delivery. |
| **INVOIC02** | INVOIC | **Outbound** | Sends a **Customer Billing Invoice** or Credit Memo to an external system or client. |
| **INVOIC02** | INVOIC | **Inbound** | Receives an **Invoice / Vendor Bill** to be parked or posted in SAP Logistics Invoice Verification (LIV). |
| **SHPCON01** | SHPCON | **Inbound** | **Shipping Confirmation:** Updates SAP delivery documents with packing, picking, and loading data from a WMS. |
| **SHPMNT05** | SHPMNT | **Inbound / Outbound** | Exchanges **Shipment Document** details (transportation planning, legs, forwarding agents) with Freight Forwarders. |
## 3. Materials Management & Production Planning (MM / PP)
| IDoc Type | Message Type | Direction | Business Process (Inbound / Outbound Context) |
|---|---|---|---|
| **ORDERS05** | ORDCHG | **Outbound** | Sends a **Purchase Order Change Request** to a vendor after updating an existing PO. |
| **ORDERS05** | ORDCHG | **Inbound** | Receives a **Sales Order Change Request** from an external customer to modify an existing order. |
| **MBGMCR03** | MBGMCR | **Inbound** | Posts a **Goods Movement** (Goods Receipt, Goods Issue, or Stock Transfer) from an external system. |
| **MBGMCR03** | MBGMCR | **Outbound** | Sends **Goods Movement data** triggered in SAP to external legacy systems or dedicated logistics platforms. |
| **SRV_BA01** | SRV_BA | **Inbound** | Imports an external **Service Entry Sheet (SES)** or Bill of Quantities into SAP MM. |
| **WPUUMS01** | WPUUMS | **Inbound** | Transmits **POS (Point of Sale) Sales Data** in bulk from retail store systems into SAP to update inventory and revenue. |
| **LOIPRO01** | LOIPRO | **Outbound** | Sends **Production Order data** to external Manufacturing Execution Systems (MES). |
## 4. Financial Accounting & Controlling (FI/CO)
| IDoc Type | Message Type | Direction | Business Process (Inbound / Outbound Context) |
|---|---|---|---|
| **FIDCCP02** | FIDCC1 / FIDCC2 | **Inbound / Outbound** | Distributed **FI Financial Postings (G/L Documents)** from a central SAP instance to regional/subsidiary instances or external payroll engines. |
| **PAYEXT01** | PAYEXT | **Outbound** | Sends an **Extended Payment Advice / Payment Order** to banks to trigger electronic funds transfers (EFT). |
| **FINSTA01** | FINSTA | **Inbound** | Receives electronic **Bank Statement data** to reconcile open cash items and bank clearings automatically. |
| **CREMUL01** | CREMUL | **Inbound** | **Multiple Credit Advice:** Processes bank notification messages confirming incoming payments into company accounts. |
## 5. Plant Maintenance (PM) & Quality Management (QM) *(New Additions)*
| IDoc Type | Message Type | Direction | Business Process (Inbound / Outbound Context) |
|---|---|---|---|
| **ALEREQ01** | ALEREQ | **Inbound / Outbound** | Exchanges **Maintenance Requests / Work Orders** between localized plant software and corporate SAP. |
| **QIDOC01** | QIDOC | **Outbound** | Pushes **Inspection Lot** requirements from SAP QM to third-party lab/testing equipment software. |
| **QCONDR01** | QCONDR | **Inbound** | Receives **Quality Inspection Results / Usage Decisions** back from external laboratory systems. |
## 6. Human Resources (HR / HCM) *(New Additions)*
| IDoc Type | Message Type | Direction | Business Process (Inbound / Outbound Context) |
|---|---|---|---|
| **HRMD_A07** | HRMD_A | **Outbound** | Replicates **HR Master Data** (Employee Info, Organizational Assignments) to external workforce management, identity systems, or benefits platforms. |
## 7. SAP Master Data Distribution (ALE / ECH)
Master data distribution is configured as an outbound push from the master system and an inbound save into receiving systems.
| IDoc Type | Message Type | Direction | Business Process (Inbound / Outbound Context) |
|---|---|---|---|
| **MATMAS05** | MATMAS | **Inbound / Outbound** | Distributes and creates **Material Master Records** between central ERP, satellite manufacturing nodes, and PLM applications. |
| **DEBMAS06** | DEBMAS | **Inbound / Outbound** | Replicates **Customer Master Records** to external CRM tools, e-commerce networks, or target landscapes. |
| **CREMAS05** | CREMAS | **Inbound / Outbound** | Distributes **Vendor/Supplier Master Data** to strategic procurement engines (e.g., Ariba or local systems). |
| **GLMAST01** | GLMAST | **Inbound / Outbound** | Distributes **Chart of Accounts / General Ledger Master Records** to subsidiary ERP environments to maintain central compliance. |
| **INFREC01** | INFREC | **Inbound / Outbound** | Replicates **Purchasing Info Records** (pricing conditions, vendor-material links) across global SAP procurement nodes. |
## Summary of Processing Status Triggers
When monitoring these inputs and outputs (via transactions **WE02** or **WE05**), you can trace the flow using the standard status codes:
 * **Outbound Path Lifecycle:**
   * 01: IDoc generated in SAP database.
   * 30: IDoc ready for dispatch to external port/middleware.
   * 03: IDoc successfully passed to the external system port / SAP CPI / PI / EDI subsystem.
 * **Inbound Path Lifecycle:**
   * 50: IDoc received in SAP from external sender.
   * 64: IDoc ready to be passed to the business application logic.
   * 51: **Error** occurred while attempting to post the business document (e.g., validation missing).
   * 53: **Success** - The SAP application document (Sales Order, Material Master, FI Journal) has been created.
