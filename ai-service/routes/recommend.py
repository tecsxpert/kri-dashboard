from flask import Blueprint, request
from services.groq_client import generate_response

recommend_bp = Blueprint("recommend", __name__)

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.json
    user_input = data.get("text", "")

    if not user_input:
        return {"error": "Input required"}, 400

    prompt = f"""
Give EXACTLY 3 recommendations for the topic below.

Rules:
- Return only bullet points
- Each point max 1 line
- No explanation
- No extra text

Topic: {user_input}
"""

    result = generate_response(prompt)

    return {
        "input": user_input,
        "recommendations": result
    }