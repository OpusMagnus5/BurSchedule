import { getMessage } from "../util/config.js";

export function setMenu() {
  document.querySelector(".menu .menu-service-list").textContent = getMessage("menu-service-list");
  document.querySelector(".menu .menu-scheduler-from-file").textContent = getMessage("menu-scheduler-from-file");
}
