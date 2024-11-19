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
    var acao = "adicionarItem";
    var valor = $(this).val();
    var nome = $('#seletor_produto option:selected').text().trim();
    var preco = parseFloat($('#seletor_produto option:selected').attr('data-preco'));
    $('#id_produto').val(valor);
    $('#id_produto').attr('name', nome);
    $('#id_produto').attr('data-preco', preco);

    document.querySelector("#acao_item").value = acao;
    document.querySelector('#nome_produto').value = nome;
    document.querySelector('#preco_produto').value = preco;
    document.querySelector('#produto_id').value = valor;
  });
});

// Funções

function mudarCliente() {
  var acaoOrcamento = document.querySelector("#acao_orcamento").value;
  const clienteId = $('#seletor_cliente').val();
  $('#id_cliente').val(clienteId);

  const idOrcamento = document.querySelector('#id_orcamento').value;
  const idCliente = document.querySelector('#id_cliente').value;

  const novaUrl = `http://localhost:8080/Samaforte/GerenciarOrcamento?acao=mudarCliente&idOrcamento=${idOrcamento}&idCliente=${idCliente}&acaoOrcamento=${acaoOrcamento}`;

  window.location.href = novaUrl;
}

function cancelarOrcamento(idOrcamento) {
  const novaUrl = `http://localhost:8080/Samaforte/GerenciarOrcamento?acao=excluir&idOrcamento=${idOrcamento}`;

  window.location.href = novaUrl;
}

function adicionarItem() {
  var acao = "adicionarItem";
  var valor = $('#seletor_produto option:selected').val();
  var nome = $('#seletor_produto option:selected').text().trim();
  var preco = parseFloat($('#seletor_produto option:selected').attr('data-preco'));

  document.querySelector("#acao_item").value = acao;
  document.querySelector('#nome_produto').value = nome;
  document.querySelector('#preco_produto').value = preco;
  document.querySelector('#produto_id').value = valor;

  modal.showModal();
}

function excluirItem(idItem, idOrcamento) {
  var acaoOrcamento = document.querySelector("#acao_orcamento").value;
  const novaUrl = `http://localhost:8080/Samaforte/GerenciarOrcamento?acao=excluirItem&idItem=${idItem}&idOrcamento=${idOrcamento}&acaoOrcamento=${acaoOrcamento}`;

  window.location.href = novaUrl;
}

function alterarItem(index, idItem) {
  const table = document.getElementsByTagName("tbody")[0];
  const linha = table.rows[index].cells;

  var acao = "alterarItem";
  var idProduto = parseInt(linha[2].textContent);
  var nome = linha[3].textContent.trim();
  var quantidade = parseInt(linha[4].textContent);
  var preco = parseFloat(linha[5].textContent);

  document.querySelector("#acao_item").value = acao;
  document.querySelector('#id_item').value = idItem;
  document.querySelector('#produto_id').value = idProduto;
  document.querySelector('#nome_produto').value = nome;
  document.querySelector('#quantidade_produto').value = quantidade;
  document.querySelector('#preco_produto').value = preco;

  modal.showModal();
}