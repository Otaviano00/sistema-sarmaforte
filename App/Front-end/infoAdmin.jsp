
<%@page import="model.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
/*   
NOTAS SOBRE O CÓDIGO:
Mesmo tendo o valor do atributo "hierarquia" guardado na session, parece
que o código só dá certo do jeito que fiz abaixo, tô com muita preguiça
de tentar entender o porquê. Não temos muito tempo para detalhes
como esse, o importante é funcionar.

NOTAS SOBRE A FUNCIONALIDADE:           
Se o usuario que quer acessar a página tiver um perfil de hierarquia nivel '2', ele é jogado para a home.
    */
int idSessao = (Integer) session.getAttribute("idUsuario");
Usuario usuarioo = UsuarioDAO.listarPorId(idSessao);
if (usuarioo.getPerfil().getHierarquia() == 2) {

    String mensagem = "Você não tem autorização.";
    out.print("<script>");
    out.print("alert('" + mensagem + "');");
    out.print("location.href = 'home.jsp';");
    out.print("</script>");

}
%>
