/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import dao.DAO;
import java.io.Serializable;

/**
 *
 * @author huypd
 */
public class Product implements Serializable{
    private  int id;
    private String name;
    private String category;
    private double price;
    private String image;

    public Product() {
    }

    public Product(int id, int quantity) {
        this.id = id;
        DAO dao = new DAO();
        Product p = dao.getProductById(id);
        this.name = p.getName();
        this.category = p.getCategory();
        this.price = p.price*quantity;
        this.image = p.image;
    }
    
    
    public Product(int id, String name, String category, double price, String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", category=" + category + ", price=" + price + ", image=" + image;
    }
    
    
}
