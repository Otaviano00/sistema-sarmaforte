let table;
$(document).ready(function() {
    table = $('.table').DataTable({
        language: {
            lengthMenu: "Exibindo _MENU_ registros por página",
            zeroRecords: "Nenhum registro encontrado",
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
            emptyTable: 'Sem registros nessa tabela'
        },
        scrollX: true,
        responsive: true,
        order: [[0, 'asc']],
        orderFixed: [[0, 'asc']],
        initComplete: function(settings, json) {
            const { filtroAtual } = getFiltrosSalvos();
            let search = document.getElementById("dt-search-0");
            search.value = filtroAtual.buscar;
        }
    });

    // Função para determinar qual filtro usar baseado na URL
    function getTipoFiltro() {
        const url = window.location.href.toLowerCase();
        if (url.includes('cliente')) {
            return 'clientes';
        } else if (url.includes('produto')) {
            return 'produtos';
        }
        return 'produtos'; // default
    }

    // Função para obter filtros salvos
    function getFiltrosSalvos() {
        const filtrosGerais = JSON.parse(sessionStorage.getItem('filtros') || '{}');
        const tipoFiltro = getTipoFiltro();
        
        if (!filtrosGerais[tipoFiltro]) {
            filtrosGerais[tipoFiltro] = {
                buscar: '',
                coluna: 2,
                tipo: 0,
                pagina: 0
            };
        }
        
        return { filtrosGerais, tipoFiltro, filtroAtual: filtrosGerais[tipoFiltro] };
    }

    // Função para salvar filtros
    function salvarFiltros(buscar, coluna, tipo, pagina) {
        const { filtrosGerais, tipoFiltro } = getFiltrosSalvos();
        
        filtrosGerais[tipoFiltro] = {
            buscar: buscar || '',
            coluna: coluna,
            tipo: tipo,
            pagina: pagina
        };
        
        sessionStorage.setItem('filtros', JSON.stringify(filtrosGerais));
    }

    // Event listener para mudança de página
    table.on('page.dt', function () {
        const info = table.page.info();
        const { filtroAtual } = getFiltrosSalvos();
        
        salvarFiltros(filtroAtual.buscar, filtroAtual.coluna, filtroAtual.tipo, info.page);
    });

    const input_filter = document.getElementById("input-filter");
    const input_type = document.getElementById("input-type");
    let search = document.getElementById("dt-search-0");

    let tipo = 0;
    let coluna = 2;

    // Aguardar um momento para garantir que a tabela foi inicializada
    setTimeout(() => {
        // Aplicar filtros salvos DEPOIS da tabela estar completamente pronta
        const { filtroAtual } = getFiltrosSalvos();
        if (filtroAtual.buscar || filtroAtual.coluna !== 2 || filtroAtual.tipo !== 0) {
            search.value = filtroAtual.buscar;
            coluna = filtroAtual.coluna;
            tipo = filtroAtual.tipo;
            
            // Atualizar os selects com os valores salvos
            if (input_filter) input_filter.value = coluna;
            if (input_type) input_type.value = tipo;
            
            searchTable(filtroAtual.buscar, coluna, tipo, filtroAtual.pagina);
            console.log("Chegou aqui né");
        }
    }, 5000); // Pequeno delay para garantir inicialização
  
    input_filter.addEventListener('change', e => {
        coluna = parseInt(e.target.value);
        let temp = search.value;

        table
            .search('')
            .columns().search('')
            .order([])
            .page('first')
            .draw();

        searchTable(temp, coluna, tipo, -1);
        
        // Salvar nova coluna
        salvarFiltros(temp, coluna, tipo, 0);
        search.value = temp;
    });
    
    input_type.addEventListener('change', e => {
        tipo = parseInt(e.target.value);
        let temp = search.value;
        searchTable(temp, coluna, tipo, -1);
        
        // Salvar novo tipo
        salvarFiltros(temp, coluna, tipo, 0);
        search.value = temp;
    });
    
    $('#dt-search-0').on('keyup', function () {
        const { filtroAtual } = getFiltrosSalvos();
        
        table
            .search('')
            .columns().search('');
            
        // Só vai para primeira página se não há página salva ou é busca diferente
        if (!filtroAtual.pagina || filtroAtual.buscar !== this.value) {
            table.page('first');
        }
        
        table.draw();
        
        searchTable(this.value, coluna, tipo, -1);

        // Salvar nova busca
        salvarFiltros(this.value, coluna, tipo, table.page());
    });

});

function searchTable(value, coluna, tipo, pagina) {
    // Verificar se a tabela foi inicializada
    if (!table || !table.column) {
        console.log('Tabela ainda não foi inicializada');
        return;
    }
    
    let raw = value.trim();
    let escaped = raw.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    let regex = '^' + escaped; 
    
    if (coluna == 5 || tipo == 1) {
        regex = escaped;
    }

    // Aplicar o filtro primeiro
    table.column(coluna).search(regex, true, false);
    
    // Depois definir a página (se especificada)
    if (pagina != -1 && pagina != null) {
        table.page(parseInt(pagina));
    }
    
    // Por último, desenhar a tabela
    table.draw();
}

function confirmarExclusao(event, url) {
    const confirmar = confirm("Você tem certeza que deseja excluir esse registro?");
    if (confirmar) {
        window.location.href = url;
    } else {
        event.preventDefault();
    }
}

function confirmarVenda(event) {
    const confirmar = confirm("Você tem certeza que deseja registrar a venda?");
    if (!confirmar) {
        event.preventDefault();
    } 
}