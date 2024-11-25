<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.Orcamento"%>
<%@page import="dao.OrcamentoDAO"%>
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
        <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">


        <title>Selecionar Orçametnto</title>
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
                Selecinar Orçamento
            </h1>
            <p>
                Selecione o orçamento para realizar a venda.
            </p>
            <button class="novo" onclick="location.href = ('GerenciarOrcamento?acao=1')">
                <div style="display: flex; justify-content: center; align-items: center; margin: auto; gap: 10px;">
                    <span style="font-size: 2em;">+</span>
                    Novo Orçamento
                </div>
            </button>
            <div class="tabela">
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
                                Selecionar
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Orcamento> orcamentos = OrcamentoDAO.listar();
    
                            for (int i = orcamentos.size()-1; i >= 0; i--) {
                                if (!orcamentos.get(i).getStatus().equals("Concluído")) {
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
                                    <% if (orcamentos.get(i).getStatus().equals("Pendente")) {%>
                                    <button class="botao_acao" onclick="location.href = 'alterar_venda.jsp?id=<%= orcamentos.get(i).getId()%>'" title="Selecionar o orçamento <%= orcamentos.get(i).getId()%>">
                                        <img src="images/icone_selecionar.svg" alt="Selecionar">
                                    </button>
                                    <%
                                        } else {   
                                    %>
                                    <button class="botao_acao" onclick="location.href = 'registrar_venda.jsp?id=<%= orcamentos.get(i).getId()%>'" title="Selecionar o orçamento <%= orcamentos.get(i).getId()%>">
                                        <img src="images/icone_selecionar.svg" alt="Selecionar">
                                    </button>
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                        <%
                                }

                            }
                        %>
                        
                    </tbody>
    
                </table>
            </div>
        </div>
    </body>
</html>