package com.vip.v_library.books;

public class BooksModel {
    private String img;
    private String id;
    private String name;
    private String publisher;
    private String edition;
    private String quantity;
    private String price;

    public BooksModel(String img, String id, String name, String publisher, String edition, String quantity, String price) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.edition = edition;
        this.quantity = quantity;
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
