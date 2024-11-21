package controller;

import dao.MenuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
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

    public static void gerenciarMenu(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {

        request.setCharacterEncoding("UTF-8");
        int acao = Integer.parseInt(request.getParameter("acao"));

        Menu me = new Menu();

        switch (acao) {

            case 1:
                // CADASTRAR MENU
                try {

                    String nome = request.getParameter("nome");
                    String link = request.getParameter("link");
                    String imagem = request.getParameter("imagem");

                    me.setNome(nome);
                    me.setLink(link);
                    me.setImagem("images/" + imagem);

                    //Tratamento de dados
                    for (Menu m : MenuDAO.listar()) {

                        if (me.getLink().equals(m.getLink())) {

                            String mensagem = "Link já existente!";
                            PrintWriter out = response.getWriter();
                            out.print("<script>");
                            out.print("alert('" + mensagem + "');");
                            out.print("location.href = 'cadastrar_menu.jsp';");
                            out.print("</script>");
                            out.close();
                            break;

                        }

                        if (me.getImagem().equals(m.getImagem())) {

                            String mensagem = "Imagem já existente!";
                            PrintWriter out = response.getWriter();
                            out.print("<script>");
                            out.print("alert('" + mensagem + "');");
                            out.print("location.href = 'cadastrar_menu.jsp';");
                            out.print("</script>");
                            out.close();
                            break;

                        }

                    }

                    String mensagem = "Deseja inserir esses dados?";
                    String mensagem2 = "Cadastro bem sussedido!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    MenuDAO.cadastrar(me);
                    out.print("alert('" + mensagem2 + "');");
                    out.print("location.href = 'menus.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {
                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'menus.jsp';");
                    out.print("</script>");
                    out.close();
                }

                break;
            case 2:
                // ALTERAR MENU
                try {
                    int idm = Integer.parseInt(request.getParameter("id"));
                    String nome = request.getParameter("nome");
                    String link = request.getParameter("link");
                    String imagem = request.getParameter("imagem");
                    boolean status = Boolean.parseBoolean(request.getParameter("status"));

                    Menu ma = MenuDAO.listarPorId(idm);

                    // Se nenhuma imagem for escolhida, conservaremos o valor dela com a do banco
                    if (imagem.equals("")) {

                        imagem = ma.getImagem();

                    } else {

                        imagem = "images/" + request.getParameter("imagem");

                    }

                    me.setId(idm);
                    me.setNome(nome);
                    me.setLink(link);
                    me.setImagem(imagem);
                    me.setStatus(status);

                    for (Menu mme : MenuDAO.listar()) {

                        if (mme.getId() != ma.getId()) {

                            if (me.getLink().equals(mme.getLink())) {

                                String mensagem = "Link já existente!";
                                PrintWriter out = response.getWriter();
                                out.print("<script>");
                                out.print("alert('" + mensagem + "');");
                                out.print("location.href = 'alterar_menu.jsp?id=" + ma.getId() + "';");
                                out.print("</script>");
                                out.close();

                            }

                            if (me.getImagem().equals(mme.getImagem())) {

                                String mensagem = "Imagem já existente!";
                                PrintWriter out = response.getWriter();
                                out.print("<script>");
                                out.print("alert('" + mensagem + "');");
                                out.print("location.href = 'alterar_menu.jsp?id=" + ma.getId() + "';");
                                out.print("</script>");
                                out.close();

                            }

                        }

                    }

                    String mensagem = "Deseja inserir esses dados?";
                    String mensagem2 = "Cadastro bem sussedido!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    MenuDAO.alterar(me);
                    out.print("alert('" + mensagem2 + "');");
                    out.print("location.href = 'menus.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {
                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'menus.jsp';");
                    out.print("</script>");
                    out.close();
                }

                break;
            case 3: 
                //ATIVAR
                try {

                    int id = Integer.parseInt(request.getParameter("id"));
                    me.setId(id);
                    MenuDAO.ativar(me);
                    response.sendRedirect("menus.jsp");

                } catch (Exception e) {
                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'menus.jsp';");
                    out.print("</script>");
                    out.close();
                }

                break;
            case 4:
                //DESATIVAR
                try {

                    int id = Integer.parseInt(request.getParameter("id"));
                    me.setId(id);
                    MenuDAO.desativar(me);
                    response.sendRedirect("menus.jsp");

                } catch (Exception e) {
                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'menus.jsp';");
                    out.print("</script>");
                    out.close();
                }

                break;
            case 5: 
                //EXCLUIR
                try {

                    int id = Integer.parseInt(request.getParameter("id"));
                    me.setId(id);
                    MenuDAO.excluir(me);
                    response.sendRedirect("menus.jsp");

                } catch (Exception e) {
                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'menus.jsp';");
                    out.print("</script>");
                    out.close();
                }

                break;

        }

    }

}
