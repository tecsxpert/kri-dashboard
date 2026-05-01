from services.groq_client import GroqClient

client = GroqClient()

response = client.generate("Explain AI in simple words")

print(response)