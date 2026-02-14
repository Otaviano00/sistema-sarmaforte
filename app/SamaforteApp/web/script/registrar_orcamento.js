let modalAdicionar = null;
let modalAlterar = null;
let modalCriarCliente = null;
let idOrcamento = null;
let tabelaItens = null;

// Timer para debounce de atualização de informações
let informacaoTimer = null;

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

    // Inicializar referências aos modais
    modalAdicionar = document.querySelector('#modal_adicionar');
    modalAlterar = document.querySelector('#modal_alterar');
    modalCriarCliente = document.querySelector('#modal_criar_cliente');

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

    // Configurar atualização automática das informações com delay
    $('#informacao').on('input', function() {
        // Limpar timer anterior se existir
        if (informacaoTimer) {
            clearTimeout(informacaoTimer);
        }

        // Criar novo timer de 1 segundo
        informacaoTimer = setTimeout(function() {
            atualizarInformacoes();
        }, 1000);
    });

    // Configurar atalhos de teclado
    configurarAtalhosTeclado();
});

// Inicializar seletor de produtos com Select2
function inicializarSeletorProdutos() {
    $('#seletor_produto').select2({
        placeholder: 'Selecione um produto',
        allowClear: true,
        matcher: function(params, data) {
            // Se não há termo de busca, retornar tudo
            if ($.trim(params.term) === '') {
                return data;
            }

            // Se é uma opção de grupo, processar recursivamente
            if (data.children) {
                const filteredChildren = $.map(data.children, function(child) {
                    return this.matcher(params, child);
                }.bind(this));

                if (filteredChildren.length) {
                    const modifiedData = $.extend({}, data, true);
                    modifiedData.children = filteredChildren;
                    return modifiedData;
                }
                return null;
            }

            const term = params.term.toLowerCase().trim();
            const text = data.text.toLowerCase().trim();

            // Aceitar se começar OU contiver o termo
            if (text.startsWith(term) || text.includes(term)) {
                return data;
            }

            return null;
        },
        sorter: function(data) {
            // Se não há termo de busca, manter ordem original
            const searchTerm = $('.select2-search__field').val();
            if (!searchTerm || searchTerm.trim() === '') {
                return data;
            }

            const term = searchTerm.toLowerCase().trim();

            // Separar em dois grupos: começa com e contém
            const startsWith = [];
            const contains = [];

            data.forEach(function(item) {
                if (item.text) {
                    const text = item.text.toLowerCase();
                    if (text.startsWith(term)) {
                        startsWith.push(item);
                    } else if (text.includes(term)) {
                        contains.push(item);
                    }
                }
            });

            // Concatenar: primeiro os que começam, depois os que contêm
            return startsWith.concat(contains);
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
            // Se não há termo de busca, retornar tudo
            if ($.trim(params.term) === '') {
                return data;
            }

            // Se é uma opção de grupo, processar recursivamente
            if (data.children) {
                const filteredChildren = $.map(data.children, function(child) {
                    return this.matcher(params, child);
                }.bind(this));

                if (filteredChildren.length) {
                    const modifiedData = $.extend({}, data, true);
                    modifiedData.children = filteredChildren;
                    return modifiedData;
                }
                return null;
            }

            const term = params.term.toLowerCase().trim();
            const text = data.text.toLowerCase().trim();

            // Aceitar se começar OU contiver o termo
            if (text.startsWith(term) || text.includes(term)) {
                return data;
            }

            return null;
        },
        sorter: function(data) {
            // Se não há termo de busca, manter ordem original
            const searchTerm = $('.select2-search__field').val();
            if (!searchTerm || searchTerm.trim() === '') {
                return data;
            }

            const term = searchTerm.toLowerCase().trim();

            // Separar em dois grupos: começa com e contém
            const startsWith = [];
            const contains = [];

            data.forEach(function(item) {
                if (item.text) {
                    const text = item.text.toLowerCase();
                    if (text.startsWith(term)) {
                        startsWith.push(item);
                    } else if (text.includes(term)) {
                        contains.push(item);
                    }
                }
            });

            // Concatenar: primeiro os que começam, depois os que contêm
            return startsWith.concat(contains);
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

                atualizarInformacoes();
            })
            .catch(error => {
                console.error('Erro ao carregar cliente:', error);
                alert('Erro ao carregar dados do cliente!');
            });
    });
}

