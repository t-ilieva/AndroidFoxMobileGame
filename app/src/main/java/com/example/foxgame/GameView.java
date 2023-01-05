package com.example.foxgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class GameView extends View {

    final long UPDATE_MILLIS = 30;
    int points = 0;
    int lives = 3;
    static int dWidth;
    static int dHeight;
    float foxX;
    float foxY;
    float oldFoxX;
    float oldX;
    ArrayList<Collision> collisions;
    ArrayList<Flower> flowers;
    Context context;
    Handler handler;
    Bitmap background;
    Bitmap ground;
    Bitmap foxCharacter;
    Bitmap showLives;
    Rect backgroundRect;
    Rect groundRect;
    Runnable runnable;
    Paint paintText = new Paint();
    float TEXT_SIZE = 120;


    public GameView(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        foxCharacter = BitmapFactory.decodeResource(getResources(), R.drawable.fox);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        showLives = BitmapFactory.decodeResource(getResources(), R.drawable.threehearts);
        backgroundRect = new Rect(0, 0, dWidth, dHeight - ground.getHeight());
        groundRect = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };


        paintText.setColor(Color.BLACK);
        paintText.setTextSize(TEXT_SIZE);
        paintText.setTextAlign(Paint.Align.LEFT);
        paintText.setTypeface(ResourcesCompat.getFont(context, R.font.springtime_font));

        flowers = new ArrayList<>();
        collisions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Flower flower = new Flower(context);
            flowers.add(flower);
        }

        foxX = dWidth / 2 - foxCharacter.getWidth() / 2;
        foxY = dHeight - ground.getHeight() - foxCharacter.getHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(ground, null, groundRect, null);
        canvas.drawBitmap(background, null, backgroundRect, null);
        canvas.drawBitmap(foxCharacter, foxX, foxY, null);

        for (int i = 0; i < flowers.size(); i++) {
            canvas.drawBitmap(flowers.get(i).getFlower(), flowers.get(i).flowerX, flowers.get(i).flowerY, null);
            flowers.get(i).flowerY += flowers.get(i).flowerVelocity;
            if (flowers.get(i).flowerY + flowers.get(i).getFlowerHeight() >= dHeight - ground.getHeight()) {
                lives--;
                Collision collision = new Collision(context);
                collision.collisionX = flowers.get(i).flowerX;
                collision.collisionY = flowers.get(i).flowerY;
                collisions.add(collision);
                flowers.get(i).resetIcon(context);
                flowers.get(i).resetPosition();
                if (lives == 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        for (int i = 0; i < flowers.size(); i++) {
            if (flowers.get(i).flowerX + flowers.get(i).getFlowerWidth() >= foxX
                    && flowers.get(i).flowerX <= foxX + foxCharacter.getWidth()
                    && flowers.get(i).flowerY + flowers.get(i).getFlowerWidth() >= foxY
                    && flowers.get(i).flowerY + flowers.get(i).getFlowerWidth() <= foxY + foxCharacter.getHeight()) {
                points += 10;
                flowers.get(i).resetIcon(context);
                flowers.get(i).resetPosition();
            }
        }

        for (int i = 0; i < collisions.size(); i++) {
            canvas.drawBitmap(collisions.get(i).getCollision(),
                    collisions.get(i).collisionX, collisions.get(i).collisionY, null);

            collisions.get(i).collisionFrame++;

            if(collisions.get(i).collisionFrame > 3){
                collisions.remove(i);
            }

        }
        if(lives == 2){
            showLives = BitmapFactory.decodeResource(getResources(), R.drawable.twohearts);
        }
        else if(lives == 1){
            showLives = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        }

        canvas.drawBitmap(showLives, dWidth/2 + 100,30, null);
        canvas.drawText("" + points, 20, TEXT_SIZE, paintText);
        handler.postDelayed(runnable, UPDATE_MILLIS);
            
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float onTouchX = event.getX();
        float onTouchY = event.getY();
        if(onTouchY >= foxY){
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldFoxX = foxX;
            }
            if (action == MotionEvent.ACTION_MOVE){
                float shift = oldX - onTouchX;
                float newFoxX = oldFoxX - shift;
                if (newFoxX <= 0){
                    foxX = 0;
                }
                else if(newFoxX >= dWidth - foxCharacter.getWidth()){
                    foxX = dWidth - foxCharacter.getWidth();
                }
                else{
                    foxX = newFoxX;
                }
            }
        }
        return true;
    }

}
