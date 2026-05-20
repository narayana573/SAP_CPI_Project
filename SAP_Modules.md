# SAP Modules – Complete List & Details

> **SAP (Systems, Applications & Products in Data Processing)** is an enterprise resource planning (ERP) platform. Its modular architecture allows organizations to implement only the components they need.

-----

## 📦 CORE ERP MODULES (SAP ECC / SAP S/4HANA)

-----

### 1. FI – Financial Accounting

|Attribute    |Details                                                             |
|-------------|--------------------------------------------------------------------|
|**Full Name**|Financial Accounting                                                |
|**Category** |Finance                                                             |
|**Purpose**  |Manages all financial transactions, ledgers, and statutory reporting|

**Key Sub-modules:**

- **FI-GL** – General Ledger Accounting
- **FI-AP** – Accounts Payable
- **FI-AR** – Accounts Receivable
- **FI-AA** – Asset Accounting
- **FI-BL** – Bank Ledger / Bank Accounting
- **FI-LC** – Legal Consolidation
- **FI-TR** – Treasury

**Key Features:**

- Real-time financial data integration
- Multi-currency and multi-company support
- Automated reconciliation with sub-ledgers
- Statutory and regulatory reporting

-----

### 2. CO – Controlling

|Attribute    |Details                                                |
|-------------|-------------------------------------------------------|
|**Full Name**|Controlling                                            |
|**Category** |Finance / Management Accounting                        |
|**Purpose**  |Internal cost management, profit analysis, and planning|

**Key Sub-modules:**

- **CO-CCA** – Cost Center Accounting
- **CO-PCA** – Profit Center Accounting
- **CO-PA** – Profitability Analysis
- **CO-PC** – Product Cost Controlling
- **CO-OPA** – Internal Orders
- **CO-ABC** – Activity-Based Costing

**Key Features:**

- Budget planning and monitoring
- Variance analysis
- Cost allocation cycles
- Integration with FI for real-time postings

-----

### 3. MM – Materials Management

|Attribute    |Details                                               |
|-------------|------------------------------------------------------|
|**Full Name**|Materials Management                                  |
|**Category** |Supply Chain / Procurement                            |
|**Purpose**  |Manages procurement, inventory, and material valuation|

**Key Sub-modules:**

- **MM-PUR** – Purchasing
- **MM-IM** – Inventory Management
- **MM-IV** – Invoice Verification (Logistics Invoice Verification)
- **MM-WM** – Warehouse Management (classic)
- **MM-CBP** – Consumption-Based Planning

**Key Features:**

- Purchase requisitions and orders
- Goods receipt and issue processing
- Material requirements planning integration
- Vendor evaluation and management

-----

### 4. SD – Sales and Distribution

|Attribute    |Details                                 |
|-------------|----------------------------------------|
|**Full Name**|Sales and Distribution                  |
|**Category** |Sales / Customer Management             |
|**Purpose**  |Handles the entire order-to-cash process|

**Key Sub-modules:**

- **SD-BF** – Basic Functions (pricing, output, availability)
- **SD-SLS** – Sales
- **SD-SHP** – Shipping
- **SD-BIL** – Billing
- **SD-CAS** – Sales Support / CRM
- **SD-FT** – Foreign Trade

**Key Features:**

- Quotation and order management
- Delivery scheduling and shipment
- Pricing conditions and discounts
- Customer credit management
- Revenue recognition

-----

### 5. PP – Production Planning

|Attribute    |Details                                   |
|-------------|------------------------------------------|
|**Full Name**|Production Planning                       |
|**Category** |Manufacturing                             |
|**Purpose**  |Plans and controls manufacturing processes|

**Key Sub-modules:**

- **PP-MRP** – Material Requirements Planning
- **PP-SFC** – Shop Floor Control
- **PP-PI** – Production Planning for Process Industries
- **PP-REM** – Repetitive Manufacturing
- **PP-CBP** – Capacity Planning
- **PP-MP** – Master Production Scheduling

**Key Features:**

- Bill of Materials (BOM) management
- Work center and routing management
- Production orders and process orders
- Integration with MM for material availability

-----

### 6. QM – Quality Management

|Attribute    |Details                                          |
|-------------|-------------------------------------------------|
|**Full Name**|Quality Management                               |
|**Category** |Manufacturing / Quality                          |
|**Purpose**  |Manages quality planning, inspection, and control|

**Key Sub-modules:**

- **QM-QP** – Quality Planning
- **QM-QI** – Quality Inspection
- **QM-QC** – Quality Control
- **QM-QN** – Quality Notifications
- **QM-CA** – Quality Certificates
- **QM-UD** – Usage Decision

**Key Features:**

- Inspection lot creation and processing
- Defect recording and analysis
- Statistical process control (SPC)
- Calibration management

