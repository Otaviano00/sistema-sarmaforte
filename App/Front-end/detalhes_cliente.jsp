<%@page import="model.Cliente"%>
<%@page import="dao.ClienteDAO"%>
<%@page import="model.Orcamento"%>
<%@page import="java.util.List"%>
<%@page import="utilities.Util"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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

    <script defer src="script/tabela.js"> </script>
    <script defer src="script/cadastrar_alterar.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Detalhes Cliente</title>
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
        <%
            Cliente cliente = ClienteDAO.listarPorId(Integer.parseInt(request.getParameter("id")));
        %>
        <h1 class="titulo">
            Detalhes Cliente
        </h1>
        <form action="#" method="post">
            <div class="campos">
                <label for="nome" class="titulo_campo">Nome: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="nome" title="Apenas caracteres alfabéticos!" value="<%= cliente.getNome()%>" required disabled readonly>
            </div>

            <div class="campos">
                <label for="telefone" class="titulo_campo">Telefone: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="telefone" title="Apenas caracteres numéricos!" value="<%= cliente.getTelefone()%>" required disabled>
            </div>

            <div class="campos">
                <label for="cpf" class="titulo_campo">CPF: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="cpf" value="<%= cliente.getCpf()%>" required disabled readonly>
            </div>

            <div class="campos">
                <label for="endereco" class="titulo_campo">Endereço:</label>
                <input type="text" name="endereco" value="<%= cliente.getEndereco()%>" disabled readonly>
            </div>

            <div class="campos"><label for="" class="titulo_campo">Orçamentos:</label></div>
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
                            List<Orcamento> orcamentos = ClienteDAO.listarOrcamentos(cliente.getId());
    
                            for (int i = orcamentos.size()-1; i >= 0; i--) {
                        %>
                            <tr>
                                <td>
                                    <%= orcamentos.size() - i%>
                                </td>
                                <td>
                                    <%= orcamentos.get(i).getId()%>
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
                                    <button type="button" onclick="location.href = 'detalhes_orcamento.jsp?id=<%= orcamentos.get(i).getId()%>'" class="botao_acao" title="Detalhes do orçamento <%= i+1%>">
                                        <img src="images/icone_detalhes.svg" alt="Detalhes">
                                    </button>
                                </td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
    
                </table>
            </div>
            <div style="display: flex; gap: 10px; margin: 20px;">
                <button type="button" class="botao_cancela" onclick="location.href = 'clientes.jsp'">Voltar</button>
            </div>
        </form>
    </div>
</body>
</html>
