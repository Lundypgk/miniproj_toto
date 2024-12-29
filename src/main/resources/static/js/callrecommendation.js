document.getElementById('recommendBtn').addEventListener('click', async () => {
    const selectedNumbers = [];
    document.querySelectorAll('input[type="checkbox"]:checked').forEach(input => {
        selectedNumbers.push(parseInt(input.value, 10));
    });

    const requestBody = { user_input: selectedNumbers };
    // console.log("1");
    try {
        const response = await fetch('/get-recommendation', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody),
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const recommendations = await response.json();
        console.log('Recommendations:', recommendations);
        const recommendationSection = document.getElementById('recommendation-section');
        const recommendationList = document.getElementById('recommendation-list');
        recommendationList.innerHTML = ''; // Clear previous recommendations
        recommendations.forEach(rec => {
            const listItem = document.createElement('li');
            listItem.textContent = `Number: ${rec.number}, Strength: ${rec.strength}`;
            recommendationList.appendChild(listItem);
        });

        recommendationSection.classList.remove('hidden');
    } catch (error) {
        console.error('Error fetching recommendations:', error);
        alert('Failed to fetch recommendations. Please try again.');
    }
});
