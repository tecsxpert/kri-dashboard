from fastapi import FastAPI
from pydantic import BaseModel
from chroma_db import seed_data, query_category

from groq import Groq
from dotenv import load_dotenv
import os

# Load .env file
load_dotenv()

app = FastAPI()

# Seed ChromaDB data once
seed_data()

# Groq client
client = Groq(api_key=os.getenv("GROQ_API_KEY"))


# ---------------------------
# REQUEST MODEL
# ---------------------------
class InputText(BaseModel):
    text: str


# ---------------------------
# OLD ENDPOINT
# ---------------------------
@app.post("/categorise")
def categorise(data: InputText):

    result = query_category(data.text)
    doc = result["documents"][0][0]

    return {
        "input": data.text,
        "matched_text": doc,
        "confidence": 0.9,
        "reasoning": "Matched using ChromaDB semantic search"
    }


# ---------------------------
# DAY 5 RAG ENDPOINT
# ---------------------------
@app.post("/query")
def query(data: InputText):

    try:
        # STEP 1: Retrieve from ChromaDB
        result = query_category(data.text)

        docs = result["documents"][0] if result.get("documents") else []

        # safety check (IMPORTANT)
        if not docs:
            return {
                "question": data.text,
                "answer": "No relevant context found in database.",
                "sources": []
            }

        context = "\n".join(docs)

        # STEP 2: Check API key
        if not os.getenv("GROQ_API_KEY"):
            return {
                "error": "GROQ_API_KEY not found in .env"
            }

        # STEP 3: Call Groq LLM
        response = client.chat.completions.create(
            model="llama-3.1-8b-instant",
            messages=[
                {
                    "role": "system",
                    "content": "You are an AI assistant. Answer only using the given context."
                },
                {
                    "role": "user",
                    "content": f"""
Context:
{context}

Question:
{data.text}
"""
                }
            ]
        )

        answer = response.choices[0].message.content

        return {
            "question": data.text,
            "answer": answer,
            "sources": [{"text": doc} for doc in docs]
        }

    except Exception as e:
        return {
            "error": str(e)
        }