-----

### 7. PM – Plant Maintenance

|Attribute    |Details                                              |
|-------------|-----------------------------------------------------|
|**Full Name**|Plant Maintenance                                    |
|**Category** |Asset & Equipment Management                         |
|**Purpose**  |Manages maintenance of technical assets and equipment|

**Key Sub-modules:**

- **PM-EQM** – Equipment and Technical Objects
- **PM-PRM** – Preventive Maintenance
- **PM-WOC** – Work Order Management
- **PM-PRO** – Maintenance Projects
- **PM-IS** – Information System

**Key Features:**

- Functional location and equipment hierarchy
- Maintenance plans and task lists
- Work order creation and execution
- Breakdown and corrective maintenance

-----

### 8. HR / HCM – Human Capital Management

|Attribute    |Details                                     |
|-------------|--------------------------------------------|
|**Full Name**|Human Capital Management                    |
|**Category** |Human Resources                             |
|**Purpose**  |Manages all HR processes from hire to retire|

**Key Sub-modules:**

- **HR-PA** – Personnel Administration
- **HR-PT** – Time Management
- **HR-PY** – Payroll
- **HR-PD** – Personnel Development
- **HR-OM** – Organizational Management
- **HR-RCF** – Recruitment
- **HR-LSO** – Learning Solution

**Key Features:**

- Employee master data management
- Time and attendance tracking
- Payroll processing (country-specific)
- Training and development management
- Succession planning

-----

### 9. PS – Project System

|Attribute    |Details                               |
|-------------|--------------------------------------|
|**Full Name**|Project System                        |
|**Category** |Project Management                    |
|**Purpose**  |Plans, controls, and monitors projects|

**Key Sub-modules:**

- **PS-ST** – Structures (WBS, Networks)
- **PS-PLN** – Planning
- **PS-EXE** – Project Execution
- **PS-IS** – Information System

**Key Features:**

- Work Breakdown Structure (WBS)
- Network scheduling (Gantt charts)
- Project cost and revenue planning
- Progress tracking and milestone billing

-----

### 10. WM – Warehouse Management

|Attribute    |Details                                         |
|-------------|------------------------------------------------|
|**Full Name**|Warehouse Management                            |
|**Category** |Logistics / Supply Chain                        |
|**Purpose**  |Manages complex warehouse operations and storage|

**Key Features:**

- Storage type, section, and bin management
- Transfer orders and movements
- Picking, packing, and putaway strategies
- Integration with MM and SD
- Slotting optimization

> **Note:** In SAP S/4HANA, WM is replaced by **EWM (Extended Warehouse Management)**.

-----

### 11. TM – Transportation Management

|Attribute    |Details                                                 |
|-------------|--------------------------------------------------------|
|**Full Name**|SAP Transportation Management                           |
|**Category** |Logistics                                               |
|**Purpose**  |Manages end-to-end transportation planning and execution|

**Key Features:**

- Freight order management
- Carrier selection and tendering
- Route optimization
- Freight cost calculation and settlement
- Real-time visibility and tracking

-----

### 12. EWM – Extended Warehouse Management

|Attribute    |Details                                               |
|-------------|------------------------------------------------------|
|**Full Name**|Extended Warehouse Management                         |
|**Category** |Logistics                                             |
|**Purpose**  |Advanced warehouse operations with greater flexibility|

**Key Features:**

- Advanced putaway and picking strategies
- Labor management
- Slotting and rearrangement
- Integration with automation and robotics
- Wave management

-----

## 🌐 SAP ENTERPRISE PLATFORM MODULES

-----

### 13. SAP BW / BI – Business Warehouse / Business Intelligence

|Attribute    |Details                                  |
|-------------|-----------------------------------------|
|**Full Name**|SAP Business Warehouse                   |
|**Purpose**  |Data warehousing and analytical reporting|

**Key Features:**

- ETL (Extract, Transform, Load) processes
- InfoCubes and DataStore Objects
- BEx Analyzer and Query Designer
- Integration with SAP HANA
- Pre-built industry content

-----

### 14. SAP BPC – Business Planning and Consolidation

|Attribute    |Details                                                      |
|-------------|-------------------------------------------------------------|
|**Full Name**|SAP Business Planning and Consolidation                      |
|**Purpose**  |Financial planning, budgeting, forecasting, and consolidation|

**Key Features:**

- Real-time planning and simulation
- Financial consolidation and reporting
- Driver-based planning
- Excel-based user interface
- Integration with BW/4HANA

-----

### 15. SAP CRM – Customer Relationship Management

|Attribute    |Details                                                           |
|-------------|------------------------------------------------------------------|
|**Full Name**|SAP Customer Relationship Management                              |
|**Purpose**  |Manages customer interactions across sales, service, and marketing|

