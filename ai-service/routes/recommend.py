from flask import Blueprint, request, jsonify
from services.groq_client import generate_response

recommend_bp = Blueprint("recommend", __name__)

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "text required"}), 400

    text = data["text"]

    result = generate_response(text)

    return jsonify({
        "input": text,
        "recommendations": result
    })