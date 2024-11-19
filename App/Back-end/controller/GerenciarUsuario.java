package controller;

import dao.PerfilDAO;
import dao.UsuarioDAO;
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
import model.Usuario;

public class GerenciarUsuario extends HttpServlet {

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

        Usuario usu = new Usuario();
        Perfil per = new Perfil();

        switch (acao) {

            case 1:
                try {

                    String nome = request.getParameter("nome");
                    String telefone = request.getParameter("telefone");
                    String login = request.getParameter("login");
                    String senha = request.getParameter("senha");
                    String cpf = request.getParameter("cpf");
                    String email = request.getParameter("email");
                    int idPer = Integer.parseInt(request.getParameter("perfil"));

                    Perfil p = PerfilDAO.listarPorId(idPer);

                    usu.setNome(nome);
                    usu.setTelefone(telefone);
                    usu.setLogin(login);
                    usu.setSenha(senha);
                    usu.setCpf(cpf);
                    usu.setEmail(email);
                    usu.setPerfil(p);
                    usu.setStatus(true);

                    // Aqui eu valido todos os campos que não podem ser repetidos.
                    for (Usuario u : UsuarioDAO.listar()) {

                        if (usu.getTelefone().equals(u.getTelefone())) {

                            String mensagem = "Telefone já existente!";
                            PrintWriter out = response.getWriter();
                            out.print("<script>");
                            out.print("alert('" + mensagem + "');");
                            out.print("location.href = 'cadastrar_usuario.jsp';");
                            out.print("</script>");
                            out.close();
                            break;

                        }

                        if (usu.getLogin().equals(u.getLogin())) {

                            String mensagem = "Login já existente!";
                            PrintWriter out = response.getWriter();
                            out.print("<script>");
                            out.print("alert('" + mensagem + "');");
                            out.print("location.href = 'cadastrar_usuario.jsp';");
                            out.print("</script>");
                            out.close();
                            break;

                        }

                        if (usu.getCpf().equals(u.getCpf())) {

                            String mensagem = "CPF já existente!";
                            PrintWriter out = response.getWriter();
                            out.print("<script>");
                            out.print("alert('" + mensagem + "');");
                            out.print("location.href = 'cadastrar_usuario.jsp';");
                            out.print("</script>");
                            out.close();
                            break;

                        }

                        if (usu.getEmail().equals(u.getEmail())) {

                            String mensagem = "CPF já existente!";
                            PrintWriter out = response.getWriter();
                            out.print("<script>");
                            out.print("alert('" + mensagem + "');");
                            out.print("location.href = 'cadastrar_usuario.jsp';");
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
                    UsuarioDAO.cadastrar(usu);
                    out.print("alert('" + mensagem2 + "');");
                    out.print("location.href = 'usuarios.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'usuarios.jsp';");
                    out.print("</script>");
                    out.close();

                }

                break;
            case 2:
                try {

                    int id = Integer.parseInt(request.getParameter("id"));
                    String nome = request.getParameter("nome");
                    String telefone = request.getParameter("telefone");
                    String login = request.getParameter("login");
                    String senha = request.getParameter("senha");
                    String cpf = request.getParameter("cpf");
                    String email = request.getParameter("email");
                    int idPer = Integer.parseInt(request.getParameter("perfil"));
                    boolean status = Boolean.parseBoolean(request.getParameter("status"));

                    // Primeiro eu pego os dados com base no id acima, usaremos isso para a validação
                    Usuario u = UsuarioDAO.listarPorId(id);

                    Perfil p = PerfilDAO.listarPorId(idPer);

                    usu.setId(id);
                    usu.setNome(nome);
                    usu.setTelefone(telefone);
                    usu.setLogin(login);
                    usu.setSenha(senha);
                    usu.setCpf(cpf);
                    usu.setEmail(email);
                    usu.setPerfil(p);
                    usu.setStatus(status);

                    // Agora comparo os dados do objeto extraidos do banco com os dados recebidos da outra página
                    // Inicio o laco de repetição e começa a validação
                    for (Usuario us : UsuarioDAO.listar()) {

                        // Aqui eu só comparo os valores com os dados de outros campos
                        if (us.getId() != u.getId()) {

                            // aqui eu começo minha validação para ver se não se reptete valores que nao podem ser repetidos na tabela
                            if (usu.getTelefone().equals(us.getTelefone())) {

                                String mensagem = "Telefone já existente!";
                                PrintWriter out = response.getWriter();
                                out.print("<script>");
                                out.print("alert('" + mensagem + "');");
                                out.print("location.href = 'alterar_usuario.jsp?id=" + u.getId() + "';");
                                out.print("</script>");
                                out.close();
                                break;

                            }

                            if (usu.getLogin().equals(us.getLogin())) {

                                String mensagem = "Login já existente!";
                                PrintWriter out = response.getWriter();
                                out.print("<script>");
                                out.print("alert('" + mensagem + "');");
                                out.print("location.href = 'alterar_usuario.jsp?id=" + u.getId() + "';");
                                out.print("</script>");
                                out.close();
                                break;

                            }

                            if (usu.getCpf().equals(us.getCpf())) {

                                String mensagem = "CPF já existente!";
                                PrintWriter out = response.getWriter();
                                out.print("<script>");
                                out.print("alert('" + mensagem + "');");
                                out.print("location.href = 'alterar_usuario.jsp?id=" + u.getId() + "';");
                                out.print("</script>");
                                out.close();
                                break;

                            }

                            if (usu.getEmail().equals(us.getEmail())) {

                                String mensagem = "Email já existente!";
                                PrintWriter out = response.getWriter();
                                out.print("<script>");
                                out.print("alert('" + mensagem + "');");
                                out.print("location.href = 'alterar_usuario.jsp?id=" + u.getId() + "';");
                                out.print("</script>");
                                out.close();
                                break;

                            }

                        }

                    }

                    String mensagem = "Deseja alterar esses dados?";
                    String mensagem2 = "Alteração bem sussedida!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    UsuarioDAO.alterar(usu);
                    out.print("alert('" + mensagem2 + "');");
                    out.print("location.href = 'usuarios.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'usuarios.jsp';");
                    out.print("</script>");
                    out.close();

                }
                break;
            case 3:
                try {

                    int in = Integer.parseInt(request.getParameter("id"));
                    usu.setId(in);
                    UsuarioDAO.ativar(usu);
                    response.sendRedirect("usuarios.jsp");

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'usuarios.jsp';");
                    out.print("</script>");
                    out.close();

                }
                break;
            case 4:
                try {

                    int id = Integer.parseInt(request.getParameter("id"));
                    usu.setId(id);
                    UsuarioDAO.desativar(usu);
                    response.sendRedirect("usuarios.jsp");

                } catch (Exception e) {

                    String mensagem = "Ocorreu um problema com o banco de dados!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'usuarios.jsp';");
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
