package com.example.clothes.Model;

public class Product {
    private String id;
    private String category;
    private String color;
    private String description;
    private String image;
    private String material;
    private String name;
    private String origin;
    private int price;
    private boolean state;
    private int stock;
    private int discountP;

    public Product() {
    }

    public Product(String id, String category, String color, String description, String image, String material, String name, String origin, int price,int discountP, boolean state, int stock) {
        this.id = id;
        this.category = category;
        this.color = color;
        this.description = description;
        this.image = image;
        this.material = material;
        this.name = name;
        this.origin = origin;
        this.price = price;
        this.state = state;
        this.stock = stock;
        this.discountP = discountP;
    }
    public int getDiscountP() {
        return discountP;
    }

    public void setDiscountP(int discountP) {
        this.discountP = discountP;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", material='" + material + '\'' +
                ", name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                ", price=" + price +
                ", state=" + state +
                ", stock=" + stock +
                '}';
    }
}
