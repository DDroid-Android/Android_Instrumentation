import json

# Path to coverage.txt
coverage_path = "coverage.txt"
# Path to store event-signature ground truth
signature_path = "event_signature.json"

data = {}
signature = {}
index = 1

with open(coverage_path, 'r') as f:
    for line in f:
        if line.strip().endswith('/'):
            key = line.strip()
            if key not in data.keys():
                data[key] = []
        else:
            data[key].append(line.strip())

for key in data:
    signature[index] = data[key]
    index += 1

with open(signature_path, 'w') as f:
    json.dump(signature, f)


