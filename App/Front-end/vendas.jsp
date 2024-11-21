<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Venda"%>
<%@page import="dao.VendaDAO"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>

    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/tabela.js"> </script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Venda</title>
    
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
            VENDAS
        </h1>
        <button class="novo" onclick="location.href = ('registrar_venda.jsp')">
            <div style="display: flex; justify-content: center; align-items: center; margin: auto; gap: 10px;">
                <span style="font-size: 2em;">+</span>
                Novo Venda
            </div>
        </button>
        <div class="tabela">
            <table class="table table-striped" style="background-color: white;">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Cliente</th>
                        <th>Vendedor</th>
                        <th>Data</th>
                        <th>Valor</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Venda> vendas = VendaDAO.listar();
                        for (int i = 0; i < vendas.size(); i++) {%>
                        <tr>
                            <td><%= i+1%></td>
                            <td><%= vendas.get(i).getOrcamento().getCliente().getNome()%></td>
                            <td><%= vendas.get(i).getUsuario().getNome()%></td> 
                            <td><%= vendas.get(i).getData().toLocalDate()%></td>       
                            <td><%= String.format("R$ %,.2f", vendas.get(i).getValor())%></td>  
                            <td>
                                <% if (hierarquia < 2) {%>
                                    <button onclick="location.href = 'alterar_venda.jsp?idVenda=<%= vendas.get(i).getId()%>'" class="botao_acao" title="Alterar dessa venda <%= i+1%>">
                                        <img src="images/icone_alterar.svg" alt="Alterar">
                                    </button>
                                    <button onclick="location.href = 'GerenciarVenda?idVenda=<%= vendas.get(i).getId()%>'" class="botao_acao" title="Excluir venda <%= i+1%>">
                                        <img src="images/icone_excluir.svg" alt="Excluir">
                                    </button>
                                <% }%>
                                <button onclick="location.href = 'detalhes_venda?idVenda=<%= vendas.get(i).getId()%>'" class="botao_acao" title="Detalhes da venda <%= i+1%>">
                                    <img src="images/icone_detalhes.svg" alt="Detalhes">
                                </button>
                            </td>
                        </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
