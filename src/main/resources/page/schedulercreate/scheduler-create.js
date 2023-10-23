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
