package com.example.hospital;

public class user {
    public String firstname;
    public String lastname;
     public String address;
     public String mobile;

    public user() {
    }

    public user(String firstname, String lastname, String mobile, String addess) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = addess;
        this.mobile = mobile;
    }
}

