package com.model.classes;

public class Perfil {

    private int id;
    private String nome;
    private String descricao;
    private int hierarquia;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getHierarquia() {
        return hierarquia;
    }

    public void setHierarquia(int hierarquia) {
        this.hierarquia = hierarquia;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Perfil() {
    }

}
