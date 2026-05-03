# AI Service — KRI Dashboard

A modular AI backend service built with Flask that provides multiple endpoints for risk analysis, recommendations, report generation, document analysis, batch processing, and optional RAG-based querying.

---

# 🚀 Overview

This service is designed as part of the **KRI Dashboard** to process risk-related inputs using AI models. It supports:

* Async report generation
* Streaming responses (SSE)
* Batch processing with rate limiting
* Document analysis with structured outputs
* Modular architecture (routes + services)
* Unit testing using pytest

---

#  Features

### Core APIs

* Describe risks
* Recommend mitigation strategies
* Generate structured reports (async)
* Stream report generation (SSE)
* Analyse documents (insights + risks)
* Batch process multiple inputs

### Advanced

* Async background processing
* Streaming with EventSource
* Error handling + fallback responses
* Unit testing with mocks
* Scalable service structure

---

# 📦 Prerequisites

Make sure your system has:

* Python 3.10+ (You are using 3.14 ✔)
* pip installed
* VS Code (recommended)
* Internet connection (for AI APIs)

---

# ⚙️ Setup Instructions

## 1. Navigate to service

```bash
cd ai-service
```

---

## 2. Create virtual environment (recommended)

```bash
python -m venv venv
venv\Scripts\activate
```

---

## 3. Install dependencies

```bash
pip install -r requirements.txt
```

---

## 4. Install missing packages (if errors occur)

```bash
pip install flask flask-cors pytest python-dotenv
```

---

# 🔐 Environment Variables

Create a `.env` file inside `ai-service/`

Example:

```
GROQ_API_KEY=your_api_key_here
HF_TOKEN=your_huggingface_token
```

### Notes:

* GROQ_API_KEY → required for AI responses
* HF_TOKEN → optional (for embeddings / RAG)
* If not set → fallback/mock responses will be used

---

# ▶️ Run the Application

```bash
python app.py
```

---

# 🌐 Server URL

```
http://127.0.0.1:5555
```

---

# 📡 FULL API REFERENCE

---

## 1. 🔹 POST `/describe`

Generate AI description of a risk.

### Request

```json
{
  "text": "Cybersecurity risk"
}
```

---

## 2. 🔹 POST `/recommend`

Generate recommendations.

### Request

```json
{
  "text": "Cybersecurity risk"
}
```

---

## 3. 🔹 POST `/generate-report` (ASYNC)

Starts report generation.

### Request

```json
{
  "text": "Cybersecurity risk"
}
```

### Response

```json
{
  "task_id": "abc123",
  "status": "processing"
}
```

---

## 4. 🔹 GET `/report-result/<task_id>`

Fetch result.

### Response (processing)

```json
{
  "status": "processing"
}
```

### Response (completed)

```json
{
  "status": "completed",
  "result": "AI generated report"
}
```

---

## 5. 🔹 GET `/generate-report-stream` (SSE)

Streams response word-by-word.

### Example

```
/generate-report-stream?text=cybersecurity
```

### Output

```
data: AI
data: generated
data: report
```

---

## 6. 🔹 POST `/analyse-document`

Extract insights + risks.

### Request

```json
{
  "text": "System has vulnerabilities"
}
```

### Response

```json
{
  "input": "...",
  "findings": [
    {
      "type": "insight",
      "description": "..."
    },
    {
      "type": "risk",
      "description": "..."
    }
  ],
  "generated_at": "timestamp"
}
```

---

## 7. 🔹 POST `/batch-process`

Process multiple inputs.

### Rules

* Max 20 items
* 100ms delay per item

### Request

```json
{
  "items": ["risk1", "risk2"]
}
```

### Response

```json
{
  "count": 2,
  "results": [
    {
      "input": "risk1",
      "output": "AI Insight..."
    }
  ]
}
```

---

## 8. 🔹 POST `/rag` (Optional)

RAG-based query (if implemented).

```json
{
  "query": "cybersecurity risk"
}
```

---

#  Testing (Pytest)

Run from root folder:

```bash
pytest
```

---

## ✔ What is tested?

* API responses
* Error handling
* Async results
* Streaming output
* AI mocking

---

# ⚠️ Common Issues & Fixes

---

## ❌ ModuleNotFoundError

Fix:

```python
import sys, os
sys.path.append(os.path.abspath("ai-service"))
```

---

## ❌ Port already in use

Change in `app.py`:

```python
app.run(port=5556)
```

---

## ❌ AI returns "Error from AI service"

Check:

* API key
* Internet connection
* groq_client.py logic

---

## ❌ pytest shows 0 tests

Check:

* folder name → `tests/`
* file name → `test_*.py`

---

# 📁 Project Structure

```
ai-service/
│
├── app.py
│
├── routes/
│   ├── describe.py
│   ├── recommend.py
│   ├── generate_report.py
│   ├── analyse_document.py
│   ├── batch_process.py
│
├── services/
│   ├── groq_client.py
│   ├── ai_service.py
│
├── prompts/
├── templates/
├── .env
└── requirements.txt
```

---

#  Architecture

```
Client → Routes → Services → AI API → Response
```

---

#  Future Improvements

* Add database (PostgreSQL / MongoDB)
* Add authentication (JWT)
* Add frontend (React dashboard)
* Improve AI prompts
* Add caching (Redis)
* Deploy to cloud (AWS / Render)

---

#  Author

Chaithanya V
AI Developer Intern

---

# ✅ Status

✔ APIs implemented
✔ Async processing
✔ Streaming working
✔ Batch processing added
✔ Unit tests completed
✔ Ready for deployment

---
