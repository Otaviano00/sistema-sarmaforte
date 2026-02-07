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
    const contentDivId = modalId.replace('Modal', 'ModalContent');
    const contentDiv = document.getElementById(contentDivId);
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
                    <div class="form">
                        <h1 class="titulo">Detalhes Venda</h1>
                        <div class="campos"><label class="titulo_campo">ID:</label> <input type="text" value="${venda.id}" disabled></div>
                        <div class="campos"><label class="titulo_campo">Cliente:</label> <input type="text" value="${venda.orcamento?.cliente?.nome || '---'}" disabled></div>
                        <div class="campos"><label class="titulo_campo">Vendedor:</label> <input type="text" value="${venda.usuario?.nome || 'EXCLUÍDO'}" disabled></div>
                        <div class="campos"><label class="titulo_campo">Data:</label> <input type="text" value="${new Date(venda.data).toLocaleDateString('pt-BR')}" disabled></div>
                        <div class="campos"><label class="titulo_campo">Valor:</label> <input type="text" value="R$ ${parseFloat(venda.valor).toFixed(3)}" disabled></div>
                        <div class="campos"><label class="titulo_campo">Desconto:</label> <input type="text" value="R$ ${parseFloat(venda.desconto).toFixed(3)}" disabled></div>
                        <div class="campos"><label class="titulo_campo">Forma de Pagamento:</label> <input type="text" value="${venda.formaPagamento}" disabled></div>
                        <div style="display: flex; gap: 10px; margin: 20px;">
                            <button type="button" class="botao_cancela" onclick="closeModal('detailsModal')">Voltar</button>
                        </div>
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

