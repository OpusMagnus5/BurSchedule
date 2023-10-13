import { providersUrl } from "../util/config.js";
import { servicesUrl } from "../util/config.js";
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
  document.querySelector(".head-number").textContent = getMessage("head-number");
  document.querySelector(".head-provider").textContent = getMessage("head-provider");
  document.querySelector(".head-title").textContent = getMessage("head-title");
  document.querySelector(".head-location").textContent = getMessage("head-location");
  document.querySelector(".head-start-date").textContent = getMessage("head-start-date");
  document.querySelector(".head-end-date").textContent = getMessage("head-end-date");
  document.querySelector(".head-status").textContent = getMessage("head-status");
  document.querySelector(".head-hours").textContent = getMessage("head-hours");

  let article = document.querySelector(".service-list");
  let emptyService = document.querySelector(".service");
  let servicesData = JSON.parse(sessionStorage.getItem("services-data"));

  servicesData.services.forEach((element) => {
    let newService = emptyService.cloneNode(true);
    newService.id = element.id;
    newService.querySelector(".service-number").textContent = element.number;
    newService.querySelector(".service-provider").textContent = element.serviceProviderName;
    newService.querySelector(".service-title").textContent = element.title;
    newService.querySelector(".service-location").textContent = element.location;
    newService.querySelector(".service-start-date").textContent = element.dateBeginningOfService;
    newService.querySelector(".service-end-date").textContent = element.dateCompletionOfService;
    newService.querySelector(".service-status").textContent = element.status;
    newService.querySelector(".service-hours").textContent = element.numberOfHours;

    article.appendChild(newService);
  });

  emptyService.remove();
}

function setFilters() {
  document.querySelector(".filter-number label").textContent = getMessage("head-number");
  document.querySelector(".filter-provider label").textContent = getMessage("head-provider");
  document.querySelector(".filter-title label").textContent = getMessage("head-title");
  document.querySelector(".filter-location label").textContent = getMessage("head-location");
  document.querySelector(".filter-from-date label").textContent = getMessage("filter-from-date");
  document.querySelector(".filter-to-date label").textContent = getMessage("filter-to-date");
  document.querySelector(".filter-status label").textContent = getMessage("head-status");

  setSelectors();
}

function setSelectors() {
  getFromApi(providersUrl).then((data) => {
    data.providers.unshift(getMessage("all-option"));
    let providerOption = document.querySelector(".filter-provider select option");
    let providerSelector = document.querySelector(".filter-provider select");
    data.providers.forEach((element) => {
      let newOption = providerOption.cloneNode(true);
      newOption.textContent = element;
      providerSelector.appendChild(newOption);
    });
    providerOption.remove();
    providerSelector.selectedIndex = 0;
  });

  getFromApi(statusesUrl).then((data) => {
    data.statuses.unshift(getMessage("all-option"));
    let statusOption = document.querySelector(".filter-status select option");
    let statusSelector = document.querySelector(".filter-status select");
    data.statuses.forEach((element) => {
      let newOption = statusOption.cloneNode(true);
      newOption.textContent = element;
      statusSelector.appendChild(newOption);
    });
    statusOption.remove();
    statusSelector.selectedIndex = 0;
  });
}

document.querySelectorAll(".filter-input").forEach((element) => {
  element.addEventListener("change", filterServices);
});

function filterServices() {
  let services = Array.from(document.querySelectorAll(".service"));

  services.forEach((row) => {
    let providerContent = row.querySelector(".service-provider").textContent;
    let providerSelector = document.querySelector(".filter-provider select");
    let providerValue = providerSelector.options[providerSelector.selectedIndex].textContent;

    let statusContent = row.querySelector(".service-status").textContent;
    let statusSelector = document.querySelector(".filter-status select");
    let statusValue = statusSelector.options[statusSelector.selectedIndex].textContent;

    let numberContent = row.querySelector(".service-number").textContent;
    let numberValue = document.querySelector(".filter-number input").value;

    let titleContent = row.querySelector(".service-title").textContent;
    let titleValue = document.querySelector(".filter-title input").value;

    let locationContent = row.querySelector(".service-location").textContent;
    let locationValue = document.querySelector(".filter-location input").value;

    let startDateContent = new Date(row.querySelector(".service-start-date").textContent);
    let endDateContent = new Date(row.querySelector(".service-end-date").textContent);
    let fromDateValue = new Date(document.querySelector(".filter-from-date input").value);
    let toDateValue = new Date(document.querySelector(".filter-to-date input").value);

    if (
      (providerValue === getMessage("all-option") || providerContent === providerValue) &&
      (statusValue === getMessage("all-option") || statusContent === statusValue) &&
      (numberValue === "" || numberContent.includes(numberValue)) &&
      (titleValue === "" || titleContent.includes(titleValue)) &&
      (locationValue === "" || locationContent.includes(locationValue)) &&
      (isNaN(fromDateValue) || fromDateValue >= startDateContent) &&
      (isNaN(toDateValue) || toDateValue <= endDateContent)
    ) {
      row.style.display = "table-row";
    } else {
      row.style.display = "none";
    }
  });

  colorEvenRows(services);
}

function colorEvenRows(rows) {
  let k = 0;
  for (let j = 0, row; (row = rows[j]); j++) {
    if (!(row.style.display === "none")) {
      if (k % 2) {
        row.style.backgroundColor = "#e3e3e3";
      } else {
        row.style.backgroundColor = "#fff";
      }
      k++;
    }
  }
}

// Wywołanie funkcji getServices() w odpowiednim momencie, na przykład po załadowaniu strony
document.addEventListener("DOMContentLoaded", getServices());
