from flask import Blueprint, request
from services.groq_client import generate_response

dashboard_bp = Blueprint("dashboard", __name__)

@dashboard_bp.route("/dashboard", methods=["POST"])
def dashboard():
    risks = request.json.get("risks", [])
    if not risks:
        return {"error": "Risks required"}, 400

    risk_map = {"Low": 3, "Medium": 6, "High": 9}

    labels, scores = [], []

    for r in risks:
        level = generate_response(
            f"Classify risk level (Low/Medium/High): {r}"
        ).strip().capitalize()

        labels.append(r)
        scores.append(risk_map.get(level, 0))

    return {"labels": labels, "scores": scores}