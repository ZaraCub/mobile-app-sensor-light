package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView greetingTextView;
    private ImageView imageView;
    private View backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greetingTextView = findViewById(R.id.helloTextView);
        imageView = findViewById(R.id.imageView);
        backgroundView = findViewById(R.id.backgroundView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Provjera dostupnosti senzora svjetline
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            greetingTextView.setText("Senzor svjetla nije dostupan");
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationX", -500f, 500f);
        animator.setDuration(5000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float ambientLight = event.values[0];

            if (ambientLight < 10) {
                backgroundView.setBackgroundColor(getResources().getColor(R.color.darkBlue));
            } else {
                backgroundView.setBackgroundColor(getResources().getColor(R.color.lightBlue));
            }

            // Prilagodba teksta i slike na temelju svjetlosti
            if (ambientLight < 10) {
                imageView.setImageResource(R.drawable.moon);
                setGreetingText("Laku noć");
            } else {
                imageView.setImageResource(R.drawable.sun);
                setGreetingText("Dobar dan");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void setGreetingText(String greeting) {
        greetingTextView.setText(greeting);
    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }
}
