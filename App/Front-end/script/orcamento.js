const modal = document.querySelector('dialog');

function openModal() {
  modal.showModal();
}

function closeModal() {
  modal.close();
}

$(document).ready(function() {
  $('.seletor').select2();

  $('#seletor_produto').on('change', function() {
    var valor = $(this).val();
    var nome = $('#seletor_produto option:selected').text().trim();
    var preco = parseFloat($('#seletor_produto option:selected').attr('data-preco'));
    $('#id_produto').val(valor);
    $('#id_produto').attr('name', nome);
    $('#id_produto').attr('data-preco', preco);

    document.querySelector('#nome_produto').value = nome;
    document.querySelector('#preco_produto').value = preco;
    document.querySelector('#produto_id').value = valor;
  });

});

// Funções

function adicionarItem() {
  var valor = $('#seletor_produto option:selected').val();
  var nome = $('#seletor_produto option:selected').text().trim();
  var preco = parseFloat($('#seletor_produto option:selected').attr('data-preco'));

  document.querySelector('#acao_item').value = "5";
  document.querySelector('#nome_produto').value = nome;
  document.querySelector('#preco_produto').value = preco;
  document.querySelector('#produto_id').value = valor;
  document.querySelector("#quantidade_produto").value = null;

  modal.showModal();
}

function alterarItem(index, idItem) {
  const table = document.getElementsByTagName("tbody")[0];
  const linha = table.rows[index].cells;

  var acao = "6";
  var idProduto = parseInt(linha[1].textContent);
  var nome = linha[3].textContent.trim();
  var quantidade = parseInt(linha[4].textContent);
  var preco = parseFloat(linha[5].textContent.replace('.', '').replace(',', '.'));

  console.log(preco);

  document.querySelector("#acao_item").value = acao;
  document.querySelector('#id_item').value = idItem;
  document.querySelector('#produto_id').value = idProduto;
  document.querySelector('#nome_produto').value = nome;
  document.querySelector('#quantidade_produto').value = quantidade;
  document.querySelector('#preco_produto').value = preco;

  modal.showModal();
}