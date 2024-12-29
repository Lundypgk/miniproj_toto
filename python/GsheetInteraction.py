from flask import Flask, jsonify, request
import gspread
import pandas as pd
from itertools import combinations
from oauth2client.service_account import ServiceAccountCredentials

# Initialize Flask app
app = Flask(__name__)

scope = [
    "https://spreadsheets.google.com/feeds",
    "https://www.googleapis.com/auth/drive",
]
creds = ServiceAccountCredentials.from_json_keyfile_name("creds.json", scope)
client = gspread.authorize(creds)

url = "https://docs.google.com/spreadsheets/d/1WaE7LIzUZgRfqkXwnxQUS52UoRbW-OOQDWQS61pFWIk/edit?gid=0#gid=0"
sheet = client.open_by_url(url)
worksheet = sheet.sheet1
data = worksheet.get_all_records()
df = pd.DataFrame(data)
clean_data = df.to_dict(orient="records")

worksheet = sheet.get_worksheet(1)
data2 = worksheet.get_all_records()
location_df = pd.DataFrame(data2)
location_df = location_df.to_dict(orient="records")


# Select the columns that contain winning numbers
winning_columns = [
    "Winning Number 1",
    "Winning Number 2",
    "Winning Number 3",
    "Winning Number 4",
    "Winning Number 5",
    "Winning Number 6",
]

# Create DataFrame
df = pd.DataFrame(data)

pairing_df = pd.DataFrame(index=range(1, 50), columns=range(1, 50)).fillna(0)

for index, row in df.iterrows():
    winning_numbers = row[
        [
            "Winning Number 1",
            "Winning Number 2",
            "Winning Number 3",
            "Winning Number 4",
            "Winning Number 5",
            "Winning Number 6",
        ]
    ].values

    for num1, num2 in combinations(winning_numbers, 2):
        pairing_df.loc[num1, num2] += 1
        pairing_df.loc[num2, num1] += 1  # Ensure symmetry

pairing_df = pairing_df.astype(int)


def recommend_numbers(user_numbers):
    recommendations = {}

    for num in user_numbers:
        paired_frequencies = pairing_df.loc[num]
        for other_num in paired_frequencies.index:
            if other_num != num and paired_frequencies[other_num] > 0:
                recommendations[other_num] = (
                    recommendations.get(other_num, 0) + paired_frequencies[other_num]
                )
    recommended_pairs = sorted(
        recommendations.items(), key=lambda x: x[1], reverse=True
    )
    return [(int(num), int(freq)) for num, freq in recommended_pairs[0:3]]


@app.route("/get-recommendation", methods=["POST"])
def get_recommendation():
    data = request.get_json()  # Ensure the client sends JSON data
    user_input = data.get("user_input")  # Expecting a list of integers
    if not isinstance(user_input, list) or not all(
        isinstance(i, int) for i in user_input
    ):
        return jsonify({"error": "user_input must be a list of integers"}), 400
    recommendation = recommend_numbers(user_input)
    return jsonify({"recommendation": recommendation})


@app.route("/get-data", methods=["GET"])
def get_data():
    return jsonify(clean_data)


@app.route("/get-outlet", methods=["GET"])
def get_location_data():
    return jsonify(location_df)


@app.route("/get-num-freq", methods=["GET"])
def get_numFreq():
    frequency_series = (
        df[winning_columns].stack().value_counts().reindex(range(1, 50), fill_value=0)
    )
    frequency_df = frequency_series.reset_index()
    frequency_df.columns = ["Number", "Winning Frequency"]
    supplementary_frequency_series = (
        pd.Series(df["Supplementary Number"])
        .value_counts()
        .reindex(range(1, 50), fill_value=0)
    )
    frequency_df["Supplementary Frequency"] = supplementary_frequency_series.values
    total_draws = len(df)
    frequency_df["Percentage"] = (frequency_df["Winning Frequency"] / total_draws) * 100
    frequnecy_df_clean = frequency_df.to_dict(orient="records")
    return jsonify(frequnecy_df_clean)


@app.route("/get-ball-freq", methods=["GET"])
def get_ballFreq():
    slot_counts = {
        "Winning Number 1": {},
        "Winning Number 2": {},
        "Winning Number 3": {},
        "Winning Number 4": {},
        "Winning Number 5": {},
        "Winning Number 6": {},
    }
    for index, row in df.iterrows():
        for i in range(1, 7):  # Slots 1 to 6
            winning_number = row[f"Winning Number {i}"]
            if winning_number not in slot_counts[f"Winning Number {i}"]:
                slot_counts[f"Winning Number {i}"][winning_number] = 0
            slot_counts[f"Winning Number {i}"][winning_number] += 1
    result = []
    for slot, counts in slot_counts.items():
        formatted_counts = {str(number): count for number, count in counts.items()}
        result.append({slot: formatted_counts})

    return jsonify(result)


@app.route("/get-pair-freq", methods=["GET"])
def get_pairFreq():
    pairing_df = {str(i): {str(j): 0 for j in range(1, 50)} for i in range(1, 50)}
    for index, row in df.iterrows():
        winning_numbers = row[winning_columns].values
        for i in range(len(winning_numbers)):
            for j in range(i + 1, len(winning_numbers)):
                num1 = str(winning_numbers[i])
                num2 = str(winning_numbers[j])
                pairing_df[num1][num2] += 1
                pairing_df[num2][num1] += 1  # Ensure symmetry

    sorted_pairing_df = {
        int(outer_key): dict(
            sorted(
                {
                    int(inner_key): value for inner_key, value in inner_dict.items()
                }.items()
            )
        )
        for outer_key, inner_dict in pairing_df.items()
    }

    return jsonify(sorted_pairing_df)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5699)
