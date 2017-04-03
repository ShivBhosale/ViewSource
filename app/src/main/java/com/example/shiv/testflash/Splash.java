package com.example.shiv.testflash;


import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Button;

public class Splash extends Activity  implements View.OnClickListener{
    TextView t, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashh);

            /*Setting a nice font*/
        t= (TextView) findViewById(R.id.textView2);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/AldotheApache.ttf");
        t.setTypeface(myCustomFont);


        t= (TextView) findViewById(R.id.textView2);
        Typeface myCustomFont2=Typeface.createFromAsset(getAssets(),"fonts/AldotheApache.ttf");
        t.setTypeface(myCustomFont2);

        t2= (TextView) findViewById(R.id.textView3);
        Typeface myCustomFont21=Typeface.createFromAsset(getAssets(),"fonts/pizdude.ttf");
        t2.setTypeface(myCustomFont21);


        t2= (TextView) findViewById(R.id.textView3);
        Typeface myCustomFont22=Typeface.createFromAsset(getAssets(),"fonts/pizdude.ttf");
        t2.setTypeface(myCustomFont22);

        /*button goes here*/
        final Button play_but = (Button) findViewById(R.id.button);
        play_but.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button:
                Button temp = (Button) findViewById(R.id.button);
                temp.setVisibility(View.GONE);
                final ImageView iv = (ImageView) findViewById(R.id.imageView);
                final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
                final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);
                iv.setVisibility(View.VISIBLE);
                iv.startAnimation(an);
                an.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                /*after the animation ends, the screen fades away and cool stuff appears*/
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv.startAnimation(an2);
                        finish();
                        Intent i = new Intent(getBaseContext(),ViewSource1.class); // ViewSource1 is the original one
                        //change the activity name in the manifest file too else the app will crash
                        startActivity(i);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;

        }

    }
}