<%@page import="model.Produto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastrar Produto</title>
    </head>
    <body>
        <h1>Cadastrar Produto</h1>
        <form action="GerenciarProduto" method="post">
            <input type="hidden" name="valor" value="1">
            <p>Nome: 
                <input type="text" name="nome" maxlength="50" 
                       title="Apenas caracteres alfabéticos!" required>
            </p>
            
            <p>Descrição:</p>
            <textarea name="descricao" rows="3" cols="30" required></textarea>

            <p>Quantidade: 
                <input type="number" name="quantidade" min="0" required>
            </p>

            <p>Quantidade Crítica: 
                <input type="number" name="quantidade_critica" min="0" required>
            </p>


            <p>Preço (R$): 
                <input type="number" name="preco" step="0.01" min="0" required>
            </p>


            <p>Custo (R$): 
                <input type="number" name="custo" step="0.01" min="0" required>
            </p>

            <p>Imagem (URL): 
                <input type="file" name="imagem">
            </p>

            <p>Fornecedor: 
                <input type="text" name="fornecedor" maxlength="50" required>
            </p>
            <p>
                <input type="submit" value="Cadastrar Produto">
            </p>
        </form>
    </body>
</html>

