const baseUrl = "/app/";

export const servicesUrl = baseUrl + "services";
export const providersUrl = servicesUrl + "/providers";
export const statusesUrl = servicesUrl + "/statuses";
export const schedulerUrl = baseUrl + "scheduler/";
export const generateUrl = schedulerUrl + "generate";

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
  ["scheduler-send-button", "Generuj harmonogram"],
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
      let contentType = response.headers.get("Content-Type");
      if (contentType === "application/json") {
        return response.json().then((json) => {
          if (response.ok) {
            return json;
          } else if (!response.ok && json.hasOwnProperty("messages")) {
            alert(json.messages[0]);
          } else {
            throw new Error(messages_pl.get("general.error"));
          }
        });
      } else if (contentType === "application/octet-stream") {
        return response.blob().then((blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement("a");
          a.href = url;
          a.download = "harmonogram.csv"; // Nazwa pliku
          a.style.display = "none";
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
        });
      }
    })
    .catch((error) => {
      alert(error.message);
    });
}

export function postToApi(url, request) {
  return fetch(url, {
    method: "POST",
    headers: {
      Accept: "application/json, application/octet-stream",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  })
    .then((response) => {
      let contentType = response.headers.get("Content-Type");
      if (contentType === "application/json") {
        return response.json().then((json) => {
          if (response.ok) {
            return json;
          } else if (!response.ok && json.hasOwnProperty("messages")) {
            alert(json.messages[0]);
          } else {
            throw new Error(messages_pl.get("general.error"));
          }
        });
      } else if (contentType === "application/octet-stream") {
        return response.blob().then((blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement("a");
          a.href = url;
          a.download = "harmonogram.csv"; // Nazwa pliku
          a.style.display = "none";
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
        });
      }
    })
    .catch((error) => {
      alert(error.message);
    });
}
