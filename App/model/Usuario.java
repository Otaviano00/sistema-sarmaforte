package model;

public class Usuario {

    private int id;
    private String nome;
    private String telefone;
    private String login;
    private String senha;
    private String cpf;
    private String email;
    private Perfil perfil;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public Usuario(int id, String login, String senha, String cpf, String email, Perfil perfil, boolean status) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.cpf = cpf;
        this.email = email;
        this.perfil = perfil;
        this.status = status;
    }

    public Usuario() {
    }

}
