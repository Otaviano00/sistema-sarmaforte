$(document).ready(function() {
   $('.table').DataTable({
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
        "loadingRecords": true,
        responsive: true
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