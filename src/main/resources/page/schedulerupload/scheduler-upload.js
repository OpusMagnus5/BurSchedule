import { setMenu } from "../templates/menu.js";
import { fileUrl, getMessage, hideLoader, postFileToApi, schowLoader } from "../util/config.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  setContent();
  setMenu();
}

function setContent() {
  document.querySelector(".input-header").textContent = getMessage("scheduler-upload-input-header");
  document.querySelector(".input-file-name").textContent = getMessage("scheduler-upload-input-file-name");
  document.querySelector(".input-send-button").textContent = getMessage("scheduler-upload-input-send-button");
  document.querySelector(".input-file-button").textContent = getMessage("scheduler-upload-input-file-button");
}

document.querySelector(".input-file-button").addEventListener("click", function () {
  document.querySelector(".input-file").click();
});

document.querySelector(".input-file").addEventListener("change", function () {
  let inputFile = document.querySelector(".input-file");
  let fileName = inputFile.files[0] ? inputFile.files[0].name : getMessage("scheduler-upload-input-file-name");
  document.querySelector(".input-file-name").textContent = fileName;
});

document.querySelector(".input-send-button").addEventListener("click", function () {
  schowLoader();
  let file = document.querySelector(".input-file").files[0];
  if (!file) {
    hideLoader();
    alert(getMessage("scheduler-upload-file-not-found"));
    return;
  }

  let formData = new FormData();
  formData.append("file", file);
  postFileToApi(fileUrl, formData).then((data) => {
    sessionStorage.setItem("scheduler", JSON.stringify(data));
    hideLoader();
    window.location.href = "scheduler-edit";
  });
});
