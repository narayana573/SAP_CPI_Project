# SAP API Management (APIM) — Interview Preparation Notes

> **Scope:** SAP Integration Suite · API Management · Developer Hub  
> **Level:** Associate to Senior/Lead  
> **Last Updated:** 2025

---

## Table of Contents

1. [Core Architecture & Concepts](#1-core-architecture--concepts)
2. [Key Policies — Deep Dive](#2-key-policies--deep-dive)
3. [API Proxy Flows — Execution Lifecycle](#3-api-proxy-flows--execution-lifecycle)
4. [Security — Deep Dive](#4-security--deep-dive)
5. [Developer Hub & API Products](#5-developer-hub--api-products)
6. [SAP CPI vs. SAP API Management](#6-sap-cpi-vs-sap-api-management)
7. [API Versioning & Lifecycle Management](#7-api-versioning--lifecycle-management)
8. [Analytics & Monitoring](#8-analytics--monitoring)
9. [Error Handling & Fault Rules](#9-error-handling--fault-rules)
10. [Scenario-Based Interview Questions](#10-scenario-based-interview-questions)
11. [Troubleshooting Scenarios](#11-troubleshooting-scenarios)
12. [Quick-Fire Definitions Cheat Sheet](#12-quick-fire-definitions-cheat-sheet)

---

## 1. Core Architecture & Concepts

### The Four Building Blocks

| Component | What It Is | Interview Angle |
|---|---|---|
| **API Provider** | Connection config pointing to a backend (URL, host, port, auth) | "Where does APIM know where to route to?" |
| **API Proxy** | A facade/gateway that sits between consumer and backend | "Where do policies live?" |
| **API Product** | A bundle of one or more proxies with a usage plan | "How do you publish and monetize?" |
| **Developer Hub** | Self-service catalog for discovery, testing, and subscription | "How do consumers onboard?" |

> **Key Rule:** A consumer can never call a proxy directly — it must be packaged inside an **API Product** and subscribed to via the **Developer Hub**.

### API Proxy Internal Structure

```
Consumer → [APIM Gateway] → Backend
               |
           API Proxy
               |
         ┌─────────────┐
         │  Request     │  ← Pre-Flow → Conditional Flow → Post-Flow
         │  Processing  │
         ├─────────────┤
         │  Target      │  ← Route to API Provider (backend)
         │  Connection  │
         ├─────────────┤
         │  Response    │  ← Pre-Flow → Conditional Flow → Post-Flow
         │  Processing  │
         └─────────────┘
```

### Virtual Host

The public-facing URL that consumers use to call your API. Multiple proxies can share the same virtual host with different base paths (e.g., `/orders`, `/invoices`). Interviewers often ask: *"How do you expose the same API on different environments?"* — answer: by assigning different virtual hosts to the same proxy deployed on different environments (Dev/QA/Prod).

---

## 2. Key Policies — Deep Dive

Policies are the core value of APIM. They execute as small programs at runtime. Understand **what** each policy does, **where** to place it in the flow, and **why**.

### 2.1 Traffic Management

#### Spike Arrest
- Smooths out traffic bursts — protects the backend from DDoS-style spikes.
- Operates in **requests per second (RPS)** or **requests per minute (RPM)**.
- **Placement:** First policy in the Request Pre-Flow (stops bad traffic before any processing).
- **Interview Tip:** Spike Arrest is *not* the same as Quota. Spike Arrest is about rate; Quota is about volume over time.

```xml
<SpikeArrest name="SA-ProtectBackend">
    <Rate>10ps</Rate>  <!-- 10 per second -->
</SpikeArrest>
```

#### Quota
- Limits the total number of API calls over a defined period (per hour, day, month).
- Typically assigned **per API Key / per Developer App**, enabling tiered pricing models.
- **Placement:** After Verify API Key (so you know *who* is calling before checking their quota).

> **Interview Tip — Spike Arrest vs. Quota:**
> - *Spike Arrest* = "No more than 10 requests per second from anyone."
> - *Quota* = "This developer's app is allowed 5,000 calls this month."

#### Concurrent Rate Limit
- Limits the number of **simultaneous open connections** to the backend.
- Use when the backend can only handle a fixed number of concurrent threads.

### 2.2 Security Policies

#### Verify API Key
- Validates that the incoming request contains a registered, active API key.
- The API Key is generated when a developer subscribes to a Product in the Developer Hub.
- **Placement:** Early in Request Pre-Flow, right after Spike Arrest.

#### OAuth 2.0 (OAuthV2)
- Supports four grant types: Authorization Code, Client Credentials, Implicit, Resource Owner Password.
- **Most common in enterprise integrations:** Client Credentials (machine-to-machine).
- Can be used to both **generate tokens** and **verify tokens** depending on policy configuration.

#### Basic Authentication
- Decodes Base64-encoded credentials from the `Authorization` header.
- Useful for legacy backends that require username/password.

#### JSON/XML Threat Protection
- Guards against injection attacks, deeply nested structures, oversized payloads, and malformed content.
- **JSON Threat Protection** checks: max array length, max string length, max container depth, max object entries.
- **XML Threat Protection** checks: max element depth, max attribute count, DTD validation.
- **Placement:** Request Pre-Flow, before any processing — fail fast on bad input.

#### HMAC / Message Signature Validation
- Validates that the message was not tampered with in transit using a shared secret and a hash algorithm (SHA-256 etc.).

### 2.3 Mediation & Transformation Policies

#### Assign Message
- The Swiss Army knife of APIM. Used to:
  - Add / remove / change **HTTP headers**
  - Add / remove **query parameters**
  - Set or overwrite the **request or response payload**
  - Change the **HTTP verb**

#### Extract Variables
- Extracts data from a request/response using **JSONPath** or **XPath** and stores it in a named variable.
- Used to pass data between policies in the same flow.

```xml
<ExtractVariables name="EV-GetOrderId">
    <JSONPayload>
        <Variable name="orderId" type="string">
            <JSONPath>$.order.id</JSONPath>
        </Variable>
    </JSONPayload>
</ExtractVariables>
```

#### XML to JSON / JSON to XML
- Converts message formats on the fly without backend changes.
- Useful when a legacy backend returns XML but the consumer expects JSON.

#### Message Logging
- Logs request/response details to an external syslog or logging endpoint.
- **Key difference:** Message Logging is **non-blocking** — it runs asynchronously and does not add latency to the main response path.
- **Placement:** PostClient Flow (after response is sent to client), so it never slows down the API.

#### Service Callout
- Makes an additional HTTP call to an external service **within** the policy flow.
- Use cases: token lookups, enrichment calls, validation against a third-party system.
- The response is stored in a variable and used by downstream policies.

#### Raise Fault
- Explicitly throws a custom fault/error from within a flow.
- Used for business rule violations (e.g., "required header X is missing").
- Returns a custom HTTP status code and error message to the consumer.

### Policy Placement Summary

| Policy | Recommended Placement |
|---|---|
| Spike Arrest | Request Pre-Flow — First |
| JSON/XML Threat Protection | Request Pre-Flow — Second |
| Verify API Key / OAuth | Request Pre-Flow — Third |
| Quota | Request Pre-Flow — After Auth |
| Extract Variables | Request/Response flow as needed |
| Assign Message | Request/Response flow as needed |
| XML↔JSON Conversion | Request or Response flow |
| Service Callout | Request flow (enrichment before routing) |
| Message Logging | PostClient Flow |
| Raise Fault | Conditional flows or Fault Rules |

---

## 3. API Proxy Flows — Execution Lifecycle

Understanding the exact order of execution is a favourite interview question.

```
Incoming Request
      │
      ▼
[1] ProxyEndpoint Request Pre-Flow     ← Runs for ALL requests (auth, spike arrest)
      │
      ▼
[2] ProxyEndpoint Conditional Flow     ← Runs only if condition matches (e.g. path = /v2/*)
      │
      ▼
[3] ProxyEndpoint Request Post-Flow    ← Runs for ALL requests (logging, header cleanup)
      │
      ▼
[4] TargetEndpoint Request Pre-Flow    ← Prepare the call to the backend
      │
      ▼
[5] >>> BACKEND SYSTEM CALLED <<<
      │
      ▼
[6] TargetEndpoint Response Pre-Flow   ← Handle raw backend response
      │
      ▼
[7] TargetEndpoint Response Post-Flow
      │
      ▼
[8] ProxyEndpoint Response Pre-Flow    ← Transform response for the consumer
      │
      ▼
[9] ProxyEndpoint Response Post-Flow
      │
      ▼
[10] PostClient Flow                   ← Fires AFTER response sent (async logging only)
      │
      ▼
Consumer Receives Response
```

> **Interview Tip:** If a **Fault** occurs at any stage, execution jumps to the **Fault Rules** and **Default Fault Rule** — normal flow is abandoned.

---

## 4. Security — Deep Dive

### 4.1 API Key vs. OAuth — When to Use Which

| Criteria | API Key | OAuth 2.0 |
|---|---|---|
| Complexity | Simple | Higher |
| Use Case | Public/internal APIs, simple access control | Delegated access, user-context APIs |
| Token Expiry | Keys don't expire (until revoked) | Tokens expire (short-lived) |
| Granularity | App-level | User + Scope level |
| Recommended For | Server-to-server, internal tools | Consumer apps accessing user data |

### 4.2 Mutual TLS (mTLS)

- Both the client and server present certificates to each other.
- Used to guarantee that only known, trusted clients can call the APIM gateway.
- Configured at the **Virtual Host** level.
- **Interview Tip:** mTLS is the strongest transport-level security. When an interviewer asks "how do you ensure only your APIM proxy can reach the backend CPI iFlow?", the answer is **client certificate pinning / mTLS on the Target Endpoint**.

### 4.3 Preventing Backend Bypass

A critical architecture point: if you expose a CPI iFlow via APIM, what stops a consumer from calling the CPI iFlow URL directly (bypassing all your policies)?

**Solution — Two-Layer Security:**
1. In APIM: Apply Verify API Key or OAuth.
2. In CPI: Configure the iFlow to **only accept requests from the APIM outbound IP range** or require a specific **client certificate** that only APIM holds.

---

## 5. Developer Hub & API Products

### API Product Configuration

When creating an API Product, you configure:
- **Proxies:** Which API Proxies are included.
- **Environments:** Which environments (Dev, QA, Prod) this product is available in.
- **Quota:** Default rate limits for all apps subscribing to this product.
- **Scopes (OAuth):** Which OAuth scopes are allowed for this product.
- **Approval Type:** Auto-approve subscriptions or require manual approval.

### Developer App & API Keys

1. A **Developer** registers on the Developer Hub.
2. The Developer creates an **App** and subscribes it to an API Product.
3. The system generates a **Consumer Key (API Key)** and **Consumer Secret** for that App.
4. The Developer uses the Consumer Key in the `apikey` query parameter or `x-api-key` header.

### Custom Attributes

Both API Products and Developer Apps support **custom attributes** (key-value pairs). These can be read inside policies using the `verifyapikey.{policy-name}.{attribute}` variable — useful for implementing custom logic based on the subscriber's tier or region.

---

## 6. SAP CPI vs. SAP API Management

| Feature | SAP Cloud Integration (CPI) | SAP API Management (APIM) |
|---|---|---|
| **Primary Goal** | Orchestration, mapping, data transformation | Security, governance, traffic control, exposure |
| **Connectivity** | 50+ adapters: SFTP, IDoc, JDBC, AS2, SOAP, REST | Primarily REST, OData, SOAP (HTTP-based) |
| **Typical Flow Type** | Synchronous and Asynchronous | Synchronous (request-response) |
| **Logic Style** | Groovy scripts, XSLT, Message Mapping | Lightweight policies, JavaScript, Python |
| **Protocol Transformation** | Yes (e.g. IDoc → REST, AS2 → SFTP) | Limited (XML↔JSON, header manipulation) |
| **Use in Combination** | iFlow provides the integration logic | APIM sits in front, handles who & how much |
| **Error Handling** | Exception subprocesses, dead-letter queues | Fault Rules, Raise Fault policy |
| **Monitoring** | Message monitoring dashboard, trace tool | API Analytics dashboard, custom reports |

> **Combined Architecture Pattern:**
> `External Consumer → APIM (security, quota) → CPI iFlow (orchestration, mapping) → SAP S/4HANA / External System`

---

## 7. API Versioning & Lifecycle Management

### Versioning Strategies

| Strategy | Example | When to Use |
|---|---|---|
| **URI Versioning** | `/api/v1/orders` | Most common; visible and explicit |
| **Header Versioning** | `Accept: application/vnd.company.v2+json` | Clean URIs; complex to implement |
| **Query Param** | `/orders?version=2` | Simple; not RESTfully ideal |

> **APIM Approach:** Create separate API Proxies with different base paths (`/v1/`, `/v2/`) and manage them independently. Old versions can be deprecated using the API lifecycle feature.

### API Lifecycle States

```
DRAFT → PUBLISHED → DEPRECATED → RETIRED
```

- **Draft:** Created but not visible in Developer Hub.
- **Published:** Visible and subscribable in Developer Hub.
- **Deprecated:** Visible but marked for removal; no new subscriptions.
- **Retired:** Removed from Developer Hub; existing calls may still work until the proxy is undeployed.

---

## 8. Analytics & Monitoring

### Out-of-the-Box Metrics

APIM provides a built-in analytics dashboard. Key metrics to know for interviews:

| Metric | What It Measures |
|---|---|
| **Total Traffic** | Total API calls in a time range |
| **Error Rate** | % of calls returning 4xx or 5xx |
| **Average Latency** | Average end-to-end response time |
| **Target Latency** | Time spent in the backend system |
| **Proxy Latency** | Time spent inside the APIM gateway (policy overhead) |
| **Cache Hit Rate** | % of responses served from cache |
| **Top APIs** | Most-called API Proxies |
| **Top Developers** | Developers with highest consumption |

### Response Cache Policy

- Caches the backend response in APIM's cache for a configurable TTL.
- On a cache hit, the backend is **not called** — response served directly from APIM.
- Use for read-heavy, low-volatility data (e.g., product catalogs, reference data).

```xml
<ResponseCache name="RC-ProductCache">
    <CacheKey>
        <KeyFragment ref="request.uri" />
    </CacheKey>
    <ExpirySettings>
        <TimeoutInSec>300</TimeoutInSec>
    </ExpirySettings>
</ResponseCache>
```

> **Interview Tip:** Always mention that the cache key must uniquely identify the response. If two different consumers call the same URL but should receive different data (e.g., tenant-specific data), the cache key must include a differentiating header or variable.

---

## 9. Error Handling & Fault Rules

### Fault Rule vs. Default Fault Rule

- **Fault Rule:** A conditional error handler — executes only when a specific condition is met (e.g., `fault.name = "InvalidApiKey"`).
- **Default Fault Rule:** Catches any error not handled by a specific Fault Rule. Always define one as a safety net.

### Common Fault Names to Know

| Fault Name | Trigger |
|---|---|
| `InvalidApiKey` | API key doesn't exist or is revoked |
| `FailedToResolveAPIKey` | `apikey` parameter missing from request |
| `AccessTokenExpired` | OAuth token has expired |
| `SpikeArrestViolation` | Request rejected due to Spike Arrest |
| `QuotaViolated` | Developer has exceeded their quota |
| `NoRoutesMatched` | No conditional flow path matched |
| `ConnectionRefused` | Backend server refused the connection |

### Best Practice: Custom Error Responses

Always use **Assign Message** inside a Fault Rule to return a consistent, consumer-friendly JSON error body instead of the raw APIM XML error.

```xml
<FaultRules>
    <FaultRule name="InvalidKeyRule">
        <Condition>fault.name = "InvalidApiKey"</Condition>
        <Step>
            <Name>AM-InvalidKeyResponse</Name>
        </Step>
    </FaultRule>
</FaultRules>
<DefaultFaultRule name="DefaultFaultRule">
    <Step>
        <Name>AM-GenericErrorResponse</Name>
    </Step>
</DefaultFaultRule>
```

---

## 10. Scenario-Based Interview Questions

### Q1: How do you secure a CPI iFlow using APIM?

**Answer:**
1. Create an **API Provider** pointing to the CPI runtime URL.
2. Create an **API Proxy** with that provider as the target.
3. Apply **Verify API Key** (or OAuth) in the Request Pre-Flow.
4. Add **Spike Arrest** and **Quota** for traffic governance.
5. **Critically:** Lock down the CPI iFlow so it only accepts requests from APIM's outbound IP or requires APIM's client certificate — preventing consumers from bypassing the proxy.

---

### Q2: A developer reports their API calls are randomly failing with 429 errors. How do you diagnose?

**Answer:**
1. Check **APIM Analytics** — look at error rate and which policy is throwing the fault (`SpikeArrestViolation` vs `QuotaViolated`).
2. If **SpikeArrestViolation**: The client is sending bursts. Recommend client-side retry with exponential backoff. Consider increasing the Spike Arrest rate if legitimate.
3. If **QuotaViolated**: The developer has hit their monthly limit. They need to upgrade their API Product subscription tier.
4. Provide the developer with quota usage data from the Developer Hub Analytics.

---

### Q3: How do you handle API versioning when you need to introduce breaking changes?

**Answer:**
1. Create a new API Proxy with path `/v2/...` — keep `/v1/` running.
2. Publish the v2 proxy as a new or updated API Product.
3. **Deprecate** the v1 product in the Developer Hub — existing subscribers see a deprecation warning.
4. Communicate a sunset date to all v1 consumers (email/changelog).
5. **Retire** v1 after the sunset date and undeploy the proxy.

---

### Q4: How do you implement a backend fan-out (call multiple backends) in APIM?

**Answer:** APIM alone is not designed for complex orchestration. The correct approach is:
- Use **Service Callout** policies for simple secondary calls (e.g., a token validation lookup).
- For true fan-out/aggregation, route to a **CPI iFlow** that orchestrates the multiple backend calls and returns an aggregated response. APIM handles the security and governance layer; CPI handles the orchestration.

---

### Q5: What is the difference between Proxy Endpoint and Target Endpoint?

**Answer:**
- **Proxy Endpoint:** The consumer-facing side. Defines the virtual host, base path, and the policies that run when the request comes *in* and when the response goes *out* to the consumer.
- **Target Endpoint:** The backend-facing side. Defines the API Provider connection and the policies that run just *before* calling the backend and just *after* receiving the backend's response.
- A single Proxy Endpoint can have **multiple Target Endpoints** with routing rules — enabling dynamic routing based on the request (e.g., route `/v1/` calls to a legacy system and `/v2/` calls to a new microservice).

---

### Q6: How do you pass user identity from the consumer through APIM to the backend?

**Answer:**
1. Consumer authenticates via OAuth (e.g., Authorization Code flow) — the JWT contains user identity.
2. In APIM, use **Extract Variables** to pull the `sub` (subject) claim from the JWT.
3. Use **Assign Message** to inject that value as a custom HTTP header (e.g., `X-User-Id: {extracted.sub}`).
4. The CPI iFlow or backend reads this trusted header (trusted because it can only come through APIM).

---

### Q7: How do you implement response caching and what are the risks?

**Answer:**
- Apply **Response Cache** policy on the Proxy.
- Set a **cache key** based on the request URI (and any headers that differentiate responses).
- Set an appropriate **TTL** based on data volatility.
- **Risks:**
  - **Stale data:** If backend data changes within the TTL window, consumers get old data. Mitigate by keeping TTL short for volatile data.
  - **Cache poisoning:** If the cache key is too broad, one consumer's response is served to another. Always include tenant/user-differentiating values in the key for personalised data.
  - For personalised or sensitive data — **do not cache**.

---

## 11. Troubleshooting Scenarios

### Scenario 1: 503 Service Unavailable from APIM

**Possible Causes & Checks:**
- Backend CPI iFlow / server is down → Check CPI monitoring.
- APIM cannot reach the backend (network/firewall) → Check API Provider URL and connectivity.
- Target Endpoint SSL certificate expired → Check certificate in Trust Store.
- Backend response timeout exceeded → Increase `<ConnectTimeout>` and `<ReadTimeout>` in Target Endpoint.

### Scenario 2: Policies Not Executing as Expected

**Diagnostic Steps:**
1. Use the **APIM Trace tool** (Debug session) — it shows each policy, its execution order, variables set, and any errors.
2. Check if the policy is attached to the correct flow (Pre-Flow vs Conditional Flow condition).
3. Verify the condition syntax — a wrong condition means the policy is silently skipped.

### Scenario 3: Consumer Gets Raw XML Instead of JSON

**Cause:** The backend returns XML and no transformation policy is applied.  
**Fix:** Add an **XML to JSON** policy in the **Response Pre-Flow** of the Proxy Endpoint.

### Scenario 4: API Key Verification Fails Despite Valid Key

**Checks:**
- Is the key being sent in the correct location? (query param `?apikey=` vs header `x-api-key`). The policy must be configured to look in the right place.
- Is the Developer App in **Approved** status in the Developer Hub?
- Is the API Product the app is subscribed to **Published** and deployed to the correct environment?

---

## 12. Quick-Fire Definitions Cheat Sheet

| Term | One-Line Definition |
|---|---|
| **API Proxy** | A facade between consumer and backend where policies are applied |
| **API Product** | A publishable bundle of one or more proxies with a usage plan |
| **API Provider** | Connection config to a backend system |
| **Developer Hub** | Self-service portal for API discovery, testing, and subscription |
| **Virtual Host** | Public URL exposed to consumers |
| **Spike Arrest** | Rate limiter (per second/minute) to prevent traffic bursts |
| **Quota** | Volume limiter (per day/month) for monetization/tiered access |
| **Pre-Flow** | Policies that run for every single request, unconditionally |
| **Conditional Flow** | Policies that run only when a specific path/verb/condition matches |
| **Fault Rule** | Error handler triggered by a specific named fault |
| **Default Fault Rule** | Catch-all error handler for any unhandled fault |
| **Assign Message** | Policy to add/remove/change headers, params, or payload |
| **Extract Variables** | Policy to pull data from payload using JSONPath/XPath |
| **Service Callout** | Policy to make a secondary HTTP call within the flow |
| **Response Cache** | Policy to cache backend responses and reduce latency |
| **Message Logging** | Non-blocking async logging policy (runs in PostClient flow) |
| **mTLS** | Mutual TLS — both client and server authenticate with certificates |
| **Consumer Key** | The API Key issued when a developer app subscribes to a product |
| **Proxy Latency** | Time APIM spends processing the request (policy overhead) |
| **Target Latency** | Time the backend spends processing the request |

---

> **Final Interview Tips**
>
> - Always mention **security in layers** (Spike Arrest → Threat Protection → Auth → Quota).
> - When discussing CPI + APIM together, use the phrase: *"APIM handles the 'who' and 'how much'; CPI handles the 'how'."*
> - For any architecture question, mention **preventing backend bypass** as a critical step — it shows senior-level thinking.
> - Know the **Trace/Debug tool** — interviewers love asking how you'd troubleshoot a live issue.
> - Understand the difference between **proxy latency** and **target latency** — it immediately shows you can read the analytics dashboard.
