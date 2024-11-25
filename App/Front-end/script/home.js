$(document).ready(function() {
    $('.table').DataTable({
        dom: 't',
        scrollX: true,
        "loadingRecords": true,
        responsive: true, 
        autoWidth: false
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