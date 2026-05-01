import json
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
Give output ONLY in JSON format with:
- title
- description (max 5 lines)
- risk_level (Low, Medium, High)

Topic: {user_input}
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


    risk_map = {
        "Low": 3,
        "Medium": 6,
        "High": 9
    }

    risk_level = parsed.get("risk_level", "Unknown").capitalize()

    parsed["risk_level"] = risk_level
    parsed["risk_score"] = risk_map.get(risk_level, 0)
    parsed["risk_label"] = f"{risk_level} Risk"

    return parsed