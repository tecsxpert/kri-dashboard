from groq import Groq
from dotenv import load_dotenv
import os

# Load .env file
load_dotenv()

# Groq client
client = Groq(api_key=os.getenv("GROQ_API_KEY"))

# 🔥 BASE PROMPT (you will improve this in Day 6)
BASE_PROMPT = """
You are a senior AI engineer assistant.

Rules:
- Always give accurate technical answers
- If you are unsure, say "I don't have enough data"
- Never skip questions
- Always explain in simple + structured format
- Add examples when possible
- Keep answers clear and complete but not too long
"""

# 🧪 Test inputs (10 questions)
inputs = [
    "What is Python used for in AI?",
    "Explain machine learning simply",
    "What is deep learning?",
    "Difference between AI and ML",
    "What is FastAPI?",
    "What is ChromaDB?",
    "What is RAG system?",
    "Why do we use embeddings?",
    "What is Groq LLM?",
    "Explain vector database"
]

# 📊 Simple scoring function
def score_response(text):
    words = len(text.split())

    if words < 20:
        return 5
    elif words < 50:
        return 7
    else:
        return 9

# 🚀 Run tests
for q in inputs:
    response = client.chat.completions.create(
        model="llama-3.1-8b-instant",
        messages=[
            {"role": "system", "content": BASE_PROMPT},
            {"role": "user", "content": q}
        ]
    )

    answer = response.choices[0].message.content
    score = score_response(answer)

    print("\n========================")
    print("Q:", q)
    print("A:", answer)
    print("Score:", score)

    test_cases = [
    {
        "question": "What is Python used for in AI?",
        "answer": "Python is widely used in AI for machine learning, deep learning, NLP, and computer vision because of libraries like TensorFlow, PyTorch, and scikit-learn."
    },
    {
        "question": "Explain machine learning simply",
        "answer": "Machine learning is a type of AI where computers learn patterns from data and make predictions without being explicitly programmed."
    },
    {
        "question": "What is deep learning?",
        "answer": "Deep learning is a subset of machine learning that uses neural networks with many layers to learn complex patterns from data."
    },
    {
        "question": "Difference between AI and ML",
        "answer": "AI is the broader field of making machines intelligent, while ML is a subset of AI that focuses on learning from data."
    },
    {
        "question": "What is FastAPI?",
        "answer": "FastAPI is a modern Python web framework used to build fast and high-performance APIs using async support and type hints."
    },
    {
        "question": "What is ChromaDB?",
        "answer": "ChromaDB is a vector database used to store embeddings and perform similarity search for AI applications."
    },
    {
        "question": "What is RAG system?",
        "answer": "RAG (Retrieval Augmented Generation) is an AI system that combines retrieval from documents with LLM generation to give better answers."
    },
    {
        "question": "Why do we use embeddings?",
        "answer": "Embeddings convert text into numerical vectors so that machines can understand meaning and similarity between words."
    },
    {
        "question": "What is Groq LLM?",
        "answer": "Groq LLM is a high-performance AI inference system designed for running large language models with very low latency."
    },
    {
        "question": "Explain vector database",
        "answer": "A vector database stores embeddings and enables fast similarity search for AI applications like recommendation and search systems."
    }
]