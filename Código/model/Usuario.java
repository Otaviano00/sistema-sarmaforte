package model;

public class Usuario {

    private int id;
    private String nome;
    private String telefone;
    private String login;
    private String senha;
    private String cpf;
    private String emil;
    private Perfil perfil;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmil() {
        return emil;
    }

    public void setEmil(String emil) {
        this.emil = emil;
    }

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

    public Usuario(int id, String login, String senha, String cpf, String emil, Perfil perfil, boolean status) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.cpf = cpf;
        this.emil = emil;
        this.perfil = perfil;
        this.status = status;
    }

    public Usuario() {
    }

}
