import { createSchedulerUrl, getMessage, postToApi } from "../util/config.js";
import { setMenu } from "../templates/menu.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  setTextData();
  setMenu();
  document.querySelector(".send-scheduler").addEventListener("click", handleSendSchedulerEvent);
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
  document.querySelector(".scheduler-hours-label").textContent = getMessage("scheduler-create-scheduler-hours-label");
  document.querySelector(".scheduler-hours-value").textContent = "00:00";
}

document.querySelector(".add-day").addEventListener("click", function () {
  let emptyDay = document.querySelector(".hidden");
  let cloneDay = emptyDay.cloneNode(true);

  let existingDays = document.querySelectorAll(".day");
  let existingDaysNo = existingDays.length;
  let prevDay = null;
  cloneDay.querySelector(".day-number").textContent = getMessage("scheduler-create-day-number-label") + (existingDaysNo + 1);
  setNextDayDate(existingDays, cloneDay);
  if (existingDaysNo !== 0) {
    prevDay = existingDays[existingDaysNo - 1];
    cloneDay.querySelector(".email-input").value = prevDay.querySelector(".email-input").value;
  }
  cloneDay.querySelector(".hours-number-value").textContent = "00:00";
  cloneDay.querySelector(".record-number").textContent = 1;

  cloneDay.classList.add("day");
  cloneDay.classList.remove("hidden");
  document.querySelector(".scheduler").appendChild(cloneDay);

  cloneDay.querySelector(".add-record").addEventListener("click", handleAddRecordEvent);
  cloneDay.querySelector(".record-start-time").addEventListener("change", calculateDayHour);
  cloneDay.querySelector(".record-end-time").addEventListener("change", calculateDayHour);
  cloneDay.querySelector(".remove-day").addEventListener("click", handleRemoveDay);
  cloneDay.querySelector(".clone-day").addEventListener("click", handleCopyDay);
  cloneDay.querySelector(".remove-record").addEventListener("click", handleRemoveRecord);
});

function handleSendSchedulerEvent() {
  let days = document.querySelectorAll(".day");
  let schedulerDTO = getSchedulerDTO(days);
  postToApi(createSchedulerUrl, schedulerDTO);
}

function getSchedulerDTO(days) {
  let schedulerDTO = {
    days: [],
  };

  days.forEach((dayNode) => {
    let records = dayNode.querySelectorAll(".record-day");
    let dayDTO = {
      email: dayNode.querySelector(".email-input").value,
      date: dayNode.querySelector(".date-input").value,
      records: [],
    };
    records.forEach((recordNode) => {
      let recordDTO = {
        subject: recordNode.querySelector(".record-subject").value,
        startTime: recordNode.querySelector(".record-start-time").value,
        endTime: recordNode.querySelector(".record-end-time").value,
      };
      dayDTO.records.push(recordDTO);
    });
    schedulerDTO.days.push(dayDTO);
  });
  return schedulerDTO;
}

function setNextDayDate(existingDays, nextDayNode) {
  let existingDaysNo = existingDays.length;
  let prevDay = null;
  nextDayNode.querySelector(".day-number").textContent = getMessage("scheduler-create-day-number-label") + (existingDaysNo + 1);
  if (existingDaysNo !== 0) {
    prevDay = existingDays[existingDaysNo - 1];
    let nextDay = new Date(prevDay.querySelector(".date-input").value);
    nextDay.setDate(nextDay.getDate() + 1);
    nextDayNode.querySelector(".date-input").valueAsDate = nextDay;
  }
}

function handleAddRecordEvent(event) {
  let day = event.target.parentElement.parentElement;
  let emptyRecord = document.querySelector(".hidden .record-day").cloneNode(true);

  let recordNumber = day.querySelectorAll(".record-day").length;
  emptyRecord.querySelector(".record-number").textContent = recordNumber + 1;

  emptyRecord.querySelector(".record-start-time").addEventListener("change", calculateDayHour);
  emptyRecord.querySelector(".record-end-time").addEventListener("change", calculateDayHour);
  emptyRecord.querySelector(".remove-record").addEventListener("click", handleRemoveRecord);
  day.appendChild(emptyRecord);
}

function calculateDayHour(event) {
  let day = event.target.parentElement.parentElement.parentElement;
  let sumInMinutes = 0;
  day.querySelectorAll(".record-day").forEach((element) => {
    let startTime = element.querySelector(".record-start-time").value;
    let endTime = element.querySelector(".record-end-time").value;
    let startDate = new Date("1970-01-01T" + startTime);
    let endDate = new Date("1970-01-01T" + endTime);
    let timeDifference = endDate - startDate;
    let differenceInMinutes = timeDifference / 60000;
    sumInMinutes += differenceInMinutes;
  });

  let hours = Math.floor(sumInMinutes / 60);
  let minutes = sumInMinutes - hours * 60;

  let formattedTime = formatTime(hours, minutes);
  let dayHours = day.querySelector(".hours-number-value");
  dayHours.textContent = isNaN(sumInMinutes) ? dayHours.textContent : formattedTime;
  calculateSchedulerHour();
}

