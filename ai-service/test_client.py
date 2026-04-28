from services.groq_client import GroqClient

client = GroqClient()

result = client.generate("Explain machine learning in simple words")

print(result)