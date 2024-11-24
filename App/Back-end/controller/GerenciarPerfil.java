package controller;

import dao.PerfilDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import model.Menu;
import model.Perfil;

public class GerenciarPerfil extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarPerfil(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarPerfil(request, response);
    }

    private static void exibirMensagem(PrintWriter out, String mensagem, String url) throws IOException {
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = '" + url + "';");
        out.print("</script>");
        out.close();
    }

    private static boolean validarUnicidade(Perfil perfil, List<Perfil> perfis, PrintWriter out, String redirectUrl) throws IOException {
        for (Perfil p : perfis) {
            if (p.getId() != perfil.getId()) { // Ignorar o próprio perfil
                if (perfil.getNome().equals(p.getNome())) {
                    exibirMensagem(out, "Nome de perfil já existente!", redirectUrl);
                    return true;
                }
            }
        }
        return false;
    }

    public static void gerenciarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession sessao = request.getSession();
        PrintWriter out = response.getWriter();

        Integer hierarquia = (Integer) sessao.getAttribute("hierarquia");
        if (hierarquia == null) {
            exibirMensagem(out, "Sessão expirada! Faça login novamente.", "login.jsp");
            return;
        }

        int acao = Integer.parseInt(request.getParameter("acao"));
        Perfil p = new Perfil();

        if (hierarquia == 2) {
            exibirMensagem(out, "Você não tem autorização para realizar essa ação!", "home.jsp");
            return;
        }

        switch (acao) {

            case 1: // Cadastro
                try {
                    String nome = request.getParameter("nome");
                    String descricao = request.getParameter("descricao");
                    int hierarquiaPerfil = Integer.parseInt(request.getParameter("hierarquia"));
                    String[] menus = request.getParameterValues("menu");
                    boolean status = true;

                    p.setNome(nome);
                    p.setDescricao(descricao);
                    p.setHierarquia(hierarquiaPerfil);
                    p.setStatus(status);

                    // Validação de unicidade
                    if (validarUnicidade(p, PerfilDAO.listar(), out, "cadastrar_perfil.jsp")) {
                        return;
                    }

                    // Registrar perfil no banco
                    if (PerfilDAO.cadastrar(p)) {
                        List<Perfil> perfis = PerfilDAO.listar();
                        int idPerfil = perfis.get(perfis.size() - 1).getId();

                        // Vincula os menus selecionados
                        for (String idMenu : menus) {
                            PerfilDAO.vincular(idPerfil, Integer.parseInt(idMenu));
                        }

                        exibirMensagem(out, "Cadastro realizado com sucesso!", "perfis.jsp");

                    } else {
                        exibirMensagem(out, "Erro ao cadastrar perfil!", "perfis.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "perfis.jsp");
                }
                break;

            case 2: // Atualização
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String nome = request.getParameter("nome");
                    String descricao = request.getParameter("descricao");
                    int hierarquiaPerfil = Integer.parseInt(request.getParameter("hierarquia"));
                    Boolean status = Boolean.parseBoolean(request.getParameter("status"));
                    String[] menus = request.getParameterValues("menu");

                    Perfil exis = PerfilDAO.listarPorId(id);

                    p.setId(id);
                    p.setNome(nome);
                    p.setDescricao(descricao);
                    p.setHierarquia(hierarquiaPerfil);
                    p.setStatus(status);

                    // Validação de unicidade
                    if (validarUnicidade(p, PerfilDAO.listar(), out, "alterar_perfil.jsp?id=" + exis.getId())) {
                        return;
                    }

                    // Desvincula os menus anteriores e vincula os novos
                    List<Menu> m = PerfilDAO.listarMenus(id);
                    for (Menu menu : m) {
                        PerfilDAO.desvincular(id, menu.getId());
                    }

                    // Vincula os novos menus
                    for (String idMenu : menus) {
                        PerfilDAO.vincular(id, Integer.parseInt(idMenu));
                    }

                    String mensagem = "Dados alterados com sucesso!";
                    if (p.getId() == 1 || p.getId() == 5) {
                        mensagem = "Não pode alterar esse perfil!";
                    } else {
                        PerfilDAO.alterar(p);
                    }

                    exibirMensagem(out, mensagem, "perfis.jsp");

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "perfis.jsp");
                }
                break;

            case 3: // Ativar Perfil
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    p.setId(id);
                    if (PerfilDAO.ativar(p)) {
                        response.sendRedirect("perfis.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao ativar perfil!", "perfis.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "perfis.jsp");
                }
                break;

            case 4: // Desativar Perfil
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    p.setId(id);

                    if (p.getId() == 1 || p.getId() == 5) {
                        exibirMensagem(out, "Não pode desativar esse perfil!", "perfis.jsp");
                    } else if (PerfilDAO.desativar(p)) {
                        response.sendRedirect("perfis.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao desativar perfil!", "perfis.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "perfis.jsp");
                }
                break;

            case 5: // Excluir Perfil (somente hierarquia 0)
                if (hierarquia != 0) {
                    exibirMensagem(out, "Você não tem autorização para excluir perfis!", "perfis.jsp");
                    return;
                }
                try {
                    int id = Integer.parseInt(request.getParameter("id"));

                    String mensagem = "Perfil excluído com sucesso!";
                    if (p.getId() == 1 || p.getId() == 5) {
                        mensagem = "Não pode excluir esse perfil!";
                    } else if (PerfilDAO.excluir(id)) {
                        exibirMensagem(out, mensagem, "perfis.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao excluir perfil!", "perfis.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "perfis.jsp");
                }
                break;

            default:
                exibirMensagem(out, "Ação não encontrada!", "perfis.jsp");
                break;
        }
    }
}
