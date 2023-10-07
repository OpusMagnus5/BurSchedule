const baseUrl = "http://localhost:8080/app/";

export const servicesUrl = baseUrl + "services";
export const messages_pl = new Map([
  ["general.error", "Przepraszamy wystąpił błąd."],
]);

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
