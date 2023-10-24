import { getMessage } from "../util/config.js";
import { setMenu } from "../templates/menu.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  setTextData();
  setMenu();
}

function setTextData() {
  document.querySelector(".email-input").placeholder = getMessage("scheduler-create-email-input-placeholder");
  document.querySelector(".add-record").textContent = getMessage("scheduler-create-add-record-button");
  document.querySelector(".remove-day").textContent = getMessage("scheduler-create-remove-day-button");
  document.querySelector(".clone-day").textContent = getMessage("scheduler-create-clone-day-button");
  document.querySelector(".hours-number-label").textContent = getMessage("scheduler-create-hours-number-label");
  document.querySelector(".record-subject").placeholder = getMessage("scheduler-create-record-subject");
  document.querySelector(".start-time-label").textContent = getMessage("scheduler-create-start-time-label");
  document.querySelector(".end-time-label").textContent = getMessage("scheduler-create-end-time-label");
  document.querySelector(".remove-record").textContent = getMessage("scheduler-create-remove-record-button");
  document.querySelector(".add-day").textContent = getMessage("scheduler-create-add-day-button");
  document.querySelector(".send-scheduler").textContent = getMessage("scheduler-create-send-scheduler-button");
}

document.querySelector(".add-day").addEventListener("click", function () {
  let emptyDay = document.querySelector(".hidden");
  let cloneDay = emptyDay.cloneNode(true);

  let existingDays = document.querySelectorAll(".day");
  let existingDaysNo = existingDays.length;
  let prevDay = null;
  cloneDay.querySelector(".day-number").textContent = getMessage("scheduler-create-day-number-label") + (existingDaysNo + 1);
  if (existingDaysNo !== 0) {
    prevDay = existingDays[existingDaysNo - 1];
    let nextDay = new Date(prevDay.querySelector(".date-input").value);
    nextDay.setDate(nextDay.getDate() + 1);
    cloneDay.querySelector(".date-input").valueAsDate = nextDay;
    cloneDay.querySelector(".email-input").value = prevDay.querySelector(".email-input").value;
  }
  cloneDay.querySelector(".hours-number-value").textContent = 0;
  cloneDay.querySelector(".record-number").textContent = 1;

  cloneDay.classList.add("day");
  cloneDay.classList.remove("hidden");
  document.querySelector(".scheduler").appendChild(cloneDay);
});
