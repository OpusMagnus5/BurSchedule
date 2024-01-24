import { generateUrl, getMessage, hideLoader, postToApi, schowLoader } from "../util/config.js";
import { setMenu } from "../templates/menu.js";

document.addEventListener("DOMContentLoaded", setScheduler);

function setScheduler() {
  let schedulerEntries = JSON.parse(sessionStorage.getItem("scheduler"));
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

  setOptions();

  document.querySelector(".send-button").textContent = getMessage("scheduler-send-button");
  document.querySelector(".edit-button").textContent = getMessage("scheduler-edit-button");
  addSendButtonListener();
  addEditButtonListener();
  setMenu();
}

function setOptions() {
  addOptionListeners();
  document.querySelector(".email-option .scheduler-option-label").textContent = getMessage("scheduler-email-checkbox");
  document.querySelector(".time-option .scheduler-option-label").textContent = getMessage("scheduler-time-checkbox");

  document.querySelectorAll(".scheduler-option-input").forEach((element) => {
    element.checked = true;
    element.dispatchEvent(new Event("change"));
  });
}

function addOptionListeners() {
  addEmailCheckboxListener();
  addTimeCheckboxListener();
  addEmailInputListener();
  addTimeInputListener();
}

function addEmailCheckboxListener() {
  document.querySelector(".email-option .scheduler-option-input").addEventListener("change", function () {
    let checked = document.querySelector(".email-option .scheduler-option-input").checked;
    let inputs = document.querySelectorAll(".scheduler .day .inputs");

    if (checked) {
      for (let i = 1; i < inputs.length; i++) {
        let input = inputs[i].querySelector("#email-input");
        input.disabled = true;
        input.value = inputs[0].querySelector("#email-input").value;
      }
    } else {
      for (let i = 1; i < inputs.length; i++) {
        inputs[i].querySelector("#email-input").disabled = false;
      }
    }
  });
}

function addTimeCheckboxListener() {
  document.querySelector(".time-option .scheduler-option-input").addEventListener("change", function () {
    let checked = document.querySelector(".time-option .scheduler-option-input").checked;
    let inputs = document.querySelectorAll(".scheduler .day .inputs");

    if (checked) {
      for (let i = 1; i < inputs.length; i++) {
        let input = inputs[i].querySelector("#start-time-input");
        input.disabled = true;
        input.value = inputs[0].querySelector("#start-time-input").value;
      }
    } else {
      for (let i = 1; i < inputs.length; i++) {
        inputs[i].querySelector("#start-time-input").disabled = false;
      }
    }
  });
}

function addEmailInputListener() {
  document.getElementById("email-input").addEventListener("change", function () {
    let emailCheckbox = document.querySelector(".email-option .scheduler-option-input").checked;

    if (emailCheckbox) {
      let inputs = document.querySelectorAll(".scheduler .day .inputs");
      for (let i = 1; i < inputs.length; i++) {
        inputs[i].querySelector("#email-input").value = inputs[0].querySelector("#email-input").value;
      }
    }
  });
}

function addTimeInputListener() {
  document.getElementById("start-time-input").addEventListener("change", function () {
    let timeCheckbox = document.querySelector(".time-option .scheduler-option-input").checked;

    if (timeCheckbox) {
      let inputs = document.querySelectorAll(".scheduler .day .inputs");
      for (let i = 1; i < inputs.length; i++) {
        inputs[i].querySelector("#start-time-input").value = inputs[0].querySelector("#start-time-input").value;
      }
    }
  });
}

function addSendButtonListener() {
  document.querySelector(".send-button").addEventListener("click", function () {
    schowLoader();
    let days = document.querySelectorAll(".day");
    let request = {
      scheduleDays: new Array(),
    };

    days.forEach((element) => {
      let day = {
        email: element.querySelector("#email-input").value,
        date: element.querySelector("#date-input").value,
        startTime: element.querySelector("#start-time-input").value,
      };
      request.scheduleDays.push(day);
    });

    postToApi(generateUrl, request);
    hideLoader();
  });
}

function addEditButtonListener() {
  document.querySelector(".edit-button").addEventListener("click", function () {
    window.location.href = "scheduler-create";
  });
}
