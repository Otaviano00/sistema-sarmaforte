<style>
    /*Menu de gerencia*/

    #menu {
        display: none;
        flex-direction: row;
        justify-content: left;
        background-color: #fff;
        position: absolute;
        min-width: fit-content;
        width: calc(10% + 20px);
        z-index: 999;
    }

    .botao_menu {
        width: fit-content; 
        min-width: 100%;
        display: flex;
        justify-content: left;
        align-items: center;
        text-decoration: none;
        padding: 10px;
        padding-left: 10%;
        box-shadow:inset 0px 0px 1px 1px #979797c2;
        background-color: #D8D8D8;
    }

    .botao_menu a {
        display: flex;
        justify-content: center;
        align-items: center;
        width: fit-content;
        padding-left: 0 2%;
        gap: 10px;


        text-decoration: none;
        font-size: 1em;
        font-weight: bold;
        font-family: 'Karla', 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
        color: #142E50;
    }

    .botao_menu img {
        width: 27px;
        height: 27px;
        padding-right: 5px;
    }

    .botao_menu:hover {
        padding-bottom: 6px;
        height: 27px;
        border-bottom: 4px solid #FFB729;
        background-color: #e4e6e9;
    }

    #gerencia:hover #menu {
        display: block;
    }

    /*Botões de navegação*/

    .botao {
        min-width: fit-content;
        width: 10%;
        max-width: 10%;
        display: flex;
        justify-content: center;
        align-items: center;
        text-decoration: none;
        padding: 6px 12px;
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
        width: 30px;
        height: 30px;
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
        border-bottom: 4px solid #FFB729;
        background-color: #e4e6e9;
    }

</style>

<script>
    function ativar_menu() {
        const menu = document.querySelector("#menu");
        menu.classList.toggle("ativo");
    }
</script>

<div class="botao" id="home">
    <a href="home.jsp"><img src="images/icone_home.svg" alt="home"></a>
</div>
<div id="gerencia" style="min-width: fit-content; width: 10%; padding-right: 20px;">
    <div class="botao" onmouseover="ativar_menu()" style=" min-width: 100%;">
        <a href="#">
            <img src="images/icone_gerencia.svg" alt="">
            <span>GERENCIA</span>
            <img src="images/icone_expandir.svg" alt="" style="width: 10%; height: 10%; margin-left: 5px;">
        </a>
    </div>
    <div id="menu">
        <div class="botao_menu">
            <a href="usuarios.jsp"><img src="images/icone_usuario.svg" alt=""><span>USUÁRIOS</span></a>
        </div>
        <div class="botao_menu">
            <a href="perfis.jsp"><img src="images/icone_perfil.svg" alt=""><span>PERFIS</span></a>
        </div>
        <div class="botao_menu">
            <a href="#"><img src="images/icone_menu.svg" alt=""><span>MENUS</span></a>
        </div>
        <div class="botao_menu">
            <a href="#"><img src="images/icone_relatorio.svg" alt=""><span>RELATÓRIOS</span></a>
        </div>
    </div>
</div>
<div class="botao">
    <a href="orcamentos.jsp"><img src="images/icone_orcamento.svg" alt=""><span>ORÇAMENTOS</span></a>
</div>
<div class="botao">
    <a href="vendas.jsp"><img src="images/icone_vendas.svg" alt="" style="position: relative; top: -3px;"><span>VENDAS</span></a>
</div>
<div class="botao">
    <a href="produtos.jsp"><img src="images/icone_produto.svg" alt=""><span>PRODUTOS</span></a>
</div>
<div class="botao">
    <a href="clientes.jsp"><img src="images/icone_cliente.svg" alt=""><span>CLIENTES</span></a>
</div>
<div class="pesquisa">
    <img src="images/icone_pesquisa.svg" alt="lupa">
    <input type="text" name="pesquisa" placeholder="Pesquisar">
</div>