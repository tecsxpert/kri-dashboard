from flask import Flask, request, jsonify
from sanitizer import sanitize_input

app = Flask(__name__)

# Connect middleware
app.before_request(sanitize_input)

# Test API
@app.route('/test', methods=['POST'])
def test():
    data = request.get_json()
    return jsonify({
        "message": "Input received successfully",
        "data": data
    })

if __name__ == '__main__':
    app.run(debug=True)