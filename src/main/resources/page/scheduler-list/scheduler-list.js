import { setMenu } from "../templates/menu.js";
import { getFromApi, getMessage, hideLoader, schedulerUrl, schowLoader } from "../util/config.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  schowLoader();
  setTextData();
  let storedSchedulers = sessionStorage.getItem("scheduler-list");
  if (storedSchedulers === "undefined" || !storedSchedulers) {
    getFromApi(schedulerUrl).then((response) => {
      sessionStorage.setItem("scheduler-list", JSON.stringify(response));
      setSchedulerList(response.schedulers);
      colorEvenRows(Array.from(document.querySelectorAll(".scheduler")));
      hideLoader();
    });
  } else {
    setSchedulerList(JSON.parse(sessionStorage.getItem("scheduler-list")).schedulers);
    colorEvenRows(Array.from(document.querySelectorAll(".scheduler")));
    hideLoader();
  }
  setMenu();
}

function setTextData() {
  document.querySelector(".filter-label.name").textContent = getMessage("scheduler-list-filter-label-name");
  document.querySelector(".filter-label.start-date").textContent = getMessage("scheduler-list-filter-label-start-date");
  document.querySelector(".filter-label.end-date").textContent = getMessage("scheduler-list-filter-label-end-date");
  document.querySelector(".reset-filters").textContent = getMessage("scheduler-list-filter-reset-filters-button");

  document.querySelector(".head-name").textContent = getMessage("scheduler-list-head-name");
  document.querySelector(".head-days-number").textContent = getMessage("scheduler-list-head-days-number");
  document.querySelector(".head-create-date").textContent = getMessage("scheduler-list-head-create-date");
  document.querySelector(".head-modify-date").textContent = getMessage("scheduler-list-head-modify-date");
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
