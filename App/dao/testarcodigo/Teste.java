package testarcodigo;

import dao.UsuarioDAO;
import java.sql.SQLException;
import model.Perfil;
import model.Usuario;

public class Teste {

    // AO TERMINAR DE TESTAR, DEIXE COMO COMENTÁRIO E FAÇA O PROXIMO TESTE

    public static void main(String[] args) {

        /*
        
        // INSERÇÃO DE USUARIOS NO BANCO
        
        try {

            Perfil per = new Perfil();
            per.setId(1);

            Usuario usu = new Usuario();

            usu.setNome("Jilson Mendes");
            usu.setTelefone("44028922");
            usu.setLogin("Jair");
            usu.setSenha("333666");
            usu.setCpf("88844422211");
            usu.setEmail("sucodelaranja@gmail.com");
            usu.setStatus(true);
            usu.setPerfil(per);

            UsuarioDAO usudao = new UsuarioDAO();

            boolean res = usudao.cadastrar(usu);

            System.out.println("Status da inserção: " + res);

        } catch (Exception e) {

            System.out.println("nada");
            e.printStackTrace();

        }*/
 /*
        
        // MOSTRAR USUARIOS DO BANCO
        
        UsuarioDAO usu = new UsuarioDAO();

        for (Usuario u : usu.listar()) {
            System.out.println(
                    "\n === Usuario ==="
                    + "\n Id: " + u.getId()
                    + "\n Nome: " + u.getNome()
                    + "\n Telefone: " + u.getTelefone()
                    + "\n Login: " + u.getLogin()
                    + "\n Senha: " + u.getSenha()
                    + "\n CPF: " + u.getCpf()
                    + "\n Email: " + u.getEmail()
                    + "\n Perfil_id: " + u.getPerfil().getId()
                    + "\n Perfil_nome: " + u.getPerfil().getNome()
                    + "\n Perfil_descricao: " + u.getPerfil().getDescricao()
                    + "\n Perfil_hierarquia: " + u.getPerfil().getHierarquia()
                    + "\n Perfil_status: " + u.getPerfil().getStatus()
            );

        }
         */
 /*

    // MOSTRAR USUARIOS POR ID

        UsuarioDAO usu = new UsuarioDAO();

        Usuario u = usu.listarPorId(0);

        System.out.println(
                "\n === Usuario ==="
                + "\n Id: " + u.getId()
                + "\n Nome: " + u.getNome()
                + "\n Telefone: " + u.getTelefone()
                + "\n Login: " + u.getLogin()
                + "\n Senha: " + u.getSenha()
                + "\n CPF: " + u.getCpf()
                + "\n Email: " + u.getEmail()
                + "\n status: " + u.getStatus()
                + "\n Perfil_id: " + u.getPerfil().getId()
                + "\n Perfil_nome: " + u.getPerfil().getNome()
                + "\n Perfil_descricao: " + u.getPerfil().getDescricao()
                + "\n Perfil_hierarquia: " + u.getPerfil().getHierarquia()
                + "\n Perfil_status: " + u.getPerfil().getStatus()
        );

         */
 /*
  
    // ALTERAR USUARIOS NO BANCO
 
  
 
        Perfil per = new Perfil();
        per.setId(1);

        Usuario usu = new Usuario();

        usu.setId(1);
        usu.setNome("Niko Bellic");
        usu.setTelefone("44028922");
        usu.setLogin("Niko");
        usu.setSenha("123456");
        usu.setCpf("33355577799");
        usu.setEmail("bellicniko@gmail.com");
        usu.setStatus(true);
        usu.setPerfil(per);

        UsuarioDAO u = new UsuarioDAO();

        boolean tes = u.alterar(usu);

        System.out.println("Status da alteração: " + tes);
         */
 /*
 
        // ATIVAR E DESATIVAR USUARIOS

        Usuario usu = new Usuario();
        usu.setId(1);

        UsuarioDAO u = new UsuarioDAO();

        boolean tes = u.destivar(usu);
        // boolean tes = u.ativar(usu);

        System.out.println("Status da aticação de desativação: " + tes);
         */
 /*
 
// EFETUAR LOGIN DE USUARIOS
        
        //     String Login = "Niko";
        //     String Senha = "123456";
       
         UsuarioDAO u = new UsuarioDAO();
         
       // String Login = "maia";
       // String Senha = "123456789";

       

        boolean tes = u.efetuarLogin(Login, Senha);

        System.out.println("Status da efetuação do Login: " + tes);
 
         */
    }

}
