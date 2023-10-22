import { getMessage } from "../util/config.js";

export function setMenu() {
  document.querySelector(".menu .menu-service-list").textContent = getMessage("menu-service-list");
  document.querySelector(".menu .menu-scheduler-from-file").textContent = getMessage("menu-scheduler-from-file");

  document.querySelectorAll(".menu-service-list").forEach((element) => {
    element.addEventListener("click", function () {
      window.location.href = "services-list";
    });
  });

  document.querySelectorAll(".menu-scheduler-from-file").forEach((element) => {
    element.addEventListener("click", function () {
      window.location.href = "scheduler-upload";
    });
  });
}
