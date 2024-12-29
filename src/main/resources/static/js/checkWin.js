document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("checkTotoForm");

    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent default form submission

        // Collect user input
        const numberInputs = Array.from(document.querySelectorAll("input[type='number']"));
        const numbers = numberInputs.map((input) => parseInt(input.value, 10));
        const drawDate = document.getElementById("drawDate").value;

        console.log(numbers);

        // Validate inputs
        if (numbers.some((num) => isNaN(num) || num < 1 || num > 49)) {
            alert("Please enter valid numbers between 1 and 49.");
            return;
        }

        if (!drawDate) {
            alert("Please select a draw date.");
            return;
        }

        // Perform the check (example: send data to the server)
        fetch(`/check-win`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                numbers: numbers,
                drawDate: drawDate,
            }),
        })
            .then((response) => response.json())
            .then((data) => {
                // Display the prize group
                alert(data.prizeGroup);
            })
            .catch((error) => {
                console.error("Error checking TOTO results:", error);
                alert("An error occurred while checking the results.");
            });
    });
});
