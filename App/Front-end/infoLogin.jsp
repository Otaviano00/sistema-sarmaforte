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


<%    
    String nome = (String) session.getAttribute("nomeUsuario");
    String cargo = (String) session.getAttribute("nomePerfil");
    
    if (nome == null || cargo == null) {
        String mensagem = "Você não está logado no sistema.";
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = 'login.html';");
        out.print("</script>");
    }

%>

<div id="include">
    <a href="#" class="info_usuario">
        <div id="nome_cargo">
            <span id="nome"><%= nome%></span>
            <span id="cargo"><%= cargo%></span>
        </div>
        <img src="images/icone_info.svg" alt="icone do usuÃ¡rio">
    </a>
    <a href="deslogar.jsp" id="sair">
        <img src="images/icone_sair.svg" alt="sair">
    </a>
</div>
