package com.example.movementdetector;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class MovementSensorService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mLight;
    private Float x = null, y = null, z = null;
    private double epsilon = 1.2;
    private int numberOfStayingStill, numberOfStayingInPocket;
    private double currAcceleration, acceleration = mSensorManager.GRAVITY_EARTH;
    private boolean moving, inPocket;

//    0: movement & device is in pocket, 1: no movement & device isn't in pocket, 2: no movement & device is in pocket
    private int state = 1;


    public MovementSensorService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            currAcceleration = Math.sqrt(x * x + y * y + z * z);
            if (!isInRange(currAcceleration, acceleration, epsilon)) {
                moving = true;
                acceleration = currAcceleration;
                numberOfStayingStill = 0;
            } else {
//                System.out.println("Not Moving");
//                System.out.println("# of still: " + numberOfStayingStill);
                if(numberOfStayingStill < 95){
                    numberOfStayingStill++;
                }
                if (numberOfStayingStill >= 95) {
                    moving = false;
                }
            }
//            System.out.println("Acceleration: " + acceleration);
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            System.out.println("# of pocket" + numberOfStayingInPocket);
//            System.out.println("Light:" + sensorEvent.values[0]);
            if(sensorEvent.values[0] < 15){
//                toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                if(numberOfStayingInPocket < 5){
                    numberOfStayingInPocket++;
                }
                if(numberOfStayingInPocket >= 5){
                    inPocket = true;
                }
            }else{
                numberOfStayingInPocket = 0;
                inPocket = false;
            }
//            textView.setText("Light: " + sensorEvent.values[0]);
        }

        if(moving){
            if(inPocket){
                if(state != 0){
                    Intent intent = new Intent();
                    intent.setAction("com.movement.broadcast.soundBroadcast");
                    intent.putExtra("state", 0);
                    sendBroadcast(intent);
                    Toast.makeText(this, "status 0 is on the way", Toast.LENGTH_SHORT).show();
                    System.out.println("status 0 is on the way");
                }
                state = 0;
            }
        }
        if(!moving && !inPocket){
            if(state != 1){
                Intent intent = new Intent();
                intent.setAction("com.movement.broadcast.soundBroadcast");
                intent.putExtra("state", 1);
                sendBroadcast(intent);
                Toast.makeText(this, "status 1 is on the way", Toast.LENGTH_SHORT).show();
                System.out.println("status 1 is on the way");
            }
            state = 1;
        }
        if(!moving && inPocket){
            if(state != 2){
                Intent intent = new Intent();
                intent.setAction("com.movement.broadcast.soundBroadcast");
                intent.putExtra("state", 2);
                sendBroadcast(intent);
                Toast.makeText(this, "status 2 is on the way", Toast.LENGTH_SHORT).show();
                System.out.println("status 2 is on the way");
            }
            state = 2;
        }

    }

    public boolean isInRange(double value, double limit, double epsilon){
        return(value >= limit - epsilon && value <= limit + epsilon);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}