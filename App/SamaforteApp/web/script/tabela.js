$(document).ready(function() {
  let table = $('.table').DataTable({
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
    responsive: true
  });
    
    const input_filter = document.getElementById("input-filter");
    const input_type = document.getElementById("input-type");
    const search = document.getElementById("dt-search-0")

    const dados = table.rows().data();

    let tipo = 0;
    let coluna = 2; // agora é let para poder mudar depois
  
    
    input_filter.addEventListener('change', e => {
        coluna = parseInt(e.target.value); // garante que é número
        let raw = search.value.trim();
        let escaped = raw.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
        let regex = '^' + escaped; 
        
        if (coluna == 5 || tipo == 1) {
            regex = escaped;
        }

        table.column(coluna).search(regex, true, false).draw();
    });
    
    input_type.addEventListener('change', e => {
        tipo = parseInt(e.target.value); // garante que é número
        let raw = search.value.trim();
        let escaped = raw.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
        let regex = '^' + escaped; 
        
        if (coluna == 5 || tipo == 1) {
            regex = escaped;
        }

        table.column(coluna).search(regex, true, false).draw();
    });
    
    $('#dt-search-0').on('keyup', function () {
          table
            .search('')
            .columns().search('')
            .order([])
            .page('first')
            .draw();
        let raw = this.value.trim();
        let escaped = raw.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
        let regex = '^' + escaped; 
        
        if (coluna == 5 || tipo == 1) {
            regex = escaped;
        }

        table.column(coluna).search(regex, true, false).draw();
    });




});


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