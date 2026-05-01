from flask import Flask

# Import all route blueprints
from routes.describe import describe_bp
from routes.recommend import recommend_bp
from routes.categorize import categorize_bp
from routes.analyze import analyze_bp
from routes.dashboard import dashboard_bp

app = Flask(__name__)

# Register all routes
app.register_blueprint(describe_bp)
app.register_blueprint(recommend_bp)
app.register_blueprint(categorize_bp)
app.register_blueprint(analyze_bp)
app.register_blueprint(dashboard_bp)


# Home route
@app.route("/")
def home():
    return {"message": "KRI AI Service Running 🚀"}


# Health check route
@app.route("/health")
def health():
    return {"status": "ok"}


# Debug: print all routes
print("Available routes:")
print(app.url_map)


# Run app
if __name__ == "__main__":
    app.run(debug=True)