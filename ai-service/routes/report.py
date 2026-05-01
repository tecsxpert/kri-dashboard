from flask import Blueprint, request, jsonify
from datetime import datetime

report_bp = Blueprint("report", __name__)

@report_bp.route("/generate-report", methods=["POST"])
def generate_report():
    data = request.get_json()


    if not data or "text" not in data:
        return jsonify({"error": "Input text required"}), 400

    user_input = data["text"]


    report = {
        "title": f"Risk Report: {user_input}",

        "executive_summary": f"This report provides a high-level summary of {user_input} and its potential impact on the organization.",

        "overview": f"{user_input} is a critical area that requires attention. It may affect business operations, security, and compliance if not properly managed.",

        "top_items": [
            f"Key concern related to {user_input}",
            f"Impact assessment of {user_input}",
            f"Existing controls for {user_input}"
        ],

        "recommendations": [
            {
                "action": "Implement preventive controls",
                "priority": "High"
            },
            {
                "action": "Monitor risks continuously",
                "priority": "Medium"
            },
            {
                "action": "Apply corrective measures",
                "priority": "High"
            }
        ],

        "generated_at": datetime.utcnow().isoformat()
    }

    return jsonify(report)