import sys
import os
sys.path.append(os.path.abspath("ai-service"))

import pytest
from app import app
from unittest.mock import patch


@pytest.fixture
def client():
    app.testing = True
    return app.test_client()


def test_generate_report_success(client):
    response = client.post("/generate-report", json={"text": "test"})
    assert response.status_code == 200
    data = response.get_json()
    assert "task_id" in data
    assert data["status"] == "processing"



def test_generate_report_no_input(client):
    response = client.post("/generate-report", json={})
    assert response.status_code == 400


def test_report_result_processing(client):
    response = client.get("/report-result/fake-id")
    assert response.status_code == 200
    assert response.get_json()["status"] == "processing"



def test_report_result_completed(client):
    from services.ai_service import ai_results

    ai_results["123"] = "done"

    response = client.get("/report-result/123")
    data = response.get_json()

    assert data["status"] == "completed"
    assert data["result"] == "done"



@patch("services.groq_client.generate_response")
def test_analyse_document_success(mock_groq, client):
    mock_groq.return_value = "Mocked AI response"

    response = client.post(
        "/analyse-document",
        json={"text": "Cybersecurity risk"}
    )

    data = response.get_json()

    assert response.status_code == 200
    assert "findings" in data
    assert len(data["findings"]) > 0



def test_analyse_document_no_input(client):
    response = client.post("/analyse-document", json={})
    assert response.status_code == 400


@patch("services.groq_client.generate_response")
def test_analyse_document_ai_failure(mock_groq, client):
    mock_groq.return_value = None

    response = client.post(
        "/analyse-document",
        json={"text": "test"}
    )

    data = response.get_json()

    assert response.status_code == 200
    assert "findings" in data



@patch("routes.generate_report.generate_response")
def test_streaming_success(mock_groq, client):
    mock_groq.return_value = "hello world"

    response = client.get(
        "/generate-report-stream?text=test",
        buffered=True
    )

    assert response.status_code == 200
    assert b"hello" in response.data


def test_streaming_no_input(client):
    response = client.get("/generate-report-stream")
    assert response.status_code == 400


@patch("services.groq_client.generate_response")
def test_generate_response_exception(mock_groq, client):
    mock_groq.side_effect = Exception("AI error")

    response = client.post(
        "/analyse-document",
        json={"text": "test"}
    )

    data = response.get_json()

    assert response.status_code == 200
    assert "findings" in data