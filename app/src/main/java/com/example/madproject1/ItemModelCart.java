package com.example.madproject1;

public class ItemModelCart {
    String name,price,url,count;

    public ItemModelCart(String count,String name, String price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.count = count;
    }

    public ItemModelCart(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
