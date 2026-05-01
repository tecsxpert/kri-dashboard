import os
import requests
from dotenv import load_dotenv

load_dotenv()

API_KEY = os.getenv("GROQ_API_KEY")

def generate_response(prompt):
    url = "https://api.groq.com/openai/v1/chat/completions"

    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json"
    }

    data = {
        "model": "llama3-8b-8192",
        "messages": [
            {"role": "user", "content": prompt}
        ]
    }

    try:
        response = requests.post(url, headers=headers, json=data)

        # ✅ Debug (proper indentation)
        print(response.status_code)
        print(response.text)

        if response.status_code == 200:
            return response.json()['choices'][0]['message']['content']
        else:
            return "Error from AI service"

    except Exception as e:
        return f"Error: {str(e)}"
    print("API KEY:", API_KEY)