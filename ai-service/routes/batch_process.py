from flask import Blueprint, request, jsonify
from services.groq_client import generate_response
import time

batch_bp = Blueprint("batch", __name__)

@batch_bp.route("/batch-process", methods=["POST"])
def batch_process():

    data = request.get_json()

    if not data or "items" not in data:
        return jsonify({"error": "items array required"}), 400

    items = data["items"]

    if not isinstance(items, list):
        return jsonify({"error": "items must be a list"}), 400

    if len(items) == 0:
        return jsonify({"error": "items list cannot be empty"}), 400

    if len(items) > 20:
        return jsonify({"error": "max 20 items allowed"}), 400

    results = []

    for item in items:
        try:
            response = generate_response(item)

            if not response:
                response = "AI service error"

            results.append({
                "input": item,
                "output": response
            })

            # 100ms delay
            time.sleep(0.1)

        except Exception as e:
            results.append({
                "input": item,
                "output": f"Error: {str(e)}"
            })

    return jsonify({
        "count": len(results),
        "results": results
    })