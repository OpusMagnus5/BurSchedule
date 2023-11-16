import { getMessage, hasUserRole, postToApi, logoutUrl } from "../util/config.js";

export function setMenu() {
  setDisplayData();
  addClickListeners();
}

function setDisplayData() {
  document.querySelector(".menu .menu-service-list").textContent = getMessage("menu-service-list");
  document.querySelector(".menu .menu-scheduler-list").textContent = getMessage("menu-scheduler-list");
  document.querySelector(".menu .menu-scheduler-from-file").textContent = getMessage("menu-scheduler-from-file");
  document.querySelector(".menu .menu-scheduler-create").textContent = getMessage("menu-scheduler-create");
  setAdminPanel();
  setLogoutPanel();
}

function setAdminPanel() {
  let adminMenu = document.querySelector(".menu .menu-admin");
  if (hasUserRole("ADMIN")) {
    adminMenu.textContent = getMessage("menu-admin");
    adminMenu.addEventListener("click", function () {
      window.location.href = "admin";
    });
  } else {
    adminMenu.remove();
  }
}

function setLogoutPanel() {
  let logoutMenu = document.querySelector(".menu .menu-logout");
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

function addClickListeners() {
  document.querySelector(".menu-service-list").addEventListener("click", function () {
    window.location.href = "services-list";
  });

  document.querySelector(".menu-scheduler-list").addEventListener("click", function () {
    window.location.href = "scheduler-list";
  });

  document.querySelector(".menu-scheduler-from-file").addEventListener("click", function () {
    window.location.href = "scheduler-upload";
  });

  document.querySelector(".menu-scheduler-create").addEventListener("click", function () {
    window.location.href = "scheduler-create";
  });
}
