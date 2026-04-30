from prompt_test import test_cases

def score_answer(answer):
    length = len(answer)

    if length > 800:
        return 10
    elif length > 500:
        return 9
    elif length > 300:
        return 8
    elif length > 150:
        return 7
    elif length > 80:
        return 5
    else:
        return 3


total = 0

print("\n===== AI ANSWER SCORING SYSTEM =====\n")

for item in test_cases:
    q = item["question"]
    a = item["answer"]

    score = score_answer(a)
    total += score

    print("========================")
    print(f"Q: {q}")
    print(f"A: {a}")
    print(f"Score: {score}\n")

print(f"AVERAGE SCORE: {total / len(test_cases):.2f}")
print("\n===== COMPLETED SUCCESSFULLY =====")