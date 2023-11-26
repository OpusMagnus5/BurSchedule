import { setMenu } from "../templates/menu.js";
import {
  allSchedulersUrl,
  deleteToApi,
  generateUrl,
  getFromApi,
  getMessage,
  hideLoader,
  schedulerUrl,
  schowLoader,
} from "../util/config.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  schowLoader();
  setTextData();
  let storedSchedulers = sessionStorage.getItem("scheduler-list");
  if (storedSchedulers === "undefined" || !storedSchedulers) {
    getFromApi(allSchedulersUrl).then((response) => {
      sessionStorage.setItem("scheduler-list", JSON.stringify(response.schedulers));
      setPageDependsOfFetchedData();
    });
  } else {
    setPageDependsOfFetchedData();
  }
}

function setPageDependsOfFetchedData() {
  let storedSchedulers = [];
  try {
    storedSchedulers = JSON.parse(sessionStorage.getItem("scheduler-list"));
  } catch {}

  setSchedulerList(storedSchedulers);
  setDataAfterFetchData();
  setMenu();
}

function setTextData() {
  document.querySelector(".filter-label.name").textContent = getMessage("scheduler-list-filter-label-name");
  document.querySelector(".filter-label.start-date").textContent = getMessage("scheduler-list-filter-label-start-date");
  document.querySelector(".filter-label.end-date").textContent = getMessage("scheduler-list-filter-label-end-date");
  document.querySelector(".filter-label.user").textContent = getMessage("scheduler-list-filter-label-user");
  document.querySelector(".reset-filters").textContent = getMessage("scheduler-list-filter-reset-filters-button");

  document.querySelector(".head-name").textContent = getMessage("scheduler-list-head-name");
  document.querySelector(".head-days-number").textContent = getMessage("scheduler-list-head-days-number");
  document.querySelector(".head-create-date").textContent = getMessage("scheduler-list-head-create-date");
  document.querySelector(".head-modify-date").textContent = getMessage("scheduler-list-head-modify-date");
  document.querySelector(".head-user").textContent = getMessage("scheduler-list-head-user");
}

function setSchedulerList(schedulers) {
  let schedulerTemplate = document.querySelector(".scheduler");
  let schedulerTable = document.querySelector(".scheduler-list tbody");
  schedulers.forEach((element) => {
    let newScheduler = schedulerTemplate.cloneNode(true);
    newScheduler.id = element.id;
    newScheduler.querySelector(".scheduler-name").textContent = element.name;
    newScheduler.querySelector(".scheduler-days-number").textContent = element.daysNumber;
    newScheduler.querySelector(".scheduler-create-date").textContent = element.createDate;
    newScheduler.querySelector(".scheduler-modify-date").textContent = element.modifyDate;
    newScheduler.querySelector(".scheduler-user").textContent = element.userName;
    schedulerTable.appendChild(newScheduler);
  });
  schedulerTemplate.remove();
}

function colorEvenRows(rows) {
  let k = 0;
  for (let j = 0, row; (row = rows[j]); j++) {
    if (!(row.style.display === "none")) {
      row.classList.remove("gray-background");
      row.classList.remove("white-background");
      if (k % 2) {
        row.classList.add("gray-background");
      } else {
        row.classList.add("white-background");
      }
      k++;
    }
  }
}

function setEventListeners() {
  Array.from(document.querySelectorAll(".filter-input")).forEach((element) => {
    element.addEventListener("change", filterSchedulers);
  });
  document.querySelector(".reset-filters").addEventListener("click", resetFiltersHandler);
  document.querySelectorAll(".scheduler-save-button").forEach((element) => {
    element.addEventListener("click", downloadScheduler);
  });
  document.querySelectorAll(".scheduler-delete-button").forEach((element) => {
    element.addEventListener("click", deleteScheduler);
  });
  document.querySelectorAll(".scheduler-edit-button").forEach((element) => {
    element.addEventListener("click", editScheduler);
  });
}

function downloadScheduler(event) {
  let scheduler = event.target.parentElement.parentElement;
  getFromApi(generateUrl + "?id=" + scheduler.id);
}

function deleteScheduler(event) {
  let scheduler = event.target.parentElement.parentElement;
  deleteToApi(schedulerUrl + "/" + scheduler.id).then((response) => {
    if (response.hasOwnProperty("message")) {
      alert(response.message);
      event.target.parentElement.parentElement.remove();
      colorEvenRows();
      sessionStorage.removeItem("scheduler-list");
    }
  });
}

function editScheduler(event) {
  schowLoader();
  let scheduler = event.target.parentElement.parentElement;
  getFromApi(schedulerUrl + "?id=" + scheduler.id).then((response) => {
    if (response.hasOwnProperty("id")) {
      sessionStorage.setItem("scheduler-to-edit", JSON.stringify(response));
      hideLoader();
      window.location.href = "scheduler-create";
    }
  });
}

function setDataAfterFetchData() {
  colorEvenRows(Array.from(document.querySelectorAll(".scheduler")));
  setEventListeners();
  hideLoader();
}

function filterSchedulers() {
  let schedulers = Array.from(document.querySelectorAll(".scheduler"));
  let nameFilter = document.querySelector(".filter-input.name").value;
  let startDate = new Date(document.querySelector(".filter-input.start-date").value);
  let endDate = new Date(document.querySelector(".filter-input.end-date").value);
  let userFilter = document.querySelector(".filter-input.user").value;

  schedulers.forEach((row) => {
    let schedulerName = row.querySelector(".scheduler-name").textContent;
    let createDate = new Date(row.querySelector(".scheduler-create-date").textContent);
    let modifyDate = new Date(row.querySelector(".scheduler-modify-date").textContent);
    let user = row.querySelector(".scheduler-user").textContent;

    if (
      (schedulerName.includes(nameFilter) || nameFilter === "") &&
      (isNaN(startDate) || createDate >= startDate || modifyDate >= startDate) &&
      (isNaN(endDate) || createDate <= endDate || modifyDate <= endDate) &&
      (user.includes(userFilter) || userFilter === "")
    ) {
      row.style.display = "table-row";
    } else {
      row.style.display = "none";
    }
  });

  colorEvenRows(schedulers);
}

function resetFiltersHandler() {
  document.querySelectorAll(".filter-input").forEach((element) => {
    element.value = "";
    element.dispatchEvent(new Event("change"));
  });
}
