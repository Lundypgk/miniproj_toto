// Map of bet type to number of inputs
const betTypeToInputs = {
  "Ordinary": 6,
  "System 7": 7,
  "System 8": 8,
  "System 9": 9,
  "System 10": 10,
  "System 11": 11,
  "System 12": 12
};

// Get references
const betTypeSelect = document.getElementById("betTypeSelect");
const numberInputsContainer = document.getElementById("numberInputs");

// Function to generate number input fields
function generateNumberInputs(count) {
  // Clear existing inputs
  numberInputsContainer.innerHTML = "";

  // Create new inputs
  for (let i = 1; i <= count; i++) {
    const input = document.createElement("input");
    input.type = "number";
    input.min = "1";
    input.max = "49";
    input.name = `numbers`; // Use the same name for all inputs to send as a list
    input.required = true;
    input.className = "p-2 bg-gray-700 rounded text-center";
    input.placeholder = i;

    numberInputsContainer.appendChild(input);
  }
}

// Event listener for bet type selection
betTypeSelect.addEventListener("change", () => {
  const selectedBetType = betTypeSelect.value;
  const inputCount = betTypeToInputs[selectedBetType] || 6; // Default to 6 inputs
  generateNumberInputs(inputCount);
});

// Generate default inputs on page load
generateNumberInputs(betTypeToInputs[betTypeSelect.value] || 6);
