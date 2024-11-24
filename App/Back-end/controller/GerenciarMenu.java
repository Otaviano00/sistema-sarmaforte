package controller;

import dao.MenuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import model.Menu;

public class GerenciarMenu extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarMenu(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarMenu(request, response);
    }

    private static void exibirMensagem(PrintWriter out, String mensagem, String url) throws IOException {
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = '" + url + "';");
        out.print("</script>");
        out.close();
    }

    private static boolean validarUnicidade(Menu menu, PrintWriter out, String redirectUrl) throws IOException {
        // Verifica se o link ou a imagem já existem no banco
        for (Menu m : MenuDAO.listar()) {
            if (m.getId() != menu.getId()) {
                if (menu.getLink().equals(m.getLink())) {
                    exibirMensagem(out, "Link já existente!", redirectUrl);
                    return true;
                }
                if (menu.getImagem().equals(m.getImagem())) {
                    exibirMensagem(out, "Imagem já existente!", redirectUrl);
                    return true;
                }  
            }
        }
        return false;
    }

    public static void gerenciarMenu(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        HttpSession sessao = request.getSession();
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String mensagem = "";

        Integer hierarquia = (Integer) sessao.getAttribute("hierarquia");
        if (hierarquia == null) {
            exibirMensagem(out, "Sessão expirada! Faça login novamente.", "login.jsp");
            return;
        }

        int acao;
        try {
            acao = Integer.parseInt(request.getParameter("acao"));
        } catch (NumberFormatException e) {
            exibirMensagem(out, "Ação inválida!", "menus.jsp");
            return;
        }

        if (hierarquia == 2) {
            exibirMensagem(out, "Você não tem autorização para realizar essa ação!", "home.jsp");
            return;
        }
        
        Menu me = new Menu();

        switch (acao) {
            case 0:
                // Caso de não autorização
                mensagem = "Você não tem autorização para realizar essa ação!";
                exibirMensagem(out, mensagem, "home.jsp");
                break;

            case 1: // Cadastrar Menu
                try {
                    String nome = request.getParameter("nome");
                    String link = request.getParameter("link");
                    String imagem = request.getParameter("imagem");

                    me.setNome(nome);
                    me.setLink(link);
                    me.setImagem("images/" + imagem);

                    // Verificar unicidade de link e imagem
                    if (validarUnicidade(me, out, "cadastrar_menu.jsp")) {
                        return;
                    }

                    // Cadastro no banco
                    if (MenuDAO.cadastrar(me)) {
                        exibirMensagem(out, "Menu cadastrado com sucesso!", "menus.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao cadastrar o menu!", "cadastrar_menu.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "menus.jsp");
                }
                break;

            case 2: // Alterar Menu
                try {
                    int idm = Integer.parseInt(request.getParameter("id"));
                    String nome = request.getParameter("nome");
                    String link = request.getParameter("link");
                    String imagem = request.getParameter("imagem");
                    boolean status = Boolean.parseBoolean(request.getParameter("status"));

                    Menu ma = MenuDAO.listarPorId(idm);

                    // Se nenhuma imagem for escolhida, conservamos a imagem do banco
                    if (imagem.equals("")) {
                        imagem = ma.getImagem();
                    } else {
                        imagem = "images/" + imagem;
                    }

                    me.setId(idm);
                    me.setNome(nome);
                    me.setLink(link);
                    me.setImagem(imagem);
                    me.setStatus(status);

                    // Verificar unicidade de link e imagem
                    if (validarUnicidade(me, out, "alterar_menu.jsp?id=" + ma.getId())) {
                        return;
                    }

                    // Atualizar no banco
                    if (MenuDAO.alterar(me)) {
                        exibirMensagem(out, "Menu alterado com sucesso!", "menus.jsp");
                        } else {
                        exibirMensagem(out, "Erro ao alterar o menu!", "menus.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "menus.jsp");                }
                break;

            case 3: // Ativar Menu
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    me.setId(id);

                    // Ativar o menu
                    if (MenuDAO.ativar(me)) {
                        response.sendRedirect("menus.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao ativar o menu!", "menus.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "menus.jsp");
                }
                break;

            case 4: // Desativar Menu
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    me.setId(id);

                    // Desativar o menu
                    if (MenuDAO.desativar(me)) {
                        response.sendRedirect("menus.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao desativar o menu!", "menus.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "menus.jsp");
                }
                break;

            case 5: // Excluir Menu
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    me.setId(id);

                    // Excluir o menu
                    if (MenuDAO.excluir(me)) {
                        response.sendRedirect("menus.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao excluir o menu!", "menus.jsp");
                    }

                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "menus.jsp");
                }
                break;

            default:
                mensagem = "Ação inválida!";
                exibirMensagem(out, mensagem, "menus.jsp");
                break;
        }
    }
}
