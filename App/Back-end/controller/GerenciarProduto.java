package controller;

import dao.ProdutoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

    private static void exibirMensagem(PrintWriter out, String mensagem, String url) throws IOException {
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = '" + url + "';");
        out.print("</script>");
        out.close();
    }

    public static void gerenciarProduto(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        HttpSession sessao = request.getSession();
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Integer hierarquia = (Integer) sessao.getAttribute("hierarquia");
        Produto produto = new Produto();

        // Verificar se a sessão expirou
        if (hierarquia == null) {
            exibirMensagem(out, "Sessão expirada! Faça login novamente.", "login.jsp");
            return;
        }

        int acao;
        try {
            acao = Integer.parseInt(request.getParameter("acao"));
        } catch (NumberFormatException e) {
            exibirMensagem(out, "Ação inválida!", "produtos.jsp");
            return;
        }

        // Verificação de hierarquia
        if (hierarquia == 2) {
            acao = 0;
        }

        switch (acao) {
            case 0: // Sem autorização
                exibirMensagem(out, "Você não tem autorização para realizar essa ação!", "home.jsp");
                break;

            case 1: // Create - Cadastro de produto
                try {
                    produto.setNome(request.getParameter("nome"));
                    produto.setDescricao(request.getParameter("descricao"));
                    produto.setQuantidade(Integer.parseInt(request.getParameter("quantidade")));
                    produto.setQuantidadeCritica(Integer.parseInt(request.getParameter("quantidade_critica")));
                    produto.setPreco(Double.parseDouble(request.getParameter("preco")));
                    produto.setCusto(Double.parseDouble(request.getParameter("custo")));
                    produto.setImagem(request.getParameter("imagem"));
                    produto.setFornecedor(request.getParameter("fornecedor"));

                    // Cadastro do produto no banco
                    if (ProdutoDAO.cadastrar(produto)) {
                        exibirMensagem(out, "Produto cadastrado com sucesso!", "produtos.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao cadastrar produto!", "produtos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "produtos.jsp");
                }
                break;

            case 2: // Update - Atualização de produto
                try {
                    produto.setCodigo(Integer.parseInt(request.getParameter("codigo")));
                    produto.setNome(request.getParameter("nome"));
                    produto.setDescricao(request.getParameter("descricao"));
                    produto.setQuantidade(Integer.parseInt(request.getParameter("quantidade")));
                    produto.setQuantidadeCritica(Integer.parseInt(request.getParameter("quantidadeCritica")));
                    produto.setPreco(Double.parseDouble(request.getParameter("preco")));
                    produto.setCusto(Double.parseDouble(request.getParameter("custo")));
                    produto.setImagem(request.getParameter("imagem"));
                    produto.setFornecedor(request.getParameter("fornecedor"));
                    produto.setStatus(Boolean.parseBoolean(request.getParameter("status")));
                    
                    // Atualização do produto no banco
                    if (ProdutoDAO.alterar(produto)) {
                        exibirMensagem(out, "Produto atualizado com sucesso!", "produtos.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao atualizar produto!", "produtos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "produtos.jsp");
                }
                break;

            case 3: // Ativar produto
                try {
                    int codigo = Integer.parseInt(request.getParameter("codigo"));
                    produto.setCodigo(codigo);

                    // Ativar o produto no banco
                    if (ProdutoDAO.ativar(produto)) {
                        response.sendRedirect("produtos.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao ativar produto!", "produtos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "produtos.jsp");
                }
                break;

            case 4: // Desativar produto
                try {
                    int codigo = Integer.parseInt(request.getParameter("codigo"));
                    produto.setCodigo(codigo);

                    // Desativar o produto no banco
                    if (ProdutoDAO.desativar(produto)) {
                        response.sendRedirect("produtos.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao desativar produto!", "produtos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "produtos.jsp");
                }
                break;

            case 5: // Excluir produto
                try {
                    int codigo = Integer.parseInt(request.getParameter("codigo"));
                    produto.setCodigo(codigo);

                    // Excluir o produto no banco
                    if (ProdutoDAO.excluir(codigo)) {
                        exibirMensagem(out, "Produto excluído com sucesso!", "produtos.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao excluir produto!", "produtos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "produtos.jsp");
                }
                break;

            default:
                exibirMensagem(out, "Ação inválida!", "produtos.jsp");
                break;
        }
    }
}
