<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Cliente"%>
<%@page import="dao.ClienteDAO"%>
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
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/tabela.js"> </script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Cliente</title>
    
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
            CLIENTES
        </h1>
        <button class="novo" onclick="location.href = ('cadastrar_cliente.jsp')">
            <div style="display: flex; justify-content: center; align-items: center; margin: auto; gap: 10px;">
                <span style="font-size: 2em;">+</span>
                Novo Cliente
            </div>
        </button>
        <div class="tabela">
            <table class="table table-striped" style="background-color: white;">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Nome</th>
                        <th>Telefone</th>
                        <th>Endereço</th>
                        <th>CPF</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Cliente> clientes = ClienteDAO.listar();
                        for (int i = 0; i < clientes.size(); i++) {%>
                        <tr>
                            <td><%= i+1%></td>
                            <td><%= clientes.get(i).getNome()%></td>
                            <td><%= clientes.get(i).getTelefone()%></td> 
                            <td><%= clientes.get(i).getEndereco() == null? "---" : clientes.get(i).getEndereco()%></td>       
                            <td><%= clientes.get(i).getCpf() == null? "---" : clientes.get(i).getCpf()%></td>  
                            <td>
                               <button onclick="location.href = 'alterar_cliente.jsp?id=<%= clientes.get(i).getId()%>'" class="botao_acao" title="Alterar dados do cliente <%= clientes.get(i).getNome()%>">
                                    <img src="images/icone_alterar.svg" alt="Alterar">
                                </button>
                                <button onclick="location.href = 'detalhes_cliente.jsp?id=<%= clientes.get(i).getId()%>'" class="botao_acao" title="Detalhes do cliente <%= clientes.get(i).getNome()%>">
                                    <img src="images/icone_detalhes.svg" alt="Detalhes">
                                </button>
                                <% if (clientes.get(i).getId() != 5) {%>
                                    <button onclick="confirmarExclusao(event, 'GerenciarCliente?id=<%= clientes.get(i).getId()%>&acao=3')" class="botao_acao" title="Excluir o cliente <%= clientes.get(i).getNome()%>">
                                        <img src="images/icone_excluir.svg" alt="Excluir">
                                    </button> 
                                <%}%>
                            </td>
                        </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
