import os
from dotenv import load_dotenv

load_dotenv()

GROQ_API_KEY = os.getenv("GROQ_API_KEY")


def generate_response(prompt):
    
    return f"AI Insight: {prompt} handled successfully"