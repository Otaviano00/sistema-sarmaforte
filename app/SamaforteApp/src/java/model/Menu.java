package model;

public class Menu {

    private int id;
    private String nome;
    private String link;
    private String imagem;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Menu(int id, String nome, String link, String imagem, boolean status) {
        this.id = id;
        this.nome = nome;
        this.link = link;
        this.imagem = imagem;
        this.status = status;
    }

    public Menu() {
    }

}
