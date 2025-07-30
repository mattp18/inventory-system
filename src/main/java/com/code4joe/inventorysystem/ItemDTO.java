package com.code4joe.inventorysystem;

public class ItemDTO {

    private String name;

    private Double price;

    private String date;

    private Boolean sold;

    public ItemDTO(String name, Double price, String date, Boolean sold) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.sold = sold;
    }

    public ItemDTO(String name, Double price, Boolean sold) {
        this.name = name;
        this.price = price;
        this.sold = sold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
