//Represent a user

package com.natali.natalishopapp.Objects;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String fName;
    private String lName;
    private String email;   //the id in firebase
    private String password;
    private String address;
    private String mobile;
    private int userType ;  // 1 = personal - client , 2 = seller
    private double chest;
    private double hips;
    //seller
    private String storeName;
    private String accountNumber;
    private String bankNumber;
    private String bankBranchNumber;
    private double shippingCost;

    public  User (){
    }

    public User(String fName, String lName, String email, String password, String address, String mobile, int userType, double chest, double hips, String storeName, String accountNumber, String bankNumber, String bankBranchNumber, double shippingCost) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.userType = userType;
        this.chest = chest;
        this.hips = hips;
        this.storeName = storeName;
        this.accountNumber = accountNumber;
        this.bankNumber = bankNumber;
        this.bankBranchNumber = bankBranchNumber;
        this.shippingCost = shippingCost;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankBranchNumber() {
        return bankBranchNumber;
    }

    public void setBankBranchNumber(String bankBranchNumber) {
        this.bankBranchNumber = bankBranchNumber;
    }

    public double getChest() {
        return chest;
    }

    public void setChest(double chest) {
        this.chest = chest;
    }

    public double getHips() {
        return hips;
    }

    public void setHips(double hips) {
        this.hips = hips;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }


    public Map<String,Object> returnAsDictionary(){
        Map<String,Object> dict = new HashMap<>();

        dict.put("fName",fName);
        dict.put("lName",lName);
        dict.put("email",email);
        dict.put("password",password);
        dict.put("address",address);
        dict.put("mobile",mobile);
        dict.put("userType",userType);
        dict.put("chest",chest);
        dict.put("hips",hips);
        dict.put("storeName",storeName);
        dict.put("accountNumber",accountNumber);
        dict.put("bankNumber",bankNumber);
        dict.put("bankBranchNumber",bankBranchNumber);
        dict.put("shippingCost",shippingCost);

        return  dict;
    }

}
