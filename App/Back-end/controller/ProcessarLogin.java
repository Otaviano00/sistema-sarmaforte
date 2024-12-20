package controller;

import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import model.Usuario;

/*
 * @author Kauã Otaviano
 */

public class ProcessarLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        
        String mensagem = "";
        
        try {
            Usuario usuario = UsuarioDAO.efetuarLogin(login, senha);
            if (usuario == null) {
                exibirMensagem("Login ou senha inválidos. Tente Novamente.", response);
                response.sendRedirect("login.html");
            } else if (!usuario.isStatus()) {
                exibirMensagem("O usuário está desativado.", response);
                response.sendRedirect("login.html");
            } else if (!usuario.getPerfil().isStatus()){
                exibirMensagem("O cargo do usuário está desativado. Contate o administrador.", response);
                response.sendRedirect("login.html");
            } else {
                criarVariavelSessao(usuario, request, response);
                response.sendRedirect("home.jsp");
            }
        } catch (Exception e) {
            exibirMensagem("Ocorreu um problema com o banco de dados.", response);
            response.sendRedirect("login.html");
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    private static void exibirMensagem(String mensagem, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = 'login.html';");
        out.print("</script>");
        out.close();
    }
    
    private static void criarVariavelSessao(Usuario usuario, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sessao = request.getSession();
        int idUsuario = usuario.getId();
        String nomeUsuario = usuario.getNome();
        int idPerfil = usuario.getPerfil().getId();
        String nomePerfil = usuario.getPerfil().getNome();
        int hierarquia = usuario.getPerfil().getHierarquia();
        
        sessao.setAttribute("idUsuario", idUsuario);
        sessao.setAttribute("nomeUsuario", nomeUsuario);
        sessao.setAttribute("idPerfil", idPerfil);
        sessao.setAttribute("nomePerfil", nomePerfil);
        sessao.setAttribute("hierarquia", hierarquia);
       
    }

}
