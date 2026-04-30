import os
import time
import logging
from groq import Groq
from dotenv import load_dotenv

load_dotenv()

logging.basicConfig(level=logging.INFO)

class GroqClient:
    def __init__(self):
        self.client = Groq(api_key=os.getenv("GROQ_API_KEY"))

    def generate(self, prompt):
        retries = 3
        delay = 2  # seconds

        for attempt in range(retries):
            try:
                response = self.client.chat.completions.create(
                    model="llama-3.3-70b-versatile",
                    messages=[
                        {"role": "user", "content": prompt}
                    ],
                    temperature=0.7,
                    max_tokens=500
                )

                # JSON parsing
                result = response.choices[0].message.content
                return result

            except Exception as e:
                logging.error(f"Attempt {attempt+1} failed: {e}")

                if attempt < retries - 1:
                    time.sleep(delay)
                    delay *= 2  # backoff (2s → 4s → 8s)
                else:
                    return "AI service unavailable"