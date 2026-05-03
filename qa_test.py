import requests
import time

API_URL = "http://127.0.0.1:8001/ask"

# ----------------------------
# 30 DEMO PROMPTS
# ----------------------------
prompts = [
    "what is ai",
    "explain machine learning",
    "what is deep learning",
    "what is fastapi",
    "explain redis",
    "what is chromadb",
    "what is caching",
    "how caching works",
    "what is uvicorn",
    "explain api",
    "what is python",
    "what is backend development",
    "what is database",
    "explain vector database",
    "what is llm",
    "what is generative ai",
    "what is nlp",
    "what is docker",
    "what is git",
    "what is github",
    "what is cloud computing",
    "what is aws",
    "what is system design",
    "what is microservices",
    "what is rest api",
    "what is json",
    "what is authentication",
    "what is authorization",
    "what is scalability",
    "what is latency"
]

# ----------------------------
# QA CHECK
# ----------------------------
passed = 0
failed = 0

for i, q in enumerate(prompts, 1):
    start = time.time()

    try:
        res = requests.get(API_URL, params={"q": q}).json()
        answer = res.get("answer", "")
        meta = res.get("meta", {})

        # Basic QA rules
        is_valid = (
            len(answer.strip()) > 50 and
            "is_fallback" in meta and
            "confidence" in meta
        )

        status = "PASS" if is_valid else "FAIL"

        if is_valid:
            passed += 1
        else:
            failed += 1

        print(f"{i}. {q} → {status}")

    except Exception as e:
        print(f"{i}. {q} → ERROR: {e}")
        failed += 1

# ----------------------------
# FINAL RESULT
# ----------------------------
print("\n===== FINAL RESULT =====")
print(f"Passed: {passed}/30")
print(f"Failed: {failed}/30")

if passed >= 25:
    print("✅ Demo Ready")
else:
    print("❌ Needs Improvement")