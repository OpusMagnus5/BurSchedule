import { getFromApi, getMessage, schedulerUrl, schowLoader } from "../util/config.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  schowLoader();
  setTextData();
  let storedSchedulers = sessionStorage.getItem("scheduler-list");
  if (storedSchedulers === "undefined" || !storedSchedulers) {
    getFromApi(schedulerUrl).then((response) => {
      sessionStorage.setItem("scheduler-list", JSON.stringify(response));
    });
  }
}

function setTextData() {
  document.querySelector(".filter-label.name").textContent = getMessage("scheduler-list-filter-label-name");
  document.querySelector(".filter-label.create-date").textContent = getMessage("scheduler-list-filter-label-create-date");
  document.querySelector(".filter-label.modify-date").textContent = getMessage("cheduler-list-filter-label-modify-date");
  document.querySelector(".reset-filters").textContent = getMessage("scheduler-list-filter-reset-filters-button");

  document.querySelector(".head-name").textContent = getMessage("scheduler-list-head-name");
  document.querySelector(".head-days-number").textContent = getMessage("scheduler-list-head-days-number");
  document.querySelector(".head-create-date").textContent = getMessage("scheduler-list-head-create-date");
  document.querySelector(".head-modify-date").textContent = getMessage("scheduler-list-head-modify-date");
}
