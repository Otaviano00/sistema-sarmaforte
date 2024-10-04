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

public class ProcessaLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        
        String mensagem = "";
        
        Usuario usuario = new Usuario();
        
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            boolean logado = usuarioDAO.efetuarLogin(login, senha);
            if (logado) {
                HttpSession sessao = request.getSession();
                sessao.setAttribute("logado", logado);
                response.sendRedirect("index.html");
            } else {
                out.print("<script>");
                out.print("alert('" + mensagem + "')");
                out.print("</script>");
                out.close();
                response.sendRedirect("login.html");
            }
        } catch (Exception e) {
            exibirMensagem("Ocorreu um problema com o banco de dados.", response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    private static void exibirMensagem(String mensagem, HttpServletResponse response) throws IOException {
        
    }

}
