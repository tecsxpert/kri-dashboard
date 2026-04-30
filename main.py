from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class InputText(BaseModel):
    text: str

@app.post("/categorise")
def categorise(data: InputText):
    text = data.text.lower()

    if "cricket" in text or "football" in text:
        return {
            "category": "sports",
            "confidence": 0.95,
            "reasoning": "Detected sports-related keywords"
        }

    if "ai" in text or "python" in text:
        return {
            "category": "technology",
            "confidence": 0.95,
            "reasoning": "Detected tech-related keywords"
        }

    return {
        "category": "unknown",
        "confidence": 0.5,
        "reasoning": "No strong match found"
    }