**Key Features:**

- Lead and opportunity management
- Service order and complaint management
- Marketing campaign management
- Interaction center (call center)
- Integration with SAP ERP backend

> **Note:** Largely replaced by **SAP C/4HANA** and **SAP Sales Cloud / Service Cloud**.

-----

### 16. SAP SRM – Supplier Relationship Management

|Attribute    |Details                                       |
|-------------|----------------------------------------------|
|**Full Name**|SAP Supplier Relationship Management          |
|**Purpose**  |Manages procurement and supplier collaboration|

**Key Features:**

- Self-service procurement (shopping cart)
- Sourcing and contract management
- Supplier evaluation
- Catalog management
- Integration with SAP MM

> **Note:** Largely replaced by **SAP Ariba**.

-----

### 17. SAP SCM – Supply Chain Management

|Attribute    |Details                                            |
|-------------|---------------------------------------------------|
|**Full Name**|SAP Supply Chain Management                        |
|**Purpose**  |Advanced planning and optimization of supply chains|

**Key Sub-components:**

- **APO** – Advanced Planning and Optimization
  - Demand Planning (DP)
  - Supply Network Planning (SNP)
  - Production Planning/Detailed Scheduling (PP/DS)
  - Global Available to Promise (GATP)

-----

### 18. SAP GRC – Governance, Risk & Compliance

|Attribute    |Details                                      |
|-------------|---------------------------------------------|
|**Full Name**|SAP Governance, Risk, and Compliance         |
|**Purpose**  |Manages compliance, risk, and access controls|

**Key Components:**

- **GRC-AC** – Access Control (SoD, role management)
- **GRC-PC** – Process Control
- **GRC-RM** – Risk Management
- **GRC-GTS** – Global Trade Services
- **GRC-FM** – Fraud Management

-----

### 19. SAP EHS – Environment, Health & Safety

|Attribute    |Details                                                                     |
|-------------|----------------------------------------------------------------------------|
|**Full Name**|SAP Environment, Health and Safety                                          |
|**Purpose**  |Manages workplace safety, environmental compliance, and hazardous substances|

**Key Features:**

- Incident management
- Hazardous substance management
- Industrial hygiene and safety
- Waste management
- Regulatory compliance reporting

-----

### 20. SAP RE-FX – Flexible Real Estate Management

|Attribute    |Details                                              |
|-------------|-----------------------------------------------------|
|**Full Name**|SAP Flexible Real Estate Management                  |
|**Purpose**  |Manages real estate portfolios, leases, and contracts|

**Key Features:**

- Lease accounting (IFRS 16 / ASC 842 compliant)
- Property and contract management
- Service charge settlement
- Space management

-----

## ☁️ SAP S/4HANA CLOUD MODULES

-----

### 21. SAP S/4HANA Finance

- Universal Journal (single source of truth)
- Embedded analytics
- Real-time financial close
- Advanced cash management
- Group reporting and consolidation

-----

### 22. SAP S/4HANA Sourcing & Procurement

- Operational purchasing
- Central procurement
- Supplier collaboration
- Invoice management
- Integration with SAP Ariba

-----

### 23. SAP S/4HANA Manufacturing

- Manufacturing execution
- Production engineering and operations
- MRP Live (ultra-fast planning)
- Predictive MRP
- Integration with SAP MES/Digital Manufacturing

-----

### 24. SAP S/4HANA Supply Chain

- Inventory management
- Extended warehouse management
- Transportation management
- Order and delivery management
- Integration with SAP IBP (Integrated Business Planning)

-----

### 25. SAP S/4HANA Asset Management

- Predictive maintenance
- Linear asset management
- Environment, health & safety integration
- Asset intelligence network

-----

## 🔧 SAP TECHNOLOGY & PLATFORM MODULES

-----

### 26. SAP BASIS

|Attribute    |Details                                                      |
|-------------|-------------------------------------------------------------|
|**Full Name**|SAP Basis (Business Application Software Integrated Solution)|
|**Purpose**  |Technical foundation and administration layer of SAP systems |

**Key Areas:**

- System installation and configuration
- Transport Management System (TMS)
- User and authorization management
- System monitoring and performance tuning
- SAP Solution Manager integration
- Patch and upgrade management

-----

### 27. SAP ABAP – Advanced Business Application Programming

|Attribute    |Details                                                                 |
|-------------|------------------------------------------------------------------------|
|**Full Name**|Advanced Business Application Programming                               |
|**Purpose**  |SAP’s proprietary programming language for customization and development|

**Key Features:**

- Report development (ALV, classical)
- BAPIs, BAdIs, User Exits, Enhancements
- SmartForms and SAPscript
- Workflow development
- Object-Oriented ABAP (OO-ABAP)
- ABAP on HANA (CDS views, AMDP)

