import { getFromApi, getMessage, postToApi, rolesUrl, userUrl } from "../util/config.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  hidePrevDisplay();
  setAddUserForm();
}

function hidePrevDisplay() {
  let actualDisplay = document.querySelector("article > :not(.hidden)");
  if (actualDisplay) {
    actualDisplay.remove();
  }
}

function setAddUserForm() {
  let form = document.querySelector(".add-user-form");
  form.querySelector(".username-input").placeholder = getMessage("admin-add-user-username-input");
  form.querySelector(".password-input").placeholder = getMessage("admin-add-user-password-input");
  form.querySelector(".submit-add-user").textContent = getMessage("admin-add-user-submit-button");

  setRolesSelector();
  let newForm = form.cloneNode(true);
  document.querySelector("article").appendChild(newForm);
  newForm.classList.remove("hidden");
}

function setRolesSelector() {
  document.querySelector(".roles-input-label").textContent = getMessage("admin-add-user-roles-input-label");
  let roleSelector = document.querySelector(".roles-input");
  let emptyOption = roleSelector.querySelector("option");

  getFromApi(rolesUrl).then((data) => {
    let roles = data.roles;
    roles.forEach((element) => {
      let option = emptyOption.cloneNode(true);
      option.textContent = element;
      roleSelector.appendChild(option);
    });
  });

  emptyOption.remove;
}

export function handleAddUserButton() {
  let hiddenAddUserFrom = document.querySelector(".add-user-form .hidden");
  let addUserForm = hiddenAddUserFrom.cloneNode(true);

  addUserForm.querySelector(".submit-add-user").addEventListener("click", handleSendFormButton);
}

function handleSendFormButton() {
  let form = document.querySelector("div:not(hidden).add-user-form");
  let rolesNode = form.querySelectorAll(".roles-input option");

  let roles = [];
  rolesNode.forEach((element) => {
    roles.push(element.textContent);
  });

  let request = {
    username: form.querySelector(".username-input").value,
    password: form.querySelector(".password-input").value,
    roles: roles,
  };

  postToApi(userUrl, request).then((data) => {
    alert(data.result);
  });
}
