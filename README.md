# AI Service (KRI Dashboard Backend)

## Overview

This is the AI backend service for the KRI Dashboard.
It provides APIs for risk analysis, recommendations, categorization, and report generation using AI.

---

## Prerequisites

* Python 3.8+
* pip (Python package manager)

---

## Installation

1. Clone the repository:

```bash
git clone <your-repo-url>
cd ai-service
```

2. Install dependencies:

```bash
pip install -r requirements.txt
```

---

## Environment Variables

Create a `.env` file in the root:

```env
GROQ_API_KEY=your_api_key_here
```

---

## Run the Service

```bash
python app.py
```

Open in browser:

```
http://127.0.0.1:5000/
```

---

## 🔌 API Endpoints

### 1. Health Check

**GET /health**

Response:

```json
{ "status": "ok" }
```

---

### 2. Describe Risk

**POST /describe**

Request:

```json
{ "text": "Cybersecurity risk" }
```

Response:

```json
{
  "title": "...",
  "description": "...",
  "risk_level": "High",
  "generated_at": "timestamp"
}
```

---

### 3. Recommend Actions

**POST /recommend**

Request:

```json
{ "text": "Improve cybersecurity" }
```

Response:

```json
{
  "recommendations": [
    {
      "action_type": "Prevention",
      "description": "...",
      "priority": "High"
    }
  ]
}
```

---

### 4. Categorize Risk

**POST /categorize**

Request:

```json
{ "text": "Phishing attack" }
```

Response:

```json
{ "category": "Cybersecurity" }
```

---

### 5. Dashboard Analysis

**POST /dashboard**

Request:

```json
{ "risks": ["Cyber risk", "Fraud"] }
```

Response:

```json
{
  "results": [...]
}
```

---

### 6. Generate Report

**POST /generate-report**

Request:

```json
{ "risks": ["Cyber risk"] }
```

Response:

```json
{
  "title": "...",
  "executive_summary": "...",
  "overview": "...",
  "top_items": [],
  "recommendations": []
}
```

---

### 7. Analyse Document

**POST /analyse-document**

Request:

```json
{ "text": "System has vulnerabilities" }
```

Response:

```json
{
  "findings": [
    {
      "type": "Risk",
      "description": "...",
      "severity": "High"
    }
  ]
}
```

---

### 8. Batch Process

**POST /batch-process**

Request:

```json
{
  "items": ["Risk1", "Risk2"]
}
```

Response:

```json
{
  "results": [
    { "input": "Risk1", "result": "..." }
  ]
}
```

---

## Notes

* All AI responses are generated using Groq API
* Ensure API key is valid
* Use JSON format for all requests

---

## Author

Chaithanya V
