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

import java.util.ArrayList;
import java.util.List;

public class ViewSource1 extends Activity {
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }
    class GameView extends SurfaceView implements Runnable {

        // This is our thread
        Thread gameThread = null;

        SurfaceHolder ourHolder;
        int InMenu = 0;
        int cter = 5;
        int head2 = 0;
        int Anim = 1;
        /*
        * 0 - Background
        * 1 - Bob or Charachter
        * 2 - idk
        * */

        /*
         * Java Sucks big time
         */

        //-----------INTERNAL OS SIMULATION--------------


        List<Integer> IQ = new ArrayList<Integer>();
           /*
        * 0 - do nothing
        * 1 - move left by 1 unit
        * 2 - move right by 1 unit
        * 3 - move up by 1 unit
        * 4 - move down by 1 unit
        * 5 - idk
        * */
        String[] FD = new String[] {"main( )"};
        String[] FL = new String[]{"Bob()", "Physics()", "moveLeft(", "moveRight(", "moveUp(", "moveDown(", "force("};
        String[] Keys = new String[] {"import ","int ", "void ", "if ", "else ", "return "};
        String[] GR = new String[] {"{", "}", "(", ")", ";"};
        String[] VL = new String[]{"b", "p", "Gravity;", "Physics;"};
        String[] Keys2 = new String[]{"Bob b = new Bob( );\n", "Physics p = new Physics( );\n"
                                    ,"return 0;\n"};
        int head = -1;
        int c = 0;


        //----------------END INTERNAL OS SIMULATION------------------

        //---------------AESTHETIC STUFF-------------------------------

        //For swipe action stuff

        OnSwipeTouchListener Swipe_Act = new OnSwipeTouchListener(ViewSource1.this) {
            public void onSwipeTop() {
                Toast.makeText(ViewSource1.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
            }

        };

        // A boolean which we will set and unset
        // when the game is running- or not.

        //----------start of cursor

        int start = 250;

        //---------sacred------------

        volatile boolean playing;
        // A Canvas and a Paint object
        private Rect rectangle;
        Canvas canvas;// = new Canvas(blackgrid.copy(Bitmap.Config.ARGB_8888, true));
        Paint paint;
        Paint yellowPaint = new Paint(); // for dashboard menu
        Paint blackPaint = new Paint();  // for writing text
        Paint Syntaxcol = new Paint();   // for syntax

        public int[] Openers = new int[]{}; //future use. for opeing menus
        // This variable tracks the game frame rate
        long fps;
        // This is used to help calculate the fps
        private long timeThisFrame;

        // Declare an object of type Bitmap
        Bitmap bitmapBob, MenuColBut, Envi, L,Ri,U,D,bsp,Editor,bobleft,bobup,bobdown;
        float SpriteX = 0;
        float SpriteY = 0;


        //----------------END AESTHETIC STUFF------------------

        //-----------------BOOLEANS-----------------------------


        // Bob starts off not moving
        boolean isMoving = false;
        boolean isMenu = false;
        boolean MenuCol = false;
        boolean Menukill = true;
        boolean Taken = false;
        boolean inOpts = false;
        //boolean inSimul = false;
        // He can walk at 150 pixels per second


        //--------------CHARACHTER CONFIG--------------------


        float walkSpeedPerSecond = 150;
        // He starts 10 pixels from the left
        float bobXPosition = 10;
        float bobYPosition = 197;
        int menu_height = 1764;

        private int frameWidth = 30*3;
        private int frameHeight = 40*3;

        // How many frames are there on the sprite sheet?
        private int frameCount = 4;

        // Start at the first frame - where else?
        private int currentFrame = 0;

        // What time was it when we last changed frames
        private long lastFrameChangeTime = 0;

        // How long should each frame last
        private int frameLengthInMilliseconds = 100;

        // A rectangle to define an area of the
        // sprite sheet that represents 1 frame
        private Rect frameToDraw = new Rect(
                0,
                0,
                frameWidth,
                frameHeight);

        // A rect that defines an area of the screen
        // on which to draw
        RectF whereToDraw = new RectF(
                bobXPosition,                bobYPosition,
                bobXPosition + frameWidth,
                frameHeight);

        //------------END CHARACHTER CONFIG--------------

        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public GameView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();

            //-------------------LOAD SPRITES AND BITMAPS HERE------------------


            bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.walkright);
            bobleft = BitmapFactory.decodeResource(this.getResources(), R.drawable.walkleft);
            bobup = BitmapFactory.decodeResource(this.getResources(), R.drawable.walkback);
            bobdown = BitmapFactory.decodeResource(this.getResources(), R.drawable.walkfront);
            Editor = BitmapFactory.decodeResource(this.getResources(), R.drawable.pencil);
            MenuColBut = BitmapFactory.decodeResource(this.getResources(), R.drawable.tick);
            Envi = BitmapFactory.decodeResource(this.getResources(), R.drawable.eye);
            L = BitmapFactory.decodeResource(this.getResources(), R.drawable.leftarr);
            Ri = BitmapFactory.decodeResource(this.getResources(), R.drawable.rightarr);
            U = BitmapFactory.decodeResource(this.getResources(), R.drawable.uparr1);
            D= BitmapFactory.decodeResource(this.getResources(), R.drawable.downarr);
            bsp= BitmapFactory.decodeResource(this.getResources(), R.drawable.backsp);

            bitmapBob = Bitmap.createScaledBitmap(bitmapBob,
                    frameWidth * frameCount,
                    frameHeight,
                    false);
            bobleft = Bitmap.createScaledBitmap(bobleft,
                    frameWidth * frameCount,
                    frameHeight,
                    false);
            bobup = Bitmap.createScaledBitmap(bobup,
                    frameWidth * frameCount,
                    frameHeight,
                    false);
            bobdown = Bitmap.createScaledBitmap(bobdown,
                    frameWidth * frameCount,
                    frameHeight,
                    false);

            //-----------------END OF BITMAP SHIT----------------------------
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

                // Calculate the   this frame
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


        //--------------FORGIVE ME IF YOU DON'T UNDERSTAND-------------------------


        public void update() {

            // If bob is moving (the player is touching the screen)
            // then move him to the right based on his target speed and the current fps.


            if(isMoving && !isMenu){
                if(!IQ.isEmpty()) {
                    if (c < 50 && head2!=IQ.size()) {
                        switch(IQ.get(head2)){
                            case 1:
                                bobXPosition = bobXPosition - (walkSpeedPerSecond / fps);
                                Anim = 1;
                                break;
                            case 2:
                                bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
                                Anim = 2;
                                break;
                            case 3:
                                bobYPosition = bobYPosition - (walkSpeedPerSecond / fps);
                                Anim = 3;
                                break;
                            case 4:
                                bobYPosition = bobYPosition + (walkSpeedPerSecond / fps);
                                Anim = 4;
                                break;
                        }
                    } else{
                        c = 0;
                        if(head2 < IQ.size()){
                            head2++;
                        }
                        else{
                            head2 = 0;
                            isMoving = false;
                        }
                    }
                }

            }
            else if(isMenu && !Menukill){
                if(menu_height > 0 && !MenuCol){
                    menu_height-= 210;
                }
                else {
                    MenuCol = true;
                }
            }
            else if(Menukill && isMenu){

                MenuCol = false;
                if(menu_height <= 1764 && !MenuCol){
                    menu_height += 210;
                }
                else{
                    menu_height = 1764;
                    isMenu = false;
                    isMoving = true;

                }

            }

        }

        public void getCurrentFrame(){

            long time  = System.currentTimeMillis();
            if(isMoving) {// Only animate if bob is moving
                if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                    lastFrameChangeTime = time;
                    currentFrame++;
                    if (currentFrame >= frameCount) {

                        currentFrame = 0;
                    }
                }
            }
            //update the left and right values of the source of
            //the next frame on the spritesheet
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;

        }


        //------------------I HAVE SINNED GREATLY------------------------------------


        public void renderBobSource(){

            int offset = 50;
            int HEIGHT = getHeight();
            int WIDTH = getWidth();
            Syntaxcol.setColor(Color.argb(255,  53, 53, 46));
            canvas.drawRect (50,230,WIDTH-50,1700,Syntaxcol);
            canvas.drawBitmap(L,50, 1764, paint);
            canvas.drawBitmap(Ri,250, 1764, paint);
            canvas.drawBitmap(U,450, 1764, paint);
            canvas.drawBitmap(D,650, 1764, paint);
            canvas.drawBitmap(bsp,850, 1764, paint);
            Syntaxcol.setTextSize(45);
            Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
            canvas.drawText("1", 110, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  244, 66, 104));
            canvas.drawText(Keys[0], 250, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
            canvas.drawText(VL[2], 250+150, start+offset, Syntaxcol);
            offset+= 50;
            Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
            canvas.drawText("2", 110, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  244, 66, 104));
            canvas.drawText(Keys[0], 250, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
            canvas.drawText(VL[3], 250+150, start+offset, Syntaxcol);
            offset+= 50;
            Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
            canvas.drawText("3", 110, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  244, 66, 104));
            canvas.drawText(Keys[2], 250, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  102, 255,102));
            canvas.drawText(FD[0], 250+100, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  255, 255,255 ));
            canvas.drawText(GR[0], 250+250, start+offset, Syntaxcol);
            offset+= 50;
            Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
            canvas.drawText("4", 110, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
            canvas.drawText(Keys2[0], 300, start+offset, Syntaxcol);
            offset+= 50;
            Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
            canvas.drawText("5", 105, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
            canvas.drawText(Keys2[1], 300, start+offset, Syntaxcol);
            offset+= 50;

            for (int i = 0; i < IQ.size(); i++) {
                Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
                canvas.drawText((i+6)+"", 110, start+offset, Syntaxcol);

                switch(IQ.get(i)){
                    case 0:
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("too fast", 250, start+offset, Syntaxcol);
                        cter = i+6;
                        break;
                    case 1:
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("b.", 300, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("moveLeft(", 340, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("p.", 540, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("force(10));", 580, start+offset, Syntaxcol);
                        //c++;
                        cter = i+6;

                        offset += 50;
                        break;
                    case 2:
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("b.", 300, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("moveRight(", 340, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("p.", 567, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("force(10));", 607, start+offset, Syntaxcol);
                        //c++;
                        cter = i+6;
                        offset += 50;
                        break;
                    case 3:
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("b.", 300, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("moveUp(",340, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("p.", 520, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("force(10));", 560, start+offset, Syntaxcol);
                        //c++;
                        cter = i+6;
                        offset += 50;
                        break;
                    case 4:
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("b.", 300, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("moveDown(", 340, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
                        canvas.drawText("p.", 580, start+offset, Syntaxcol);
                        Syntaxcol.setColor(Color.argb(255,  51, 204, 255));
                        canvas.drawText("force(10));", 620, start+offset, Syntaxcol);
                        //c++;
                        cter = i+6;
                        offset += 50;
                        break;
                    default:
                        canvas.drawText("too fast", 250, start+offset, Syntaxcol);
                        break;

                }
            }

            Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
            canvas.drawText((cter+1)+"", 105, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  244, 66, 104));
            canvas.drawText(Keys[5], 300, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
            canvas.drawText("0;", 300+150, start+offset, Syntaxcol);
            offset+=50;
            Syntaxcol.setColor(Color.argb(255, 132, 132, 130));
            canvas.drawText((cter+2)+"", 105, start+offset, Syntaxcol);
            Syntaxcol.setColor(Color.argb(255,  255, 255, 255));
            canvas.drawText("}", 250, start+offset, Syntaxcol);
            //canvas.drawText(Keys[10], 250, start+offset, Syntaxcol);
            try{
                            Thread.sleep(230);
                        }catch(InterruptedException ex){
                            //do stuff
                        }
            inOpts = false;

        }


        //----------------------THOSE WEREN'T MY ONLY SINS---------------------


        public void Animate_sprite(Bitmap spritesheet){
            whereToDraw.set((int) bobXPosition,
                    bobYPosition,
                    (int) bobXPosition + frameWidth,
                    frameHeight + bobYPosition);

            getCurrentFrame();

            canvas.drawBitmap(spritesheet,
                    frameToDraw,
                    whereToDraw, paint);
        }


        //-------------------------------------------I HAVE SINNED AGAIN---------------------------------------


        public void draw() {
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
            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();
                // Draw the background color
                canvas.drawColor(Color.argb(255,  26, 128, 182));
                canvas.drawLines(grid_pts, paint);
                canvas.drawLines(grid_pts2, paint);
                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  249, 129, 0));
                yellowPaint.setColor(Color.argb(255,241, 251, 51));
                // Make the text a bit bigger
                paint.setTextSize(45);
                // Display the current fps on the screen
                canvas.drawText("FPS:" + fps, 20, 40, paint);
                // Draw bob at bobXPosition, 200 pixels

                //-----------------------IF NOT IN MENU------------------------------


                if(!MenuCol){
                    //System.out.println(c+"\n"); //for my own verification
                    if(c < 50) {
                        switch(Anim){
                            case 1:
                                Animate_sprite(bobleft);
                                break;
                            case 2:
                                Animate_sprite(bitmapBob);
                                break;
                            case 3:
                                Animate_sprite(bobup);
                                 break;
                            case 4:
                                Animate_sprite(bobdown);
                                break;
                        }
                        if(!isMenu && !IQ.isEmpty()) {
                            c++;
                        }
                    }
                    else{
                        isMoving = false;
                        Animate_sprite(bobdown);
                    }
                }
                canvas.drawRect (0,menu_height,WIDTH,HEIGHT,yellowPaint);

                if(MenuCol){
                    blackPaint.setColor(Color.argb(255,0, 0, 0));
                    canvas.drawBitmap(MenuColBut,WIDTH-200, 50, blackPaint);
                    switch (InMenu){
                        case 0:
                            canvas.drawBitmap(Envi,50, 50, paint);
                            break;
                        case 1:
                            canvas.drawBitmap(Envi,50, 50, paint);
                            renderBobSource();
                            canvas.drawRect (200,0,WIDTH-200,230,yellowPaint);
                            break;
                    }
                }
                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

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
                InMenu = 1;
                Taken = true;
                return true;
            }
            if(x < WIDTH && x >= 0  && y < HEIGHT && y > 1764 && !Taken){
                InMenu = 0;
                return true;
            }
            else return false;
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        public void getOptions(int c, int x, int y){
            inOpts = true;
            //int quantum = 180;

            switch(c){
                case 1:

                    if(x < 50+150 && x >= 50-150  && y < 1764+150 && y > 1764-150){
                        head +=1;
                        IQ.add(1);
//                        try{
//                            Thread.sleep(quantum);
//                        }catch(InterruptedException ex){
//                            //do stuff
//                        }
                    }
                    else if(x < 250+150 && x >= 250-150  && y < 1764+150 && y > 1764-150){
                        head +=1;
                        IQ.add(2);
//                        try{
//                            Thread.sleep(quantum);
//                        }catch(InterruptedException ex){
//                            //do stuff
//                        }
                    }
                    else if(x < 450+150 && x >= 450-150  && y < 1764+150 && y > 1764-150){
                        head +=1;
                        IQ.add(3);
//                        try{
//                            Thread.sleep(quantum);
//                        }catch(InterruptedException ex){
//                            //do stuff
//                        }
                    }
                    else if(x < 650+150 && x >= 650-150  && y < 1764+150 && y > 1764-150){
                        head +=1;
                        IQ.add(4);

                    }
                    else if(x < 850+150 && x >= 850-150  && y < 1764+150 && y > 1764-150){
                        IQ.remove(head);
                        head -= 1;

                    }
                    break;
            }

        }
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
//            try{
//                Thread.sleep(300);
//            }catch(InterruptedException ex){
//                //do stuff
//            }
            int x = (int)motionEvent.getX();
            int y = (int)motionEvent.getY();

            //pauses the game on click of the sprite
            if(opensMenu(x, y)){
                isMoving = false;
                isMenu = true;
                Menukill = false;
            }

            if(MenuCol && x < (int)getWidth()-200+150 && x >= (int)getWidth()-200-150  && y < (int)50+150 && y > (int)50-150){
                Taken = false;
                Menukill = true;

            }

            switch(InMenu){
                case 0:
                    break;
                case 1:
                    if(!inOpts){
                        getOptions(1, x, y);
                    }
                    break;
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
                    isMoving = true;

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