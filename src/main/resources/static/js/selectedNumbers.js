const checkboxes = document.querySelectorAll('input[type="checkbox"][name="numbers"]');
const selectedNumbersEl = document.getElementById('selectedNumbers');
const recommendBtn = document.getElementById('recommendBtn');

checkboxes.forEach(checkbox => {
  checkbox.addEventListener('change', function () {
    toggleBoxColor(checkbox);
    updateSelectedNumbers();
  });
});

function toggleBoxColor(checkbox) {
  const label = checkbox.parentElement;
  if (checkbox.checked) {
    label.classList.add('bg-gray-700');
    label.classList.remove('bg-gray-800');
  } else {
    label.classList.add('bg-gray-800');
    label.classList.remove('bg-gray-700');
  }
}

function updateSelectedNumbers() {
  const checkedValues = [];
  checkboxes.forEach(cb => {
    if (cb.checked) {
      checkedValues.push(cb.value);
    }
  });
  selectedNumbersEl.textContent = checkedValues.join(', ');
}
