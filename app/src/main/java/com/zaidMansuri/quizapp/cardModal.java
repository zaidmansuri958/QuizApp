package com.zaidMansuri.quizapp;

import androidx.cardview.widget.CardView;

public class cardModal {
    String image,name;
    CardView card;


    public cardModal(String image, String name, CardView card) {
        this.image = image;
        this.name = name;
        this.card = card;
    }

    public cardModal() {
    }

    public CardView getCard() {
        return card;
    }

    public void setCard(CardView card) {
        this.card = card;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