// Carregar dados do orçamento
function carregarDadosOrcamento() {
    fetch(`GerenciarOrcamento?id=${idOrcamento}`)
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

    // Definir colunas base
    const colunas = [
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
        }
    ];

    // Adicionar coluna de Opções apenas se NÃO for página de detalhes
    if (!window.isDetalhesPage) {
        colunas.push({
            "data": null,
            "orderable": false,
            "searchable": false,
            "render": function (data, type, row, meta) {
                // Se o item já foi vendido, não mostra os botões de ação
                if (row.statusVenda) {
                    return '<span style="color: #6c757d; font-style: italic;">-</span>';
                }

                return `
                    <button onclick="alterarItem(${meta.row}, ${row.id})" class="botao_acao" title="Alterar item">
                        <img src="images/icone_alterar.svg" alt="Alterar">
                    </button>
                    <button onclick="excluirItem(${row.id})" class="botao_acao" title="Excluir item">
                        <img src="images/icone_excluir.svg" alt="Excluir">
                    </button>
                `;
            }
        });
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
                d.isPaginado = true;
            },
            "dataSrc": function(json) {
                atualizarTotalGeral(json.totalOrcamento);
                return json.data;
            }
        },
        "columns": colunas,
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
function atualizarTotalGeral(total) {
    // Verifica se o total é um número válido
    if (typeof total !== 'number' || isNaN(total)) {
        total = 0;
    }

    const totalFormatado = 'R$ ' + total.toFixed(3).replace('.', ',');
    $('tfoot td:last').text(totalFormatado);
}

// Função para atualizar o cliente no backend
function atualizarClienteNoBackend(idCliente) {
    const orcamento = {
        id: parseInt(idOrcamento),
        cliente: {
            id: idCliente
        }
    };

    fetch('GerenciarOrcamento', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orcamento)
    })
    .then(response => response.json())
    .then(result => {
        if (result.error) {
            console.error('Erro ao atualizar cliente:', result.error);
            alert('Erro ao atualizar cliente: ' + result.error);
        } else {
            console.log('Cliente atualizado com sucesso');
            // Mostrar feedback visual discreto (opcional)
            mostrarFeedbackSucesso('Cliente atualizado');
        }
    })
    .catch(error => {
        console.error('Erro ao atualizar cliente:', error);
        alert('Erro ao atualizar cliente!');
    });
}

// Função para atualizar as informações no backend
function atualizarInformacoes() {
    const informacao = $('#informacao').val();
    const idCliente = $('#seletor_cliente').val();

    if (!idCliente) {
        return;
    }

    const orcamento = {
        id: parseInt(idOrcamento),
        cliente: {
            id: parseInt(idCliente)
        },
        informacao: informacao
    };

    fetch('GerenciarOrcamento', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orcamento)
    })
    .then(response => response.json())
    .then(result => {
        if (result.error) {
            console.error('Erro ao atualizar informações:', result.error);
            alert('Erro ao atualizar informações: ' + result.error);
        } else {
            mostrarFeedbackSucesso('Informações salvas');
        }
    })
    .catch(error => {
        console.error('Erro ao atualizar informações:', error);
        alert('Erro ao atualizar informações!');
    });
}

