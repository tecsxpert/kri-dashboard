# SECURITY.md
## Tool-08 - KRI Dashboard Security documentation 

## 1. Injection Attacks

### Attack Scenario:
An attacker sends malicious input such as SQL queries or harmful text to manipulate the system or database.

### Mitigation:
- Validate and sanitize all user inputs
- Use parameterized queries
- Avoid executing raw user input

## 2. Broken Authentication

### Attack Scenario:
An attacker accesses the system without proper login or uses stolen credentials.

### Mitigation:
- Implement secure authentication (JWT)
- Validate user identity on every request
- Use strong password policies

## 3. Sensitive Data Exposure

### Attack Scenario:
Sensitive data like passwords or API keys are exposed in logs or responses.

### Mitigation:
- Store secrets in environment variables
- Do not expose sensitive data in API responses
- Use encryption where necessary

## 4. Broken Access Control

### Attack Scenario:
A user accesses resources or performs actions they are not allowed to.

### Mitigation:
- Implement role-based access control (RBAC)
- Restrict access based on user roles
- Validate permissions on every request

## 5. Security Misconfiguration

### Attack Scenario:
Improper system configuration exposes the application to vulnerabilities.

### Mitigation:
- Use secure default configurations
- Enable security headers
- Regularly update dependencies

## Additional Security Threats (Tool-Specific)

## 1. Prompt Injection Attack

### Attack Scenario:
A user sends malicious input like "Ignore previous instructions" to manipulate AI output.

### Damage Potential:
- AI gives wrong or unsafe results
- System behavior is altered

### Mitigation:
- Detect suspicious phrases
- Block prompt injection patterns
- Limit AI instructions

## 2. AI Hallucination

### Attack Scenario:
AI generates incorrect or misleading information due to unclear input.

### Damage Potential:
- Wrong insights
- Bad decision making

### Mitigation:
- Use structured prompts
- Add confidence score
- Use RAG (ChromaDB) for accuracy

## 3. Invalid Input Handling

### Attack Scenario:
User sends empty or malformed data to API.

### Damage Potential:
- System crash
- Unexpected errors

### Mitigation:
- Validate input before processing
- Reject empty requests
- Return proper error messages

## 4. AI API Failure

### Attack Scenario:
AI service (Groq API) fails or times out.

### Damage Potential:
- System stops responding
- Poor user experience

### Mitigation:
- Use try-except handling
- Provide fallback responses
- Set timeout limits

## 5. Logging Sensitive Data

### Attack Scenario:
Sensitive information is stored in logs.

### Damage Potential:
- Data leakage
- Security breach

### Mitigation:
- Avoid logging sensitive data
- Mask confidential fields
- Use secure logging

##  Week 1 Security Testing Results

###  Endpoint: /test

| Test Case | Input | Result | Status |
|----------|------|--------|--------|
| Empty Input | {} | Accepted / Processed | PASS |
| SQL Injection | ' OR 1=1 -- | Blocked by sanitizer | PASS |
| Prompt Injection | ignore previous instructions | Blocked by sanitizer | PASS |


### Endpoint: /generate-report

| Test Case | Input | Result | Status |
|----------|------|--------|--------|
| Empty Input | {} | Accepted | PASS |
| SQL Injection | ' OR 1=1 -- | Blocked | PASS |
| Prompt Injection | bypass system rules | Blocked | PASS |
| Rate Limit | >10 requests/min | 429 error returned | PASS |


##  Summary

All endpoints were tested against:
- Empty input
- SQL injection
- Prompt injection
- Rate limiting

 The system successfully blocked malicious inputs  
 Rate limiting is functioning correctly

 [7:31 pm, 03/05/2026] Amulya BV: ## 🛡️ OWASP ZAP Scan Results

### 🔍 Target
http://127.0.0.1:5000

---

###  Findings by Severity

#### 🔴 High
- None

#### 🟠 Medium
- None

#### 🟡 Low
- None

