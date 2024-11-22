package controller;

import dao.ProdutoDAO;
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
import model.Produto;

public class GerenciarProduto extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarProduto(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarProduto(request, response);
    }

    public static void gerenciarProduto(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        request.setCharacterEncoding("UTF-8");

        int valor = Integer.parseInt(request.getParameter("valor"));

        Produto produto = new Produto();

        switch (valor) {
            case 1: // Create
                try {
                    String nome = request.getParameter("nome");
                    String descricao = request.getParameter("descricao");
                    int quantidade = Integer.parseInt(request.getParameter("quantidade"));
                    int quantidadeCritica = Integer.parseInt(request.getParameter("quantidade_critica"));
                    double preco = Double.parseDouble(request.getParameter("preco"));
                    double custo = Double.parseDouble(request.getParameter("custo"));
                    String imagem = request.getParameter("imagem");
                    String fornecedor = request.getParameter("fornecedor");

                    produto.setNome(nome);
                    produto.setDescricao(descricao);
                    produto.setQuantidade(quantidade);
                    produto.setQuantidadeCritica(quantidadeCritica);
                    produto.setPreco(preco);
                    produto.setCusto(custo);
                    produto.setImagem(imagem);
                    produto.setFornecedor(fornecedor);

                    ProdutoDAO.cadastrar(produto);

                    String mensagem = "Produto cadastrado com sucesso!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'produtos.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {
                    String mensagem = "Erro ao cadastrar produto!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'cadastrar_produto.jsp';");
                    out.print("</script>");
                    out.close();
                }
                break;

            case 2: // Updade
                try {
                    int codigo = Integer.parseInt(request.getParameter("codigo"));
                    String nome = request.getParameter("nome");
                    String descricao = request.getParameter("descricao");
                    int quantidade = Integer.parseInt(request.getParameter("quantidade"));
                    int quantidadeCritica = Integer.parseInt(request.getParameter("quantidadeCritica"));
                    double preco = Double.parseDouble(request.getParameter("preco"));
                    double custo = Double.parseDouble(request.getParameter("custo"));
                    String imagem = request.getParameter("imagem");
                    String fornecedor = request.getParameter("fornecedor");
                    boolean status =  Boolean.parseBoolean(request.getParameter("status"));

                    produto.setCodigo(codigo);
                    produto.setNome(nome);
                    produto.setDescricao(descricao);
                    produto.setQuantidade(quantidade);
                    produto.setQuantidadeCritica(quantidadeCritica);
                    produto.setPreco(preco);
                    produto.setCusto(custo);
                    produto.setImagem(imagem);
                    produto.setFornecedor(fornecedor);
                    produto.setStatus(status);

                    ProdutoDAO.alterar(produto);

                    String mensagem = "Produto atualizado com sucesso!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'produtos.jsp';");
                    out.print("</script>");
                    out.close();

                } catch (Exception e) {
                    String mensagem = "Erro ao atualizar produto!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'produtos.jsp';");
                    out.print("</script>");
                    out.close();
                }
                break;

            case 3: // Ativar produto
                try {
                    int codigo = Integer.parseInt(request.getParameter("codigo"));
                    produto.setCodigo(codigo);

                    ProdutoDAO.ativar(produto);
                    response.sendRedirect("produtos.jsp");

                } catch (Exception e) {
                    String mensagem = "Erro ao ativar produto!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'produtos.jsp';");
                    out.print("</script>");
                    out.close();
                }
                break;

            case 4: // Desativar Produto
                try {
                    int codigo = Integer.parseInt(request.getParameter("codigo"));
                    produto.setCodigo(codigo);

                    ProdutoDAO.desativar(produto);
                    response.sendRedirect("produtos.jsp");

                } catch (Exception e) {
                    String mensagem = "Erro ao desativar produto!";
                    PrintWriter out = response.getWriter();
                    out.print("<script>");
                    out.print("alert('" + mensagem + "');");
                    out.print("location.href = 'produtos.jsp';");
                    out.print("</script>");
                    out.close();
                }
                break;

            default:
                String mensagem = "Ação inválida!";
                PrintWriter out = response.getWriter();
                out.print("<script>");
                out.print("alert('" + mensagem + "');");
                out.print("location.href = 'produtos.jsp';");
                out.print("</script>");
                out.close();
                break;
        }
    }
}
