package com.dreamdevstudio.zomber.gameElements;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.dreamdevstudio.zomber.GameView;

/**
 * Created by root on 19.08.15.
 */
public class Bullet
{
    /**Картинка*/
    private Bitmap bmp;

    /**Позиция*/
    public int x;
    public int y;

    /**Скорость по Y=15*/
    private int mSpeed=25;

    public double angle;

    /**Ширина*/
    public int width;

    /**Ввыоста*/
    public  int height;

    public GameView gameView;

    /**Конструктор*/
    public Bullet(GameView gameView, Bitmap bmp, int screenWidth, int screenHeight) {
        this.gameView=gameView;
        this.bmp=bmp;

        this.x = screenWidth/2;            //позиция по Х
        this.y = screenHeight;          //позиция по У
        this.width = 20;       //ширина снаряда
        this.height = 20;      //высота снаряда

        //угол полета пули в зависипости от координаты косания к экрану

        angle = Math.atan((double)(y - gameView.shotY) / (x - gameView.shotX));
    }

    /**Перемещение объекта, его направление*/
    private void update() {
        if(angle<=0) {
            x += mSpeed * Math.cos(angle);         //движение по Х со скоростью mSpeed и углу заданном координатой angle
            y += mSpeed * Math.sin(angle);         // движение по У -//-
        }
        else{
            x -= mSpeed * Math.cos(angle);
            y -= mSpeed * Math.sin(angle);
        }
    }

    /**Рисуем наши спрайты*/
    public void onDraw(Canvas canvas) {
        update();                              //говорим что эту функцию нам нужно вызывать для работы класса
        canvas.drawBitmap(bmp, x, y, null);
    }
}