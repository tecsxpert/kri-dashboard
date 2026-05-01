print("NEW RECOMMEND CODE RUNNING")
import json
from flask import Blueprint, request
from services.groq_client import generate_response

recommend_bp = Blueprint("recommend", __name__)

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.json
    user_input = data.get("text", "")

    if not user_input:
        return {"error": "Input required"}, 400

    try:
        with open("prompts/recommend_prompt.txt") as f:
            template = f.read()
        prompt = template.replace("{input}", user_input)

        result = generate_response(prompt)

        parsed = json.loads(result)
        return parsed

    except:
        # ✅ fallback (IMPORTANT)
        return [
            {
                "action_type": "Preventive",
                "description": "Implement basic security controls",
                "priority": "High"
            },
            {
                "action_type": "Detective",
                "description": "Monitor system activity regularly",
                "priority": "Medium"
            },
            {
                "action_type": "Corrective",
                "description": "Apply patches and fixes promptly",
                "priority": "High"
            }
        ]