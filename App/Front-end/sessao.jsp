<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String nome = (String) session.getAttribute("nomeUsuario");
    Integer idU = (Integer) session.getAttribute("idUsuario");
    String cargo = (String) session.getAttribute("nomePerfil");
    Integer idP = (Integer) session.getAttribute("idPerfil");
    Integer hierarquia = (Integer) session.getAttribute("hierarquia");

    if (nome == null || cargo == null || idU == null || hierarquia == null || idP == null) {
        String mensagem = "Você não está logado no sistema.";
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = 'login.html';");
        out.print("</script>");
        return; 
    }

    hierarquia = hierarquia == null ? 0 : hierarquia;
%>