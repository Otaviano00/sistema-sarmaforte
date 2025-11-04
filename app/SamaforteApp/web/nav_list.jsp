﻿<%@page import="model.Menu"%>
<%@page import="dao.PerfilDAO"%>
<%@page import="java.util.List"%>

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
        min-width: calc(100% + 20px);
        width: calc(100% + 20px); 
        display: flex;
        justify-content: left;
        align-items: center;
        text-decoration: none;
        padding: 10px;
        padding-left: 10%;
        background-color: #D8D8D8;
        transition: background-color 0.3s, border-left 0.3s;
        cursor: pointer;
    }

    .botao_menu a {
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

    .botao_menu img {
        width: 27px;
        height: 27px;
        padding-right: 5px;
    }

    .botao_menu:hover {
        height: 27px;
        border-left: 4px solid #FFB729;
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
        transition: background-color 0.3s, border-bottom 0.3s;
        cursor: pointer;
    }

    .botao a {
        display: flex;
        justify-content: center;
        align-items: center;
        width: fit-content;
        height: fit-content;
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

<div class="botao" id="home" onclick="location.href = 'home.jsp'">
    <a href="home.jsp"><img src="images/icone_home.svg" alt="home"></a>
</div>

<%
    List<Menu> navMenu = PerfilDAO.listarMenus(idP);
    if (hierarquia < 2) {
%>
    <div id="gerencia" style="min-width: fit-content; width: 10%; padding-right: 20px;">
        <div class="botao" onmouseover="ativar_menu()" style=" min-width: 100%;">
            <a href="#">
                <img src="images/icone_gerencia.svg" alt="">
                <span>GERENCIA</span>
                <img src="images/icone_expandir.svg" alt="" style="width: 10%; height: 10%; margin-left: 5px;">
            </a>
        </div>
        <div id="menu">
            <%
                for (int i = 0; i < navMenu.size(); i++) {
                    Menu btnMenu = navMenu.get(i);
                    if ((btnMenu.getLink().equals("usuarios.jsp") || btnMenu.getLink().equals("perfis.jsp") || btnMenu.getLink().equals("menus.jsp") || btnMenu.getLink().equals("relatorios.jsp")) && btnMenu.isStatus()) {
            %>
                    <div class="botao_menu" onclick="location.href = '<%= btnMenu.getLink()%>'">
                        <a href="#"><img src="<%= btnMenu.getImagem()%>" alt="<%= btnMenu.getNome()%>"><span><%= btnMenu.getNome()%></span></a>
                    </div>
            <%
                    }

                }
            %>
        </div>
    </div>
<% } %>

<%
    for (int i = 0; i < navMenu.size(); i++) {
        Menu btnMenu = navMenu.get(i);
        if ((!btnMenu.getLink().equals("usuarios.jsp") && !btnMenu.getLink().equals("perfis.jsp") && !btnMenu.getLink().equals("menus.jsp") && !btnMenu.getLink().equals("relatorios.jsp")) && btnMenu.isStatus()) {
%>
        <div class="botao" onclick="location.href = '<%= btnMenu.getLink()%>'">
            <a href="#"><img src="<%= btnMenu.getImagem()%>" alt="<%= btnMenu.getNome()%>"><span><%= btnMenu.getNome()%></span></a>
        </div>
<%
        }

    }
%>

<!-- <div class="pesquisa" style="overflow: visible;">
    <img src="images/icone_pesquisa.svg" alt="lupa">
    <input type="text" name="pesquisa" placeholder="Pesquisar">
</div> -->