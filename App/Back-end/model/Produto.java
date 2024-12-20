package model;

public class Produto {

    private int codigo;
    private String descricao;
    private String nome;
    private int quantidade;
    private int quantidadeCritica;
    private String imagem;
    private String fornecedor;
    private double preco;
    private double custo;
    private boolean status;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeCritica() {
        return quantidadeCritica;
    }

    public void setQuantidadeCritica(int quantidadeCritica) {
        this.quantidadeCritica = quantidadeCritica;
    }
    
    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Produto(int codigo, String descricao, String nome, int quantidade, int quantidadeCritica, String imagem, String fornecedor, double preco, double custo, boolean status) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.nome = nome;
        this.quantidade = quantidade;
        this.quantidadeCritica = quantidadeCritica;
        this.imagem = imagem;
        this.fornecedor = fornecedor;
        this.preco = preco;
        this.custo = custo;
        this.status = status;
    }
    
    public Produto() {
    }

}
