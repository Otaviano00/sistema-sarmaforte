const basePath = "GerenciarVenda";

document.addEventListener('DOMContentLoaded', function () {
    const table = $('#lista-vendas').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": `${basePath}`,
            "type": "GET",
            "data": function (d) {
                d.filterColumn = $('#input-filter').val();
                d.filterType = $('#input-type').val();
            }
        },
        "columns": [
            { "data": "id" },
            {
                "data": "orcamento",
                "render": function (data, type, row) {
                    return data && data.cliente ? data.cliente.nome : '---';
                }
            },
            {
                "data": "usuario",
                "render": function (data, type, row) {
                    return data ? data.nome : 'EXCLUÍDO';
                }
            },
            {
                "data": "data",
                "render": function (data, type, row) {
                    if (data) {
                        const date = new Date(data);
                        return date.toLocaleDateString('pt-BR');
                    }
                    return '---';
                }
            },
            {
                "data": "valor",
                "render": function (data, type, row) {
                    return `R$ ${parseFloat(data).toFixed(3).replace('.', ',')}`;
                }
            },
            { "data": "formaPagamento" },
            {
                "data": null,
                "orderable": false,
                "searchable": false,
                "render": function (data, type, row) {
                    const hierarquia = parseInt(document.getElementById('hierarquia-value')?.value || 2);
                    let actions = '';

                    // Botão de alterar - apenas se hierarquia < 2 e orçamento não concluído
                    if (hierarquia < 2 && row.orcamento && row.orcamento.status !== 'Concluído') {
                        actions += `
                            <button onclick="openModal('editModal', ${row.id}, 'alterar')" class="botao_acao" title="Alterar venda">
                                <img src="images/icone_alterar.svg" alt="Alterar">
                            </button>
                        `;
                    }

                    // Botão de detalhes - sempre visível
                    actions += `
                        <button onclick="openModal('detailsModal', ${row.id}, 'detalhes')" class="botao_acao" title="Detalhes da venda">
                            <img src="images/icone_detalhes.svg" alt="Detalhes">
                        </button>
                    `;

                    // Botão de excluir - apenas para hierarquia 0
                    if (hierarquia === 0) {
                        actions += `
                            <button onclick="confirmarExclusao(event, ${row.id})" class="botao_acao" title="Excluir venda">
                                <img src="images/icone_excluir.svg" alt="Excluir">
                            </button>
                        `;
                    }

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
        "order": [[0, "desc"]], // Ordenar por ID decrescente (vendas mais recentes primeiro)
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

function confirmarExclusao(event, id) {
    if (confirm('Tem certeza que deseja excluir esta venda?')) {
        fetch(`${basePath}?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    $('#lista-vendas').DataTable().ajax.reload(null, false);
                } else {
                    alert('Erro ao excluir a venda.');
                }
            })
            .catch(error => console.error('Erro ao excluir a venda:', error));
    }
}

async function openModal(modalId, id, tipo) {
    const modal = document.getElementById(modalId);
    const contentDiv = document.getElementById(modalId.replace('Modal', 'ModalContent'));
    let html = '';

    switch (tipo) {
        case 'alterar':
            // Redirecionar para página de alteração (mantém a lógica existente)
            window.location.href = `alterar_venda.jsp?id=${id}`;
            return;

        case 'detalhes':
            try {
                const response = await fetch(`${basePath}?id=${id}`);
                const venda = await response.json();

                html = `
                    <div class="modal-header">
                        <span class="close-btn" onclick="closeModal('detailsModal')">&times;</span>
                        <h1 class="titulo">Detalhes Venda</h1>
                    </div>
                    <div class="modal-body">
                        <div class="form">
                            <div class="campos"><label class="titulo_campo">ID:</label> <input type="text" value="${venda.id}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Cliente:</label> <input type="text" value="${venda.orcamento?.cliente?.nome || '---'}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Vendedor:</label> <input type="text" value="${venda.usuario?.nome || 'EXCLUÍDO'}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Data:</label> <input type="text" value="${new Date(venda.data).toLocaleDateString('pt-BR')}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Valor:</label> <input type="text" value="R$ ${parseFloat(venda.valor).toFixed(3)}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Desconto:</label> <input type="text" value="R$ ${parseFloat(venda.desconto).toFixed(3)}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Forma de Pagamento:</label> <input type="text" value="${venda.formaPagamento}" disabled></div>
                            
                            <!-- Campo Expansível para Itens -->
                            <div class="campos">
                                <button type="button" class="botao_expansivel" onclick="toggleItensOrcamento(${venda.id})">
                                    <span>📦 Ver itens do orçamento</span>
                                    <span id="arrow-${venda.id}" class="arrow-icon">▼</span>
                                </button>
                                <div id="itens-container-${venda.id}" class="itens-container">
                                    <table id="itens-table-${venda.id}" class="display" style="width: 100%; font-size: 0.9em;">
                                        <thead>
                                            <tr>
                                                <th>Produto</th>
                                                <th>Quantidade</th>
                                                <th>Preço Un.</th>
                                                <th>Total</th>
                                                <th>Vendido</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="botao_cancela" onclick="closeModal('detailsModal')">Voltar</button>
                    </div>
                `;
                contentDiv.innerHTML = html;
                modal.style.display = 'flex';
            } catch (error) {
                console.error('Erro ao carregar dados da venda:', error);
                alert('Erro ao carregar detalhes da venda.');
            }
            break;
    }
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function toggleItensOrcamento(vendaId) {
    const container = document.getElementById(`itens-container-${vendaId}`);
    const arrow = document.getElementById(`arrow-${vendaId}`);
    const tableId = `itens-table-${vendaId}`;
    const isExpanded = container.classList.contains('expanded');

    if (!isExpanded) {
        // Expandir com animação suave
        container.classList.add('expanded');
        arrow.classList.add('rotated');

        // Inicializar DataTable se ainda não foi criado
        if (!$.fn.DataTable.isDataTable(`#${tableId}`)) {
            $(`#${tableId}`).DataTable({
                ajax: {
                    url: basePath,
                    type: 'GET',
                    data: {
                        id: vendaId,
                        itens: true
                    },
                    dataSrc: ''  // Dados vêm diretamente como array
                },
                columns: [
                    {
                        data: 'produto',
                        render: function(data) {
                            return data ? data.nome : '---';
                        }
                    },
                    {
                        data: 'quantidade',
                        render: function(data) {
                            return parseFloat(data).toFixed(2);
                        }
                    },
                    {
                        data: 'preco',
                        render: function(data) {
                            return `R$ ${parseFloat(data).toFixed(3)}`;
                        }
                    },
                    {
                        data: null,
                        render: function(data) {
                            const total = data.quantidade * data.preco;
                            return `R$ ${parseFloat(total).toFixed(3)}`;
                        }
                    },
                    {
                        data: 'statusVenda',
                        render: function(data) {
                            return data ?
                                '<span style="color: green; font-weight: bold;">✓ Sim</span>' :
                                '<span style="color: red; font-weight: bold;">✗ Não</span>';
                        }
                    }
                ],
                language: {
                    lengthMenu: "Exibindo _MENU_ registros por página",
                    zeroRecords: "Nenhum item encontrado",
                    info: "Mostrando página _PAGE_ de _PAGES_",
                    infoEmpty: "Nenhum registro disponível",
                    infoFiltered: "(filtrado de _MAX_ registros no total)",
                    search: "Buscar:",
                    paginate: {
                        first: "<<",
                        last: ">>",
                        next: ">",
                        previous: "<"
                    },
                    loadingRecords: "Carregando...",
                    processing: "Processando...",
                    emptyTable: "Nenhum item no orçamento"
                },
                pageLength: 5,
                lengthMenu: [[5, 10, 25, -1], [5, 10, 25, "Todos"]],
                order: [[0, 'asc']],
                processing: true
            });
        }
    } else {
        // Recolher com animação suave
        container.classList.remove('expanded');
        arrow.classList.remove('rotated');
    }
}

window.onclick = function(event) {
    const detailsModal = document.getElementById('detailsModal');
    const editModal = document.getElementById('editModal');
    if (event.target == detailsModal) {
        detailsModal.style.display = "none";
    }
    if (event.target == editModal) {
        editModal.style.display = "none";
    }
}

