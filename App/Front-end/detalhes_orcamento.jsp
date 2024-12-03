<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.Orcamento"%>
<%@page import="dao.OrcamentoDAO"%>
<%@page import="model.ItemOrcamento"%>
<%@page import="utilities.Util" %>

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

        <link rel="stylesheet" href="style/main.css">
        <link rel="stylesheet" href="style/cadastrar_alterar.css">
        <link rel="stylesheet" href="style/orcamento.css">
        <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">

        <title>Detalhes Orçamento</title>
    </head>
    <body>
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            Orcamento orcamento = OrcamentoDAO.listarPorId(id);
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
            <br>
            <section id="dados_orcamento" class="bloco" style="min-width: 97.5%;"> 
                <h2>Dados do orçamento</h2>
                <form id="dados_cliente" style="margin-top: -30px;">
                    <input type="hidden" name="id_orcamento" id="id_orcamento" value="<%= id%>">
                    <input type="hidden" id="id_cliente" value="<%= orcamento.getCliente().getId()%>">
                    <input type="hidden" name="acao" value="4">

                    <br>                     
                    <div style="display: flex; flex-direction: row; justify-content: space-around; min-width: 100%">
                        <div class="campo_cliente campos">
                            <label>Nome:</label>
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
                    <div class="campos">
                        <label for="informacao"> Informações:</label>
                        <textarea name="informacao" id="informacao" style="min-height: fit-content; max-height: 50px;" readonly disabled><%= orcamento.getInformacao() == null? "" : orcamento.getInformacao().trim()%></textarea>
                    </div>
                </form>
            </section>
            <br>
                <div class="tabela bloco" style="width: 95%; justify-content: center;">
                    <h2 style="margin-bottom: -40px;">Itens do Orçamento</h2>
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
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<ItemOrcamento> itens = OrcamentoDAO.listarItensOrcamento(id);
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
                                        <%= itens.get(i).isStatusVenda()? "Vendido" : "Não Vendido"%>
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
                                <td colspan="2">
                                    <%= String.format("R$ %,.2f", total)%>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>

            <section id="finalizar" class="bloco" >
                <div style="position: absolute; left: 0;">
                    <button class="botao_cancela" onclick="location.href = document.referrer;" >
                        Voltar
                    </button>
                </div>
                <button class="botao_confirma" id="botao_imprime" onclick="location.href = 'imprimir_orcamento.jsp?id=<%= id%>'">
                    Imprimir Orçamento
                </button>  
                <button class="botao_confirma" id="botao_venda" onclick="location.href = 'registrar_venda.jsp?id=<%= id%>'">
                    Realizar Venda
                </button>   
            </section>
        </div>
    </body>
</html>