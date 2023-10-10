import { servicesUrl } from "../util/config.js";
import { getFromApi } from "../util/config.js";

var servicesData;

function getServices() {
  getFromApi(servicesUrl)
  .then(data => {
    servicesData = data;
    showData();
  });
}

function showData() {
  let article = document.querySelector(".service-list");
  let emptyService = document.querySelector(".service");

  servicesData.services.forEach((element) => {
    let newService = emptyService.cloneNode(true);
    newService.id = element.id;
    newService.querySelector(".service-number").textContent = element.number;
    newService.querySelector(".service-title").textContent = element.title;
    newService.querySelector(".service-location").textContent =
      element.location;
    newService.querySelector(".service-start-date").textContent =
      element.dateBeginningOfService;
    newService.querySelector(".service-end-date").textContent =
      element.dateCompletionOfService;
    newService.querySelector(".service-status").textContent = element.status;
    newService.querySelector(".service-hours").textContent =
      element.numberOfHours;

    newService.style.display = "flex";
    article.appendChild(newService);
  });
}

// Wywołanie funkcji getServices() w odpowiednim momencie, na przykład po załadowaniu strony
document.addEventListener("DOMContentLoaded", getServices());
