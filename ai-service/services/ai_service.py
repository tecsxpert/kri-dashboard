import threading
from services.groq_client import generate_response

ai_results = {}

def run_ai_task(task_id, text):
    try:
        result = generate_response(text)

        if not result:
            result = "AI service unavailable"

        ai_results[task_id] = result

    except Exception as e:
        ai_results[task_id] = f"Error: {str(e)}"


def process_async(task_id, text):
    thread = threading.Thread(target=run_ai_task, args=(task_id, text))
    thread.start()