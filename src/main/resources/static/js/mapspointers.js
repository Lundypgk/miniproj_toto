function initMap() {
  const singapore = { lat: 1.3521, lng: 103.8198 };
  const map = new google.maps.Map(document.getElementById('map'), {
    zoom: 12,
    center: singapore
  });

  const rows = document.querySelectorAll('#locations-table tr');
  let activeRow = null;

  rows.forEach(row => {
    const lat = parseFloat(row.getAttribute('data-lat'));
    const lon = parseFloat(row.getAttribute('data-lon'));
    const locationName = row.children[0].innerText;

    if (!isNaN(lat) && !isNaN(lon)) {
      const marker = new google.maps.Marker({
        position: { lat: lat, lng: lon },
        map: map,
        title: locationName
      });

      row.addEventListener('click', () => {
        map.setCenter({ lat: lat, lng: lon });
        map.setZoom(15);

        if (activeRow) {
          activeRow.style.backgroundColor = "";
        }
        row.style.backgroundColor = "#4A5568";
        activeRow = row;

        if (activeMarker) {
          activeMarker.setIcon("http://maps.google.com/mapfiles/ms/icons/red-dot.png");
        }
        marker.setIcon("http://maps.google.com/mapfiles/ms/icons/blue-dot.png");
        activeMarker = marker;
      });
    }
  });
}