####  Informational
- No significant issues detected during scan

---

### Remediation Plan (Medium+)

No Medium or High severity vulnerabilities were identified.

Recommended improvements:
- Add security headers (CSP, X-Frame-Options)
- Continue periodic security testing
- Expand endpoints for deeper testing

---

###  Conclusion

OWASP ZAP scan completed successfully.
No vulnerabilities were detected in the current API structure.
[7:33 pm, 03/05/2026] Amulya BV: ## 🛡️ OWASP ZAP Scan Results

### 🔍 Target
http://127.0.0.1:5000

---

###  Findings by Severity

#### 🔴 High
- None

#### 🟠 Medium
- None

#### 🟡 Low
- None

#### Informational
- No significant issues detected during scan

---

### Remediation Plan (Medium+)

No Medium or High severity vulnerabilities were identified.

Recommended improvements:
- Add security headers (CSP, X-Frame-Options)
- Continue periodic security testing
- Expand endpoints for deeper testing

---

### Conclusion

OWASP ZAP scan completed successfully.
No vulnerabilities were detected in the current API structure.

## Day 8: ZAP Remediation

###  Previous Findings
- Missing security headers were identified during the OWASP ZAP scan (Day 7)

---

###  Fixes Applied
- Added X-Content-Type-Options: nosniff
- Added X-Frame-Options: DENY

---

###  Re-scan Result
- Re-scanned application using OWASP ZAP
- No alerts found after re-scan
- Previously identified header-related issues are resolved

---

### Conclusion
Security headers were successfully implemented.
OWASP ZAP re-scan confirms that no vulnerabilities are present.
Application security has been improved.

##  Day 9: PII Audit

###  Audit Scope
- Reviewed all API endpoints and input handling
- Checked for storage, logging, and exposure of personal data

---

###  Findings
- User input is received using request.get_json() in app.py and sanitizer.py
- Input is processed temporarily and not stored in any files or databases
- No logging of user input (no print or logger usage found)
- API responses return input only for testing purposes and do not persist data

---

###  Security Measures
- Input sanitization is implemented
- No persistence of sensitive data
- No exposure of confidential information

---

###  Conclusion
The application does not process or store any Personally Identifiable Information (PII).
No privacy risks were identified.

##  Day 10: Week  Security Sign-Off

###  Security Controls Verification

- JWT Enforcement:
  - Implemented using flask-jwt-extended
  - Verified: API access fails without token (401) and succeeds with valid token

- Rate Limiting:
  - Implemented using Flask-Limiter
  - Verified: 429 Too Many Requests returned when limit exceeded

- Injection Protection:
  - Input sanitization implemented
  - SQL injection and prompt injection attempts successfully blocked

---

###  Final Sign-Off

All required security controls have been successfully implemented and verified.

The application meets the required security standards for the current scope.

##  Day 11: Full OWASP ZAP Active Scan

###  Scan Details
- Tool: OWASP ZAP
- Scan Type: Active Scan
- Target: http://127.0.0.1:5000

---

###  Findings

####  Critical
- None found → No action required

####  High
- None found → No action required

####  Medium
- None found → No acceptance or remediation needed

---

###  Action Taken
- Since no Critical or High issues were detected, no fixes were required
- Application already secured with JWT, rate limiting, and input sanitization

---

### Conclusion
Application successfully passed full OWASP ZAP active scan with zero vulnerabilities.
All required security controls are effective.

##  Day 12: Security Hardening with Flask-Talisman

###  Enhancements Applied
- Integrated Flask-Talisman for automatic security headers
- Enabled protection against common web vulnerabilities

---

###  Re-scan Results (OWASP ZAP)
- Critical: None
- High: None
- Medium: None / Minor
- Low: Informational only

---

###  Outcome
- All security headers enforced
- No remaining Critical or High vulnerabilities

---

###  Conclusion
Application security strengthened using Flask-Talisman.
ZAP re-scan confirms zero Critical/High issues