from flask import Blueprint, request
from services.groq_client import generate_response

analyze_bp = Blueprint("analyze", __name__)

@analyze_bp.route("/analyze", methods=["POST"])
def analyze():
    data = request.json
    risks = data.get("risks", [])

    if not risks:
        return {"error": "Risks list required"}, 400

    risk_map = {
        "Low": 3,
        "Medium": 6,
        "High": 9
    }

    results = []
    total_score = 0

    for risk in risks:
        prompt = f"""
Classify the risk level for the following:

Risk: {risk}

Return ONLY one word:
Low, Medium, or High
"""

        result = generate_response(prompt).strip().capitalize()
        score = risk_map.get(result, 0)

        results.append({
            "risk": risk,
            "risk_level": result,
            "risk_score": score
        })

        total_score += score

    avg_score = total_score / len(risks)

    if avg_score >= 7:
        overall = "High"
    elif avg_score >= 4:
        overall = "Medium"
    else:
        overall = "Low"

    return {
        "results": results,
        "average_score": avg_score,
        "overall_risk": overall
    }