from fastapi import FastAPI
import time
import chromadb
from collections import deque

app = FastAPI()

# ----------------------------
# BASIC METRICS STORAGE
# ----------------------------
start_time = time.time()

# last 10 response times
response_times = deque(maxlen=10)

# simple cache
cache = {}

MODEL_NAME = "groq-llama-model"

# ----------------------------
# CHROMA DB CLIENT (FIXED)
# ----------------------------
# IMPORTANT: use correct persistent path
chroma_client = chromadb.PersistentClient(path="./chroma_db")

collection = chroma_client.get_or_create_collection(name="default")

# ----------------------------
# MIDDLEWARE: TRACK RESPONSE TIME
# ----------------------------
@app.middleware("http")
async def add_process_time_header(request, call_next):
    start = time.time()

    response = await call_next(request)

    process_time = time.time() - start
    response_times.append(process_time)

    return response

# ----------------------------
# HEALTH ENDPOINT (TASK 7 FINAL)
# ----------------------------
@app.get("/health")
def health():

    # avg response time (last 10 requests)
    avg_response_time = (
        sum(response_times) / len(response_times)
        if response_times else 0
    )

    # ChromaDB document count
    try:
        doc_count = collection.count()
    except Exception:
        doc_count = 0

    # uptime
    uptime_seconds = time.time() - start_time

    # cache stats
    cache_stats = {
        "size": len(cache),
        "keys": list(cache.keys())[:5]
    }

    return {
        "model_name": MODEL_NAME,
        "avg_response_time_last_10": round(avg_response_time, 4),
        "chroma_doc_count": doc_count,
        "uptime_seconds": round(uptime_seconds, 2),
        "cache_stats": cache_stats
    }