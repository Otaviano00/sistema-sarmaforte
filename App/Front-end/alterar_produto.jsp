<%@page import="model.Produto"%>
<%@page import="dao.ProdutoDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alterar Produto</title>
    </head>
    <body>
        <h1>Alterar Produto</h1>

        <form action="GerenciarProduto" method="post">
            <% 
                Produto produto = ProdutoDAO.listarPorId(Integer.parseInt(request.getParameter("codigo"))); 
            %>

            <input type="hidden" name="valor" value="2"> <!-- Valor 2 para indicar alteração -->
            <input type="hidden" name="codigo" value="<%= produto.getCodigo() %>">
            <input type="hidden" name="status" value="<%= produto.isStatus() %>">

            <p>
                Nome: 
                <input type="text" name="nome" maxlength="100" value="<%= produto.getNome() %>" required>
            </p>
            <p>
                Descrição:
                <textarea name="descricao" rows="4" cols="50" required><%= produto.getDescricao() %></textarea>
            </p>
            <p>
                Quantidade:
                <input type="number" name="quantidade" min="0" value="<%= produto.getQuantidade() %>" required>
            </p>
            <p>
                Quantidade Crítica:
                <input type="number" name="quantidadeCritica" min="0" value="<%= produto.getQuantidadeCritica() %>" required>
            </p>
            <p>
                Preço (R$):
                <input type="number" name="preco" step="0.01" min="0" value="<%= produto.getPreco() %>" required>
            </p>
            <p>
                Custo (R$):
                <input type="number" name="custo" step="0.01" min="0" value="<%= produto.getCusto() %>" required>
            </p>
            <p>
                Fornecedor:
                <input type="text" name="fornecedor" maxlength="100" value="<%= produto.getFornecedor() %>" required>
            </p>
            <p>
                <input type="submit" value="Alterar Produto">
            </p>
        </form>
    </body>
</html>
