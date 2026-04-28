from flask import Flask
from routes.describe import describe_bp
from routes.recommend import recommend_bp
from routes.categorize import categorize_bp
from routes.analyze import analyze_bp

app = Flask(__name__)

# Register all routes
app.register_blueprint(describe_bp)
app.register_blueprint(recommend_bp)
app.register_blueprint(categorize_bp)
app.register_blueprint(analyze_bp)

# Home route
@app.route("/")
def home():
    return {"message": "AI Service Running"}

# Health check
@app.route("/health")
def health():
    return {"status": "ok"}

# Debug: show all routes (optional)
print("Available routes:")
print(app.url_map)

if __name__ == "__main__":
    app.run(port=5000, debug=True)