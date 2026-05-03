import requests
import time

API_URL = "http://127.0.0.1:8001/ask"

test_inputs = [
    "hello",
    "what is AI",
    "explain redis",
    "what is fastapi",
    "what is chromadb",
    "how caching works",
    "what is uvicorn",
    "explain vector database",
    "give python example",
    "what is machine learning"
]

def score(answer):
    if len(answer) > 80:
        return 5
    elif len(answer) > 50:
        return 4
    elif len(answer) > 30:
        return 3
    else:
        return 2

scores = []

for q in test_inputs:
    start = time.time()
    res = requests.get(API_URL, params={"q": q}).json()
    end = time.time()

    ans = res["answer"]
    s = score(ans)
    scores.append(s)

    print("\nQ:", q)
    print("A:", ans)
    print("Score:", s)
    print("Time:", round((end - start)*1000, 2), "ms")

print("\nAVG SCORE:", sum(scores)/len(scores))