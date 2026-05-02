import os
print("RUNNING FROM:", os.getcwd())
from flask import Flask, render_template

from routes.describe import describe_bp
from routes.recommend import recommend_bp
from routes.categorize import categorize_bp
from routes.analyze import analyze_bp
from routes.dashboard import dashboard_bp
from routes.rag import rag_bp
from routes.report import report_bp
from routes.generate_report import report_bp

app = Flask(__name__)

app.register_blueprint(describe_bp)
app.register_blueprint(recommend_bp)
app.register_blueprint(categorize_bp)
app.register_blueprint(analyze_bp)
app.register_blueprint(dashboard_bp)
app.register_blueprint(rag_bp)
app.register_blueprint(report_bp)

@app.route("/")
def home():
    return render_template("index.html")

@app.route("/health")
def health():
    return {"status": "ok"}

if __name__ == "__main__":
    app.run(debug=True, port=5555)