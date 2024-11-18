<%@page import="model.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
<%@page import="model.Perfil"%>
<%@page import="dao.PerfilDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link rel="stylesheet" href="style/main.css">
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
        <form action="GerenciarUsuario" method="post">

            <% Usuario usu = UsuarioDAO.listarPorId(Integer.parseInt(request.getParameter("id"))); %>
            <% if (usu.getLogin().equals("admin")) {%>

            <input type="hidden" name="valor" value="2">
            <input type="hidden" name="id" value="<%= usu.getId()%>">
            <input type="hidden" name="nome" value="<%= usu.getNome()%>">
            <input type="hidden" name="telefone" value="<%= usu.getTelefone()%>">
            <input type="hidden" name="login" value="<%= usu.getLogin()%>">
            <input type="hidden" name="cpf" value="<%= usu.getCpf()%>">
            <input type="hidden" name="email" value="<%= usu.getEmail()%>">
            <input type="hidden" name="perfil" value="<%= usu.getPerfil().getId()%>">
            <input type="hidden" name="status" value="<%= usu.isStatus()%>">

            <p>
                Nome:<%= usu.getNome()%>
            </p>
            <p>
                Login:<%= usu.getLogin()%>
            </p>
            <p>
                Senha:<input type="password" name="senha" maxlength="8" size="8" value="<%= usu.getSenha()%>" required>
            </p>
            <p>
                Perfil: <%= usu.getPerfil().getNome()%>   
            </p>
            <p>
                <input type="submit" value="Alterar dados">
            </p>

            <% } else {%>

            <input type="hidden" name="valor" value="2">
            <input type="hidden" name="id" value="<%= usu.getId()%>">
            <input type="hidden" name="status" value="<%= usu.isStatus()%>">

            <p>
                Nome:<input type="text" name="nome" 
                            maxlength="50" title="Apenas caracters alfabéticos!" value="<%= usu.getNome()%>" required>
            </p> 
            <p>
                Telefone:<input type="text" name="telefone"
                                placeholder="(00)90000-0000"  maxlength="11" 
                                title="Apenas caracters numericos!" size="11" value="<%= usu.getTelefone()%>" required>
            </p>
            <p>
                Login:<input type="text" name="login" pattern="[a-z]+" maxlength="6" 
                             title="Apenas caracters  alfabéticos!" size="6" value="<%= usu.getLogin()%>" required>
            </p>
            <p>
                Senha:<input type="password" name="senha" maxlength="8" size="8" value="<%= usu.getSenha()%>" required>
            </p>
            <p>
                CPF:<input type="text" name="cpf" 
                           placeholder="000.000.000-00" maxlength="11" 
                           title="Apenas caracters numericos!" size="11" value="<%= usu.getCpf()%>" required>
            </p>
            <p>
                Email:<input type="email" name="email" placeholder="seuemail@exemplo.com" value="<%= usu.getEmail()%>" required>
            </p>
            <p>
                Perfil:
                <select name="perfil">
                    <% for (Perfil p : PerfilDAO.listar()) {%> 
                    <% if (!p.getNome().equals("Admin") && p.isStatus() != false) {%>
                    <% if (usu.getPerfil().getNome().equals(p.getNome())) {%>

                    <option value="<%= p.getId()%>" selected><%= p.getNome()%></option>

                    <% } else {%>

                    <option value="<%= p.getId()%>"><%= p.getNome()%></option>

                    <% }%> 
                    <% }%> 
                    <% }%>
                </select>
            </p>
            <p>
                <input type="submit" value="Alterar dados">
            </p>
            <% }%>
        </form>  
    </div>
    <footer>
        <p> &copy; Samaforte - Natanel</p>
    </footer>
</body>
</html>
