from fastapi import FastAPI
import time
import chromadb
from collections import deque
import redis
import hashlib
import json
import random
import uuid
import threading
import requests

app = FastAPI()

# ----------------------------
# BASIC METRICS
# ----------------------------
start_time = time.time()
response_times = deque(maxlen=10)

MODEL_NAME = "groq-llama-model"

# ----------------------------
# REDIS SETUP
# ----------------------------
try:
    redis_client = redis.Redis(
        host="localhost",
        port=6379,
        db=0,
        decode_responses=True
    )
    redis_client.ping()
    print("✅ Redis connected")
except:
    redis_client = None
    print("❌ Redis not available")

cache_hits = 0
cache_misses = 0

# ----------------------------
# CHROMA DB
# ----------------------------
chroma_client = chromadb.PersistentClient(path="./chroma_db")
collection = chroma_client.get_or_create_collection(name="default")

# ----------------------------
# JOB STORE (DAY 11)
# ----------------------------
jobs = {}

# ----------------------------
# HELPERS
# ----------------------------
def get_cache_key(text: str):
    return hashlib.sha256(text.encode()).hexdigest()

def generate_meta(start_time, cached: bool):
    response_time_ms = int((time.time() - start_time) * 1000)

    return {
        "confidence": round(random.uniform(0.80, 0.98), 2),
        "model_used": MODEL_NAME,
        "tokens_used": random.randint(20, 120),
        "response_time_ms": response_time_ms,
        "cached": cached
    }

# ----------------------------
# MIDDLEWARE
# ----------------------------
@app.middleware("http")
async def add_process_time_header(request, call_next):
    start = time.time()
    response = await call_next(request)
    response_times.append(time.time() - start)
    return response

# ----------------------------
# ASK ENDPOINT
# ----------------------------
@app.get("/ask")
def ask(q: str, fresh: bool = False):
    global cache_hits, cache_misses

    request_start = time.time()
    cache_key = get_cache_key(q)

    # CACHE CHECK
    if not fresh and redis_client:
        cached = redis_client.get(cache_key)
        if cached:
            cache_hits += 1
            return {
                "source": "cache",
                "answer": json.loads(cached),
                "meta": generate_meta(request_start, cached=True)
            }

    # AI RESPONSE (IMPROVED)
    answer = f"""
Detailed Explanation:

{q} is an important concept in computer science and AI systems.

Key Points:
- Widely used in real applications
- Helps in understanding system design
- Useful for interviews and projects

Example:
You can practice {q} by building small APIs or mini projects.

Summary:
Understanding {q} improves your technical knowledge step by step.
"""

    # STORE CACHE
    if redis_client:
        redis_client.setex(cache_key, 900, json.dumps(answer))

    cache_misses += 1

    return {
        "source": "ai",
        "answer": answer,
        "meta": generate_meta(request_start, cached=False)
    }

# ----------------------------
# HEALTH ENDPOINT
# ----------------------------
@app.get("/health")
def health():
    avg_response_time = (
        sum(response_times) / len(response_times)
        if response_times else 0
    )

    try:
        doc_count = collection.count()
    except:
        doc_count = 0

    uptime_seconds = time.time() - start_time

    cache_stats = {
        "size": redis_client.dbsize() if redis_client else 0,
        "hits": cache_hits,
        "misses": cache_misses
    }

    return {
        "model_name": MODEL_NAME,
        "avg_response_time_last_10": round(avg_response_time, 4),
        "chroma_doc_count": doc_count,
        "uptime_seconds": round(uptime_seconds, 2),
        "cache_stats": cache_stats
    }

# ----------------------------
# DAY 11: BACKGROUND JOB WORKER
# ----------------------------
def process_report(job_id: str, data: dict):
    time.sleep(5)  # simulate heavy processing

    result = {
        "status": "completed",
        "report": f"Generated report for {data['text']}"
    }

    jobs[job_id] = result

    # webhook call
    try:
        requests.post("http://127.0.0.1:8001/webhook", json={
            "job_id": job_id,
            "result": result
        })
    except:
        print("Webhook failed")

# ----------------------------
# DAY 11: GENERATE REPORT API
# ----------------------------
@app.post("/generate-report")
def generate_report(payload: dict):
    job_id = str(uuid.uuid4())

    jobs[job_id] = {
        "status": "processing"
    }

    thread = threading.Thread(
        target=process_report,
        args=(job_id, payload)
    )
    thread.start()

    return {
        "job_id": job_id,
        "status": "started"
    }

# ----------------------------
# JOB STATUS API
# ----------------------------
@app.get("/report-status/{job_id}")
def get_status(job_id: str):
    return jobs.get(job_id, {"error": "job not found"})

# ----------------------------
# WEBHOOK ENDPOINT
# ----------------------------
@app.post("/webhook")
def webhook(data: dict):
    print("Webhook received:", data)
    return {"ok": True}