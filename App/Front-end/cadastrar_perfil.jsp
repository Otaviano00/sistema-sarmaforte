
<%@page import="model.Menu"%>
<%@page import="dao.MenuDAO"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="sessao.jsp" %>
<%@include file="infoAdmin.jsp" %>

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
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Cadastrar Perfil</title>
    
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
            Cadastrar Perfil
        </h1>
        <form action="GerenciarPerfil" method="post">
            <input type="hidden" name="acao" value="1">
            <div class="campos">
                <label for="nome" class="titulo_campo">Nome: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="nome" title="Apenas caracters alfabéticos!" required>
            </div>
            
            <div class="campos">
                <label for="descricao" class="titulo_campo">Descrição:</label>
                <textarea name="descricao" rows="3" cols="30"></textarea>
            </div>
            
            <div class="campos">
                <label for="hierarquia" class="titulo_campo">Hierarquia: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                    <div style="display: flex; justify-content: start; align-items: start; gap: 10px;">
                        <label for="alta"  class="botao_radio">Alta 
                            <input type="radio" name="hierarquia" id="alta" value="1">
                        </label>
                        <label for="baixa" class="botao_radio">Baixa
                            <input type="radio" name="hierarquia" id="baixa" value="2" checked>
                        </label>
                    </div>
            </div>

            <div class="campos"><label class="titulo_campo">Menus de Acesso: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label></div>
            <div class="tabela">
                <table class="table table-striped" style="background-color: white;">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Imagem</th>
                            <th>Nome</th>
                            <th>Permissão</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Menu> menus = MenuDAO.listar();
                            for (int i = 0; i < menus.size(); i++) {
                                if (menus.get(i).isStatus()) {
                        %>
                                <tr>
                                    <td><%= i+1%></td>
                                    <td><img src="<%= menus.get(i).getImagem()%>" alt="<%= menus.get(i).getNome()%>"></td>
                                    <td><%= menus.get(i).getNome()%></td>
                                    <td>
                                    <input type="checkbox" name="menu" value="<%= menus.get(i).getId()%>">     
                                    </td>
                                </tr>
                            
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>

            <div style="display: flex; gap: 10px; margin: 20px; ">
                <button type="button" class="botao_cancela" onclick="location.href = 'perfis.jsp'"> Cancelar</button>
                <input type="submit" value="Cadastrar Dados" class="botao_confirma">
            </div>
        </form>
    </div>

</body>
</html>