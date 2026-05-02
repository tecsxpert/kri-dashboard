from flask import Blueprint, request, jsonify
from services.groq_client import generate_response
from datetime import datetime

analyse_bp = Blueprint("analyse", __name__)

@analyse_bp.route("/analyse-document", methods=["POST"])
def analyse_document():

    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "Input text required"}), 400

    text = data["text"]

    prompt = f"""
    Analyze the following document and extract key insights and risks.

    Document:
    {text}

    Return:
    - insights (list)
    - risks (list)
    """

    ai_output = generate_response(prompt)

    findings = {
        "input": text,
        "findings": [
            {
                "type": "insight",
                "description": ai_output
            },
            {
                "type": "risk",
                "description": "Potential risks identified from the document"
            }
        ],
        "generated_at": datetime.utcnow().isoformat()
    }

    return jsonify(findings)