// Função para mostrar feedback visual discreto
function mostrarFeedbackSucesso(mensagem) {
    // Criar elemento de feedback se não existir
    let feedback = document.getElementById('feedback-sucesso');
    if (!feedback) {
        feedback = document.createElement('div');
        feedback.id = 'feedback-sucesso';
        feedback.style.position = 'fixed';
        feedback.style.bottom = '20px';
        feedback.style.right = '20px';
        feedback.style.padding = '10px 20px';
        feedback.style.backgroundColor = '#28a745';
        feedback.style.color = 'white';
        feedback.style.borderRadius = '5px';
        feedback.style.boxShadow = '0 2px 8px rgba(0,0,0,0.2)';
        feedback.style.zIndex = '10000';
        feedback.style.transition = 'opacity 0.3s ease';
        document.body.appendChild(feedback);
    }

    // Mostrar mensagem
    feedback.textContent = mensagem;
    feedback.style.opacity = '1';
    feedback.style.display = 'block';

    // Esconder após 2 segundos
    setTimeout(function() {
        feedback.style.opacity = '0';
        setTimeout(function() {
            feedback.style.display = 'none';
        }, 300);
    }, 2000);
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

        // Montar objeto Orcamento completo
        const orcamento = {
            id: parseInt(idOrcamento),
            cliente: {
                id: parseInt(idCliente)
            },
            informacao: informacao
        };

        fetch('GerenciarOrcamento', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(orcamento)
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
}

// Funções para manipulação dos modais
function closeModalAdicionar() {
    if (modalAdicionar) {
        modalAdicionar.close();
    }
}

function closeModalAlterar() {
    if (modalAlterar) {
        modalAlterar.close();
    }
}

// Função para detectar Enter no modal de adicionar
function handleEnterAdicionar(event) {
    if (event.key === 'Enter' || event.keyCode === 13) {
        event.preventDefault();
        confirmarAdicionar();
    }
}

// Função para detectar Enter no modal de alterar
function handleEnterAlterar(event) {
    if (event.key === 'Enter' || event.keyCode === 13) {
        event.preventDefault();
        confirmarAlterar();
    }
}

// Função para adicionar item (abre modal)
function adicionarItem() {
    const valor = $('#seletor_produto').val();

    if (!valor) {
        alert('Selecione um produto!');
        return;
    }

    // Fazer fetch para obter os detalhes do produto selecionado
    fetch(`GerenciarProduto?codigo=${valor}`)
        .then(response => response.json())
        .then(produto => {
            if (produto.error) {
                alert('Erro: ' + produto.error);
                return;
            }

            $('#add_produto_id').val(produto.codigo);
            $('#add_nome_produto').val(produto.nome);
            $('#add_quantidade_produto').val('1');
            $('#add_preco_produto').val(produto.preco.toFixed(3));

            modalAdicionar.showModal();
        })
        .catch(error => {
            console.error('Erro ao carregar produto:', error);
            alert('Erro ao carregar produto!');
        });
}

// Função para confirmar adição de item (botão do modal)
function confirmarAdicionar() {
    const idProduto = $('#add_produto_id').val();
    let quantidade = $('#add_quantidade_produto').val();
    let preco = $('#add_preco_produto').val();

    // Converter vírgula para ponto se necessário
    quantidade = parseFloat(quantidade.toString().replace(',', '.'));
    preco = parseFloat(preco.toString().replace(',', '.'));

    if (!idProduto || !quantidade || !preco || isNaN(quantidade) || isNaN(preco)) {
        alert('Preencha todos os campos corretamente!');
        return;
    }

    const data = {
        idProduto: parseInt(idProduto),
        idOrcamento: parseInt(idOrcamento),
        quantidade: quantidade,
        preco: preco
    };

    fetch('GerenciarItemOrcamento', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(result => {
        if (result.error) {
            alert('Erro: ' + result.error);
        } else {
            closeModalAdicionar();
            // Recarregar tabela e o total será atualizado automaticamente via dataSrc
            tabelaItens.ajax.reload(null, false);
        }
    })
    .catch(error => {
        console.error('Erro ao adicionar item:', error);
        alert('Erro ao adicionar item!');
    });
}

// Função para alterar item (abre modal)
function alterarItem(index, idItem) {
    // Buscar dados do item via API
    fetch(`GerenciarItemOrcamento?id=${idItem}`)
        .then(response => response.json())
        .then(item => {
            if (item.error) {
                alert('Erro: ' + item.error);
                return;
            }

            $('#edit_item_id').val(item.id);
            $('#edit_produto_id').val(item.produto.codigo);
            $('#edit_nome_produto').val(item.produto.nome);
            $('#edit_quantidade_produto').val(item.quantidade.toFixed(3));
            $('#edit_preco_produto').val(item.preco.toFixed(3));

            modalAlterar.showModal();
        })
        .catch(error => {
            console.error('Erro ao carregar item:', error);
            alert('Erro ao carregar item!');
        });
}

// Função para confirmar alteração de item (botão do modal)
function confirmarAlterar() {
    const idItem = $('#edit_item_id').val();
    const idProduto = $('#edit_produto_id').val();
    let quantidade = $('#edit_quantidade_produto').val();
    let preco = $('#edit_preco_produto').val();

    // Converter vírgula para ponto se necessário
    quantidade = parseFloat(quantidade.toString().replace(',', '.'));
    preco = parseFloat(preco.toString().replace(',', '.'));

    if (!idItem || !idProduto || !quantidade || !preco || isNaN(quantidade) || isNaN(preco)) {
        alert('Preencha todos os campos corretamente!');
        return;
    }

    const data = {
        idItem: parseInt(idItem),
        idProduto: parseInt(idProduto),
        idOrcamento: parseInt(idOrcamento),
        quantidade: quantidade,
        preco: preco
    };

    fetch('GerenciarItemOrcamento', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(result => {
        if (result.error) {
            alert('Erro: ' + result.error);
        } else {
            closeModalAlterar();
            // Recarregar tabela e o total será atualizado automaticamente via dataSrc
            tabelaItens.ajax.reload(null, false);
        }
    })
    .catch(error => {
        console.error('Erro ao alterar item:', error);
        alert('Erro ao alterar item!');
    });
}

function excluirItem(idItem) {
    if (!confirm('Tem certeza que deseja excluir este item?')) {
        return;
    }

    fetch(`GerenciarItemOrcamento?idItem=${idItem}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            // Recarregar tabela e o total será atualizado automaticamente via dataSrc
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

function confirmarExclusao() {
    const confirmar = confirm("Você tem certeza que deseja excluir esse orçamento?");
    if (confirmar) {
        fetch(`GerenciarOrcamento?id=${idOrcamento}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok || response.status === 204) {
                window.location.href = 'orcamentos.jsp';
            } else {
                return response.json().then(data => {
                    alert('Erro ao excluir orçamento: ' + (data.error || 'Erro desconhecido'));
                });
            }
        })
        .catch(error => {
            console.error('Erro ao excluir orçamento:', error);
            alert('Erro ao excluir orçamento!');
        });
    }
}

