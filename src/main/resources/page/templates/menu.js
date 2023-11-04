import { handleAddUserButton } from "../admin/admin.js";
import { getMessage } from "../util/config.js";

export function setMenu() {
  document.querySelector(".menu .menu-service-list").textContent = getMessage("menu-service-list");
  document.querySelector(".menu .menu-scheduler-from-file").textContent = getMessage("menu-scheduler-from-file");
  document.querySelector(".menu .menu-scheduler-create").textContent = getMessage("menu-scheduler-create");
  document.querySelector(".menu .menu-add-user").textContent = getMessage("menu-add-user");

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

  document.querySelector(".menu-admin").addEventListener("click", handleAdminButton);
}
