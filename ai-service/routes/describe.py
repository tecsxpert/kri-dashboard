import json
from datetime import datetime
from flask import Blueprint, request
from services.groq_client import generate_response

describe_bp = Blueprint("describe", __name__)

@describe_bp.route("/describe", methods=["POST"])
def describe():
    data = request.json
    user_input = data.get("text", "")

    if not user_input:
        return {"error": "Input required"}, 400

    prompt = f"""
You are a professional risk analyst.

Analyze the given risk and generate structured output.
Risk: {user_input}

Return ONLY JSON:
{{
  "title": "Short professional title",
  "description": "Clear and concise explanation",
  "risk_level": "Low/Medium/High"
}}

Do not add any extra text outside JSON.
"""
    result = generate_response(prompt)

    try:
        parsed = json.loads(result)
    except:
        parsed = {
            "title": user_input,
            "description": result,
            "risk_level": "Unknown"
        }

    parsed["generated_at"] = datetime.now().isoformat()
    return parsed