// ==========================================
// FUNÇÕES DO MODAL DE CRIAR CLIENTE
// ==========================================

// Abrir modal de criar cliente
function abrirModalCriarCliente() {
    // Limpar formulário
    document.getElementById('createClienteForm').reset();

    // Abrir modal
    modalCriarCliente.showModal();

    // Configurar evento de submit do formulário
    const form = document.getElementById('createClienteForm');

    // Remover listeners anteriores para evitar duplicação
    const newForm = form.cloneNode(true);
    form.parentNode.replaceChild(newForm, form);

    // Adicionar listener ao novo formulário
    newForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        await criarNovoCliente();
    });
}

// Fechar modal de criar cliente
function closeModalCriarCliente() {
    modalCriarCliente.close();
}

// Criar novo cliente
async function criarNovoCliente() {
    const formData = new FormData(document.getElementById('createClienteForm'));
    const cliente = Object.fromEntries(formData.entries());

    // Validar campos obrigatórios
    if (!cliente.nome || !cliente.telefone) {
        alert('Nome e Telefone são obrigatórios!');
        return;
    }

    // Limpar campos vazios
    if (cliente.cpf && cliente.cpf.trim() === '') {
        cliente.cpf = null;
    }
    if (cliente.endereco && cliente.endereco.trim() === '') {
        cliente.endereco = null;
    }

    try {
        // Fazer requisição POST para criar cliente
        const response = await fetch('GerenciarCliente', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(cliente)
        });

        const result = await response.json();

        if (response.ok) {
            // Fechar modal
            closeModalCriarCliente();

            // Recarregar lista de clientes no Select2
            await recarregarListaClientes(result.id);

            // Mostrar feedback de sucesso
            mostrarFeedbackSucesso('Cliente cadastrado com sucesso!');
        } else {
            alert('Erro ao cadastrar cliente: ' + (result.error || 'Erro desconhecido'));
        }
    } catch (error) {
        console.error('Erro ao criar cliente:', error);
        alert('Erro ao cadastrar cliente!');
    }
}

