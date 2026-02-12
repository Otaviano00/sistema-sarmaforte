const basePath = "GerenciarOrcamento";

document.addEventListener('DOMContentLoaded', function () {
    const table = $('#lista-orcamentos').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": `${basePath}`,
            "type": "GET",
            "data": function (d) {
                d.filterColumn = $('#input-filter').val();
                d.filterType = $('#input-type').val();
                d.isPaginado = true;
            }
        },
        "columns": [
            { "data": "id" },
            {
                "data": "cliente",
                "render": function (data, type, row) {
                    return data && data.nome ? data.nome : '---';
                }
            },
            {
                "data": "dataCriacao",
                "render": function (data, type, row) {
                    if (data) {
                        const date = new Date(data);
                        return date.toLocaleDateString('pt-BR');
                    }
                    return '---';
                }
            },
            {
                "data": "dataValidade",
                "render": function (data, type, row) {
                    if (data) {
                        const date = new Date(data);
                        return date.toLocaleDateString('pt-BR');
                    }
                    return '---';
                }
            },
            { "data": "status" },
            {
                "data": null,
                "orderable": false,
                "searchable": false,
                "render": function (data, type, row) {
                    const hierarquia = parseInt(document.getElementById('hierarquia-value')?.value || 2);
                    let actions = '';

                    // Botão de alterar - não mostrar se concluído e usuário não é admin
                    if (!(row.status === 'Concluído' && hierarquia !== 0)) {
                        actions += `
                            <button onclick="alterarOrcamento(${row.id})" class="botao_acao" title="Alterar orçamento">
                                <img src="images/icone_alterar.svg" alt="Alterar">
                            </button>
                        `;
                    }

                    // Botão de detalhes - sempre visível
                    actions += `
                        <button onclick="verDetalhesOrcamento(${row.id})" class="botao_acao" title="Detalhes do orçamento">
                            <img src="images/icone_detalhes.svg" alt="Detalhes">
                        </button>
                    `;

                    // Botão de excluir
                    actions += `
                        <button onclick="confirmarExclusao(event, ${row.id})" class="botao_acao" title="Excluir orçamento">
                            <img src="images/icone_excluir.svg" alt="Excluir">
                        </button>
                    `;

                    // Botão de imprimir
                    actions += `
                        <button class="botao_acao" onclick="location.href='imprimir_orcamento.jsp?id=${row.id}'" title="Imprimir orçamento">
                            <img src="images/icone_imprimir.svg" alt="Imprimir">
                        </button>
                    `;

                    return actions;
                }
            }
        ],
        "language": {
            "lengthMenu": "Exibindo _MENU_ registros por página",
            "zeroRecords": "Nenhum registro encontrado",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": "Nenhum registro disponível",
            "infoFiltered": "(filtrado de _MAX_ registros no total)",
            "search": "Buscar:",
            "paginate": {
                "first": "<<",
                "last": ">>",
                "next": ">",
                "previous": "<"
            },
            "loadingRecords": "Carregando...",
            "processing": "Processando...",
            "emptyTable": "Sem registros nessa tabela"
        },
        "order": [[0, "desc"]], // Ordenar por ID decrescente (orçamentos mais recentes primeiro)
        "initComplete": function() {
            const tableApi = this.api();
            const searchInput = $('div.dataTables_filter input');
            let debounceTimer;

            $('#input-filter, #input-type').on('change', function() {
                tableApi.ajax.reload(null, false);
            });

            searchInput.off('keyup.DT').on('keyup.DT', function () {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(function() {
                    tableApi.ajax.reload(null, false);
                }, 500);
            });
        }
    });
});

const modal = document.querySelector('dialog');

function openModal() {
  modal.showModal();
}

function closeModal() {
  modal.close();
}

$(document).ready(function() {
  $('.seletor').select2({
    matcher: function(params, data) {
    if ($.trim(params.term) === '') {
      return data;
    }

    const term = params.term.toLowerCase().trim();
    const text = data.text.toLowerCase().trim();
    
    if (text.startsWith(term)) {
      return data;
    }
        

    return null;
  }
      
  });

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
  var quantidade = parseFloat(linha[4].textContent.replace('.', '').replace(',', '.'));
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

// Função para criar novo orçamento
function criarNovoOrcamento() {
  // Fazer requisição POST para criar novo orçamento
  fetch('GerenciarOrcamento', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Erro ao criar orçamento');
    }
    return response.json();
  })
  .then(orcamento => {
    // Redirecionar para a página de registro com o ID do novo orçamento
    window.location.href = `registrar_orcamento.jsp?id=${orcamento.id}`;
  })
  .catch(error => {
    console.error('Erro ao criar orçamento:', error);
    alert('Erro ao criar novo orçamento. Tente novamente.');
  });
}

// Função para alterar orçamento (redireciona para registrar_orcamento.jsp)
function alterarOrcamento(idOrcamento) {
  window.location.href = `alterar_orcamento.jsp?id=${idOrcamento}`;
}

// Função para ver detalhes do orçamento
function verDetalhesOrcamento(idOrcamento) {
  window.location.href = `detalhes_orcamento.jsp?id=${idOrcamento}`;
}

// Função para confirmar e excluir orçamento
function confirmarExclusao(event, idOrcamento) {
  event.preventDefault();

  if (!confirm('Tem certeza que deseja excluir este orçamento?')) {
    return;
  }

  // Fazer requisição DELETE para excluir o orçamento
  fetch(`GerenciarOrcamento?id=${idOrcamento}`, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.ok || response.status === 204) {
      // Recarregar a tabela DataTables
      $('#lista-orcamentos').DataTable().ajax.reload(null, false);
    } else {
      return response.json().then(data => {
        throw new Error(data.error || 'Erro ao excluir orçamento');
      });
    }
  })
  .catch(error => {
    console.error('Erro ao excluir orçamento:', error);
    alert('Erro ao excluir orçamento: ' + error.message);
  });
}

