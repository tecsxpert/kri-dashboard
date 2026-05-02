import requests
import time
import statistics

BASE_URL = "http://127.0.0.1:8001"

ENDPOINTS = [
    "/ask?q=hello",
    "/health"
]

NUM_REQUESTS = 50


def percentile(data, p):
    size = len(data)
    return sorted(data)[int(size * p / 100)]


for endpoint in ENDPOINTS:
    times = []

    print(f"\nTesting {endpoint}")

    for i in range(NUM_REQUESTS):
        start = time.time()

        requests.get(BASE_URL + endpoint)

        end = time.time()
        times.append((end - start) * 1000)  # ms

    p50 = percentile(times, 50)
    p95 = percentile(times, 95)
    p99 = percentile(times, 99)

    print(f"p50: {p50:.2f} ms")
    print(f"p95: {p95:.2f} ms")
    print(f"p99: {p99:.2f} ms")