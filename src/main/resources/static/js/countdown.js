function getNextDrawDate() {
    const now = new Date();
    const currentDay = now.getDay();
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();

    let nextDraw = new Date();
    nextDraw.setHours(21, 30, 0, 0);

    if (currentDay === 2 || currentDay === 4) {
        // Tuesday or Thursday
        if (currentHour > 21 || (currentHour === 21 && currentMinute >= 30)) {
            // If past today's draw time move to the next draw
            nextDraw.setDate(now.getDate() + (currentDay === 2 ? 2 : 4)); // Skip to Thursday or next Tuesday
        }
    } else {
        // Calculate days to the next draw
        const daysUntilNextDraw =
            currentDay < 2 ? 2 - currentDay : // Before Tuesday
            currentDay < 4 ? 4 - currentDay : // Before Thursday
            9 - currentDay; // After Thursday, go to next Tuesday
        nextDraw.setDate(now.getDate() + daysUntilNextDraw);
    }

    return nextDraw;
}

const nextDrawDate = getNextDrawDate();
// Function to update the countdown timer
function updateCountdown() {
    const now = new Date();
    const timeDifference = nextDrawDate - now;

    if (timeDifference <= 0) {
        document.getElementById("countdown-timer").innerText = "The draw is happening now!";
        clearInterval(timerInterval);
        return;
    }

    const days = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
    const hours = Math.floor((timeDifference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((timeDifference % (1000 * 60)) / (1000 * 60));
    const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);

    document.getElementById("countdown-timer").innerText =
        `${days}d ${hours}h ${minutes}m ${seconds}s`;
}

const timerInterval = setInterval(updateCountdown, 1000);
updateCountdown();
