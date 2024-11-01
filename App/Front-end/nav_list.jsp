<style>
    .botao {
        min-width: fit-content;
        width: 10%;
        display: flex;
        justify-content: center;
        align-items: center;
        text-decoration: none;
        padding: 6px;
    }

    .botao a {
        display: flex;
        justify-content: center;
        align-items: center;
        width: fit-content;
        padding-left: 0 2%;

        text-decoration: none;
        font-size: 1em;
        font-weight: bold;
        font-family: 'Karla', 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
        color: #142E50;
    }

    .botao img {
        width: 27px;
        height: 27px;
        padding-right: 5px;
    }

    div#home {
        width: 10px;
    }

    div#home a{
        padding: 0;
    }

    .botao:hover {
        padding-bottom: 2px;
        height: 27px;
        border-bottom: 4px solid #FFB729;
        background-color: #e4e6e9;
    }
</style>

<div class="botao" id="home">
    <a href="teste_kaua.html"><img src="images/icone_home.svg" alt="home"></a>
</div>
<div class="botao">
    <a href="#"><img src="images/icone_gerencia.svg" alt=""><span>GERENCIA</span></a>
</div>
<div class="botao">
    <a href="#"><img src="images/icone_orcamento.svg" alt=""><span>ORÇAMENTOS</span></a>
</div>
<div class="botao">
    <a href="#"><img src="images/icone_vendas.svg" alt="" style="position: relative; top: -3px;"><span>VENDAS</span></a>
</div>
<div class="botao">
    <a href="#"><img src="images/icone_produto.svg" alt=""><span>PRODUTOS</span></a>
</div>
<div class="botao">
    <a href="#"><img src="images/icone_cliente.svg" alt=""><span>CLIENTES</span></a>
</div>
<div class="pesquisa">
    <img src="images/icone_pesquisa.svg" alt="lupa">
    <input type="text" name="pesquisa" placeholder="Pesquisar">
</div>