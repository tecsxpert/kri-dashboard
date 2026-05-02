from flask import Blueprint, request, jsonify
from services.ai_service import process_async, ai_results
import uuid

report_bp = Blueprint("report", __name__)

@report_bp.route("/generate-report", methods=["POST"])
def generate_report():

    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "Input required"}), 400

    text = data["text"]

    task_id = str(uuid.uuid4())

    process_async(task_id, text)

    return jsonify({
        "task_id": task_id,
        "status": "processing"
    })



@report_bp.route("/report-result/<task_id>", methods=["GET"])
def get_result(task_id):

    result = ai_results.get(task_id)

    if not result:
        return jsonify({
            "status": "processing"
        })

    return jsonify({
        "status": "completed",
        "result": result
    })