import { handleAddUserButton } from "../../admin/admin.js";
import { getMessage } from "../../util/config.js";

export function setMenu() {
  document.querySelector(".menu .menu-service-list").textContent = getMessage("menu-service-list");
  document.querySelector(".menu .menu-add-user").textContent = getMessage("menu-add-user");

  document.querySelectorAll(".menu-service-list").forEach((element) => {
    element.addEventListener("click", function () {
      window.location.href = "services-list";
    });
  });

  document.querySelector(".menu-add-user").addEventListener("click", handleAddUserButton);
}
