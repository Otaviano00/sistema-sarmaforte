<%@page import="model.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
<%@page import="model.Perfil"%>
<%@page import="dao.PerfilDAO"%>
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
    <script defer src="script/cadastrar_alterar.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Alterar Usuário</title>
    
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
            Usuario usuario = UsuarioDAO.listarPorId(Integer.parseInt(request.getParameter("id")));
        %>
        <h1 class="titulo">
            Alterar Usuário
        </h1>
        <form action="GerenciarUsuario" method="post">
            <input type="hidden" name="acao" value="2">
            <input type="hidden" name="id" value="<%= usuario.getId()%>">
            <div class="campos">
                <label for="nome" class="titulo_campo">Nome: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="nome" title="Apenas caracters alfabéticos!" value="<%= usuario.getNome()%>" required>
            </div>

            <div class="campos">
                <label for="telefone" class="titulo_campo">Telefone: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="telefone" title="Apenas caracteres númericos!" value="<%= usuario.getTelefone()%>" required>
            </div>
            
            <div class="campos">
                <label for="login" class="titulo_campo">Login: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="login" value="<%= usuario.getLogin()%>" required>
            </div>

            <div class="campos">
                <label for="senha" class="titulo_campo">Senha: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="password" name="senha" value="<%= usuario.getSenha()%>" required>
            </div>

            <div class="campos">
                <label for="cpf" class="titulo_campo">CPF: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="cpf" value="<%= usuario.getCpf()%>" required>
            </div>

            <div class="campos">
                <label for="email" class="titulo_campo">Email:</label>
                <input type="email" name="email" value="<%= usuario.getEmail()%>">
            </div>

            <div class="campos" >
                <label for="perfil" class="titulo_campo">Perfil: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <select name="perfil" class="seletor">
                    <% for (Perfil p : PerfilDAO.listar()) {%>
                        <% if (!p.getNome().equals("Admin") && p.isStatus() != false) {%>
                            <option value="<%= p.getId()%>" <%= p.getId() == usuario.getPerfil().getId()? "selected" : " "%>><%= p.getNome()%></option>
                        <% }%>
                    <% }%>
                </select>
            </div>

            <div class="campos" style="gap: 5px;">
                <label for="status" class="titulo_campo">Status: </label>
                <div style="display: flex; justify-content: start; align-items: start; gap: 10px;">
                    <label for="ativo"  class="botao_radio">Ativo
                        <input type="radio" name="status" id="ativo" value="true" <%= usuario.isStatus()? "checked" : " "%> >
                    </label>
                    <label for="desativo" class="botao_radio">Desativo
                        <input type="radio" name="status" id="desativo" value="false" <%= !usuario.isStatus()? "checked" : " "%>>
                    </label>
                </div>
            </div>

            <div style="display: flex; gap: 10px; margin: 20px; ">
                <button type="button" class="botao_cancela" onclick="location.href = 'usuarios.jsp'"> Cancelar</button>
                <input type="submit" value="Alterar Dados" class="botao_confirma">
            </div>
        </form>
    </div>
</body>
</html>