import json
from datetime import datetime
from flask import Blueprint, request
from services.groq_client import generate_response

describe_bp = Blueprint("describe", __name__)

@describe_bp.route("/describe", methods=["POST"])
def describe():
    data = request.json
    user_input = data.get("text", "")

    # 1. Validate input
    if not user_input:
        return {"error": "Input required"}, 400

    # 2. Load prompt template
    with open("prompts/describe_prompt.txt") as f:
        template = f.read()

    prompt = template.replace("{input}", user_input)

    # 3. Call AI
    result = generate_response(prompt)

    # 4. Parse JSON
    try:
        parsed = json.loads(result)
    except:
        parsed = {
            "title": user_input,
            "description": result,
            "risk_level": "Unknown"
        }

    # 5. Add timestamp
    parsed["generated_at"] = datetime.now().isoformat()

    return parsed