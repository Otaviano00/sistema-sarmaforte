package model;

import java.time.LocalDate;

public class Venda {

    private int id;
    private LocalDate data;
    private double valor;
    private double desconto;
    private String formaPagamento;
    private Usuario usuario;
    private Orcamento orcamento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Venda(int id, LocalDate data, double valor, double desconto, String formaPagamento, Usuario usuario, Orcamento orcamento) {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.desconto = desconto;
        this.formaPagamento = formaPagamento;
        this.usuario = usuario;
        this.orcamento = orcamento;
    }

    public Venda() {
    }

}
