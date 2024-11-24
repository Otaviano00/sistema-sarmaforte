<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    #include {
        display: flex;
        flex-direction: row;
        justify-content: right;
        align-items: center;
        position: absolute;
        right: 0;
    }

    .info_usuario {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        text-decoration: none;
        margin: 10px;
    }

    #nome_cargo {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: right;
        text-align: right;
        padding-right: 10px;
        font-family: 'Karla'; 
    }

    #nome {
        font-size: 1.5em;
        font-style: bold;
        color: #FFB729;
    }

    #cargo {
        position: relative;
        font-size: 1em;
        top: -5px;
        color: white;
    }

    #sair {
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 10px;
    }
</style>

<div id="include">
    <a href="detalhes_usuario.jsp?id=<%= idU%>" class="info_usuario">
        <div id="nome_cargo">
            <span id="nome"><%= nome%></span>
            <span id="cargo"><%= cargo%></span>
        </div>
        <img src="images/icone_info.svg" alt="icone do usuário">
    </a>
    <a href="deslogar.jsp" id="sair">
        <img src="images/icone_sair.svg" alt="sair">
    </a>
</div>
