$(document).ready(function() {
    $('.seletor').select2();

});

document.getElementsByTagName("form")[0].addEventListener("submit", function (event) {
    const checkboxes = document.querySelectorAll('input[name="menu"]');
    if (checkboxes == null) {
        const isAnyChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);
    
        if (!isAnyChecked) {
        event.preventDefault(); // Impede o envio do formulário
        alert("Selecione pelo menos um menu!");
        }
    }
  });