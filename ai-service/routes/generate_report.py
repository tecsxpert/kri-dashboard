from flask import Blueprint, request, jsonify, Response
from services.groq_client import generate_response
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

    if result is None:
        return jsonify({"status": "processing"})

    return jsonify({
        "status": "completed",
        "result": result
    })


@report_bp.route("/generate-report-stream", methods=["GET"])
def generate_report_stream():
    text = request.args.get("text")

    if not text:
        return {"error": "text required"}, 400

    def stream():
        try:
            full_response = generate_response(text)

            if not full_response:
                yield "data: AI error\n\n"
                return

            for word in full_response.split():
                yield f"data: {word} \n\n"

        except Exception as e:
            yield f"data: Error: {str(e)}\n\n"

    return Response(stream(), mimetype="text/event-stream")