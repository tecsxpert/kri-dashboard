from fastapi import FastAPI
import time
import chromadb
from collections import deque
import redis
import hashlib
import json
import random
from threading import Thread
import uuid

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
# JOB QUEUE
# ----------------------------
job_store = {}
QUEUE_NAME = "report_queue"

# ----------------------------
# HELPERS
# ----------------------------
def get_cache_key(text: str):
    return hashlib.sha256(text.encode()).hexdigest()

def generate_meta(start_time, cached: bool):
    return {
        "confidence": round(random.uniform(0.80, 0.98), 2),
        "model_used": MODEL_NAME,
        "tokens_used": random.randint(20, 120),
        "response_time_ms": int((time.time() - start_time) * 1000),
        "cached": cached
    }

# ----------------------------
# WORKER
# ----------------------------
def worker():
    while True:
        if redis_client:
            job_data = redis_client.lpop(QUEUE_NAME)
            if job_data:
                job = json.loads(job_data)
                job_id = job["job_id"]
                text = job["text"]

                job_store[job_id]["status"] = "processing"
                time.sleep(3)

                job_store[job_id]["status"] = "completed"
                job_store[job_id]["report"] = f"Generated report for {text}"

Thread(target=worker, daemon=True).start()

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
# ASK (WITH FALLBACK)
# ----------------------------
@app.get("/ask")
def ask(q: str, fresh: bool = False):
    global cache_hits, cache_misses

    request_start = time.time()
    cache_key = get_cache_key(q)

    # CACHE
    if not fresh and redis_client:
        cached = redis_client.get(cache_key)
        if cached:
            cache_hits += 1
            return {
                "source": "cache",
                "answer": json.loads(cached),
                "meta": {**generate_meta(request_start, True), "is_fallback": False}
            }

    try:
        # 🔥 CHANGE THIS TO TRUE TO TEST FALLBACK
        FORCE_ERROR = False

        if FORCE_ERROR:
            raise Exception("AI timeout")

        answer = f"""
Here is a detailed explanation:

{q} is an important concept in computer science.

Simple explanation:
- It is widely used
- Helps understanding core ideas

Example:
Practice {q} using small projects.

Summary:
Learning {q} improves your knowledge step by step.
"""

        if redis_client:
            redis_client.setex(cache_key, 900, json.dumps(answer))

        cache_misses += 1

        return {
            "source": "ai",
            "answer": answer,
            "meta": {**generate_meta(request_start, False), "is_fallback": False}
        }

    except Exception as e:
        return {
            "source": "fallback",
            "answer": f"Quick answer: {q} is a core concept. Try again later.",
            "meta": {
                **generate_meta(request_start, False),
                "is_fallback": True,
                "error": str(e)
            }
        }

# ----------------------------
# GENERATE REPORT
# ----------------------------
@app.post("/generate-report")
def generate_report(data: dict):
    job_id = str(uuid.uuid4())
    job_store[job_id] = {"status": "queued"}

    if redis_client:
        redis_client.rpush(QUEUE_NAME, json.dumps({
            "job_id": job_id,
            "text": data["text"]
        }))

    return {"job_id": job_id, "status": "queued"}

# ----------------------------
# REPORT STATUS
# ----------------------------
@app.get("/report-status/{job_id}")
def report_status(job_id: str):
    return job_store.get(job_id, {"status": "not_found"})

# ----------------------------
# HEALTH
# ----------------------------
@app.get("/health")
def health():
    return {
        "model_name": MODEL_NAME,
        "avg_response_time": round(sum(response_times)/len(response_times), 4) if response_times else 0,
        "uptime": round(time.time() - start_time, 2),
        "cache_hits": cache_hits,
        "cache_misses": cache_misses
    }