-----

### 28. SAP Fiori

|Attribute    |Details                                 |
|-------------|----------------------------------------|
|**Full Name**|SAP Fiori                               |
|**Purpose**  |Modern UX framework for SAP applications|

**Key Features:**

- Role-based, task-centric apps
- Responsive design (mobile, tablet, desktop)
- SAP Fiori Launchpad
- Fiori Elements and Freestyle apps
- Integration with SAP S/4HANA and BTP

-----

### 29. SAP BTP – Business Technology Platform

|Attribute    |Details                                                             |
|-------------|--------------------------------------------------------------------|
|**Full Name**|SAP Business Technology Platform                                    |
|**Purpose**  |Cloud platform for integration, extension, analytics, and automation|

**Key Services:**

- **SAP Integration Suite** – API and integration management
- **SAP Extension Suite** – Low-code/no-code development
- **SAP Analytics Cloud** – BI and planning
- **SAP HANA Cloud** – In-memory database
- **SAP AI Core / AI Launchpad** – Machine learning services

-----

### 30. SAP HANA

|Attribute    |Details                                    |
|-------------|-------------------------------------------|
|**Full Name**|SAP High-Performance Analytic Appliance    |
|**Purpose**  |In-memory database and application platform|

**Key Features:**

- Columnar in-memory storage
- Real-time analytics on transactional data
- OLAP and OLTP in a single system
- SAP HANA Studio and Web IDE
- CDS (Core Data Services) views
- Spatial and graph processing

-----

## 📊 SAP INDUSTRY-SPECIFIC MODULES

|Module               |Industry          |Description                                   |
|---------------------|------------------|----------------------------------------------|
|**SAP IS-Retail**    |Retail            |Merchandise management, pricing, promotions   |
|**SAP IS-Banking**   |Banking           |Loans, deposits, payments, risk management    |
|**SAP IS-Insurance** |Insurance         |Policy management, claims, reinsurance        |
|**SAP IS-Healthcare**|Healthcare        |Patient management, clinical processes        |
|**SAP IS-Oil & Gas** |Oil & Gas         |Exploration, production, downstream processing|
|**SAP IS-Utilities** |Utilities         |Meter reading, billing, device management     |
|**SAP IS-Telecom**   |Telecom           |Convergent charging, mediation                |
|**SAP IS-Media**     |Media             |Contract and rights management                |
|**SAP IS-Auto**      |Automotive        |Dealer management, vehicle tracking           |
|**SAP IS-AFS**       |Apparel & Footwear|Grid article management, season planning      |

-----

## 🔗 SAP MODULE INTEGRATION OVERVIEW

```
SD ──────────────────────────────────────────────┐
  (Order-to-Cash)                                 │
                                                  ▼
MM ──────────────────────────────────────────► FI / CO
  (Procure-to-Pay)                         (Finance & Controlling)
                                                  ▲
PP ──────────────────────────────────────────────┘
  (Plan-to-Produce)

QM ◄──── PP (Quality checks during production)
PM ◄──── MM (Spare parts procurement)
HR ◄──── FI/CO (Payroll postings)
PS ◄──── CO (Project cost management)
```

-----

## 📋 Quick Reference Summary

|Module                           |Code  |Category        |
|---------------------------------|------|----------------|
|Financial Accounting             |FI    |Finance         |
|Controlling                      |CO    |Finance         |
|Materials Management             |MM    |Procurement     |
|Sales & Distribution             |SD    |Sales           |
|Production Planning              |PP    |Manufacturing   |
|Quality Management               |QM    |Quality         |
|Plant Maintenance                |PM    |Maintenance     |
|Human Capital Management         |HCM/HR|HR              |
|Project System                   |PS    |Projects        |
|Warehouse Management             |WM/EWM|Logistics       |
|Transportation Management        |TM    |Logistics       |
|Business Warehouse               |BW/BI |Analytics       |
|Business Planning & Consolidation|BPC   |Finance Planning|
|Customer Relationship Management |CRM   |Sales/Service   |
|Supplier Relationship Management |SRM   |Procurement     |
|Supply Chain Management          |SCM   |Supply Chain    |
|Governance, Risk & Compliance    |GRC   |Compliance      |
|Environment, Health & Safety     |EHS   |Safety          |
|Real Estate Management           |RE-FX |Real Estate     |
|SAP Basis                        |BASIS |Technical       |
|ABAP Development                 |ABAP  |Development     |
|SAP Fiori                        |Fiori |UX/UI           |
|SAP Business Technology Platform |BTP   |Cloud Platform  |
|SAP HANA                         |HANA  |Database        |

-----

*Document generated: May 2026 | SAP Modules Reference Guide*
