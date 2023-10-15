const baseUrl = "/app/";

export const servicesUrl = baseUrl + "services";
export const providersUrl = servicesUrl + "/providers";
export const statusesUrl = servicesUrl + "/statuses";
export const schedulerUrl = baseUrl + "scheduler/";

export const messages_pl = new Map([
  ["general.error", "Przepraszamy wystąpił błąd."],
  ["head-number", "Numer"],
  ["head-provider", "Dostawca"],
  ["head-title", "Tytuł"],
  ["head-location", "Miejscowość"],
  ["head-status", "Status"],
  ["head-hours", "Liczba godzin"],
  ["filter-from-date", "Data (od)"],
  ["filter-to-date", "Data (do)"],
  ["all-option", "Wszystkie"],
  ["reset-filters", "Wyczyść filtry"],
  ["scheduler-day-label", "Dzień "],
  ["scheduler-email-label", "Email"],
  ["scheduler-date-label", "Data"],
  ["scheduler-time-label", "Godzina rozpoczęcia"],
  ["scheduler-email-checkbox", "Powiel email"],
  ["scheduler-time-checkbox", "Powiel czas rozpoczęcia"],
]);

export const messages_en = new Map();

export function getMessage(code) {
  let messages = new Map([
    ["pl-PL", messages_pl],
    ["en-US", messages_en],
  ]);

  let pickedMessages = messages.get(navigator.language);
  pickedMessages = pickedMessages != null ? pickedMessages : messages.get("pl-PL");

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
      } else {
        return data;
      }
    })
    .catch((error) => {
      alert(error.message);
    });
}
