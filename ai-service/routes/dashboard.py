from flask import Blueprint, request
from services.groq_client import generate_response

dashboard_bp = Blueprint("dashboard", __name__)

@dashboard_bp.route("/dashboard", methods=["POST"])
def dashboard():
    data = request.json
    risks = data.get("risks", [])

    if not risks:
        return {"error": "Risks required"}, 400


    risk_map = {
        "Low": 3,
        "Medium": 6,
        "High": 9
    }

    labels = []
    scores = []

    for r in risks:
    
        response = generate_response(
         f"""
         Classify the risk into Low, Medium, or High based on severity.

         Examples:
         - Minor issue → Low
         -Moderate impact → Medium
         - Serious threats (fraud, cyber attack, data breach) → High

         Risk: {r}

         Answer ONLY one word: Low, Medium, or High.
         """
         )

        response = response.strip().lower()

        if "high" in response:
            level = "High"
        elif "medium" in response:
            level = "Medium"
        elif "low" in response:
            level = "Low"
        else:
            level = "Low"  # default fallback

        score = risk_map.get(level, 0)

        labels.append(r)
        scores.append(score)

    return {
        "labels": labels,
        "scores": scores
    }