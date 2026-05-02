from flask import Flask
from flask_cors import CORS
from routes.generate_report import report_bp

app = Flask(__name__)
CORS(app)

app.register_blueprint(report_bp)

if __name__ == "__main__":
    app.run(debug=True, port=5555)