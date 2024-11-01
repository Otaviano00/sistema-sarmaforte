<style>
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
        font-size: 1.7em;
        font-style: bold;
        color: #FFB729;
    }

    #cargo {
        position: relative;
        font-size: 1.3em;
        top: -5px;
        color: white;
    }
</style>

<div id="include">
    <a href="#" class="info_usuario">
        <div id="nome_cargo">
            <span id="nome">Admininistrador</span>
            <span id="cargo">Admin</span>
        </div>
        <img src="images/icone_info.svg" alt="icone do usuário">
    </a>
    <a href="#" id="sair">
        <img src="../images/icone_sair.svg" alt="sair">
    </a>
</div>