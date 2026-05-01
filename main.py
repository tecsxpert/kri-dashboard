from fastapi import FastAPI
import time
import chromadb
from collections import deque

import redis
import hashlib
import json

app = FastAPI()

# ----------------------------
# BASIC METRICS STORAGE
# ----------------------------
start_time = time.time()
response_times = deque(maxlen=10)

MODEL_NAME = "groq-llama-model"

# ----------------------------
# REDIS SETUP (FIXED)
# ----------------------------
def connect_redis():
    try:
        # Try both localhost and WSL bridge
        for host in ["127.0.0.1", "localhost"]:
            try:
                client = redis.Redis(
                    host=host,
                    port=6379,
                    db=0,
                    decode_responses=True
                )
                client.ping()
                print(f"✅ Redis connected on {host}")
                return client
            except:
                continue
    except:
        pass

    print("❌ Redis not available")
    return None

redis_client = connect_redis()

cache_hits = 0
cache_misses = 0

# ----------------------------
# CHROMA DB
# ----------------------------
chroma_client = chromadb.PersistentClient(path="./chroma_db")
collection = chroma_client.get_or_create_collection(name="default")

# ----------------------------
# HELPER
# ----------------------------
def get_cache_key(text: str):
    return hashlib.sha256(text.encode()).hexdigest()

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

    cache_key = get_cache_key(q)

    # CHECK CACHE
    if not fresh and redis_client:
        cached = redis_client.get(cache_key)
        if cached:
            cache_hits += 1
            return {
                "source": "cache",
                "answer": json.loads(cached)
            }

    # SIMULATED AI RESPONSE
    answer = f"AI response for: {q}"

    # STORE CACHE (15 min)
    if redis_client:
        redis_client.setex(cache_key, 900, json.dumps(answer))

    cache_misses += 1

    return {
        "source": "ai",
        "answer": answer
    }

# ----------------------------
# HEALTH
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