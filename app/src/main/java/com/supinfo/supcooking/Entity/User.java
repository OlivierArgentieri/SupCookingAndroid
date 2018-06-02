package com.supinfo.supcooking.Entity;

import org.json.JSONObject;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by User on 30/05/2018.
 */

public class User implements Serializable {

    private String username;
    private String password;
    private String phoneNumber;
    private String firstname;
    private String lastname;
    private String postalAddress;
    private String email;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {

      return password;
    }

    public void setPassword(String password) {
        this.password = SHA256(password);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalCode) {
        this.postalAddress = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public User() {}


    public User(String email, String password, String username) {
        this.setEmail(email);
        this.setPassword(password);
        this.setUsername(username);
    }

    public User(String username, String password,  String phoneNumber, String firstname, String lastname, String postalCode,  String email){
        this.setUsername(username);
        this.setPassword(password);
        this.setPhoneNumber(phoneNumber);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setPostalAddress(postalCode);
        this.setEmail(email);
    }
    public User(JSONObject json){
        try{
            this.setUsername(json.getJSONObject("user").getString("username"));
            this.setUsername(json.getJSONObject("user").getString("password"));
            this.setPhoneNumber(json.getJSONObject("user").getString("phoneNumber"));
            this.setFirstname(json.getJSONObject("user").getString("lastName"));
            this.setLastname(json.getJSONObject("user").getString("firstName"));
            this.setEmail(json.getJSONObject("user").getString("email"));
            this.setPostalAddress(json.getJSONObject("user").getString("address"));
        }
        catch (Exception ignored){

        }

    }
    private static String SHA256(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
