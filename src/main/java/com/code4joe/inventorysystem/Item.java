package com.code4joe.inventorysystem;


public class Item {

    private int id;

    private String name;

    private String image;

    private Double price;

    private String date;

    private Boolean sold;

    private int quantity;

    private Category category;

    public Item(String name, Double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Item(int id, String name, String date, Boolean sold, Double price, int quantity, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.date = date;
        this.sold = sold;
        this.quantity = quantity;
        this.id = id;
    }

    public Item(String name, String image, Double price, String date) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.date = date;
    }

    public Item(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Item(String name, String date, Boolean sold, int id, Double price) {
        this.name = name;
        this.date = date;
        this.sold = sold;
        this.id = id;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public Category getCategory() {
        return category;
    }
}
