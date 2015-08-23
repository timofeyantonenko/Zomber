package com.dreamdevstudio.zomber.gameElements;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.dreamdevstudio.zomber.GameView;

/**
 * Created by root on 19.08.15.
 */
public class Player
{
    /**Объект главного класса*/
    GameView gameView;

    //спрайт
    Bitmap bmp;

    //х и у координаты рисунка
    int x;
    int y;

    //конструктор
    public Player(GameView gameView, Bitmap bmp, int xCoord, int yCoord)
    {
        this.gameView = gameView;
        this.bmp = bmp;                    //возвращаем рисунок

        this.x = 300;   //делаем по центру
        this.y = 300; //отступ по y нет
    }

    //рисуем наш спрайт
    public void onDraw(Canvas c)
    {
        c.drawBitmap(bmp, x, y, null);
    }
}