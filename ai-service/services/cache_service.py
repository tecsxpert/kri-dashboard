import json

try:
    import redis
    r = redis.Redis(host="localhost", port=6379, decode_responses=True)
    r.ping()
    REDIS_AVAILABLE = True
    print("✅ Redis connected")
except:
    REDIS_AVAILABLE = False
    print("⚠️ Redis not available, using local cache")
    local_cache = {}

def get_cache(key):
    try:
        if REDIS_AVAILABLE:
            value = r.get(key)
            if value:
                return json.loads(value)
        else:
            return local_cache.get(key)
    except:
        return None

def set_cache(key, value, ttl=300):
    try:
        if REDIS_AVAILABLE:
            r.setex(key, ttl, json.dumps(value))
        else:
            local_cache[key] = value
    except:
        pass