import { getMessage, loginUrl, postToApi } from "../util/config.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  sessionStorage.clear();
  setTextData();
}

function setTextData() {
  document.querySelector(".username-input").placeholder = getMessage("login.username.input.placeholder");
  document.querySelector(".password-input").placeholder = getMessage("login.password.input.placeholder");
  document.querySelector(".submit-login-buttton").textContent = getMessage("login.send.button");
}

document.querySelector(".submit-login-buttton").addEventListener("click", handleSendLoginForm);

function handleSendLoginForm() {
  let request = {
    username: document.querySelector(".username-input").value,
    password: document.querySelector(".password-input").value,
  };

  postToApi(loginUrl, request).then((response) => {
    if (response) {
      sessionStorage.setItem("userRoles", JSON.stringify(response.userRoles));
      sessionStorage.setItem("loggedUser", JSON.stringify(response.userName));
      window.location.href = "services-list";
    }
  });
}
