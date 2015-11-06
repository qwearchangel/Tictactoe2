package com.filipferm.tictactoe2;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by Filip on 2015-04-09.
 *
 */
public class ShakeActivity extends Activity implements SensorEventListener {

    private static final int MIN_SHAKE_ACCELERATION = 6; //speed needed
    private static final int MIN_MOVEMENTS = 6; //number of movement
    private static final int MAX_SHAKE_DURATION = 1000; //duration of the shake before counter reset

    // arrays to store gravity and acceleration values
    private float[] mGravity = { 0.0f, 0.0f, 0.0f };
    private float[] mLinearAcceleration = { 0.0f, 0.0f, 0.0f };

    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    private OnShakeListener mShakeListener;

    long startTime = 0;// start time for the shake detection
    int moveCount = 0; // counter for shake movements

    public ShakeActivity(OnShakeListener shakeListener) { //constructor
        mShakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        setCurrentAcceleration(event);

        // get the max linear acceleration in any direction
        float maxLinearAcceleration = getMaxCurrentLinearAcceleration();

        // check if the acceleration is greater than our minimum threshold
        if (maxLinearAcceleration > MIN_SHAKE_ACCELERATION) {
            long now = System.currentTimeMillis();

            if (startTime == 0) {
                startTime = now;
            }

            long elapsedTime = now - startTime;

            if (elapsedTime > MAX_SHAKE_DURATION) {
                resetShakeDetection();
            }
            else {
                moveCount++;

                if (moveCount > MIN_MOVEMENTS) {

                    mShakeListener.onShake();
                    resetShakeDetection();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Intentionally blank
    }

    private void setCurrentAcceleration(SensorEvent event) {

        final float alpha = 0.8f;

        // Gravity components of x, y, and z acceleration
        mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X];
        mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y];
        mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z];

        // Linear acceleration along the x, y, and z axes (gravity effects removed)
        mLinearAcceleration[X] = event.values[X] - mGravity[X];
        mLinearAcceleration[Y] = event.values[Y] - mGravity[Y];
        mLinearAcceleration[Z] = event.values[Z] - mGravity[Z];
    }

    private float getMaxCurrentLinearAcceleration() {
        float maxLinearAcceleration = mLinearAcceleration[X];

        if (mLinearAcceleration[Y] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Y];
        }

        if (mLinearAcceleration[Z] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Z];
        }
        return maxLinearAcceleration;
    }

    //resets the counter and time
    private void resetShakeDetection() {
        startTime = 0;
        moveCount = 0;
    }

    public interface OnShakeListener { //interface for override.
        public void onShake();
    }
}