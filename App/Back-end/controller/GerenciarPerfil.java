package controller;

import dao.PerfilDAO;
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

    public static void gerenciarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {

        request.setCharacterEncoding("UTF-8");
        int acao = Integer.parseInt(request.getParameter("acao"));
        Perfil p = new Perfil();

        switch (acao) {

            case 1:

                try {

                    // INSERIR DADOS
                    String nome = request.getParameter("nome");
                    String descricao = request.getParameter("descricao");
                    int hierarquia = Integer.parseInt(request.getParameter("hierarquia"));
                    boolean status = true;

                    p.setNome(nome);
                    p.setDescricao(descricao);
                    p.setHierarquia(hierarquia);
                    p.setStatus(status);

                    /*
                      Sei que oq eu fiz abaixo não é uma boa prática, já que envolve muito 
                    processamento desnecessario, e na hora de escalar, vai ser uma dor de cabeça e,
                    criar um método DAO que receba um nome e retorne um id correspondente
                    é bem mais eficiente, mas isso não altera em nada o TCC, e nenhum de nós 
                    estamos afim de tocar na DAO das classes. 
                     */
                    for (Perfil pe : PerfilDAO.listar()) {

                        if (pe.getNome().equals(p.getNome())) {

                            String mensagem = "Nome já existente!";
                            PrintWriter out = response.getWriter();
                            out.print("<script>");
                            out.print("alert('" + mensagem + "');");
                            out.print("location.href = 'cadastrar_perfil.jsp';");
                            out.print("</script>");
                            out.close();

                            break;

                        }

                    }

                    /*
                      Não sei o que voce queria dizer quando disse "exibir uma janela com as informações de cadastro"
                    o alert parece não aceitar o clássico '\n', vai saber, vou deixar para o vcs do Front-End.
                     */
                    String mensagem = "Deseja inserir esses dados?";
                    String mensagem2 = "Cadastro bem sussedido!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    PerfilDAO.cadastrar(p);
                    out.print("alert('" + mensagem2 + "');");
                    out.print("location.href = 'perfis.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'perfis.jsp';");
                    out.print("</script>");
                    out.close();

                }

                break;

            case 2:

                try {

                    // ALTERAR DADOS
                    int id = Integer.parseInt(request.getParameter("id"));
                    String nome = request.getParameter("nome");
                    String descricao = request.getParameter("descricao");
                    int hierarquia = Integer.parseInt(request.getParameter("hierarquia"));
                    boolean status = Boolean.parseBoolean(request.getParameter("status"));

                    // Primeiro eu pego os dados com base no id acima, usaremos isso para a validação
                    Perfil exis = PerfilDAO.listarPorId(id);

                    p.setId(id);
                    p.setNome(nome);
                    p.setDescricao(descricao);
                    p.setHierarquia(hierarquia);
                    p.setStatus(status);

                    // Agora comparo os dados do objeto extraidos do banco com os dados recebidos da outra página
                    // Inicio o laco de repetição e começa a validação
                    for (Perfil lala : PerfilDAO.listar()) {

                        // Aqui eu só comparo os valores com os dados de outros campos
                        if (lala.getId() != exis.getId()) {

                            // aqui eu começo minha validação para ver se não se reptete valores que nao podem ser repetidos na tabela
                            if (p.getNome().equals(lala.getNome())) {

                                String mensagem = "Nome de perfil já existente!";
                                PrintWriter out = response.getWriter();
                                out.print("<script>");
                                out.print("alert('" + mensagem + "');");
                                out.print("location.href = 'alterar_perfil.jsp?idPerfil=" + exis.getId() + "';");
                                out.print("</script>");
                                out.close();
                                break;

                            }

                        }
                    }


                    /*
                    Não sei o que voce queria dizer quando disse "exibir uma janela com as informações alteradas"
                    o alert parece não aceitar o clássico '\n', vai saber, vou deixar para o vcs do Front-End.
                     */
                    String mensagem = "Deseja alterar esses dados?";
                    String mensagem2 = "Dados alterados com susseco!";

                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    PerfilDAO.alterar(p);
                    out.print("alert('" + mensagem2 + "');");
                    out.print("location.href = 'perfis.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'perfis.jsp';");
                    out.print("</script>");
                    out.close();

                }

                break;

            case 3:

                try {

                    // ATIVAR PERFIL
                    int id = Integer.parseInt(request.getParameter("id"));
                    p.setId(id);
                    PerfilDAO.ativar(p);
                    response.sendRedirect("perfis.jsp");

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'perfis.jsp';");
                    out.print("</script>");
                    out.close();

                }

                break;

            case 4:

                try {
                    // DESATIVAR PERFIL
                    int id = Integer.parseInt(request.getParameter("id"));
                    p.setId(id);
                    PerfilDAO.desativar(p);
                    response.sendRedirect("perfis.jsp");

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'perfis.jsp';");
                    out.print("</script>");
                    out.close();

                }

                break;
            default: 
                String mensagem = "Ação não encontrada!";
                PrintWriter out = response.getWriter();
                out.print("<script>");
                out.print("alert('" + mensagem + "');");
                out.print("location.href = 'usuarios.jsp';");
                out.print("</script>");
                out.close();

        }

    }

}
