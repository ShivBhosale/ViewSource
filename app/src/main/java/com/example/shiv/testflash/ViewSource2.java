package com.example.shiv.testflash;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Rect;
import android.widget.Toast;

public class ViewSource2 extends Activity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        setContentView(gameView);

    }
    class GameView extends SurfaceView implements Runnable {

        // This is our thread
        Thread gameThread = null;

        SurfaceHolder ourHolder;

        volatile boolean playing;


        //---------------- ALL MY DRAWING STUFF-------------
        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;
        Paint yellowPaint = new Paint(); // for dashboard menu

        //-----------------END OF MY DRAWING STUFF-------------------

        // This variable tracks the game frame rate
        long fps;

        // This is used to help calculate the fps
        private long timeThisFrame;


        //---------------MY SPRTIES GO HERE------------
        // Declare an object of type Bitmap
        Bitmap bitmapBob,MenuColBut;

        //----------------END SPRITES--------------


        // ALL MY BOOLEANS GO HERE--------------------------------

        // Bob starts off not moving
        boolean isMoving = false;
        boolean inMenu = false; //not in the menu at the start of the game
        boolean MenuFull = false; //tells when menu is fully opened
        boolean Menukill = true;


        //---------------END OF BOOLEANS--------------------

        // He can walk at 150 pixels per second
        float walkSpeedPerSecond = 150;

        // He starts 10 pixels from the left
        float bobXPosition = 10;
        float bobYPosition = 197;
        int menu_height = 1764;

        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public GameView(Context context) {
            // The next line of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            //----------------ALL MY SPRITES INSTANTIATED--------------

            // Load Bob from his .png file
            bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.leftarr);
            MenuColBut = BitmapFactory.decodeResource(this.getResources(), R.drawable.tick);


            //----------END SPRITE STUFF-----------------------

            // Set our boolean to true - game on!
            playing = true;

        }

        @Override
        public void run() {
            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                // Update the frame
                update();

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }

            }

        }

        // Everything that needs to be updated goes in here
        // In later projects we will have dozens (arrays) of objects.
        // We will also do other things like collision detection.
        public void update() {

            // If bob is moving (the player is touching the screen)
            // then move him to the right based on his target speed and the current fps.
            if(isMoving && !Menukill){
                bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
            }
            if(inMenu && !Menukill){
                if(menu_height > 0 && !MenuFull){
                    menu_height-= 210;
                }
                else {
                    MenuFull = true;
                }

            }
            else if(Menukill && inMenu){
                MenuFull = false;
                if(menu_height <= 1764 && !MenuFull){
                    menu_height += 210;
                }
                else{
                    menu_height = 1764;
                    inMenu = false;
                    isMoving = true;

                }
            }

        }

        //------------------DRAWING FUNCTIONS-------------------------------------------------------

        public void back_grid(){
            int HEIGHT = getHeight();
            int WIDTH = getWidth();
            int box_width = (int)WIDTH/11;
            int box_height = box_width;
            float[] grid_pts = new float[] {0, 0, 0, HEIGHT,box_width,0,box_width,HEIGHT, 2*box_width,0,2*box_width,HEIGHT
                    ,3*box_width,0,3*box_width,HEIGHT,4*box_width,0,4*box_width,HEIGHT,5*box_width,0,5*box_width,HEIGHT,
                    6*box_width,0,6*box_width,HEIGHT,7*box_width,0,7*box_width,HEIGHT,8*box_width,0,8*box_width,HEIGHT
                    ,9*box_width,0,9*box_width,HEIGHT,10*box_width,0,10*box_width,HEIGHT,11*box_width,0,11*box_width,HEIGHT};

            float[] grid_pts2 = new float[] {0,0,WIDTH,0,0,box_height,WIDTH,box_height,0, 2*box_height,WIDTH,2*box_height,0,3*box_height,WIDTH,3*box_height,
                    0,4*box_height,WIDTH,4*box_height,0,5*box_height,WIDTH,5*box_height,0,6*box_height,WIDTH,6*box_height
                    ,0,7*box_height,WIDTH,7*box_height,0,8*box_height,WIDTH,8*box_height,0,9*box_height,WIDTH,9*box_height
                    ,0,10*box_height,WIDTH,10*box_height,0,11*box_height,WIDTH,11*box_height,0,12*box_height,WIDTH,12*box_height
                    ,0,13*box_height,WIDTH,13*box_height,0,14*box_height,WIDTH,14*box_height,0,15*box_height,WIDTH,15*box_height
                    ,0,16*box_height,WIDTH,16*box_height,0,17*box_height,WIDTH,17*box_height,0,18*box_height,WIDTH,18*box_height
                    ,0,19*box_height,WIDTH,19*box_height,0,20*box_height,WIDTH,20*box_height,};
            canvas.drawColor(Color.argb(255,  26, 128, 182));
            canvas.drawLines(grid_pts, paint);
            canvas.drawLines(grid_pts2, paint);

            yellowPaint.setColor(Color.argb(255,241, 251, 51));
            canvas.drawRect (0,menu_height,WIDTH,HEIGHT,yellowPaint);

        }

        // Draw the newly updated scene
        public void draw() {
            int HEIGHT = getHeight();
            int WIDTH = getWidth();

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();
                // Draw the background color and the grids
                back_grid();
                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  249, 129, 0));
                // Make the text a bit bigger
                paint.setTextSize(45);
                // Display the current fps on the screen
                canvas.drawText("FPS:" + fps, 20, 40, paint);

                /*
                *  THE GAME LOOP WILL HAVE TWO SIMPLE PHASES
                *  1) A MENU LOOK AND A
                *  2) PLAY LOOK
                *
                *  I'LL USE A BOOLEAN TO POP UP THE MENU LOOP OR PLAY LOOK*
                */


                if(inMenu){

                    if(MenuFull){
                        canvas.drawBitmap(MenuColBut,WIDTH-200, 50, yellowPaint);
                    }

                }
                else{

                    // Draw bob at bobXPosition, 200 pixels
                    canvas.drawBitmap(bitmapBob, bobXPosition, 200, paint);

                }

                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }


        //-----------------------------END OF DRAWING FUNCS---------------------------

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        public boolean opensMenu(int x, int y){
            int HEIGHT = getHeight();
            int WIDTH = getWidth();

            if(x < (int)bobXPosition+150 && x >= (int)bobXPosition-150  && y < (int)bobYPosition+150 && y > (int)bobYPosition-150){
//                InMenu = 1;
//                Taken = true;
                return true;
            }
            if(x < WIDTH && x >= 0  && y < HEIGHT && y > 1764){
//                InMenu = 0;
                return true;
            }
            else return false;
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int x = (int)motionEvent.getX();
            int y = (int)motionEvent.getY();

            if(opensMenu(x, y)){
                isMoving = false;
                inMenu = true;
                Menukill = false;
            }
            if(MenuFull && x < (int)getWidth()-200+150 && x >= (int)getWidth()-200-150  && y < (int)50+150 && y > (int)50-150){
                //Taken = false;
                Menukill = true;

            }

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {



                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:

                    // Set isMoving so Bob is moved in the update method
                    isMoving = true;

                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    // Set isMoving so Bob does not move
                    isMoving = false;

                    break;
            }
            return true;
        }

    }
    // This is the end of our GameView inner class

    // More SimpleGameEngine methods will go here

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }

}