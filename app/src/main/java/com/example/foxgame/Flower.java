package com.example.foxgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Flower {

    Random random = new Random();
    Bitmap flower;
    int[] flowers = {R.drawable.flower1,
            R.drawable.flower2, R.drawable.flower3,
            R.drawable.flower4, R.drawable.flower5,
            R.drawable.flower6, R.drawable.flower7,
            R.drawable.flower8};
    int flowerX;
    int flowerY;
    int flowerVelocity;

    public Flower(Context context){
        resetIcon(context);
        resetPosition();
    }


    public int getFlowerHeight() {
        return flower.getHeight();
    }

    public int getFlowerWidth() {
        return flower.getWidth();
    }

    public Bitmap getFlower() {
        return flower;
    }

    public void resetIcon(Context context) {
        flower = BitmapFactory.decodeResource
                (context.getResources(), flowers[random.nextInt(flowers.length)]);
    }

    public void resetPosition() {
        flowerX = random.nextInt(GameView.dWidth - getFlowerWidth());
        flowerY = -200 + random.nextInt(600) * -1;
        flowerVelocity = 25 + random.nextInt(16);
    }
}
