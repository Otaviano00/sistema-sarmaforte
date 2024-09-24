package model;

import java.time.LocalDate;

public class Venda {

    private int id;
    private LocalDate data;
    private double valor;
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

    public Venda(int id, LocalDate data, double valor, Usuario usuario, Orcamento orcamento) {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.usuario = usuario;
        this.orcamento = orcamento;
    }

    public Venda() {
    }

}
