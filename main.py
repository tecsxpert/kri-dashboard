from fastapi import FastAPI
from pydantic import BaseModel
from chroma_db import seed_data, query_category

app = FastAPI()

seed_data()

class InputText(BaseModel):
    text: str

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