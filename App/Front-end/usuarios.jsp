<%@page import="model.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Perfil"%>
<%@page import="dao.PerfilDAO"%>
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
    <title>Cadastrar Usuário</title>
    
</head>
<body>
    <%@include file="infoAdmin.jsp" %>
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
            USUÁRIOS
        </h1>
        <button class="novo" onclick="location.href = ('cadastrar_usuario.jsp')">
            <div style="display: flex; justify-content: center; align-items: center; margin: auto; gap: 10px;">
                <span style="font-size: 2em;">+</span>
                Novo Usuário
            </div>
        </button>
        <div class="tabela">
            <table class="table table-striped" style="background-color: white;">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Nome</th>   
                        <th>Perfil</th>
                        <th>Telefone</th>   
                        <th>Login</th> 
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Usuario> usuarios = UsuarioDAO.listar();
                        for (int i = 0; i < usuarios.size(); i++) {   
                            Usuario user = usuarios.get(i);     
                    %>
                            
                        <tr>
                            <td><%= i+1%></td>
                            <td><%= user.getNome()%></td>
                            <td><%= user.getPerfil().getNome()%></td>
                            <td><%= user.getTelefone()%></td>
                            <td><%= user.getLogin()%></td>                  
                            <% if (user.getPerfil().getNome().equals("Admin")) {%>
                                <td>
                                    <button class="botao_acao botao_ativo">
                                        Ativo
                                    </button>
                                </td>
                                <td>
                                    --
                                </td>
                            <% } else {%>
                                <td>
                                    <% if (user.isStatus()) {%>
                                        <button onclick="location.href = 'GerenciarUsuario?id=<%= user.getId()%>&valor=4'" class="botao_acao botao_ativo" title="Clique para desativar o usuário <%= user.getNome()%>">
                                            Ativo
                                        </button>
                                        
                                    <% } else {%>
                                        <button onclick="location.href = 'GerenciarUsuario?id=<%= user.getId()%>&valor=3'" class="botao_acao botao_desativo" title="Clique para ativar o usuário <%= user.getNome()%>">
                                            Desativo
                                        </button>
                                    <% }%>
                                </td>
                                <td>
                                    <button onclick="location.href = 'alterar_usuario.jsp?id=<%= user.getId()%>'" class="botao_acao" title="Alterar dados do <%= user.getNome()%>">
                                        <img src="images/icone_alterar.svg" alt="Alterar">
                                    </button>
                                </td>
                            <% }%>
                        </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

