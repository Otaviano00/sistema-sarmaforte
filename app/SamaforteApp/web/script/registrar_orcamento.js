const modal = document.querySelector('dialog');
let idOrcamento = null;
let tabelaItens = null;

// Função para obter ID do orçamento da URL
function getOrcamentoId() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
}

// Inicialização quando o DOM estiver pronto
$(document).ready(function() {
    idOrcamento = getOrcamentoId();

    if (!idOrcamento) {
        alert('ID do orçamento não encontrado!');
        window.location.href = 'orcamentos.jsp';
        return;
    }

    // Inicializar Select2 para produtos
    inicializarSeletorProdutos();

    // Inicializar Select2 para clientes
    inicializarSeletorClientes();

    // Carregar dados do orçamento
    carregarDadosOrcamento();

    // Inicializar DataTable para itens
    inicializarTabelaItens();

    // Configurar eventos de formulários
    configurarEventos();
});

// Inicializar seletor de produtos com Select2
function inicializarSeletorProdutos() {
    $('#seletor_produto').select2({
        placeholder: 'Selecione um produto',
        allowClear: true,
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

    // Carregar produtos via API
    fetch('GerenciarProduto')
        .then(response => response.json())
        .then(data => {
            // GerenciarProduto retorna array direto, não objeto com data
            const produtos = Array.isArray(data) ? data : (data.data || []);
            const select = $('#seletor_produto');

            select.empty();
            select.append('<option value="">Selecione um produto</option>');

            produtos.forEach(produto => {
                const preco = produto.preco.toFixed(3).replace('.', ',');
                const texto = `${produto.nome} --- R$ ${preco}`;
                const option = new Option(texto, produto.codigo, false, false);
                select.append(option);
            });

            select.trigger('change');
        })
        .catch(error => {
            console.error('Erro ao carregar produtos:', error);
            alert('Erro ao carregar produtos!');
        });
}

// Inicializar seletor de clientes com Select2
function inicializarSeletorClientes() {
    $('#seletor_cliente').select2({
        placeholder: 'Selecione um cliente',
        allowClear: true,
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

    // Carregar clientes via API
    fetch('GerenciarCliente')
        .then(response => response.json())
        .then(data => {
            // GerenciarCliente retorna array direto, não objeto com data
            const clientes = Array.isArray(data) ? data : (data.data || []);
            const select = $('#seletor_cliente');

            select.empty();
            select.append('<option value="">Selecione um cliente</option>');

            clientes.forEach(cliente => {
                const option = new Option(cliente.nome, cliente.id, false, false);
                select.append(option);
            });

            select.trigger('change');
        })
        .catch(error => {
            console.error('Erro ao carregar clientes:', error);
            alert('Erro ao carregar clientes!');
        });

    // Evento de mudança no seletor de clientes
    $('#seletor_cliente').on('change', function() {
        var idCliente = $(this).val();

        if (!idCliente) {
            idCliente = 5;
        }

        // Fazer fetch para buscar dados completos do cliente
        fetch(`GerenciarCliente?id=${idCliente}`)
            .then(response => response.json())
            .then(cliente => {
                if (cliente.error) {
                    alert('Erro ao carregar dados do cliente: ' + cliente.error);
                    return;
                }

                $('#id_cliente').val(cliente.id);

                // Preencher campos readonly com os dados do servidor
                const campos = $('.campo_cliente input');
                $(campos[0]).val(cliente.nome || '---');
                $(campos[1]).val(cliente.telefone || '---');
                $(campos[2]).val(cliente.cpf || '---');
                $(campos[3]).val(cliente.endereco || '---');
            })
            .catch(error => {
                console.error('Erro ao carregar cliente:', error);
                alert('Erro ao carregar dados do cliente!');
            });
    });
}

// Carregar dados do orçamento
function carregarDadosOrcamento() {
    fetch(`GerenciarOrcamento?id=${idOrcamento}&attr=NO_ATTR`)
        .then(response => response.json())
        .then(orcamento => {
            if (orcamento.error) {
                alert('Erro ao carregar orçamento: ' + orcamento.error);
                window.location.href = 'orcamentos.jsp';
                return;
            }

            // Preencher informações
            $('#informacao').val(orcamento.informacao || '');

            // Selecionar cliente
            if (orcamento.cliente && orcamento.cliente.id) {
                $('#seletor_cliente').val(orcamento.cliente.id).trigger('change');
            }
        })
        .catch(error => {
            console.error('Erro ao carregar orçamento:', error);
        });
}

// Inicializar DataTable para itens do orçamento
function inicializarTabelaItens() {
    // Verificar se a tabela já foi inicializada
    if ($.fn.DataTable.isDataTable('#tabela-itens')) {
        // Se já existe, destruir antes de recriar
        $('#tabela-itens').DataTable().destroy();
    }

    tabelaItens = $('#tabela-itens').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": `GerenciarItemOrcamento`,
            "type": "GET",
            "data": function (d) {
                d.idOrcamento = idOrcamento;
                d.filterColumn = '';
                d.filterType = '';
            },
            "dataSrc": function(json) {
                atualizarTotalGeral(json.data);
                return json.data;
            }
        },
        "columns": [
            {
                "data": null,
                "orderable": false,
                "searchable": false,
                "render": function (data, type, row, meta) {
                    return meta.row + 1;
                }
            },
            { "data": "produto.codigo" },
            {
                "data": "dataHora",
                "render": function(data) {
                    if (!data) return '---';
                    const date = new Date(data);
                    return date.toLocaleDateString('pt-BR');
                }
            },
            { "data": "produto.nome" },
            {
                "data": "quantidade",
                "render": function(data) {
                    return parseFloat(data).toFixed(3).replace('.', ',');
                }
            },
            {
                "data": "preco",
                "render": function(data) {
                    return 'R$ ' + parseFloat(data).toFixed(3).replace('.', ',');
                }
            },
            {
                "data": null,
                "render": function(data, type, row) {
                    const valor = row.quantidade * row.preco;
                    return 'R$ ' + valor.toFixed(3).replace('.', ',');
                }
            },
            {
                "data": "statusVenda",
                "render": function(data) {
                    return data ? 'Vendido' : 'Aberto';
                }
            },
            {
                "data": null,
                "orderable": false,
                "searchable": false,
                "render": function (data, type, row, meta) {
                    return `
                        <button onclick="alterarItem(${meta.row}, ${row.id})" class="botao_acao" title="Alterar item">
                            <img src="images/icone_alterar.svg" alt="Alterar">
                        </button>
                        <button onclick="excluirItem(${row.id})" class="botao_acao" title="Excluir item">
                            <img src="images/icone_excluir.svg" alt="Excluir">
                        </button>
                    `;
                }
            }
        ],
        "language": {
            "lengthMenu": "Exibindo _MENU_ registros por página",
            "zeroRecords": "Nenhum item no orçamento",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": "Nenhum item disponível",
            "infoFiltered": "(filtrado de _MAX_ itens no total)",
            "search": "Buscar:",
            "paginate": {
                "first": "<<",
                "last": ">>",
                "next": ">",
                "previous": "<"
            },
            "loadingRecords": "Carregando...",
            "processing": "Processando...",
            "emptyTable": "Sem itens neste orçamento"
        },
        "order": [[1, 'asc']],
        "pageLength": 10
    });
}

// Atualizar total geral
function atualizarTotalGeral(itens) {
    let total = 0;
    itens.forEach(item => {
        total += item.quantidade * item.preco;
    });

    const totalFormatado = 'R$ ' + total.toFixed(3).replace('.', ',');
    $('tfoot td:last').text(totalFormatado);
}

// Configurar eventos de formulários
function configurarEventos() {
    // Formulário de atualizar informações do cliente
    $('#dados_cliente').on('submit', function(e) {
        e.preventDefault();

        const idCliente = $('#seletor_cliente').val();
        const informacao = $('#informacao').val();

        if (!idCliente) {
            alert('Selecione um cliente!');
            return;
        }

        const data = {
            idOrcamento: parseInt(idOrcamento),
            idCliente: parseInt(idCliente),
            informacao: informacao
        };

        fetch('GerenciarOrcamento?action=updateInfo', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            if (result.error) {
                alert('Erro: ' + result.error);
            } else {
                alert('Informações atualizadas com sucesso!');
            }
        })
        .catch(error => {
            console.error('Erro ao atualizar informações:', error);
            alert('Erro ao atualizar informações!');
        });
    });

    // Formulário de adicionar/alterar item
    $('#dados_item').on('submit', function(e) {
        e.preventDefault();

        const acao = $('#acao_item').val();
        const idItem = $('#id_item').val();
        const idProduto = $('#produto_id').val();
        let quantidade = $('#quantidade_produto').val();
        let preco = $('#preco_produto').val();

        // Converter vírgula para ponto
        quantidade = parseFloat(quantidade.replace(',', '.'));
        preco = parseFloat(preco.replace(',', '.'));

        if (!idProduto || !quantidade || !preco) {
            alert('Preencha todos os campos!');
            return;
        }

        if (acao === '5') {
            // Adicionar item
            const data = {
                idProduto: parseInt(idProduto),
                idOrcamento: parseInt(idOrcamento),
                quantidade: quantidade,
                preco: preco
            };

            fetch('GerenciarOrcamento?action=addItem', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(result => {
                if (result.error) {
                    alert('Erro: ' + result.error);
                } else {
                    closeModal();
                    tabelaItens.ajax.reload(null, false);
                }
            })
            .catch(error => {
                console.error('Erro ao adicionar item:', error);
                alert('Erro ao adicionar item!');
            });
        } else if (acao === '6') {
            // Alterar item
            const data = {
                idItem: parseInt(idItem),
                idProduto: parseInt(idProduto),
                idOrcamento: parseInt(idOrcamento),
                quantidade: quantidade,
                preco: preco
            };

            fetch('GerenciarOrcamento?action=updateItem', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(result => {
                if (result.error) {
                    alert('Erro: ' + result.error);
                } else {
                    closeModal();
                    tabelaItens.ajax.reload(null, false);
                }
            })
            .catch(error => {
                console.error('Erro ao alterar item:', error);
                alert('Erro ao alterar item!');
            });
        }
    });
}

// Funções globais para manipulação de modal e itens
function openModal() {
    modal.showModal();
}

function closeModal() {
    modal.close();
}

function adicionarItem() {
    const valor = $('#seletor_produto').val();

    if (!valor) {
        alert('Selecione um produto!');
        return;
    }

    // Faça um fetch para obter os detalhes do produto selecionado
    fetch(`GerenciarProduto?codigo=${valor}`)
        .then(response => response.json())
        .then(produto => {
            if (produto.error) {
                alert('Erro: ' + produto.error);
                return;
            }

            $('#acao_item').val('5');
            $('#id_item').val('');
            $('#produto_id').val(produto.codigo);
            $('#nome_produto').val(produto.nome);
            $('#quantidade_produto').val('1,000');
            $('#preco_produto').val(produto.preco.toFixed(3).replace('.', ','));

            modal.showModal();
        })
        .catch(error => {
            console.error('Erro ao carregar produto:', error);
            alert('Erro ao carregar produto!');
        });

    modal.showModal();
}

function alterarItem(index, idItem) {
    // Buscar dados do item via API
    fetch(`GerenciarItemOrcamento?id=${idItem}`)
        .then(response => response.json())
        .then(item => {
            if (item.error) {
                alert('Erro: ' + item.error);
                return;
            }

            $('#acao_item').val('6');
            $('#id_item').val(item.id);
            $('#produto_id').val(item.produto.codigo);
            $('#nome_produto').val(item.produto.nome);
            $('#quantidade_produto').val(item.quantidade.toFixed(3));
            $('#preco_produto').val(item.preco.toFixed(3));

            modal.showModal();
        })
        .catch(error => {
            console.error('Erro ao carregar item:', error);
            alert('Erro ao carregar item!');
        });
}

function excluirItem(idItem) {
    if (!confirm('Tem certeza que deseja excluir este item?')) {
        return;
    }

    fetch(`GerenciarOrcamento?action=deleteItem&idItem=${idItem}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            tabelaItens.ajax.reload(null, false);
        } else {
            return response.json().then(data => {
                alert('Erro: ' + (data.error || 'Erro ao excluir item'));
            });
        }
    })
    .catch(error => {
        console.error('Erro ao excluir item:', error);
        alert('Erro ao excluir item!');
    });
}

function confirmarExclusao(event, url) {
    if (confirm('Tem certeza que deseja cancelar este orçamento?')) {

    }
}



