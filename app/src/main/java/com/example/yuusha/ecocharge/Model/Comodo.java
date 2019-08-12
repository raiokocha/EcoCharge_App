package com.example.yuusha.ecocharge.Model;

public class Comodo {

    public int id;
    public String nome;
    public String data_criacao;
    public String status;
    
    public Comodo() {


    }

    public Comodo(int id, String nome,String data_criacao, String status) {
        this.id = id;
        this.nome = nome;
        this.data_criacao = data_criacao;
        this.status = status;
    }

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

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getStatus() {
        return status;
    }

    public void  setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
