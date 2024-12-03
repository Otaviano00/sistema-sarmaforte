<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utilities.Util"%>
<%@page import="model.Venda"%>
<%@page import="dao.VendaDAO"%>
<%@page import="model.Orcamento"%>
<%@page import="dao.OrcamentoDAO"%>
<%@page import="java.util.List"%>

<%@include file="sessao.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>

    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/home.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="stylesheet" href="style/orcamento.css">
    <link rel="stylesheet" href="style/home.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Home</title>

</head>
<body>
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
        <h1 class="titulo">
            DASHBOARD
            <br>
        </h1>
        <section class="content">
            <!-- Análise de Vendas -->
            <div class="head">
                <img src="images/icone_vendas.svg" alt="Vendas" style="margin-top: -10px;">
                <span>
                    Vendas - <%= Util.pegarMes()%>
                </span>
                <hr>
            </div>
            <section class="dados">
                <%@include file="dashboard/grafico_vendas.jsp" %>
                <div class="tabela">
                    <div class="campo">
                        <label class="titulo_campo">ÚLTIMAS VENDAS</label>
                    </div>
                    <table class="table table-striped" style="background-color: white;">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Cliente</th>
                                <th>Vendedor</th>
                                <th>Data</th>
                                <th>Valor</th>
                                <th>Forma Pagamento</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Venda> vendas = VendaDAO.listar();
                                int tamanho = vendas.size() > 5? 5 : vendas.size(); 
                                for (int i = tamanho - 1; i >= 0; i--) {%>
                                <tr>
                                    <td><%= tamanho - i%></td>
                                    <td><%= vendas.get(i).getOrcamento().getCliente().getNome()%></td>
                                    <td><%= vendas.get(i).getUsuario() == null? "EXCLUÍDO" : vendas.get(i).getUsuario().getNome()%></td> 
                                    <td><%= Util.converteData(vendas.get(i).getData().toLocalDate())%></td>       
                                    <td><%= String.format("R$ %,.2f", vendas.get(i).getValor())%></td> 
                                    <td><%= vendas.get(i).getFormaPagamento()%></td>  
                                    <td>
                                        <% if (hierarquia < 2 && !vendas.get(i).getOrcamento().getStatus().equals("Concluído")) {%>
                                            <button onclick="location.href = 'alterar_venda.jsp?id=<%= vendas.get(i).getId()%>'" class="botao_acao" title="Alterar dessa venda <%= i+1%>">
                                                <img src="images/icone_alterar.svg" alt="Alterar">
                                            </button>
                                        <% }%>
                                        <button onclick="location.href = 'detalhes_venda.jsp?id=<%= vendas.get(i).getId()%>'" class="botao_acao" title="Detalhes da venda <%= i+1%>">
                                            <img src="images/icone_detalhes.svg" alt="Detalhes">
                                        </button>
                                        <% if (hierarquia == 0) {%>
                                            <button onclick="confirmarExclusao(event, 'GerenciarVenda?id=<%= vendas.get(i).getId()%>&acao=3')" class="botao_acao" title="Excluir venda <%= i+1%>">
                                                <img src="images/icone_excluir.svg" alt="Excluir">
                                            </button>
                                        <% }%>
        
                                    </td>
                                </tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
            </section>
            
            <!-- Análise de Orçamentos -->
            <div class="head">
                <img src="images/icone_orcamento.svg" alt="Orçamentos"> 
                <span>
                    Orçamentos -<%= Util.pegarMes()%></span>
                <hr>
            </div>
            <section class="dados" style="margin-bottom: 5%;">
                <div class="infos">
                    <div class="info_orcamento">
                        <span style="color: #FF2D29;"><%= OrcamentoDAO.listarPorStatus("Aberto").size() + " " %></span> ABERTOS
                    </div>
                    <div class="info_orcamento">
                        <span style="color: #FFB729;"><%= OrcamentoDAO.listarPorStatus("Pendente").size()  + " "%></span>PENDENTES
                    </div>
                    <div class="info_orcamento">
                        <span style="color: #12a123;"><%= OrcamentoDAO.listarPorStatus("Concluído").size()  + " "%></span>CONCLUÍDOS
                    </div>
                </div>
                <div class="tabela" style="min-height: fit-content;">
                    <div class="campo">
                        <label class="titulo_campo">ÚLTIMOS ORÇAMENTOS</label>
                    </div>
                    <table class="table table-striped" style="background-color: white;">
                        <thead>
                            <tr>
                                <th>
                                    #
                                </th>
                                <th>
                                    ID
                                </th>
                                <th>
                                    Cliente
                                </th>
                                <th>
                                    Data de Criação
                                </th>
                                <th>
                                    Data de Validade
                                </th>
                                <th>
                                    Status
                                </th>
                                <th>
                                    Ações
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Orcamento> orcamentos = OrcamentoDAO.listar();
        
                                for (int i = orcamentos.size()-1; i >= orcamentos.size() - 5; i--) {
                            %>
                                <tr>
                                    <td>
                                        <%= orcamentos.size() - i%>
                                    </td>
                                    <td>
                                        <%= orcamentos.get(i).getId()%>
                                    </td>
                                    <td>
                                        <%= orcamentos.get(i).getCliente().getNome() == null? "---" : orcamentos.get(i).getCliente().getNome() %>
                                    </td>
                                    <td>
                                        <%= Util.converteData(orcamentos.get(i).getDataCriacao().toLocalDate())%>
                                    </td>
                                    <td>
                                        <%= Util.converteData(orcamentos.get(i).getDataValidade().toLocalDate())%>
                                    </td>
                                    <td>
                                        <%= orcamentos.get(i).getStatus()%>
                                    </td>
                                    
                                    <td>
                                        <button onclick="location.href = 'GerenciarOrcamento?id=<%= orcamentos.get(i).getId()%>&acao=2'" class="botao_acao" title="Alterar do orçamento <%= i+1%>">
                                            <img src="images/icone_alterar.svg" alt="Alterar">
                                        </button>
                                        <button onclick="location.href = 'detalhes_orcamento.jsp?id=<%= orcamentos.get(i).getId()%>'" class="botao_acao" title="Detalhes do orçamento <%= i+1%>">
                                            <img src="images/icone_detalhes.svg" alt="Detalhes">
                                        </button>
                                        <button onclick="confirmarExclusao(event, 'GerenciarOrcamento?id=<%= orcamentos.get(i).getId()%>&acao=3')" class="botao_acao" title="Excluir o orçamento <%= i+1%>">
                                            <img src="images/icone_excluir.svg" alt="Excluir">
                                        </button>
                                    </td>
                                </tr>
                            <%
                                }
                            %>
                            
                        </tbody>
        
                    </table>
                </div>
            </section>


            <!-- Análise de Produtos
            <div class="head">
                <img src="images/icone_produto.svg" alt="Produtos">
                <span>
                    Produtos - <%= Util.pegarMes()%>
                </span>
                <hr>
            </div>
            <section class="dados" id="info_produtos">
            </section> -->
        </section>
    </div>
</body>
</html>