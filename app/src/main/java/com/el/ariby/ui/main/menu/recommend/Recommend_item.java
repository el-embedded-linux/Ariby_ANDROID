package com.el.ariby.ui.main.menu.recommend;

public class Recommend_item {

    String image;
    String title;
    String rating;
    String kmText;
    String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getKmText() {
        return kmText;
    }

    public void setKmText(String kmText) {
        this.kmText = kmText;
    }

    public Recommend_item(String image, String title, String kmText, String number) {
        this.image = image;
        this.title = title;
        this.kmText = kmText;
        this.number = number;
    }


}