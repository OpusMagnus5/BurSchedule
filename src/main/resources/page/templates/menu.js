import { getMessage, hasUserRole, postToApi, logoutUrl } from "../util/config.js";

export function setMenu() {
  setDisplayData();
  addClickListeners();
}

function setDisplayData() {
  document.querySelector(".menu .menu-service-list").textContent = getMessage("menu-service-list");
  document.querySelector(".menu .menu-scheduler-from-file").textContent = getMessage("menu-scheduler-from-file");
  document.querySelector(".menu .menu-scheduler-create").textContent = getMessage("menu-scheduler-create");

  let adminMenu = document.querySelector(".menu .menu-admin");
  if (hasUserRole("ADMIN")) {
    adminMenu.textContent = getMessage("menu-admin");
    adminMenu.addEventListener("click", function () {
      window.location.href = "admin";
    });
  } else {
    adminMenu.remove();
  }

  let logoutMenu = document.querySelector(".menu .menu-logout");
  if (hasUserRole("USER")) {
    logoutMenu.textContent = getMessage("menu-logout");
    logoutMenu.addEventListener("click", function () {
      postToApi(logoutUrl, {}).then((data) => {
        if (data) {
          sessionStorage.clear();
          window.location.href = "login";
        } else {
          alert(getMessage("logout-failure"));
        }
      });
    });
  }
}

function addClickListeners() {
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
