import re
from flask import request, jsonify

def strip_html(text):
    return re.sub(r'<.*?>', '', text)

def detect_prompt_injection(text):
    patterns = [
        "ignore previous instructions",
        "bypass",
        "override",
        "system prompt",
        "act as"
    ]
    for pattern in patterns:
        if pattern.lower() in text.lower():
            return True
    return False

def sanitize_input():
    if request.method == "POST":
        data = request.get_json()

        if not data:
            return jsonify({"error": "Invalid or empty input"}), 400

        for key, value in data.items():
            if isinstance(value, str):

                # Remove HTML
                clean_value = strip_html(value)

                # Detect prompt injection
                if detect_prompt_injection(clean_value):
                    return jsonify({
                        "error": "Prompt injection detected"
                    }), 400

                data[key] = clean_value