import json

# # Path to coverage.txt
coverage_path = "coverage.txt"
# Path to store event-signature ground truth
signature_path = "event_signature.json"

data = {}

with open(signature_path, "r", encoding='utf-8-sig') as f:
    signature_data = json.load(f)

with open(coverage_path, 'r') as f:
    for line in f:
        if line.strip().endswith('/'):
            key = line.strip()
            if key not in data.keys():
                data[key] = []
        else:
            data[key].append(line.strip())

for key in data:
    for k in signature_data:
        if signature_data[k] == data[key]:
            print("Event " + k + " has been executed!")
