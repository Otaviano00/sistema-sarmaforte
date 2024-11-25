<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.Orcamento"%>
<%@page import="dao.OrcamentoDAO"%>
<%@page import="model.Venda"%>
<%@page import="dao.VendaDAO"%>
<%@page import="model.ItemOrcamento"%>
<%@page import="utilities.Util" %>
<%@page import="java.time.LocalDate" %>

<%@include file="sessao.jsp" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
        
        <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
        <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

        <script defer src="script/orcamento.js"></script>
        <script defer src="script/tabela.js"> </script>
        <script defer src="script/venda.js"></script>

        <link rel="stylesheet" href="style/main.css">
        <link rel="stylesheet" href="style/cadastrar_alterar.css">
        <link rel="stylesheet" href="style/orcamento.css">
        <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">

        <title>Detalhes Venda</title>
    </head>
    <body>
        <%
            Integer id = Integer.parseInt(request.getParameter("id"));
            Venda venda = VendaDAO.listarPorId(id);
            Orcamento orcamento = OrcamentoDAO.listarPorId(venda.getOrcamento().getId());
        %>
        <header>
             <div class="logo">
                <img id="bloco1_logo" src="images/blocos_esquerda.svg" alt="blocos">
                <a href="home.jsp">
                    <img id="samaforte" src="images/logo.svg" alt="logo da Samaforte">
                </a>
                <img id="bloco2_logo" src="images/blocos_direita.svg" alt="">
            </div>
            <%@include file="infoLogin.jsp" %>
        </header>
        <nav>
            <%@include file="nav_list.jsp"%>
        </nav>
        <div class="flex">
            <form action="GerenciarVenda" method="post">
                <br>
                <input type="hidden" name="id_venda" id="id_venda" value="<%= id%>">
                <input type="hidden" name="id_orcamento" id="id_orcamento" value="<%= orcamento.getId()%>">
                <input type="hidden" name="id_usuario" id="id_usuario" value="<%= idU%>">
                <input type="hidden" name="acao" value="2">
                <section id="dados_orcamento" class="bloco" style="min-width: 97.5%;">
                    <h2>Dados da Venda</h2>
                    <div id="dados_cliente" class="form">
                        <br>
                        <div style="display: flex; flex-direction: row; justify-content: space-around; min-width: 100%">
                            <div class="campo_cliente campos">
                                <label>Cliente:</label>
                                <input type="text" value="<%= orcamento.getCliente().getNome()%>" disabled readonly>
                            </div>
                            <div class="campo_cliente campos">
                                <label>Telefone:</label>
                                <input type="text" value="<%= orcamento.getCliente().getTelefone()%>" disabled readonly>
                            </div>
                            <div class="campo_cliente campos">
                                <label>CPF:</label>
                                <input type="text" value="<%= orcamento.getCliente().getCpf() != null? orcamento.getCliente().getCpf() : "---"%>" disabled readonly>
                            </div>
                            <div class="campo_cliente campos">
                                <label>Endereço:</label>
                                <input type="text" value="<%= orcamento.getCliente().getEndereco() != null? orcamento.getCliente().getEndereco() : "---"%>" disabled readonly>
                            </div>
                            <br>
                        </div>
                        <div style="display: flex; flex-direction: row; justify-content: space-around; min-width: 100%">
                            <div class="campo_cliente campos">
                                <label> Vendedor: </label>
                                <input type="text" value="<%= venda.getUsuario() == null? "EXCLUÍDO" : venda.getUsuario().getNome() %>" disabled readonly>
                            </div>
                            <div class="campo_cliente campos">
                                <label> Cargo:  </label>
                                <input type="text" value="<%= venda.getUsuario() == null? "EXCLUÍDO" : venda.getUsuario().getPerfil().getNome()%>" disabled readonly>
                            </div>
                        </div>
                        <div style="display: flex; flex-direction: row; justify-content: space-around; min-width: 100%">
                            <div class="campo_cliente campos">
                                <label> Data da Venda: </label>
                                <input type="text" value="<%= Util.converteData(venda.getData().toLocalDate())%>" disabled readonly>
                            </div>
                            <div class="campo_cliente campos">
                                <label> Desconto (R$): </label>
                                <input type="number" step="0.01" name="desconto" id="desconto" value="<%= venda.getDesconto()%>" disabled readonly>
                            </div>
                            <div class="campo_cliente campos">
                                <label> Forma de Pagamento: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr> </label>
                                <input type="text" name="forma_pagamento" value="<%= venda.getFormaPagamento()%>" required disabled readonly>
                            </div>
                        </div>
                        <div class="campos">
                            <label for="informacao"> Informações:</label>
                            <textarea name="informacao" id="informacao" style="min-height: fit-content; max-height: 50px;" readonly disabled><%= orcamento.getInformacao() == null? "" : orcamento.getInformacao().trim()%></textarea>
                        </div>
                    </div>
                </section>
                <br>
                <section id="listar_itens" class="bloco">
                    <h2>Itens do Orçamento</h2>
                    <div class="tabela" style="width: 98.5%; justify-content: center;">
                        <table class="table table-striped" style="background-color: white;">
                            <thead>
                                <p></p>
                                <br>
                                <tr>
                                    <th>#</th>
                                    <th>Código</th>
                                    <th>Data</th>
                                    <th>Nome</th>
                                    <th>Quantidade</th>
                                    <th>Preço Unitário</th>
                                    <th>Valor</th>
                                    <th>Ação</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    List<ItemOrcamento> itens = OrcamentoDAO.listarItensOrcamento(orcamento.getId());
                                    double total = 0;
                                    for (int i = 0; i < itens.size(); i++) {
                                        double precoTotal = itens.get(i).getPreco() * itens.get(i).getQuantidade();
                                        total += precoTotal;
                                        String data = Util.converteData(itens.get(i).getDataHora().toLocalDate());
                                        String hora = Util.converteHora(itens.get(i).getDataHora());
                                        String dataHora = data + " - " + hora;

                                %>
                                    <tr>
                                        <td>
                                            <%= i+1%>
                                        </td>
                                        <td>
                                            <%= itens.get(i).getProduto().getCodigo()%>
                                        </td>
                                        <td>
                                            <%= dataHora%>
                                        </td>
                                        <td>
                                            <%= itens.get(i).getProduto().getNome()%>
                                        </td>
                                        <td>
                                            <%= itens.get(i).getQuantidade()%>
                                        </td>
                                        <td>
                                            <%= String.format("%,.2f", itens.get(i).getPreco())%>
                                        </td>
                                        <td>
                                            <%= String.format("%,.2f", precoTotal)%>
                                        </td>
                                        <td>
                                            <label for="produto-checkbox<%= itens.get(i).getId()%>" class="botao_radio" style="max-width: fit-content; cursor: pointer;">
                                                <input type="checkbox" name="produto-checkbox" style="width: 20px; height: 20px; cursor: pointer;" value="<%= itens.get(i).getId()%>" id="produto-checkbox<%= itens.get(i).getId()%>" class="produto-checkbox" data-preco="<%= precoTotal%>" onchange="atualizarTotal()" <%= itens.get(i).isStatusVenda()? "checked" : "" %> disabled readonly>
                                                Vender
                                            </label>
                                        </td>
                                    </tr>
                                <%
                                    }
                                %>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="6" >
                                        TOTAL
                                    </td>
                                    <td colspan="2" id="valorTotal">
                                        <%= String.format("R$ %,.2f", 0f) %>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                        <input type="hidden" name="valor" id="valor" value="0">
                    </div>
                
                </section>
                <section id="finalizar" class="bloco" >
                    <div style="position: absolute; left: 0;">
                        <button type="button" class="botao_cancela" onclick="location.href = document.referrer;" >
                            Voltar
                        </button>
                    </div>
                    <button type="button" class="botao_confirma" id="botao_imprime" onclick="location.href = 'orcamentos.jsp'">
                        Imprimir Orçamento
                    </button>
                </section>
            </form>
        </div>
    </body>
</html>