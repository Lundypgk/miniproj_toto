
// Grab the data from the global variable
const rawData = window.ballFrequencyData || [];
// Transform the data for Google Charts
let distributionMap = {};
// Loop through each slot in rawData
for (let slotIndex = 0; slotIndex < rawData.length; slotIndex++) {
  let slotKey = Object.keys(rawData[slotIndex])[0];
  let slotData = rawData[slotIndex][slotKey];
  for (let numberStr in slotData) {
    let count = slotData[numberStr];
    let number = parseInt(numberStr, 10);
    if (!distributionMap[number]) {
      distributionMap[number] = new Array(rawData.length).fill(0);
    }
    distributionMap[number][slotIndex] = count;
  }
}
let allNumbers = Object.keys(distributionMap)
  .map(n => parseInt(n, 10))
  .sort((a, b) => a - b);

// Build chart data array
let chartData = [];
chartData.push([
  "Number",
  "Slot 1",
  "Slot 2",
  "Slot 3",
  "Slot 4",
  "Slot 5",
  "Slot 6",
  "Total"
]);

for (let num of allNumbers) {
  let counts = distributionMap[num];
  let total = counts.reduce((acc, val) => acc + val, 0);
  chartData.push([num, ...counts, total]);
}

// Load and draw chart
google.charts.load("current", { packages: ["corechart"] });
google.charts.setOnLoadCallback(drawComboChart);

function drawComboChart() {
  const data = google.visualization.arrayToDataTable(chartData);

  const options = {
    title: "Ball Frequency Distribution (Combo Chart)",
    backgroundColor: "#2d3748",
    hAxis: { textStyle: { color: "#fff" } },
    vAxis: { textStyle: { color: "#fff" } },
    legend: { textStyle: { color: "#fff" } },
    seriesType: "bars",
    series: {
      6: {
        type: "line",
        lineWidth: 2,
        color: "#e53e3e"
      }
    }
  };

  const chart = new google.visualization.ComboChart(document.getElementById("combo_chart"));
  chart.draw(data, options);
}
