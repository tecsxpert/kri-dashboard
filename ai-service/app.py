from flask import Flask
from flask_cors import CORS

from routes.generate_report import report_bp
from routes.analyse_document import analyse_bp

app = Flask(__name__)
CORS(app)

app.register_blueprint(report_bp)
app.register_blueprint(analyse_bp)

if __name__ == "__main__":
    app.run(debug=True, port=5555)