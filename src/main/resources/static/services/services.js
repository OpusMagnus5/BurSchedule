import {servicesUrl} from "../util/config.js";

var servicesData;

function getServices() {
  fetch(servicesUrl, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  })
    .then((response) => {
      if (!response.ok) {
        // Obsługa błędu, jeśli to konieczne
        throw new Error("Network response was not ok", response);
      }
      return response.json();
    })
    .then((data) => {
      servicesData = data;
    })
    .catch((error) => {
      // Tutaj możesz obsłużyć błędy
      console.error("There was a problem with the fetch operation from " + servicesUrl, error);
    });
}

// Wywołanie funkcji getServices() w odpowiednim momencie, na przykład po załadowaniu strony
document.addEventListener("DOMContentLoaded", getServices);
