import { servicesUrl } from "../util/config.js";
import { getFromApi } from "../util/config.js";
import { getMessage } from "../util/config.js";

var servicesData;

function getServices() {
  getFromApi(servicesUrl).then((data) => {
    servicesData = data;
    showData();
  });
}

function showData() {
  document.querySelector(".head-number").textContent =
    getMessage("head-number");
  document.querySelector(".head-provider").textContent =
    getMessage("head-provider");
  document.querySelector(".head-title").textContent = getMessage("head-title");
  document.querySelector(".head-location").textContent =
    getMessage("head-location");
  document.querySelector(".head-start-date").textContent =
    getMessage("head-start-date");
  document.querySelector(".head-end-date").textContent =
    getMessage("head-end-date");
  document.querySelector(".head-status").textContent =
    getMessage("head-status");
  document.querySelector(".head-hours").textContent = getMessage("head-hours");

  let article = document.querySelector(".service-list");
  let emptyService = document.querySelector(".service");

  servicesData.services.forEach((element) => {
    let newService = emptyService.cloneNode(true);
    newService.id = element.id;
    newService.querySelector(".service-number").textContent = element.number;
    newService.querySelector(".service-provider").textContent =
      element.serviceProviderName;
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

    article.appendChild(newService);
  });

  emptyService.style.display = "none";
}

// Wywołanie funkcji getServices() w odpowiednim momencie, na przykład po załadowaniu strony
document.addEventListener("DOMContentLoaded", getServices());
