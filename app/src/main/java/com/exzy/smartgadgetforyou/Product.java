package com.exzy.smartgadgetforyou;

/**
 * Created by sas-maxnot19 on 6/27/2017 AD.
 */

public class Product {
    private String product_name;
    private String product_price;
    private int product_img;
    private int content;
    private String category;
    private boolean clicked;

    public Product(String name, String price, int product_img, int content, String category) {
        this.product_name = name;
        this.product_price = price;
        this.product_img = product_img;
        this.content = content;
        this.category = category;
    }

    public int getProduct_img() {
        return product_img;
    }

    public void setProduct_img(int product_img) {
        this.product_img = product_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClick(boolean click) {
        clicked = click;
    }

    public String getCategory() { return this.category; }

    public void setCategory(String category) { this.category = category; }
}
