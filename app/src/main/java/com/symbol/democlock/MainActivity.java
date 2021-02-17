package com.symbol.democlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.symbol.zebrahud.ZebraHud;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ZebraHud.EventListener {
    static final String TAG = "HudDemoClock";
    //static final String TIME_FORMAT = "hh:mm:ss a";
    static final String TIME_FORMAT = "HH:mm:ss";
    private ZebraHud hud = new ZebraHud();
    int n=0;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

//    private byte[] cachedImage = null;
//    private TextView viewText = null;
//    private ImageView hudImage = null;

    // our private Handler messages
    enum HandlerMsg {
        TIMER
    }

    Button btClickMe;
    Button btIntermediateAPI;
    Button btAdvancedJsonAPI;
    Button btHudInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

  //      viewText = findViewById(R.id.text);
  //      hudImage = findViewById(R.id.hudimage);

        btClickMe = findViewById(R.id.btClickMe);
        btClickMe.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             basicAPIExample();
                                         }
                                     }
        );

        btIntermediateAPI = findViewById(R.id.btInterm);
        btIntermediateAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intermediateAPIExample();
            }
        });

        btAdvancedJsonAPI = findViewById(R.id.btAdvanced);
        btAdvancedJsonAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advancedJSONAPI();
            }
        });

        btHudInterface = findViewById(R.id.btIntentAPI);
        btHudInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hudInterfaceAPI();
            }
        });
    }

    void basicAPIExample(){
        hud.showMessage("CXNT48", "THIS IS HD4000 "+ n++);
    }

    void intermediateAPIExample(){
        View customView = hud.createHudView(R.layout.custom_hud_layout);
        ImageView civ = customView.findViewById(R.id.hud_imageView);
        TextView ctv = customView.findViewById(R.id.hud_textView);
        switch (n%3) {
            case 0:   civ.setImageResource(R.raw.i0); break;
            case 1:   civ.setImageResource(R.raw.i1); break;
            case 2:   civ.setImageResource(R.raw.i2); break;
        }
        ctv.setText("intermediateAPIExample - "+n++);

        hud.showHudView( customView );

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("hudadv.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    void advancedJSONAPI(){
        //must add to gradle=>   implementation 'androidx.constraintlayout:constraintlayout:1.1.3'

        hud.showJim( loadJSONFromAsset() );
    }

    void hudInterfaceAPI(){
        //requires "HUD Interface (vX.X).apk" with overlay permission granted?
        Intent i = new Intent("com.symbol.zebrahudservice.DISPLAY_TEXT");
        i.putExtra("text.text", "AppDev-TVT");
        i.putExtra("text.size", "14");
        i.putExtra("text.justification", "RIGHT");
        i.putExtra("text.background_colour", "GREEN");
        sendBroadcast(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hud.onStart(this);
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) hud.clearDisplay();
        hud.onStop(this, !isFinishing());
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    hud.onPause(this);
        //myHandler.removeMessages(HandlerMsg.TIMER.ordinal());
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
   //     hud.onResume(this, this);
        //myHandler.sendEmptyMessage(HandlerMsg.TIMER.ordinal());
        Log.d(TAG, "onResume");
    }

    // ZebraHud.EventListener
    @Override
    public void onConnected(Boolean connected) {
        /*
        if (connected) hud.showCachedImage(cachedImage); // handle hud disconnect/reconnect
        else hudImage.setImageBitmap(null);

         */
        int zxc=0;
    }

    // ZebraHud.EventListener
    @Override
    public void onImageUpdated(byte[] image) {
        /*
        cachedImage = image; // handle hud disconnect/reconnect
        // mirror the hud image on the device-screen
        hudImage.setImageBitmap(
                null != image && image.length > 0 ?
                        BitmapFactory.decodeByteArray(
                                image,
                                0,
                                image.length)
                        : null);

         */
    }

    // ZebraHud.EventListener
    @Override
    public void onCameraImage(Bitmap bitmap) {
    }

    @Override
    public void onCameraImageRawJpeg(@NonNull byte[] bytes) {

    }

    /*
    @SuppressLint("SimpleDateFormat")
    private void updateClock() {
        String time = new SimpleDateFormat(TIME_FORMAT).format(new Date(System.currentTimeMillis()));
        viewText.setText(time);
        hud.showMessage("RED-ZONE TIME", time);
        Log.d(TAG, "updateClock");
    }

     */

    /*
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> activity;

  //      MyHandler(final MainActivity parent) {
            activity = new WeakReference<>(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (!activity.get().isFinishing() && HandlerMsg.TIMER.ordinal() == msg.what) {
                    activity.get().updateClock();
                    sendEmptyMessageDelayed(HandlerMsg.TIMER.ordinal(), 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/
//    private MyHandler myHandler = new MyHandler(this);
}
