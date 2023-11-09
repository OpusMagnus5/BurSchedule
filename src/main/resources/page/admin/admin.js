import { setMenu } from "../templates/admin/admin-menu.js";
import { getFromApi, getMessage, postToApi, rolesUrl, userUrl } from "../util/config.js";

document.addEventListener("DOMContentLoaded", setPage);

function setPage() {
  setMenu();
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
  form.querySelector(".roles-box-label").textContent = getMessage("admin-add-user-roles-box-label");

  setRoleCheckboxes();
}

function setRoleCheckboxes() {
  let rolesBox = document.querySelector(".roles-box");
  let emptyRole = document.querySelector(".role");

  getFromApi(rolesUrl).then((data) => {
    let roles = data.roles;
    roles.forEach((element) => {
      let newRole = emptyRole.cloneNode(true);
      newRole.querySelector(".role-input-label").textContent = element;
      newRole.querySelector(".role-input").value = element;
      rolesBox.appendChild(newRole);
    });
  });

  emptyRole.remove();
}

export function handleAddUserButton() {
  hidePrevDisplay();
  let hiddenAddUserFrom = document.querySelector(".add-user-form.hidden");
  let addUserForm = hiddenAddUserFrom.cloneNode(true);
  let article = document.querySelector("article");
  article.appendChild(addUserForm);
  addUserForm.classList.remove("hidden");

  addUserForm.querySelector(".submit-add-user").addEventListener("click", handleSendFormButton);
}

function handleSendFormButton() {
  let form = document.querySelector("div:not(.hidden).add-user-form");
  let rolesNode = form.querySelectorAll(".roles-box .role");

  let roles = [];
  rolesNode.forEach((element) => {
    let roleCheckbox = element.querySelector(".role-input");
    if (roleCheckbox.checked) {
      roles.push(roleCheckbox.value);
    }
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
