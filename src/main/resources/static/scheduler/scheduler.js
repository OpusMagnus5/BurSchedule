import { getMessage } from "../util/config.js";

document.addEventListener("DOMContentLoaded", setScheduler());

function setScheduler() {
  let schedulerEntries = JSON.parse(sessionStorage.getItem("scheduler")).schedulerEntries;
  let scheduler = document.querySelector(".scheduler");
  let emptyDayElement = document.querySelector(".day");

  for (let i = 0; i < schedulerEntries.length; i++) {
    let element = schedulerEntries[i];
    let newDay = emptyDayElement.cloneNode(true);

    newDay.querySelector(".day-number").textContent = getMessage("scheduler-day-label") + (i + 1);

    newDay.querySelector(".inputs .email .scheduler-label").textContent = getMessage("scheduler-email-label");

    newDay.querySelector(".inputs .date .scheduler-label").textContent = getMessage("scheduler-date-label");
    newDay.querySelector("#date-input").value = element.date;

    newDay.querySelector(".inputs .time .scheduler-label").textContent = getMessage("scheduler-time-label");
    newDay.querySelector("#start-time-input").value = element.startTime;

    scheduler.appendChild(newDay);
  }
  emptyDayElement.remove();
}
