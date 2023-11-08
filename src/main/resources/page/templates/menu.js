import { getMessage, hasUserRole } from "../util/config.js";

export function setMenu() {
  document.querySelector(".menu .menu-service-list").textContent = getMessage("menu-service-list");
  document.querySelector(".menu .menu-scheduler-from-file").textContent = getMessage("menu-scheduler-from-file");
  document.querySelector(".menu .menu-scheduler-create").textContent = getMessage("menu-scheduler-create");

  if (hasUserRole("ADMIN")) {
    document.querySelector(".menu .menu-admin").textContent = getMessage("menu-admin");
    document.querySelector(".menu-admin").addEventListener("click", function () {
      window.location.href = "admin";
    });
  } else {
    document.querySelector(".menu .menu-admin").remove();
  }

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

  document.querySelectorAll(".menu-scheduler-create").forEach((element) => {
    element.addEventListener("click", function () {
      window.location.href = "scheduler-create";
    });
  });
}
