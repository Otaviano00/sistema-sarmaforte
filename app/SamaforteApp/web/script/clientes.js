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
                        <button onclick="location.href = 'alterar_cliente.jsp?id=${row.id}'" class="botao_acao" title="Alterar dados do cliente ${row.nome}">
                            <img src="images/icone_alterar.svg" alt="Alterar">
                        </button>
                        <button onclick="location.href = 'detalhes_cliente.jsp?id=${row.id}'" class="botao_acao" title="Detalhes do cliente ${row.nome}">
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

