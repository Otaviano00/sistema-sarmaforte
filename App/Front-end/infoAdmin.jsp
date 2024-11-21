
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    /*   
    NOTAS SOBRE A FUNCIONALIDADE:           
    Se o usuario que quer acessar a página tiver um perfil de hierarquia nivel '2', ele é jogado para a home.
        */

if (hierarquia == 2) {

    String mensagem = "Você não tem autorização.";
    out.print("<script>");
    out.print("alert('" + mensagem + "');");
    out.print("location.href = 'home.jsp';");
    out.print("</script>");

}
%>
