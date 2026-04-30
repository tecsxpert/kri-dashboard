import chromadb

# Persistent DB (stores data permanently)
client = chromadb.PersistentClient(path="./chroma_db")

collection = client.get_or_create_collection(name="categories")

# Seed data (training examples)
def seed_data():
    existing = collection.get()
    if len(existing["ids"]) == 0:
        collection.add(
            documents=[
                "I love playing cricket",
                "Python and AI development",
                "Football match today",
                "FastAPI backend development",
                "Machine learning project"
            ],
            ids=["1", "2", "3", "4", "5"]
        )

def query_category(text):
    result = collection.query(
        query_texts=[text],
        n_results=1
    )

    return result