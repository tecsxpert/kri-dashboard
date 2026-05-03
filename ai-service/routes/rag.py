from flask import Blueprint, request, jsonify
import chromadb

rag_bp = Blueprint("rag", __name__)


client = chromadb.Client()


collection = client.get_or_create_collection("rag_collection")

collection.add(
    documents=["Cybersecurity risk includes phishing, malware, and data breaches"],
    ids=["1"]
)

@rag_bp.route("/rag", methods=["POST"])
def rag_query():
    data = request.get_json()

    if not data or "query" not in data:
        return jsonify({"error": "query required"}), 400

    query = data["query"]

    results = collection.query(
        query_texts=[query],
        n_results=2
    )

    return jsonify(results)