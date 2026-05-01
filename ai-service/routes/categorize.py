from flask import Blueprint, request
from services.groq_client import generate_response

categorize_bp = Blueprint("categorize", __name__)

@categorize_bp.route("/categorize", methods=["POST"])
def categorize():
    data = request.json
    user_input = data.get("text", "")

    if not user_input:
        return {"error": "Input required"}, 400

    prompt = f"""
Classify the following text into ONLY ONE category.

Choose from:
Cybersecurity, Financial, Operational, Compliance

Rules:
- Return ONLY the category name
- No explanation
- No extra text

Text: {user_input}
"""

    result = generate_response(prompt)

    return {
        "input": user_input,
        "category": result.strip()
    } 