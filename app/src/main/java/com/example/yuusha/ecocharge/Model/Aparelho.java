package com.example.yuusha.ecocharge.Model;

public class Aparelho {

    public int id;
    public String nome;
    public int comodo_id;
    public int arduino_id;

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    public boolean isAtivo;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String color;

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

    public int getComodo_id() {
        return comodo_id;
    }

    public void setComodo_id(int comodo_id) {
        this.comodo_id = comodo_id;
    }

    public int getArduino_id() {
        return arduino_id;
    }

    public void setArduino_id(int arduino_id) {
        this.arduino_id = arduino_id;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
