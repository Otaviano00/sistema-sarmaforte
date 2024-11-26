
const checkboxes = document.querySelectorAll(".produto-checkbox");
const valorTotalTd = document.getElementById("valorTotal");
let desconto = 0;

function atualizarTotal() {
    let total = 0;
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            const valor = parseFloat(checkbox.dataset.preco);
            if (!isNaN(valor)) {
                total += valor;
            }
        }
    });
    desconto = parseFloat(document.getElementById("desconto").value) || 0;

    total = total - desconto

    if (total < 0) {
        total = 0;
    }

    let precoFormatado = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(total);
    valorTotalTd.textContent = precoFormatado; 

    document.querySelector('#valor').value = total;
}

function atualizarDesconto() {
    desconto = parseFloat(document.getElementById("desconto").value) || 0;
    atualizarTotal();
}

$(document).ready(function() {
    atualizarTotal();
});

$('.seletor').select2();