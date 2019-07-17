package com.el.ariby.ui.main.menu.recommend;

public class Recommend_item {

        String image;
        String title;
        String kmText;
        String rating;
    String getImage() {
            return this.image;
        }
    String getTitle() {
            return this.title;
        }

    public String getKmText() {
        return kmText;
    }

    public String getRating() {
        return rating;
    }

    Recommend_item(String image, String title,String kmText, String rating) {
            this.image = image;
            this.title = title;
            this.kmText = kmText;
            this.rating = rating;
        }


}