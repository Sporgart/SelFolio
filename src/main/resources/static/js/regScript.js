document.getElementById("registrationForm").addEventListener("submit", function(event) {
  var usernameInput = document.getElementById("username");
  var emailInput = document.getElementById("email");
  var passwordInput = document.getElementById("password");
  var confirmPasswordInput = document.getElementById("confirmPassword");

  var username = usernameInput.value.trim();
  var email = emailInput.value.trim();
  var password = passwordInput.value.trim();
  var confirmPassword = confirmPasswordInput.value.trim();

  if (username === "") {
    alert("Пожалуйста, введите имя пользователя.");
    usernameInput.focus();
    event.preventDefault(); // Предотвращаем отправку формы
    return;
  }

  if (email === "") {
    alert("Пожалуйста, введите адрес электронной почты.");
    emailInput.focus();
    event.preventDefault(); // Предотвращаем отправку формы
    return;
  }

  if (!isValidEmail(email)) {
    alert("Пожалуйста, введите корректный адрес электронной почты.");
    emailInput.focus();
    event.preventDefault(); // Предотвращаем отправку формы
    return;
  }

  if (password === "") {
    alert("Пожалуйста, введите пароль.");
    passwordInput.focus();
    event.preventDefault(); // Предотвращаем отправку формы
    return;
  }

  if (password.length < 8) {
    alert("Пароль должен содержать не менее 8 символов.");
    passwordInput.focus();
    event.preventDefault(); // Предотвращаем отправку формы
    return;
  }

  if (password !== confirmPassword) {
    alert("Пароли не совпадают.");
    confirmPasswordInput.focus();
    event.preventDefault(); // Предотвращаем отправку формы
    return;
  }

});

function isValidEmail(email) {
  // Простая проверка на корректный формат адреса электронной почты
  var emailRegex = /\S+@\S+\.\S+/;
  return emailRegex.test(email);
}
