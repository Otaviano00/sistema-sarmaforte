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
          processing: "Processando..."
      }
    });
  

});