// Recarregar lista de clientes e selecionar o novo
async function recarregarListaClientes(idNovoCliente) {
    try {
        // Buscar todos os clientes
        const response = await fetch('GerenciarCliente?isPaginado=false');
        const clientes = await response.json();

        if (!Array.isArray(clientes)) {
            console.error('Resposta inválida do servidor:', clientes);
            return;
        }

        // Limpar e repopular o Select2
        const $seletor = $('#seletor_cliente');
        $seletor.empty();

        // Adicionar opção padrão
        $seletor.append(new Option('Selecione um cliente', '', false, false));

        // Adicionar todos os clientes
        clientes.forEach(cliente => {
            const option = new Option(
                cliente.nome,
                cliente.id,
                false,
                cliente.id === idNovoCliente
            );
            $seletor.append(option);
        });

        // Atualizar Select2
        $seletor.trigger('change');

        // Se foi criado um novo cliente, selecioná-lo e atualizar backend
        if (idNovoCliente) {
            $seletor.val(idNovoCliente).trigger('change');
        }
    } catch (error) {
        console.error('Erro ao recarregar lista de clientes:', error);
        alert('Erro ao atualizar lista de clientes!');
    }
}

// Configurar atalhos de teclado para facilitar o uso
function configurarAtalhosTeclado() {
    // Não configurar atalhos na página de detalhes (somente leitura)
    if (window.isDetalhesPage) {
        return;
    }

    let produtoSelecionadoPeloTeclado = false;

    // Atalho global: ESPAÇO para focar no select de produtos
    document.addEventListener('keydown', function(event) {
        // Verificar se o alvo não é um input, textarea ou select
        const targetTag = event.target.tagName.toLowerCase();
        const isInputField = ['input', 'textarea', 'select'].includes(targetTag);

        // Se estiver em um campo de input, não ativar o atalho
        if (isInputField) {
            return;
        }

        // ESPAÇO - Abrir select de produtos
        if (event.code === 'Space' || event.keyCode === 32) {
            event.preventDefault();
            $('#seletor_produto').select2('open');
            produtoSelecionadoPeloTeclado = false;
        }
    });

    // Listener para quando um produto é selecionado no select
    $('#seletor_produto').on('select2:select', function(e) {
        produtoSelecionadoPeloTeclado = true;

        // Focar no container do Select2 para capturar o ENTER
        setTimeout(() => {
            const select2Container = $('.select2-container--open');
            if (select2Container.length) {
                select2Container.focus();
            }
        }, 100);
    });

    // Listener para ENTER após selecionar produto
    $('#seletor_produto').on('select2:close', function(e) {
        if (produtoSelecionadoPeloTeclado) {
            // Pequeno delay para garantir que o valor foi atualizado
            setTimeout(() => {
                const valor = $('#seletor_produto').val();
                if (valor) {
                    adicionarItem();
                    produtoSelecionadoPeloTeclado = false;
                }
            }, 100);
        }
    });

    // Alternativa: Capturar ENTER enquanto o select está aberto
    $(document).on('keydown', function(event) {
        // Se o Select2 está aberto e pressionou ENTER
        if ($('.select2-container--open').length > 0 && (event.code === 'Enter' || event.keyCode === 13)) {
            const valor = $('#seletor_produto').val();
            if (valor && produtoSelecionadoPeloTeclado) {
                event.preventDefault();
                $('#seletor_produto').select2('close');
                // O evento select2:close vai disparar adicionarItem()
            }
        }
    });
}



