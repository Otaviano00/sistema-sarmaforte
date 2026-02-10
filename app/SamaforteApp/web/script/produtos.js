const basePath = "GerenciarProduto";

document.addEventListener('DOMContentLoaded', function () {
    const table = $('#lista-produtos').DataTable({
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
            { "data": "codigo" },
            { "data": "codigo" },
            { "data": "nome" },
            { "data": "fornecedor" },
            { "data": "quantidade" },
            {
                "data": "preco",
                "render": function (data, type, row) {
                    return `R$ ${parseFloat(data).toFixed(3).replace('.', ',')}`;
                }
            },
            {
                "data": "status",
                "orderable": false,
                "render": function (data, type, row) {
                    const hierarquia = parseInt(document.getElementById('hierarquia-value')?.value || 2);
                    if (hierarquia < 2) {
                        if (data === true || data === 'true' || data === 1) {
                            return `<button onclick="alterarStatus('${row.codigo}', false)" class="botao_acao botao_ativo" title="Clique para desativar o produto ${row.nome}">Ativo</button>`;
                        } else {
                            return `<button onclick="alterarStatus('${row.codigo}', true)" class="botao_acao botao_desativo" title="Clique para ativar o produto ${row.nome}">Desativo</button>`;
                        }
                    } else {
                        return `<button class="botao_acao botao_ativo">Ativo</button>`;
                    }
                }
            },
            {
                "data": null,
                "orderable": false,
                "searchable": false,
                "render": function (data, type, row) {
                    const hierarquia = parseInt(document.getElementById('hierarquia-value')?.value || 2);
                    let actions = '';

                    if (hierarquia < 2) {
                        actions += `
                            <button onclick="openModal('editModal', '${row.codigo}', 'alterar')" class="botao_acao" title="Alterar dados do produto ${row.nome}">
                                <img src="images/icone_alterar.svg" alt="Alterar">
                            </button>
                        `;
                    }

                    actions += `
                        <button onclick="openModal('detailsModal', '${row.codigo}', 'detalhes')" class="botao_acao" title="Detalhes sobre o produto ${row.nome}">
                            <img src="images/icone_detalhes.svg" alt="Detalhes">
                        </button>
                    `;

                    if (hierarquia < 2) {
                        actions += `
                            <button onclick="confirmarExclusao(event, '${row.codigo}')" class="botao_acao" title="Excluir o produto ${row.nome}">
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

function alterarStatus(codigo, novoStatus) {
    fetch(`${basePath}?codigo=${codigo}&status=${novoStatus}`, { method: 'PUT' })
        .then(response => {
            if (response.ok) {
                $('#lista-produtos').DataTable().ajax.reload(null, false);
            } else {
                alert('Erro ao alterar status do produto.');
            }
        })
        .catch(error => console.error('Erro ao alterar status:', error));
}

function confirmarExclusao(event, codigo) {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
        fetch(`${basePath}?codigo=${codigo}&acao=5`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    $('#lista-produtos').DataTable().ajax.reload(null, false);
                } else {
                    alert('Erro ao excluir o produto.');
                }
            })
            .catch(error => console.error('Erro ao excluir o produto:', error));
    }
}

async function openModal(modalId, codigo, tipo) {
    const modal = document.getElementById(modalId);
    const contentDiv = document.getElementById(modalId.replace('Modal', 'ModalContent'));
    let html = '';

    switch (tipo) {
        case 'create':
            html = `
                <div class="modal-header">
                    <span class="close-btn" onclick="closeModal('createModal')">&times;</span>
                    <h1 class="titulo">Novo Produto</h1>
                </div>
                <div class="modal-body">
                    <div class="form">
                        <form id="createForm">
                            <div class="campos">
                                <label for="codigo" class="titulo_campo">Código:</label>
                                <input type="text" name="codigo" required>
                            </div>
                            <div class="campos">
                                <label for="nome" class="titulo_campo">Nome:</label>
                                <input type="text" name="nome" required>
                            </div>
                            <div class="campos">
                                <label for="fornecedor" class="titulo_campo">Fornecedor:</label>
                                <input type="text" name="fornecedor" required>
                            </div>
                            <div class="campos">
                                <label for="quantidade" class="titulo_campo">Quantidade:</label>
                                <input type="number" name="quantidade" required>
                            </div>
                            <div class="campos">
                                <label for="preco" class="titulo_campo">Preço:</label>
                                <input type="number" step="0.001" name="preco" required>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="botao_cancela" onclick="closeModal('createModal')">Cancelar</button>
                    <button type="submit" form="createForm" class="botao_confirma">Cadastrar</button>
                </div>
            `;
            contentDiv.innerHTML = html;
            modal.style.display = 'flex';

            document.getElementById('createForm').addEventListener('submit', async function (e) {
                e.preventDefault();
                const formData = new FormData(this);
                const produto = Object.fromEntries(formData.entries());

                // Adicionar valores padrão para campos não presentes no formulário
                produto.descricao = produto.descricao || '';
                produto.quantidadeCritica = produto.quantidadeCritica || 0;
                produto.imagem = produto.imagem || '';
                produto.custo = produto.custo || 0;

                // Converter campos numéricos
                produto.quantidade = parseInt(produto.quantidade);
                produto.quantidadeCritica = parseInt(produto.quantidadeCritica);
                produto.preco = parseFloat(produto.preco);
                produto.custo = parseFloat(produto.custo);

                try {
                    const response = await fetch(basePath, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(produto)
                    });
                    if (response.ok) {
                        closeModal('createModal');
                        $('#lista-produtos').DataTable().ajax.reload(null, false);
                    } else {
                        alert('Erro ao cadastrar produto.');
                    }
                } catch (error) {
                    console.error('Erro ao cadastrar produto:', error);
                }
            });
            break;

        case 'detalhes':
        case 'alterar':
            try {
                const response = await fetch(`${basePath}?codigo=${codigo}`);
                const produto = await response.json();

                if (tipo === 'detalhes') {
                    html = `
                        <div class="modal-header">
                            <span class="close-btn" onclick="closeModal('detailsModal')">&times;</span>
                            <h1 class="titulo">Detalhes Produto</h1>
                        </div>
                        <div class="modal-body">
                            <div class="form">
                                <div class="campos"><label class="titulo_campo">Código:</label> <input type="text" value="${produto.codigo}" disabled></div>
                                <div class="campos"><label class="titulo_campo">Nome:</label> <input type="text" value="${produto.nome}" disabled></div>
                                <div class="campos"><label class="titulo_campo">Fornecedor:</label> <input type="text" value="${produto.fornecedor}" disabled></div>
                                <div class="campos"><label class="titulo_campo">Quantidade:</label> <input type="text" value="${produto.quantidade}" disabled></div>
                                <div class="campos"><label class="titulo_campo">Preço:</label> <input type="text" value="R$ ${parseFloat(produto.preco).toFixed(3)}" disabled></div>
                                <div class="campos"><label class="titulo_campo">Status:</label> <input type="text" value="${produto.status ? 'Ativo' : 'Desativo'}" disabled></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="botao_cancela" onclick="closeModal('detailsModal')">Voltar</button>
                        </div>
                    `;
                } else { // 'alterar'
                    html = `
                        <div class="modal-header">
                            <span class="close-btn" onclick="closeModal('editModal')">&times;</span>
                            <h1 class="titulo">Alterar Produto</h1>
                        </div>
                        <div class="modal-body">
                            <div class="form">
                                <form id="editForm">
                                    <input type="hidden" name="codigo" value="${produto.codigo}">
                                    <input type="hidden" name="status" value="${produto.status}">
                                    <input type="hidden" name="descricao" value="${produto.descricao || ''}">
                                    <input type="hidden" name="quantidadeCritica" value="${produto.quantidadeCritica || 0}">
                                    <input type="hidden" name="imagem" value="${produto.imagem || ''}">
                                    <input type="hidden" name="custo" value="${produto.custo || 0}">
                                    <div class="campos"><label class="titulo_campo">Código:</label> <input type="text" value="${produto.codigo}" disabled></div>
                                    <div class="campos"><label class="titulo_campo">Nome:</label> <input type="text" name="nome" value="${produto.nome}" required></div>
                                    <div class="campos"><label class="titulo_campo">Fornecedor:</label> <input type="text" name="fornecedor" value="${produto.fornecedor}" required></div>
                                    <div class="campos"><label class="titulo_campo">Quantidade:</label> <input type="number" name="quantidade" value="${produto.quantidade}" required></div>
                                    <div class="campos"><label class="titulo_campo">Preço:</label> <input type="number" step="0.001" name="preco" value="${produto.preco}" required></div>
                                </form>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="botao_cancela" onclick="closeModal('editModal')">Cancelar</button>
                            <button type="submit" form="editForm" class="botao_confirma">Salvar Alterações</button>
                        </div>
                    `;
                }
                contentDiv.innerHTML = html;
                modal.style.display = 'flex';

                if (tipo === 'alterar') {
                    document.getElementById('editForm').addEventListener('submit', async function (e) {
                        e.preventDefault();
                        const formData = new FormData(this);
                        const produtoData = Object.fromEntries(formData.entries());

                        // Converter status de string para boolean
                        produtoData.status = produtoData.status === 'true' || produtoData.status === true;

                        // Converter campos numéricos
                        produtoData.codigo = parseInt(produtoData.codigo);
                        produtoData.quantidade = parseInt(produtoData.quantidade);
                        produtoData.quantidadeCritica = parseInt(produtoData.quantidadeCritica);
                        produtoData.preco = parseFloat(produtoData.preco);
                        produtoData.custo = parseFloat(produtoData.custo);

                        try {
                            const putResponse = await fetch(basePath, {
                                method: 'PUT',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(produtoData)
                            });
                            if (putResponse.ok) {
                                closeModal('editModal');
                                $('#lista-produtos').DataTable().ajax.reload(null, false);
                            } else {
                                alert('Erro ao atualizar produto.');
                            }
                        } catch (error) {
                            console.error('Erro ao atualizar produto:', error);
                        }
                    });
                }
            } catch (error) {
                console.error('Erro ao carregar dados do produto:', error);
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

