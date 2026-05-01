import chromadb
from sentence_transformers import SentenceTransformer

model = SentenceTransformer("all-MiniLM-L6-v2")


client = chromadb.Client()


collection = client.get_or_create_collection(name="kri_collection")



documents = [
    "Cybersecurity risk involves unauthorized system access and data breaches.",
    "Financial risk includes market volatility and credit risk exposure.",
    "Operational risk arises from failures in internal processes.",
    "Compliance risk relates to legal, regulatory, and policy violations."
]



def chunk_text(text, chunk_size=500, overlap=50):
    chunks = []
    start = 0

    while start < len(text):
        end = start + chunk_size
        chunks.append(text[start:end])
        start += chunk_size - overlap

    return chunks



def store_documents():
    all_chunks = []
    all_ids = []

    for i, doc in enumerate(documents):
        chunks = chunk_text(doc)

        for j, chunk in enumerate(chunks):
            all_chunks.append(chunk)
            all_ids.append(f"doc_{i}_chunk_{j}")

    
    embeddings = model.encode(all_chunks).tolist()


    collection.add(
        documents=all_chunks,
        embeddings=embeddings,
        ids=all_ids
    )


def retrieve(query, top_k=2):
    query_embedding = model.encode([query]).tolist()

    results = collection.query(
        query_embeddings=query_embedding,
        n_results=top_k
    )

    return results["documents"][0]