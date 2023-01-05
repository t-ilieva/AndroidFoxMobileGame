package com.example.foxgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Collision {

    Bitmap collision;
    int[] collisions = {R.drawable.petals1,
            R.drawable.petals2, R.drawable.petals3};
    Random random = new Random();

    int collisionX;
    int collisionY;
    int collisionFrame = 0;

    public Collision(Context context){
        setCollision(context);
    }

    public void setCollision(Context context){
        collision = BitmapFactory.decodeResource
                (context.getResources(),
                collisions[random.nextInt(collisions.length)]);
    }

    public Bitmap getCollision(){
        return collision;
    }
}

