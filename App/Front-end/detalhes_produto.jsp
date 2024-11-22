<%@page import="model.Produto"%>
<%@page import="dao.ProdutoDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Detalhes do Produto</title>
    </head>
    <body>
        <h1>Detalhes do Produto</h1>

        <% 
            Produto produto = ProdutoDAO.listarPorId(Integer.parseInt(request.getParameter("codigo"))); 
        %>

        <table border="1" cellpadding="10">
            <tr>
                <th>Código</th>
                <td><%= produto.getCodigo() %></td>
            </tr>
            <tr>
                <th>Nome</th>
                <td><%= produto.getNome() %></td>
            </tr>
            <tr>
                <th>Descrição</th>
                <td><%= produto.getDescricao() %></td>
            </tr>
            <tr>
                <th>Quantidade</th>
                <td><%= produto.getQuantidade() %></td>
            </tr>
            <tr>
                <th>Quantidade Crítica</th>
                <td><%= produto.getQuantidadeCritica() %></td>
            </tr>
            <tr>
                <th>Preço</th>
                <td>R$ <%= String.format("%.2f", produto.getPreco()) %></td>
            </tr>
            <tr>
                <th>Custo</th>
                <td>R$ <%= String.format("%.2f", produto.getCusto()) %></td>
            </tr>
            <tr>
                <th>Fornecedor</th>
                <td><%= produto.getFornecedor() %></td>
            </tr>
            <tr>
                <th>Status</th>
                <td><%= produto.isStatus() ? "Ativo" : "Inativo" %></td>
            </tr>
        </table>

        <br>
        <a href="produtos.jsp">Voltar para a lista de produtos</a>
    </body>
</html>
