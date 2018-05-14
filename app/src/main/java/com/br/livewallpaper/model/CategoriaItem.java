package com.br.livewallpaper.model;

public class CategoriaItem {


    private String nome;
    private String imageLink;

    public CategoriaItem() {
    }

    public CategoriaItem(String nome, String imageLink) {
        this.nome = nome;
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imagemLink) {
        this.imageLink = imagemLink;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
