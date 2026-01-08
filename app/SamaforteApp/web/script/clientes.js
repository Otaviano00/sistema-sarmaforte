const basePath = "GerenciarCliente";

document.addEventListener('DOMContentLoaded', function () {
    const table = $('#lista-clientes').DataTable({
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
            { "data": "nome" },
            { "data": "telefone" },
            { "data": "endereco", "defaultContent": "---" },
            { "data": "cpf", "defaultContent": "---" },
            {
                "data": null,
                "orderable": false,
                "searchable": false,
                "render": function (data, type, row) {
                    let actions = `
                        <button onclick="openModal('editModal', ${row.id}, 'alterar')" class="botao_acao" title="Alterar dados do cliente ${row.nome}">
                            <img src="images/icone_alterar.svg" alt="Alterar">
                        </button>
                        <button onclick="openModal('detailsModal', ${row.id}, 'detalhes')" class="botao_acao" title="Detalhes do cliente ${row.nome}">
                            <img src="images/icone_detalhes.svg" alt="Detalhes">
                        </button>
                    `;
                    if (row.id !== 5) {
                        actions += `
                            <button onclick="confirmarExclusao(event, ${row.id})" class="botao_acao" title="Excluir o cliente ${row.nome}">
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
        "initComplete": function() {
            const tableApi = this.api();
            const searchInput = $('div.dataTables_filter input');
            let debounceTimer;

            $('#input-filter, #input-type').on('change', function() {
                tableApi.ajax.reload();
            });

            searchInput.off('keyup.DT').on('keyup.DT', function () {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(function() {
                    tableApi.ajax.reload();
                }, 500);
            });
        }
    });
});

function confirmarExclusao(event, id) {
    if (confirm('Tem certeza que deseja excluir este cliente?')) {
        fetch(`${basePath}?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    $('#lista-clientes').DataTable().ajax.reload();
                } else {
                    alert('Erro ao excluir o cliente.');
                }
            })
            .catch(error => console.error('Erro ao excluir o cliente:', error));
    }
}

async function openModal(modalId, id, tipo) {
    const modal = document.getElementById(modalId);
    const contentDivId = modalId.replace('Modal', 'ModalContent');
    const contentDiv = document.getElementById(contentDivId);
    let html = '';

    switch (tipo) {
        case 'create':
            html = `
                <div class="form">
                    <h1 class="titulo">Novo Cliente</h1>
                    <form id="createForm">
                        <div class="campos">
                            <label for="nome" class="titulo_campo">Nome:</label>
                            <input type="text" name="nome" required>
                        </div>
                        <div class="campos">
                            <label for="telefone" class="titulo_campo">Telefone:</label>
                            <input type="text" name="telefone" required>
                        </div>
                        <div class="campos">
                            <label for="cpf" class="titulo_campo">CPF:</label>
                            <input type="text" name="cpf">
                        </div>
                        <div class="campos">
                            <label for="endereco" class="titulo_campo">Endereço:</label>
                            <input type="text" name="endereco">
                        </div>
                        <div style="display: flex; gap: 10px; margin: 20px;">
                            <button type="button" class="botao_cancela" onclick="closeModal('createModal')">Cancelar</button>
                            <button type="submit" class="botao_confirma">Cadastrar</button>
                        </div>
                    </form>
                </div>
            `;
            contentDiv.innerHTML = html;
            modal.style.display = 'flex';

            document.getElementById('createForm').addEventListener('submit', async function (e) {
                e.preventDefault();
                const formData = new FormData(this);
                const cliente = Object.fromEntries(formData.entries());

                if (cliente.cpf && cliente.cpf.trim() === '') {
                    cliente.cpf = null;
                }
                if (cliente.endereco && cliente.endereco.trim() === '') {
                    cliente.endereco = null;
                }

                try {
                    const response = await fetch(basePath, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(cliente)
                    });
                    if (response.ok) {
                        closeModal('createModal');
                        $('#lista-clientes').DataTable().ajax.reload();
                    } else {
                        alert('Erro ao cadastrar cliente.');
                    }
                } catch (error) {
                    console.error('Erro ao cadastrar cliente:', error);
                }
            });
            break;

        case 'detalhes':
        case 'alterar':
            try {
                const response = await fetch(`${basePath}?id=${id}`);
                const cliente = await response.json();

                if (tipo === 'detalhes') {
                    html = `
                        <div class="form">
                            <h1 class="titulo">Detalhes Cliente</h1>
                            <div class="campos"><label class="titulo_campo">Nome:</label> <input type="text" value="${cliente.nome}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Telefone:</label> <input type="text" value="${cliente.telefone}" disabled></div>
                            <div class="campos"><label class="titulo_campo">CPF:</label> <input type="text" value="${cliente.cpf || ''}" disabled></div>
                            <div class="campos"><label class="titulo_campo">Endereço:</label> <input type="text" value="${cliente.endereco || ''}" disabled></div>
                            <div style="display: flex; gap: 10px; margin: 20px;">
                                <button type="button" class="botao_cancela" onclick="closeModal('detailsModal')">Voltar</button>
                            </div>
                        </div>
                    `;
                } else { // 'alterar'
                    html = `
                        <div class="form">
                            <h1 class="titulo">Alterar Cliente</h1>
                            <form id="editForm">
                                <input type="hidden" name="id" value="${cliente.id}">
                                <div class="campos"><label class="titulo_campo">Nome:</label> <input type="text" name="nome" value="${cliente.nome}" required></div>
                                <div class="campos"><label class="titulo_campo">Telefone:</label> <input type="text" name="telefone" value="${cliente.telefone}" required></div>
                                <div class="campos"><label class="titulo_campo">CPF:</label> <input type="text" name="cpf" value="${cliente.cpf || ''}"></div>
                                <div class="campos"><label class="titulo_campo">Endereço:</label> <input type="text" name="endereco" value="${cliente.endereco || ''}"></div>
                                <div style="display: flex; gap: 10px; margin: 20px;">
                                    <button type="button" class="botao_cancela" onclick="closeModal('editModal')">Cancelar</button>
                                    <button type="submit" class="botao_confirma">Salvar Alterações</button>
                                </div>
                            </form>
                        </div>
                    `;
                }
                contentDiv.innerHTML = html;
                modal.style.display = 'flex';

                if (tipo === 'alterar') {
                    document.getElementById('editForm').addEventListener('submit', async function (e) {
                        e.preventDefault();
                        const formData = new FormData(this);
                        const clienteData = Object.fromEntries(formData.entries());

                        if (clienteData.cpf && clienteData.cpf.trim() === '') {
                            clienteData.cpf = null;
                        }
                        if (clienteData.endereco && clienteData.endereco.trim() === '') {
                            clienteData.endereco = null;
                        }

                        try {
                            const putResponse = await fetch(basePath, {
                                method: 'PUT',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(clienteData)
                            });
                            if (putResponse.ok) {
                                closeModal('editModal');
                                $('#lista-clientes').DataTable().ajax.reload();
                            } else {
                                alert('Erro ao atualizar cliente.');
                            }
                        } catch (error) {
                            console.error('Erro ao atualizar cliente:', error);
                        }
                    });
                }
            } catch (error) {
                console.error('Erro ao carregar dados do cliente:', error);
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
    const createModal = document.getElementById('createModal');
    if (event.target == detailsModal) {
        detailsModal.style.display = "none";
    }
    if (event.target == editModal) {
        editModal.style.display = "none";
    }
    if (event.target == createModal) {
        createModal.style.display = "none";
    }
}
