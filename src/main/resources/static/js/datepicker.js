document.addEventListener('DOMContentLoaded', function () {
    const drawDateSelect = document.getElementById('draw-date');

    if (drawDateSelect) {
        drawDateSelect.addEventListener('change', function () {
            const selectedDate = this.value;

            // Log the selected date to confirm the event is detected
            console.log(`Selected draw date: ${selectedDate}`);

            // Fetch data for the selected date
            fetch(`/data/${selectedDate}`)
                .then(response => response.json())
                .then(data => {
                    if (data) {
                        // Update the date
                        const dateElement = document.getElementById('date');
                        if (dateElement) {
                            dateElement.textContent = data['Date'];
                        }

                        // Update the winning numbers
                        const winningNumbers = [
                            data['Winning Number 1'],
                            data['Winning Number 2'],
                            data['Winning Number 3'],
                            data['Winning Number 4'],
                            data['Winning Number 5'],
                            data['Winning Number 6']
                        ];
                        const winningNumbersDiv = document.getElementById('winning-numbers');
                        if (winningNumbersDiv) {
                            winningNumbersDiv.innerHTML = '';
                            winningNumbers.forEach(number => {
                                const div = document.createElement('div');
                                div.className = 'bg-gray-700 p-4 rounded text-center';
                                div.textContent = number;
                                winningNumbersDiv.appendChild(div);
                            });
                        }

                        // Update the supplementary number
                        const supplementaryNumberElement = document.getElementById('supplementary-number');
                        if (supplementaryNumberElement) {
                            supplementaryNumberElement.textContent = data['Supplementary Number'];
                        }
                    }
                })
                .catch(error => console.error('Error fetching data:', error));
        });
    } else {
        console.error("Dropdown with id 'draw-date' not found in the DOM.");
    }
});
