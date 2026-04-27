from flask import Blueprint, request
from services.groq_client import generate_response

describe_bp = Blueprint("describe", __name__)

@describe_bp.route("/describe", methods=["POST"])
def describe():
    data = request.json
    user_input = data.get("text", "")

    if not user_input:
        return {"error": "Input required"}, 400

    prompt = f"Generate a professional description for: {user_input}"

    result = generate_response(prompt)

    return {
        "input": user_input,
        "description": result
    }