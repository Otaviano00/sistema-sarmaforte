<%@page import="dao.PerfilDAO"%>
<%@page import="model.Menu"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String nomePagina = request.getRequestURI();
    nomePagina = nomePagina.substring(nomePagina.lastIndexOf("/") + 1, nomePagina.lastIndexOf(".")); 

    if (nomePagina.equals("detalhes_usuario")) {
        Integer idDetalhes = Integer.parseInt(request.getParameter("id"));
        if (idDetalhes != idU && hierarquia == 2) {
            String mensagem = "Você não tem autorização para acessar essa página!";
            out.print("<script>");
            out.print("alert('" + mensagem + "');");
            out.print("location.href = 'home.jsp';");
            out.print("</script>");
            return; 
        }
    } else if (hierarquia == 2) {
        String mensagem = "Você não tem autorização para acessar essa página!";
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = 'home.jsp';");
        out.print("</script>");
        return; 
    }
%>
