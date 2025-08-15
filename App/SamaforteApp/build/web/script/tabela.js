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
            if (paginaPermiteFiltros()) {
                const { filtrosGerais, tipoFiltro } = getFiltrosSalvos();
                let search = document.getElementById('dt-search-0');
                search.value = filtrosGerais[tipoFiltro].buscar;
            } 
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
        return null; // Retorna null para outras páginas
    }

    // Verificar se a página atual deve ter filtros
    function paginaPermiteFiltros() {
        const tipoFiltro = getTipoFiltro();
        return tipoFiltro === 'clientes' || tipoFiltro === 'produtos';
    }

    // Função para obter filtros salvos
    function getFiltrosSalvos() {
        if (!paginaPermiteFiltros()) {
            return {
                filtrosGerais: {},
                tipoFiltro: null,
                filtroAtual: { buscar: '', coluna: 2, tipo: 0, pagina: 0 }
            };
        }

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
        if (!paginaPermiteFiltros()) {
            return; // Não salva filtros para outras páginas
        }

        const { filtrosGerais, tipoFiltro } = getFiltrosSalvos();
        
        filtrosGerais[tipoFiltro] = {
            buscar: buscar || '',
            coluna: coluna,
            tipo: tipo,
            pagina: pagina
        };
        
        sessionStorage.setItem('filtros', JSON.stringify(filtrosGerais));
    }

    // Event listener para mudança de página (só para páginas permitidas)
    if (paginaPermiteFiltros()) {
        table.on('page.dt', function () {
            const info = table.page.info();
            const { filtroAtual } = getFiltrosSalvos();
            
            salvarFiltros(filtroAtual.buscar, filtroAtual.coluna, filtroAtual.tipo, info.page);
        });
    }

    const input_filter = document.getElementById("input-filter");
    const input_type = document.getElementById("input-type");
    let search = document.getElementById("dt-search-0");

    let tipo = 0;
    let coluna = 2;

    // Aplicar filtros salvos apenas se for página permitida
    if (paginaPermiteFiltros()) {
        setTimeout(() => {
            const { filtroAtual } = getFiltrosSalvos();
            if (filtroAtual.buscar || filtroAtual.coluna !== 2 || filtroAtual.tipo !== 0) {
                coluna = filtroAtual.coluna;
                tipo = filtroAtual.tipo;
                
                // Atualizar os selects com os valores salvos
                if (input_filter) input_filter.value = coluna;
                if (input_type) input_type.value = tipo;
                
                // Aplicar filtros SEM chamar searchTable para evitar interferência
                let raw = filtroAtual.buscar.trim();
                let escaped = raw.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
                let regex = '^' + escaped; 
                
                if (coluna == 5 || tipo == 1) {
                    regex = escaped;
                }

                // Aplicar filtro e página
                table.column(coluna).search(regex, true, false);
                if (filtroAtual.pagina != null && filtroAtual.pagina >= 0) {
                    table.page(parseInt(filtroAtual.pagina));
                }
                table.draw();
                
                // Definir valor do campo DEPOIS de aplicar filtros
                search.value = filtroAtual.buscar;
                
                console.log("Filtros aplicados para:", getTipoFiltro());
            }
        }, 150);
    }
  
    // Event listeners apenas para páginas permitidas
    if (paginaPermiteFiltros() && input_filter) {
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
    }
    
    if (paginaPermiteFiltros() && input_type) {
        input_type.addEventListener('change', e => {
            tipo = parseInt(e.target.value);
            let temp = search.value;
            searchTable(temp, coluna, tipo, -1);
            
            // Salvar novo tipo
            salvarFiltros(temp, coluna, tipo, 0);
            search.value = temp;
        });
    }
    
    if (paginaPermiteFiltros()) {
        $('#dt-search-0').on('keyup', function () {
            const { filtroAtual } = getFiltrosSalvos();
            let temp = search.value;

            table
                .search('')
                .columns().search('');
                
            // Só vai para primeira página se não há página salva ou é busca diferente
            if (!filtroAtual.pagina || filtroAtual.buscar !== this.value) {
                table.page('first');
            }
            
            table.draw();
            
            searchTable(temp, coluna, tipo, -1);

            // Salvar nova busca
            salvarFiltros(temp, coluna, tipo, table.page());
            search.value = temp;
        });
    }

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