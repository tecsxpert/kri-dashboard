from flask import Flask
from flask_cors import CORS

from routes.describe import describe_bp
from routes.recommend import recommend_bp
from routes.generate_report import report_bp
from routes.analyse_document import analyse_bp
from routes.batch_process import batch_bp
from routes.rag import rag_bp

try:
    from services.embedding_service import load_model
except:
    def load_model():
        print(" Embedding model not found, skipping preload")


app = Flask(__name__)
CORS(app)


app.register_blueprint(describe_bp)
app.register_blueprint(recommend_bp)
app.register_blueprint(report_bp)
app.register_blueprint(analyse_bp)
app.register_blueprint(batch_bp)
app.register_blueprint(rag_bp)

@app.route("/")
def home():
    return {
        "status": "running",
        "message": "AI Service is up "
    }


if __name__ == "__main__":
    load_model()  # preload model (optional)
    print("Starting Flask server...")
    app.run(host="127.0.0.1", port=5555, debug=True)