import os
from groq import Groq
from dotenv import load_dotenv

load_dotenv()

<<<<<<< HEAD
class GroqClient:
    def __init__(self):
        self.client = Groq(api_key=os.getenv("GROQ_API_KEY"))

    def generate(self, prompt):
        try:
            response = self.client.chat.completions.create(
                model="llama-3.3-70b-versatile",
                messages=[
                    {"role": "user", "content": prompt}
                ],
                temperature=0.7,
                max_tokens=500
            )

            return response.choices[0].message.content

        except Exception as e:
            print("Error:", e)
            return "AI service unavailable"
=======
client = Groq(api_key=os.getenv("GROQ_API_KEY"))

def generate_response(prompt):
    try:
        response = client.chat.completions.create(
            model="llama-3.1-8b-instant",
            messages=[
                {"role": "user", "content": prompt}
            ],
            temperature=0.5
        )
        return response.choices[0].message.content

    except Exception as e:
        return f"ERROR: {str(e)}"
    print("API KEY:", os.getenv("GROQ_API_KEY"))
>>>>>>> c33a9c2732f54cd81b9201aa294c09b33b2cedf7
