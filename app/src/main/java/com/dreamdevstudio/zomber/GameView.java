package com.dreamdevstudio.zomber;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.dreamdevstudio.zomber.gameElements.Bullet;
import com.dreamdevstudio.zomber.gameElements.Player;
import com.dreamdevstudio.zomber.gameElements.Zombie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 19.08.15.
 */
public class GameView extends SurfaceView implements Runnable
{
    Context context;

    private final int width;
    private final int height;

    //задаю время паузы между выстрелпми пули
    private long SHOT_PAUSE = 250L;
    //задаю текущее время между выстрелпми пули
    private long currentPause = 0;
    //хэндлер для подсчета секунд
    private Handler customHandler = new Handler();
    Timer timer;
    MyTimerTask myTimerTask;
    private boolean isShooting = false;

    /**Объект класса GameLoopThread*/
    private GameThread mThread;

    public int shotX;
    public int shotY;

    /**Переменная запускающая поток рисования*/
    private boolean running = false;

    /*Создаем для прорисовки*/
    private List<Bullet> bullets = new ArrayList<Bullet>();
    private Player player;

    Bitmap players;

    private List<Zombie> zombie = new ArrayList<Zombie>();
    Bitmap zombies;

    private Thread zombieThred = new Thread(this);


    //-------------Start of GameThread--------------------------------------------------\\

    public class GameThread extends Thread
    {
        /**Объект класса*/
        private GameView view;

        /**Конструктор класса*/
        public GameThread(GameView view)
        {
            this.view = view;
        }

        /**Задание состояния потока*/
        public void setRunning(boolean run)
        {
            running = run;
        }

        /** Действия, выполняемые в потоке */
        @SuppressLint("WrongCall")
        public void run()
        {
            while (running)
            {
                Canvas canvas = null;
                try
                {
                    // подготовка Canvas-а
                    canvas = view.getHolder().lockCanvas();
                    synchronized (view.getHolder()) {
                        // собственно рисование
                        onDraw(canvas);
                        testCollision();
                        Log.d("Tag", "Count of zombies: " + zombie.size()
                                + "Count of bullets: " + bullets.size());
                    }
                }
                catch (Exception e) { }
                finally
                {
                    if (canvas != null)
                    {
                        view.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    //-------------End of GameThread--------------------------------------------------\\

    public GameView(Context context)
    {
        super(context);
        this.context=context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated

        mThread = new GameThread(this);
        zombieThred.start();

        /*Рисуем все наши объекты и все все все*/
        getHolder().addCallback(new SurfaceHolder.Callback() {
            /*** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                mThread.setRunning(false);
                while (retry) {
                    try {
                        // ожидание завершение потока
                        mThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder) {
                mThread.setRunning(true);
                mThread.start();

            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });

        players= BitmapFactory.decodeResource(getResources(), R.drawable.ninja);
        player= new Player(this, players, 100, 100);

        zombies = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
        zombie.add(new Zombie(this, zombies, width, height));
    }

    /**Функция рисующая все спрайты и фон*/
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        Iterator<Bullet> j = bullets.iterator();
        while(j.hasNext()) {
            Bullet b = j.next();
            if(b.y >= -120 && b.y <= height+120) {
                b.onDraw(canvas);
            } else {
                j.remove();
            }
        }
        canvas.drawBitmap(players, width / 2, height - 120, null);

        Iterator<Zombie> i = zombie.iterator();
        while(i.hasNext()) {
            Zombie e = i.next();
            if(e.y >= -120 & e.y <= height+120) {
                e.onDraw(canvas);
            } else {
                i.remove();
            }
        }
    }
    public Bullet createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Bullet(this, bmp, width, height);
    }
    public boolean onTouchEvent(MotionEvent e)
    {
        shotX = (int) e.getX();
        shotY = (int) e.getY();

        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            isShooting = true;
            startShooting();
        }
        if(e.getAction()==MotionEvent.ACTION_MOVE){
            //startShooting();
        }
        if(e.getAction()==MotionEvent.ACTION_UP){
            stopShooting();
            isShooting = false;
        }
        return true;
    }

    private void addBullet(List<Bullet> list){
        Log.d("myLogs", "addbullet");
        list.add(createSprite(R.drawable.bullet));
    }

//метод для запуска таймера выстрелов между пулями
    private void startShooting(){
        if(timer != null&&(isShooting)){
            timer.cancel();
        }
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(myTimerTask, 0L, 250L);

    }
    //метод для запуска таймера выстрелов между пулями
    private void stopShooting(){
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
        Log.d("myLogs", "stopShooting");

    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            addBullet(bullets);
        }

    }
    public void run() {
        while(true) {
            Random rnd = new Random();
            try {
                Thread.sleep(rnd.nextInt(2000));
                zombie.add(new Zombie(this, zombies, width, height));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /*Проверка на столкновения*/
    private void testCollision() {
        Iterator<Bullet> b = bullets.iterator();
        while(b.hasNext()) {
            Bullet balls = b.next();
            Iterator<Zombie> i = zombie.iterator();
            while(i.hasNext()) {
                Zombie enemies = i.next();

                if ((Math.abs(balls.x - enemies.x) <= (balls.width + enemies.width) / 2f)
                        && (Math.abs(balls.y - enemies.y) <= (balls.height + enemies.height) / 2f)) {
                    i.remove();
                    b.remove();
                }
            }
        }
    }
}
