document.addEventListener('DOMContentLoaded', function () {
    $('#lista-clientes').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": "GerenciarCliente",
            "type": "POST",
            "data": function (d) {
                d.acao = 4;
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
                        <button onclick="openModal('editModal', 'GerenciarCliente?acao=5&id=${row.id}', 'alterar')" class="botao_acao" title="Alterar dados do cliente ${row.nome}">
                            <img src="images/icone_alterar.svg" alt="Alterar">
                        </button>
                        <button onclick="openModal('detailsModal', 'GerenciarCliente?acao=5&id=${row.id}', 'detalhes')" class="botao_acao" title="Detalhes do cliente ${row.nome}">
                            <img src="images/icone_detalhes.svg" alt="Detalhes">
                        </button>
                    `;
                    if (row.id !== 5) {
                        actions += `
                            <button onclick="confirmarExclusao(event, 'GerenciarCliente?id=${row.id}&acao=3')" class="botao_acao" title="Excluir o cliente ${row.nome}">
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
        }
    });
});

function confirmarExclusao(event, url) {
    if (confirm('Tem certeza que deseja excluir este cliente?')) {
        fetch(url, { method: 'POST' })
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

function openModal(modalId, url, tipo) {
    const modal = document.getElementById(modalId);
    let contentDiv;
    let html = '';

    switch (tipo) {
        case 'create':
            contentDiv = document.getElementById('createModalContent');
            html = `
                <h1 class="titulo">Novo Cliente</h1>
                <form action="GerenciarCliente" method="post">
                    <input type="hidden" name="acao" value="1">
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
                        <input type="submit" value="Cadastrar" class="botao_confirma">
                    </div>
                </form>
            `;
            contentDiv.innerHTML = html;
            modal.style.display = 'block';
            break;

        case 'detalhes':
        case 'alterar':
            contentDiv = document.getElementById(modalId === 'detailsModal' ? 'detailsModalContent' : 'editModalContent');
            fetch(url)
                .then(response => response.json())
                .then(data => {
                    const cliente = data.cliente;
                    if (tipo === 'detalhes') {
                        html = `
                            <h1 class="titulo">Detalhes Cliente</h1>
                            <form action="#" method="post">
                                <div class="campos">
                                    <label for="nome" class="titulo_campo">Nome:</label>
                                    <input type="text" value="${cliente.nome}" disabled readonly>
                                </div>
                                <div class="campos">
                                    <label for="telefone" class="titulo_campo">Telefone:</label>
                                    <input type="text" value="${cliente.telefone}" disabled readonly>
                                </div>
                                <div class="campos">
                                    <label for="cpf" class="titulo_campo">CPF:</label>
                                    <input type="text" value="${cliente.cpf || ''}" disabled readonly>
                                </div>
                                <div class="campos">
                                    <label for="endereco" class="titulo_campo">Endereço:</label>
                                    <input type="text" value="${cliente.endereco || ''}" disabled readonly>
                                </div>
                                <div style="display: flex; gap: 10px; margin: 20px;">
                                    <button type="button" class="botao_cancela" onclick="closeModal('detailsModal')">Voltar</button>
                                </div>
                            </form>
                        `;
                    } else { // 'alterar'
                        html = `
                            <h1 class="titulo">Alterar Cliente</h1>
                            <form action="GerenciarCliente" method="post">
                                <input type="hidden" name="acao" value="2">
                                <input type="hidden" name="id" value="${cliente.id}">
                                <div class="campos">
                                    <label for="nome" class="titulo_campo">Nome:</label>
                                    <input type="text" name="nome" value="${cliente.nome}" required>
                                </div>
                                <div class="campos">
                                    <label for="telefone" class="titulo_campo">Telefone:</label>
                                    <input type="text" name="telefone" value="${cliente.telefone}" required>
                                </div>
                                <div class="campos">
                                    <label for="cpf" class="titulo_campo">CPF:</label>
                                    <input type="text" name="cpf" value="${cliente.cpf || ''}">
                                </div>
                                <div class="campos">
                                    <label for="endereco" class="titulo_campo">Endereço:</label>
                                    <input type="text" name="endereco" value="${cliente.endereco || ''}">
                                </div>
                                <div style="display: flex; gap: 10px; margin: 20px;">
                                    <button type="button" class="botao_cancela" onclick="closeModal('editModal')">Cancelar</button>
                                    <input type="submit" value="Salvar Alterações" class="botao_confirma">
                                </div>
                            </form>
                        `;
                    }
                    contentDiv.innerHTML = html;
                    modal.style.display = 'block';
                })
                .catch(error => console.error('Erro ao carregar o conteúdo do modal:', error));
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
