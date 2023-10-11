const baseUrl = "http://localhost:8080/app/";

export const servicesUrl = baseUrl + "services";
export const messages_pl = new Map([
  ["general.error", "Przepraszamy wystąpił błąd."],
  ["head-number", "Numer"],
  ["head-provider", "Dostawca"],
  ["head-title", "Tytuł"],
  ["head-location", "Miejscowość"],
  ["head-start-date", "Data rozpoczęcia"],
  ["head-end-date", "Data zakończenia"],
  ["head-status", "Status"],
  ["head-hours", "Liczba godzin"],
]);

export const messages_en = new Map();

export function getMessage(code) {
  let messages = new Map([
    ["pl-PL", messages_pl],
    ["en_US", messages_en],
  ]);

  let pickedMessages = messages.get(navigator.language);

  return pickedMessages.get(code);
}

export function getFromApi(url) {
  return fetch(url, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      } else if (!response.ok) {
        return response.json();
      } else {
        throw new Error(messages_pl.get("general.error"));
      }
    })
    .then((data) => {
      if (data.hasOwnProperty("message")) {
        alert("data.message");
      } else if (data.hasOwnProperty("services")) {
        return data;
      } else {
        throw new Error(messages_pl.get("general.error"));
      }
    })
    .catch((error) => {
      alert(error.message);
    });
}
