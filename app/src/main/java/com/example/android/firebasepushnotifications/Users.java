package com.example.android.firebasepushnotifications;

public class Users {
    String name,image,uid;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Users(String name, String image, String uid) {
        this.name = name;
        this.image = image;
        this.uid = uid;
    }

    public Users() {
    }
}
