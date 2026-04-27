from flask import Flask
from routes.describe import describe_bp

app = Flask(__name__)

# register route
app.register_blueprint(describe_bp)

@app.route("/")
def home():
    return {"message": "AI Service Running"}

@app.route("/health")
def health():
    return {"status": "ok"}

if __name__ == "__main__":
    app.run(port=5000, debug=True)