const baseUrl = "http://localhost:8080/app/";
const services = getServices();

function getServices() {
  fetch(baseUrl + "services", {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  })
    .then((response) => {
      if (!response.ok) {
      }
      console.log(response);
      return response.json(); // Parsowanie odpowiedzi jako JSON
    })
    .then((data) => {
      // Tutaj możesz obsłużyć dane z API
      console.log(data);
    })
    .catch((error) => {
      // Tutaj możesz obsłużyć błędy
      console.error("There was a problem with the fetch operation:", error);
    });
}
