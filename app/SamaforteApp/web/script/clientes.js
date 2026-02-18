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
                d.isPaginado = true;
            }
        },
        "columns": [
            { "data": "id", "defaultContent": "---" },
            { "data": "nome", "defaultContent": "---" },
            { "data": "telefone", "defaultContent": "---" },
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
    if (confirm('Tem certeza que deseja excluir este cliente?')) {
        fetch(`${basePath}?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    $('#lista-clientes').DataTable().ajax.reload(null, false);
                } else {
                    alert('Erro ao excluir o cliente.');
                }
            })
            .catch(error => console.error('Erro ao excluir o cliente:', error));
    }
}

async function openModal(modalId, id, tipo) {
    const modal = document.getElementById(modalId);
    const contentDiv = document.getElementById(modalId.replace('Modal', 'ModalContent'));
    let html = '';

    switch (tipo) {
        case 'create':
            html = `
                <button class="botao_fechar_modal" onclick="closeModal('createModal')" title="Fechar">×</button>
                <div class="modal-content">
                    <div class="modal-body">
                        <h2>Novo Cliente</h2>
                        <form id="createForm">
                            <div class="form-group">
                                <label for="nome" class="form-label">Nome <span style="color: red;">*</span></label>
                                <input type="text" name="nome" class="form-input" required>
                            </div>
                            <div class="form-group">
                                <label for="telefone" class="form-label">Telefone</label>
                                <input type="text" name="telefone" class="form-input">
                            </div>
                            <div class="form-group">
                                <label for="cpf" class="form-label">CPF</label>
                                <input type="text" name="cpf" class="form-input">
                            </div>
                            <div class="form-group">
                                <label for="endereco" class="form-label">Endereço</label>
                                <input type="text" name="endereco" class="form-input">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" onclick="closeModal('createModal')">Cancelar</button>
                        <button type="submit" form="createForm" class="btn btn-primary">
                            <span>+</span> Cadastrar
                        </button>
                    </div>
                </div>
            `;
            contentDiv.innerHTML = html;
            modal.showModal();

            document.getElementById('createForm').addEventListener('submit', async function (e) {
                e.preventDefault();
                const formData = new FormData(this);
                const cliente = Object.fromEntries(formData.entries());

                if (cliente.telefone && cliente.telefone.trim() === '') {
                    cliente.telefone = null;
                }
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
                        $('#lista-clientes').DataTable().ajax.reload(null, false);
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
                        <button class="botao_fechar_modal" onclick="closeModal('detailsModal')" title="Fechar">×</button>
                        <div class="modal-content">
                            <div class="modal-body">
                                <h2>Detalhes Cliente</h2>
                                <div class="form-group">
                                    <label class="form-label">Nome</label>
                                    <input type="text" value="${cliente.nome}" class="form-input" disabled>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Telefone</label>
                                    <input type="text" value="${cliente.telefone}" class="form-input" disabled>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">CPF</label>
                                    <input type="text" value="${cliente.cpf || ''}" class="form-input" disabled>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Endereço</label>
                                    <input type="text" value="${cliente.endereco || ''}" class="form-input" disabled>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" onclick="closeModal('detailsModal')">Voltar</button>
                            </div>
                        </div>
                    `;
                } else { // 'alterar'
                    html = `
                        <button class="botao_fechar_modal" onclick="closeModal('editModal')" title="Fechar">×</button>
                        <div class="modal-content">
                            <div class="modal-body">
                                <h2>Alterar Cliente</h2>
                                <form id="editForm">
                                    <input type="hidden" name="id" value="${cliente.id}">
                                    <div class="form-group">
                                        <label class="form-label">Nome <span style="color: red;">*</span></label>
                                        <input type="text" name="nome" value="${cliente.nome}" class="form-input" required>
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Telefone</label>
                                        <input type="text" name="telefone" value="${cliente.telefone || ''}" class="form-input">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">CPF</label>
                                        <input type="text" name="cpf" value="${cliente.cpf || ''}" class="form-input">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Endereço</label>
                                        <input type="text" name="endereco" value="${cliente.endereco || ''}" class="form-input">
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" onclick="closeModal('editModal')">Cancelar</button>
                                <button type="submit" form="editForm" class="btn btn-primary">
                                    <span>✓</span> Salvar Alterações
                                </button>
                            </div>
                        </div>
                    `;
                }
                contentDiv.innerHTML = html;
                modal.showModal();

                if (tipo === 'alterar') {
                    document.getElementById('editForm').addEventListener('submit', async function (e) {
                        e.preventDefault();
                        const formData = new FormData(this);
                        const clienteData = Object.fromEntries(formData.entries());

                        if (clienteData.telefone && clienteData.telefone.trim() === '') {
                            clienteData.telefone = null;
                        }
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
                                $('#lista-clientes').DataTable().ajax.reload(null, false);
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
    document.getElementById(modalId).close();
}
