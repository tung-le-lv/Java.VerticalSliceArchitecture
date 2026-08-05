# Technical Brief — Maturity Intelligence Systems

**Internal only** · From: Mai Hoang · For: the three assigned engineers

## 👋 What I need from you

Please read this brief, then **send me your technical questions by the end of today**.

I will collect all the questions, pick the strongest ones, and send them to the client. Important: **your questions are the first thing the client will see from us** — before the proposal, before any code review. So please make them good ones. 🙂

---

## 1. The client and what they build

- **Company:** Maturity Intelligence Systems — a small US software startup. The founder is based in Washington State.
- **Founder:** Patrick Zweber. He is the **only engineer** — he built everything himself.

### What does the product do?

Honest answer: **we don't fully know yet.** Patrick told us a lot about *how* the system is built, but not *what* it does. Here is what we can guess, and how sure we are:

| What we think | How sure? |
|---|---|
| It is probably a **maturity assessment platform** — software that helps companies measure how mature they are in some area (security, engineering practice, compliance, etc.). We guess this from the company name and Patrick's background. | 🟡 A guess — not confirmed |
| It is a **B2B product** (sold to companies, not to normal consumers). We know this because it integrates with Slack, Teams, and Zoom, and it uses SAML login — nobody builds SAML for consumers. | 🟢 High confidence |
| He has his **first real customers arriving end of August**, and he mentioned future customer support needs. So real users will be on the system soon after our assessment. | ✅ Confirmed |

👉 "What does the product actually do?" is a **fair question to ask him** — maybe the most important one. We need to understand the domain to judge if the architecture fits the problem. **Tip on wording:** ask for his *data model and user workflows* so we can assess the architecture properly. Don't make it sound like simple curiosity.

---

## 2. About Patrick — read this before you look at the stack

Patrick built and maintains the whole system **alone**. He is technically strong, understands security, and uses AI tools to build fast. Two things this means for you:

1. **Every question about the code is a question about his personal work.** Questions that sound curious and precise will land well. Questions that sound like judgement (before we have even seen the code!) will land badly.
2. **He will notice a lazy question immediately — and he will also notice a sharp one.** A question that clearly shows you read his stack list carefully is how we win his trust before we even send the proposal.

---

## 3. The stack — what he told us

These are facts as Patrick stated them, plus the things they make me wonder about. **Please add your own questions.**

| Layer | What he told us | What I wonder about (add yours!) |
|---|---|---|
| **Frontend** | React / TypeScript on Azure Static Web Apps | How is it built and deployed? State management? How does it talk to the API? |
| **Backend** | Node.js API **plus a separate worker service** | Why a separate worker — queues? scheduled jobs? long tasks? How do API and worker talk to each other? What happens when the worker fails? |
| **Database** | PostgreSQL, migrations in the repo | Which migration tool? Can he roll back? Where do reporting/analytics queries run? |
| **Infra** | Azure Container Apps (API), Terraform IaC (*"some, incomplete"*), Docker, GitHub Actions CI/CD | What does Terraform cover vs. what was clicked together in the Azure portal? Are there dev/staging/prod environments? What does CI actually check — tests? linting? nothing? |
| **Auth** | **Custom SAML-based authentication** | ⚠️ **The highest-risk item on the list.** Which identity providers? Which SAML flows? Session handling? Assertion validation? And why custom instead of a library or a managed identity provider? |
| **Integrations** | Slack, Microsoft Teams, Zoom | How are tokens stored and rotated? Webhook verification? What scopes does he request? What data flows out to these platforms? |

### Known unknowns — please don't guess these

These either go on the question list, or we wait for repo access:

- **Codebase size and age.** He hasn't said. The sanitized mirror will show us — only ask if we need it *before* getting access.
- **Testing.** Zero information about test coverage, test types, or QA. **A prime question.**
- **Observability.** Nothing about logging, monitoring, alerting, or error tracking. **Prime question** — this is half of launch-readiness.
- **Data sensitivity.** What customer data will the platform hold at launch? This directly decides how deep our security review must go.
- **Environments and release process.** How does he ship today, working solo?

---

## 4. What we've been hired to assess

Fixed scope, **four areas**, finished by **August 14**. His launch is end of August — the gap between our deadline and his launch is his time to fix things.

1. **Architecture review** — does the structure fit the product, and will it survive the next year?
2. **Code quality** — maintainability, consistency, the general health of a codebase built by one person.
3. **Security — moderate depth.** Enough confidence for a v1 launch. ⚠️ **This is explicitly NOT a penetration test** (his words, confirmed in writing). We work at the code and configuration level: auth, sessions, secrets, dependency risk, integration token handling, data protection. **Please don't let your questions drift into pentest territory.**
4. **Launch-readiness** — infra, CI/CD, migrations, rollback, observability, and whether the system can survive real customers in September.

No specific regulations apply. Standard privacy and security expectations.

### Our deliverable — this is what won us the conversation

Not a normal findings report. We deliver a **verifiable specification of what his system actually does and why**, with every finding traced to evidence in the code. It's an artifact he keeps, whether or not he ever works with us again.

👉 Your questions should sound like they come from **people who build specifications, not people who follow checklists.**

### How the work will run

1. **Free scoping pass** (2–3 days, sizing only) on a **sanitized repo mirror** — no secrets, no customer data, no production access, named-engineer access only.
2. Then a **fixed-price proposal**. Work starts only after his **written approval**.
3. The three of you review **in parallel and cross-check each other's findings**. We sold this structure to him as a quality feature — so start thinking now about how you would naturally split this system three ways.

---

**Reminder: questions to me by end of today. Make them count. 💪**
