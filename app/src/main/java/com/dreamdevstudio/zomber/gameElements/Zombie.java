package com.dreamdevstudio.zomber.gameElements;

/**
 * Created by root on 22.08.15.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.dreamdevstudio.zomber.GameView;

import java.util.Random;

public class Zombie {
    /**Х и У коорданаты*/
    public int x;
    public int y;

    /**Скорость*/
    public int speed;

    /**Выосота и ширина спрайта*/
    public int width;
    public int height;

    public GameView gameView;
    public Bitmap bmp;

    /**Конструктор класса*/
    public Zombie(GameView gameView, Bitmap bmp, int screenWidth, int screenHeight){
        this.gameView = gameView;
        this.bmp = bmp;

        Random rnd = new Random();
        this.x = rnd.nextInt(screenWidth);
        this.y = 0;
        this.speed = rnd.nextInt(9)+1;

        this.width = 27;
        this.height = 40;
    }

    public void update(){
        y += speed;
    }

    public void onDraw(Canvas c){
        update();
        c.drawBitmap(bmp, x, y, null);
    }
}
