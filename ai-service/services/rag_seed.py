import chromadb
from chromadb.utils import embedding_functions

client = chromadb.PersistentClient(path="./chroma_db")


embedding_function = embedding_functions.SentenceTransformerEmbeddingFunction(
    model_name="all-MiniLM-L6-v2"
)

# Reset collection
try:
    client.delete_collection("kri_docs")
except:
    pass

collection = client.get_or_create_collection(
    name="kri_docs",
    embedding_function=embedding_function
)

documents = [
    "Cybersecurity risk refers to threats like hacking and malware attacks.",
    "Data breach risk involves unauthorized access to sensitive data.",
    "Operational risk arises from failures in systems or human errors.",
    "Financial risk includes market fluctuations and credit risks.",
    "Compliance risk relates to violation of laws and regulations.",
    "Reputational risk affects brand image and trust.",
    "Third-party risk comes from external vendors.",
    "Cloud security risk involves misconfigured cloud services.",
    "Network security risk includes unauthorized access and attacks.",
    "AI risk includes bias and incorrect predictions."
]

ids = [f"id_{i}" for i in range(len(documents))]

collection.add(documents=documents, ids=ids)

print("Docs count:", collection.count())