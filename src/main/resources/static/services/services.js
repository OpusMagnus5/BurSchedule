import { servicesUrl } from "../util/config.js";
import { providersUrl } from "../util/config.js";
import { statusesUrl } from "../util/config.js";
import { getFromApi } from "../util/config.js";
import { getMessage } from "../util/config.js";

function getServices() {
  getFromApi(servicesUrl).then((data) => {
    sessionStorage.setItem("services-data", JSON.stringify(data));
    showData();
  });
  setFilters();
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
  let servicesData = JSON.parse(sessionStorage.getItem("services-data"));

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

function setFilters() {
  document.querySelector(".filter-number-label").textContent =
    getMessage("head-number");
  document.querySelector(".filter-provider-label").textContent =
    getMessage("head-provider");
  document.querySelector(".filter-title-label").textContent =
    getMessage("head-title");
  document.querySelector(".filter-location-label").textContent =
    getMessage("head-location");
  document.querySelector(".filter-start-date-label").textContent =
    getMessage("head-start-date");
  document.querySelector(".filter-end-date-label").textContent =
    getMessage("head-end-date");
  document.querySelector(".filter-status-label").textContent =
    getMessage("head-status");
  document.querySelector(".filter-hours-label").textContent =
    getMessage("head-hours");

  setSelectors();
}

function setSelectors() {
  getFromApi(providersUrl).then((data) => {
    let providerOption = document.querySelector(
      ".filter-provider-selector option"
    );
    let providerSelector = document.querySelector(".filter-provider-selector");
    data.providers.forEach((element) => {
      let newOption = providerOption.cloneNode(true);
      newOption.textContent = element;
      providerSelector.appendChild(newOption);
    });
    providerOption.style.display = "none";
  });

  getFromApi(statusesUrl).then((data) => {
    let statusOption = document.querySelector(".filter-status-selector option");
    let statusSelector = document.querySelector(".filter-status-selector");
    data.statuses.forEach((element) => {
      let newOption = statusOption.cloneNode(true);
      newOption.textContent = element;
      statusSelector.appendChild(newOption);
    });
    statusOption.style.display = "none";
  });
}

// Wywołanie funkcji getServices() w odpowiednim momencie, na przykład po załadowaniu strony
document.addEventListener("DOMContentLoaded", getServices());
