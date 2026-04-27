from flask import Blueprint, request
from services.groq_client import generate_response

recommend_bp = Blueprint("recommend", __name__)

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.json
    user_input = data.get("text", "")

    if not user_input:
        return {"error": "Input required"}, 400

    prompt = f"Give 3 recommendations for: {user_input}"

    result = generate_response(prompt)

    return {
        "input": user_input,
        "recommendations": result
    }