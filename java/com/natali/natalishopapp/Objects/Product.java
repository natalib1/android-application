//Represent an item

package com.natali.natalishopapp.Objects;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable{

    private String productId;
    private String name;
    private String description;
    private String category;
    private double price;
    private double shippingPrice;   //from the seller
//    private String color;
    private String sellerId;
    private String code;   //the id in firebase
    private String imageUrl;

    private String reservationId;
    private String reservationDate;
    private String costumerId;

    public Product() {
    }

    public Product(String productId, String name, String description, String category, double price, double shippingPrice, String color, String sellerId, String code, String imageUrl, String reservationId, String costumerId, String reservationDate) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.shippingPrice = shippingPrice;  //from the seller
//        this.color = color;
        this.sellerId = sellerId;
        this.code = code;
        this.imageUrl = imageUrl;
        this.reservationId = reservationId;
        this.costumerId = costumerId;
        this.reservationDate = reservationDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(String costumerId) {
        this.costumerId = costumerId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }


    public Map<String, Object> returnAsDictionary() {
        Map<String, Object> dict = new HashMap<>();

        dict.put("productId", productId);
        dict.put("name", name);
        dict.put("description", description);
        dict.put("category", category);
        dict.put("price", price);
        dict.put("shippingPrice", shippingPrice);  //from the seller
//        dict.put("color", color);
        dict.put("sellerId", sellerId);
        dict.put("code", code);
        dict.put("imageUrl", imageUrl);
        dict.put("reservationId", reservationId);
        dict.put("costumerId", costumerId);
        dict.put("reservationDate", reservationDate);

        return dict;
    }

}
