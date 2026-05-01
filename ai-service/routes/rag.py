from flask import Blueprint, request, jsonify
from services.rag_service import store_documents, retrieve

rag_bp = Blueprint("rag", __name__)


store_documents()

@rag_bp.route("/rag", methods=["POST"])
def rag_query():
    data = request.get_json()


    if not data or "query" not in data:
        return jsonify({"error": "Query required"}), 400

    query = data["query"]

    
    docs = retrieve(query)

    return jsonify({
        "query": query,
        "relevant_docs": docs
    })