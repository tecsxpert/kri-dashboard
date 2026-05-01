import chromadb

# Create persistent storage (saves data in folder)
client = chromadb.PersistentClient(path="./chroma_db")

# Create collection
collection = client.get_or_create_collection(name="ai_test")

# Add sample data
collection.add(
    documents=[
        "I love playing cricket",
        "Python is used for AI development",
        "FastAPI is a Python web framework"
    ],
    ids=["1", "2", "3"]
)

# Query test (semantic search)
result = collection.query(
    query_texts=["I am learning Python for AI"],
    n_results=2
)

print(result)