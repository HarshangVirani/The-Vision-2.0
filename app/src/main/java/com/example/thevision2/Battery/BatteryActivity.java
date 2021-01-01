package com.example.thevision2.Battery;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.thevision2.R;
import java.util.Locale;

public class BatteryActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener{

   // private BatteryReceiver mBatteryReceiver = new BatteryReceiver();
    private IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private TextToSpeech tts;
    private String data;
    private TextView statusLabel,percentageLabel;
    private ImageView batteryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        this.registerReceiver(this.mBatteryReceiver,mIntentFilter);
        tts = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);


        setReference();
       // title = statusLabel.getText().toString() + percentageLabel.getText().toString();

    }

    public void setReference(){
        statusLabel = findViewById(R.id.battery_status);
        percentageLabel = findViewById(R.id.battery_level);
        batteryImage = findViewById(R.id.battery_img);

    }

    private  BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)) {

                // Status
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                String message = "";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_FULL:
                        message = "Full";
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        message = "Charging";
                        break;
                    /*case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        message = "Discharging";
                        break;*/
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        message = "Not charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        message = "Unknown";
                        break;
                }
                statusLabel.setText(message);

                // Percentage
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int percentage = (level * 100 / scale);
                percentageLabel.setText(percentage + "%");

                // Image
                Resources res = context.getResources();
                if (percentage >= 90) {
                    batteryImage.setImageDrawable(res.getDrawable(R.drawable.b100));
                } else if (90 > percentage && percentage >= 65) {
                    batteryImage.setImageDrawable(res.getDrawable(R.drawable.b75));
                } else if (65 > percentage && percentage >= 40) {
                    batteryImage.setImageDrawable(res.getDrawable(R.drawable.b50));
                } else if (40 > percentage && percentage >= 20) {
                    batteryImage.setImageDrawable(res.getDrawable(R.drawable.b25));
                } else {
                    batteryImage.setImageDrawable(res.getDrawable(R.drawable.b0));
                }
            } data = statusLabel.getText().toString() + percentageLabel.getText().toString();
        }
    };

    //Text To Speech...
    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.UK);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.setSpeechRate(0.8f);
                tts.setPitch(1.0f);
                tts.speak(data, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        },500);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mBatteryReceiver);
        tts.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        tts.shutdown();
        super.onDestroy();
    }

}