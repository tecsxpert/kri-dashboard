from flask import Blueprint, request
from services.groq_client import generate_response

dashboard_bp = Blueprint("dashboard", __name__)

@dashboard_bp.route("/dashboard", methods=["POST"])
def dashboard():
    data = request.json
    risks = data.get("risks", [])

    if not risks:
        return {"error": "Risks required"}, 400

    risk_map = {"Low": 3, "Medium": 6, "High": 9}

    results = []

    for r in risks:
        
        response = generate_response(f"""
Classify the risk into Low, Medium, or High based on severity.

Risk: {r}

Answer only one word: Low, Medium, or High.
""")

        response = response.strip().lower()

        if "high" in response:
            level = "High"
        elif "medium" in response:
            level = "Medium"
        else:
            level = "Low"

        score = risk_map[level]

        
        category = generate_response(f"""
Classify this risk into one category:
Cybersecurity, Financial, Operational

Risk: {r}

Answer only one word.
""").strip()

        # 🔹 Recommendation
        recommendation = generate_response(f"""
Give one short recommendation to reduce this risk:

Risk: {r}
""").strip()

        results.append({
            "risk": r,
            "level": level,
            "score": score,
            "category": category,
            "recommendation": recommendation
        })

    return {"results": results}