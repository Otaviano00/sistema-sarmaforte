package controller;

import dao.PerfilDAO;
import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import model.Perfil;
import model.Usuario;

public class GerenciarUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarUsuario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarUsuario(request, response);
    }

    private static void exibirMensagem(PrintWriter out, String mensagem, String url) throws IOException {
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = '" + url + "';");
        out.print("</script>");
        out.close();
    }

    private static boolean validarUnicidade(Usuario usuario, List<Usuario> usuarios, PrintWriter out, String redirectUrl) throws IOException {
        for (Usuario u : usuarios) {
            if (u.getId() != usuario.getId()) { // Ignorar o próprio usuário
                if (usuario.getTelefone().equals(u.getTelefone())) {
                    exibirMensagem(out, "Telefone já existente! Por favor, informe outro número.", redirectUrl);
                    return true;
                }
                if (usuario.getLogin().equals(u.getLogin())) {
                    exibirMensagem(out, "Login já existente! Escolha outro nome de usuário.", redirectUrl);
                    return true;
                }
                if (usuario.getCpf().equals(u.getCpf())) {
                    exibirMensagem(out, "CPF já existente! Verifique os dados informados.", redirectUrl);
                    return true;
                }
                if (usuario.getEmail().equals(u.getEmail())) {
                    exibirMensagem(out, "Email já existente! Tente usar outro email.", redirectUrl);
                    return true;
                }
            }
        }
        return false;
    }

    public static void gerenciarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        HttpSession sessao = request.getSession();
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Integer hierarquia = (Integer) sessao.getAttribute("hierarquia");
        if (hierarquia == null) {
            exibirMensagem(out, "Sessão expirada! Faça login novamente.", "login.jsp");
            return;
        }

        int acao;
        try {
            acao = Integer.parseInt(request.getParameter("acao"));
        } catch (NumberFormatException e) {
            exibirMensagem(out, "Ação inválida!", "usuarios.jsp");
            return;
        }

        if (hierarquia == 2) {
            exibirMensagem(out, "Você não tem autorização para realizar essa ação!", "home.jsp");
            return;
        }

        Usuario usuario = new Usuario();
        switch (acao) {
            case 1: // Cadastro
                try {
                    usuario.setNome(request.getParameter("nome"));
                    usuario.setTelefone(request.getParameter("telefone"));
                    usuario.setLogin(request.getParameter("login"));
                    usuario.setSenha(request.getParameter("senha"));
                    usuario.setCpf(request.getParameter("cpf"));
                    usuario.setEmail(request.getParameter("email"));
                    Perfil perfil = PerfilDAO.listarPorId(Integer.parseInt(request.getParameter("perfil")));
                    usuario.setPerfil(perfil);
                    usuario.setStatus(true);

                    if (validarUnicidade(usuario, UsuarioDAO.listar(), out, "cadastrar_usuario.jsp")) {
                        return;
                    }

                    if (UsuarioDAO.cadastrar(usuario)) {
                        exibirMensagem(out, "Usuário cadastrado com sucesso!", "usuarios.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao cadastrar o usuário!", "usuarios.jsp");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log da exceção para diagnóstico
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "cadastrar_usuario.jsp");
                }
                break;

            case 2: // Atualização
                try {
                    usuario.setId(Integer.parseInt(request.getParameter("id")));
                    usuario.setNome(request.getParameter("nome"));
                    usuario.setTelefone(request.getParameter("telefone"));
                    usuario.setLogin(request.getParameter("login"));
                    usuario.setSenha(request.getParameter("senha"));
                    usuario.setCpf(request.getParameter("cpf"));
                    usuario.setEmail(request.getParameter("email"));
                    Perfil perfil = PerfilDAO.listarPorId(Integer.parseInt(request.getParameter("perfil")));
                    usuario.setPerfil(perfil);
                    usuario.setStatus(Boolean.parseBoolean(request.getParameter("status")));

                    if (validarUnicidade(usuario, UsuarioDAO.listar(), out, ("alterar_usuario.jsp?id=" + usuario.getId()))) {
                        return;
                    }

                    if (UsuarioDAO.alterar(usuario)) {
                        exibirMensagem(out, "Usuário alterado com sucesso!", "usuarios.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao alterar o usuário!", "usuarios.jsp");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log da exceção para diagnóstico
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "alterar_usuario.jsp");
                }
                break;

            case 3: // Ativar
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    usuario.setId(id);

                    if (!UsuarioDAO.ativar(usuario)) {
                        exibirMensagem(out, "Erro ao ativar o usuário!", "usuarios.jsp");
                    } else {
                        response.sendRedirect("usuarios.jsp");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log da exceção para diagnóstico
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "usuarios.jsp");
                }
                break;

            case 4: // Desativar
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    usuario.setId(id);
                    if (!UsuarioDAO.desativar(usuario)) {
                        exibirMensagem(out, "Erro ao desativar o usuário!", "usuarios.jsp");
                    } else {
                        response.sendRedirect("usuarios.jsp");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log da exceção para diagnóstico
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "usuarios.jsp");
                }
                break;

            case 5: // Excluir (somente hierarquia 0)
                if (hierarquia != 0) {
                    exibirMensagem(out, "Você não tem autorização para excluir usuários!", "usuarios.jsp");
                    return;
                }
                try {
                    int id = Integer.parseInt(request.getParameter("id"));

                    if (UsuarioDAO.excluir(id)) {
                        exibirMensagem(out, "Usuário excluído com sucesso!", "usuarios.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao excluir o usuário!", "usuarios.jsp");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log da exceção para diagnóstico
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "usuarios.jsp");
                }
                break;

            default:
                exibirMensagem(out, "Ação inválida!", "usuarios.jsp");
                break;
        }
    }
}