function calculateDayHourByDay(day) {
  let sumInMinutes = 0;
  day.querySelectorAll(".record-day").forEach((element) => {
    let startTime = element.querySelector(".record-start-time").value;
    let endTime = element.querySelector(".record-end-time").value;
    let startDate = new Date("1970-01-01T" + startTime);
    let endDate = new Date("1970-01-01T" + endTime);
    let timeDifference = endDate - startDate;
    let differenceInMinutes = timeDifference / 60000;
    sumInMinutes += differenceInMinutes;
  });

  let hours = Math.floor(sumInMinutes / 60);
  let minutes = sumInMinutes - hours * 60;

  let formattedTime = formatTime(hours, minutes);
  let dayHours = day.querySelector(".hours-number-value");
  dayHours.textContent = isNaN(sumInMinutes) ? dayHours.textContent : formattedTime;
  calculateSchedulerHour();
}

function calculateSchedulerHour() {
  let sumInMinutes = 0;

  document.querySelectorAll(".day").forEach((element) => {
    let dayTime = element.querySelector(".hours-number-value").textContent;
    let dayMinutes = stringToMinutes(dayTime);
    if (!isNaN(dayMinutes)) {
      sumInMinutes += dayMinutes;
    }
  });

  let hours = Math.floor(sumInMinutes / 60);
  let minutes = sumInMinutes - hours * 60;

  let formattedTime = formatTime(hours, minutes);
  let schedulerHours = document.querySelector(".scheduler-hours-value");
  schedulerHours.textContent = formattedTime;
}

function handleRemoveDay(event) {
  let dayToRemove = event.target.parentElement.parentElement;
  dayToRemove.remove();
  calculateSchedulerHour();
  recalculateDayNumbers();
}

function handleCopyDay(event) {
  let copiedDay = event.target.parentElement.parentElement.cloneNode(true);
  copiedDay.querySelectorAll(".record-day").forEach((element) => {
    element.querySelector(".record-subject").value = "";
  });
  let scheduler = event.target.parentElement.parentElement.parentElement;

  let existingDays = scheduler.querySelectorAll(".day");
  setNextDayDate(existingDays, copiedDay);

  scheduler.appendChild(copiedDay);
  copiedDay.querySelector(".add-record").addEventListener("click", handleAddRecordEvent);
  copiedDay.querySelectorAll(".record-start-time").forEach((element) => {
    element.addEventListener("change", calculateDayHour);
  });
  copiedDay.querySelectorAll(".record-end-time").forEach((element) => {
    element.addEventListener("change", calculateDayHour);
  });
  copiedDay.querySelector(".remove-day").addEventListener("click", handleRemoveDay);
  copiedDay.querySelector(".clone-day").addEventListener("click", handleCopyDay);
  copiedDay.querySelectorAll(".remove-record").forEach((element) => {
    element.addEventListener("click", handleRemoveRecord);
  });
  calculateSchedulerHour();
  recalculateDayNumbers();
}

function handleRemoveRecord(event) {
  let recordToRemove = event.target.parentElement;
  let day = event.target.parentElement.parentElement;
  recordToRemove.remove();

  calculateDayHourByDay(day);
  recalculateRecordNumbers(day);
}

function recalculateDayNumbers() {
  let days = document.querySelectorAll(".day");
  for (let i = 0; days[i]; i++) {
    days[i].querySelector(".day-number").textContent = getMessage("scheduler-create-day-number-label") + (i + 1);
  }
}

function recalculateRecordNumbers(day) {
  let records = day.querySelectorAll(".record-day");
  for (let i = 0; records[i]; i++) {
    records[i].querySelector(".record-number").textContent = i + 1;
  }
}

function formatTime(hours, minutes) {
  let formattedHours = hours < 10 ? "0" + hours : hours;
  let formattedMinutes = minutes < 10 ? "0" + minutes : minutes;
  let formattedTime = formattedHours + ":" + formattedMinutes;
  return formattedTime;
}

function stringToMinutes(timeString) {
  let parts = timeString.split(":");

  if (parts.length === 2) {
    let hours = parseInt(parts[0], 10);
    let minutes = parseInt(parts[1], 10);

    if (!isNaN(hours) && !isNaN(minutes)) {
      return hours * 60 + minutes;
    }
  }

  return NaN;
}
