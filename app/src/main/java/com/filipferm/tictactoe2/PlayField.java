package com.filipferm.tictactoe2;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayField extends ActionBarActivity implements View.OnClickListener {

    TextView playerTurnTextView, roundTextView;
    ImageView row11, row12, row13, row21, row22, row23, row31, row32, row33;
    LinearLayout field, top, center, bottom;
    int numberOfPlayers;
    private int playerTurn = 1;
    private int round = 0;  // so there can be a draw
    String winOrDraw; //tells the fragment what message to have
    Boolean win = false; //makes sure the ai wont win when the player win.
    ImageView[] arr; //arrays of image views. Sends to AI
    AI ai; //the AI. activates if numberOfPlayers == 1
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeActivity mShakeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);
        initializeViews();
        Intent getNumberOfPlayers = getIntent();
        numberOfPlayers = Integer.parseInt(getNumberOfPlayers.getStringExtra("players"));
        if(numberOfPlayers == 1) {
            ai = new AI();
            ai.getArray(arr);
        }

        setPlayFieldSize();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeActivity = new ShakeActivity(new ShakeActivity.OnShakeListener() {
            @Override
            public void onShake() {
                recreate();
            }
        });
        roundTextView.setText("Players: " + numberOfPlayers);
    }

    private void setPlayFieldSize() {
        Display display = getWindowManager().getDefaultDisplay();

        int displayWidth = display.getWidth() - 100;
        int displayHeight = displayWidth;

        field.getLayoutParams().width = displayWidth;
        field.getLayoutParams().height = displayHeight;
        field.requestLayout();

        top.getLayoutParams().width = displayWidth;
        top.getLayoutParams().height = displayHeight / 3;
        top.requestLayout();

        center.getLayoutParams().width = displayWidth;
        center.getLayoutParams().height = displayHeight / 3;
        center.requestLayout();

        bottom.getLayoutParams().width = displayWidth;
        bottom.getLayoutParams().height = displayHeight / 3;
        bottom.requestLayout();

        row11.getLayoutParams().width = displayWidth / 3;
        row11.getLayoutParams().height = displayHeight / 3;
        row11.requestLayout();

        row12.getLayoutParams().width = displayWidth / 3;
        row12.getLayoutParams().height = displayHeight / 3;
        row12.requestLayout();

        row13.getLayoutParams().width = displayWidth / 3;
        row13.getLayoutParams().height = displayHeight / 3;
        row13.requestLayout();

        row21.getLayoutParams().width = displayWidth / 3;
        row21.getLayoutParams().height = displayHeight / 3;
        row21.requestLayout();

        row22.getLayoutParams().width = displayWidth / 3;
        row22.getLayoutParams().height = displayHeight / 3;
        row22.requestLayout();

        row23.getLayoutParams().width = displayWidth / 3;
        row23.getLayoutParams().height = displayHeight / 3;
        row23.requestLayout();

        row31.getLayoutParams().width = displayWidth / 3;
        row31.getLayoutParams().height = displayHeight / 3;
        row31.requestLayout();

        row32.getLayoutParams().width = displayWidth / 3;
        row32.getLayoutParams().height = displayHeight / 3;
        row32.requestLayout();

        row33.getLayoutParams().width = displayWidth / 3;
        row33.getLayoutParams().height = displayHeight / 3;
        row33.requestLayout();
    }

    //initializes the views and puts some of them to the onClickListener
    public void initializeViews() {

        row11 = (ImageView) findViewById(R.id.row_1_1);
        row12 = (ImageView) findViewById(R.id.row_1_2);
        row13 = (ImageView) findViewById(R.id.row_1_3);
        row21 = (ImageView) findViewById(R.id.row_2_1);
        row22 = (ImageView) findViewById(R.id.row_2_2);
        row23 = (ImageView) findViewById(R.id.row_2_3);
        row31 = (ImageView) findViewById(R.id.row_3_1);
        row32 = (ImageView) findViewById(R.id.row_3_2);
        row33 = (ImageView) findViewById(R.id.row_3_3);

        field = (LinearLayout) findViewById(R.id.playField);
        top = (LinearLayout) findViewById(R.id.playField_top);
        center = (LinearLayout) findViewById(R.id.playField_center);
        bottom =  (LinearLayout) findViewById(R.id.playField_bottom);

        row11.setOnClickListener(this);
        row12.setOnClickListener(this);
        row13.setOnClickListener(this);
        row21.setOnClickListener(this);
        row22.setOnClickListener(this);
        row23.setOnClickListener(this);
        row31.setOnClickListener(this);
        row32.setOnClickListener(this);
        row33.setOnClickListener(this);

        playerTurnTextView = (TextView) findViewById(R.id.playerTurnTextView);
        roundTextView = (TextView) findViewById(R.id.round_text_view);
        arr = new ImageView[]{row11, row12, row13, row21, row22, row23, row31, row32, row33};
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeActivity, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_field, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        } else if (id == R.id.action_restart) {
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }

    //Main game function.
    @Override
    public void onClick(View v) {
        if (numberOfPlayers == 2) {
            playerMove(v);
            checkWinOrDraw();
            nextPlayer();
        }
        if (numberOfPlayers == 1) {
            playerMove(v);
            checkWinOrDraw();
            nextPlayer();
            if (win != true) {
                ai.aiMove();
                checkWinOrDraw();
                nextPlayer();
            }
        }
    }

    /**
     * Checks the PlayField if there is three in a row or
     * if its a draw. will then call on a fragment.
     */
    private void checkWinOrDraw() {

        round++;
        roundTextView.setText("Round: " + round);
        //player 1
        //horizontal win
        if (row11.getTag() == "o" && row12.getTag() == "o" && row13.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row21.getTag() == "o" && row22.getTag() == "o" && row23.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row31.getTag() == "o" && row32.getTag() == "o" && row33.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        //vertical win
        if (row11.getTag() == "o" && row21.getTag() == "o" && row31.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row12.getTag() == "o" && row22.getTag() == "o" && row32.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row13.getTag() == "o" && row23.getTag() == "o" && row33.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        //diagonal win
        if (row11.getTag() == "o" && row22.getTag() == "o" && row33.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row13.getTag() == "o" && row22.getTag() == "o" && row31.getTag() == "o") {
            winOrDraw = "Player 1 wins!";
            win = true;
            getWinOrDrawFragment();
        }

        //player 2 or AI
        //horizontal win
        if (row11.getTag() == "x" && row12.getTag() == "x" && row13.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row21.getTag() == "x" && row22.getTag() == "x" && row23.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row31.getTag() == "x" && row32.getTag() == "x" && row33.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        //vertical
        // win
        if (row11.getTag() == "x" && row21.getTag() == "x" && row31.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row12.getTag() == "x" && row22.getTag() == "x" && row32.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row13.getTag() == "x" && row23.getTag() == "x" && row33.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        //diagonal win
        if (row11.getTag() == "x" && row22.getTag() == "x" && row33.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        if (row13.getTag() == "x" && row22.getTag() == "x" && row31.getTag() == "x") {
            winOrDraw = "Player 2 wins!";
            win = true;
            getWinOrDrawFragment();
        }
        // if no one wins
        if (round >= 9 && win == false) {
            winOrDraw = "Draw";
            getWinOrDrawFragment();
        }
    }

    //Gets the fragment
    private void getWinOrDrawFragment() {
        DialogFragment fragmentWinOrDraw = new WinOrDrawFragment();
        Bundle bundle = new Bundle();
        bundle.putString("winOrDrawString", winOrDraw);
        bundle.putInt("numberOfPlayers", numberOfPlayers);
        fragmentWinOrDraw.setArguments(bundle);
        fragmentWinOrDraw.show(getFragmentManager(), "winOrDraw");

    }

    //Sets the turn to the other player
    private void nextPlayer() {
        if (playerTurn == 1) {
            playerTurn = 2;
            playerTurnTextView.setText("Player " + playerTurn + " turn!");
        } else {
            playerTurn = 1;
            playerTurnTextView.setText("Player " + playerTurn + " turn!");
        }
    }

    /**
     * Sets a tag on the clicked area and sets a picture
     * and turns off the click function on the area
     */
    private void playerMove(View v) {

        if (v == row11) {
            if (playerTurn == 1 && row11.isClickable()) {
                row11.setImageResource(R.drawable.tic_o);
                row11.setTag("o");
                row11.setClickable(false);
            } else if (playerTurn == 2 && row11.isClickable()) {
                row11.setImageResource(R.drawable.tic_x);
                row11.setTag("x");
                row11.setClickable(false);
            }
        } else if (v == row12) {
            if (playerTurn == 1 && row12.isClickable()) {
                row12.setImageResource(R.drawable.tic_o);
                row12.setTag("o");
                row12.setClickable(false);
            } else if (playerTurn == 2 && row12.isClickable()) {
                row12.setImageResource(R.drawable.tic_x);
                row12.setTag("x");
                row12.setClickable(false);
            }
        } else if (v == row13) {
            if (playerTurn == 1 && row13.isClickable()) {
                row13.setImageResource(R.drawable.tic_o);
                row13.setTag("o");
                row13.setClickable(false);
            } else if (playerTurn == 2 && row13.isClickable()) {
                row13.setImageResource(R.drawable.tic_x);
                row13.setTag("x");
                row13.setClickable(false);
            }
        } else if (v == row21) {
            if (playerTurn == 1 && row21.isClickable()) {
                row21.setImageResource(R.drawable.tic_o);
                row21.setTag("o");
                row21.setClickable(false);
            } else if (playerTurn == 2 && row21.isClickable()) {
                row21.setImageResource(R.drawable.tic_x);
                row21.setTag("x");
                row21.setClickable(false);
            }
        } else if (v == row22) {
            if (playerTurn == 1 && row22.isClickable()) {
                row22.setImageResource(R.drawable.tic_o);
                row22.setTag("o");
                row22.setClickable(false);
            } else if (playerTurn == 2 && row22.isClickable()) {
                row22.setImageResource(R.drawable.tic_x);
                row22.setTag("x");
                row22.setClickable(false);
            }
        }else if (v == row23) {
            if (playerTurn == 1 && row23.isClickable()) {
                row23.setImageResource(R.drawable.tic_o);
                row23.setTag("o");
                row23.setClickable(false);
            } else if (playerTurn == 2 && row23.isClickable()) {
                row23.setImageResource(R.drawable.tic_x);
                row23.setTag("x");
                row23.setClickable(false);
            }
        }else if (v == row31) {
            if (playerTurn == 1 && row31.isClickable()) {
                row31.setImageResource(R.drawable.tic_o);
                row31.setTag("o");
                row31.setClickable(false);
            } else if (playerTurn == 2 && row31.isClickable()) {
                row31.setImageResource(R.drawable.tic_x);
                row31.setTag("x");
                row31.setClickable(false);
            }
        }else if (v == row32) {
            if (playerTurn == 1 && row32.isClickable()) {
                row32.setImageResource(R.drawable.tic_o);
                row32.setTag("o");
                row32.setClickable(false);
            } else if (playerTurn == 2 && row32.isClickable()) {
                row32.setImageResource(R.drawable.tic_x);
                row32.setTag("x");
                row32.setClickable(false);
            }
        }else if (v == row33) {
            if (playerTurn == 1 && row33.isClickable()) {
                row33.setImageResource(R.drawable.tic_o);
                row33.setTag("o");
                row33.setClickable(false);
            } else if (playerTurn == 2 && row33.isClickable()) {
                row33.setImageResource(R.drawable.tic_x);
                row33.setTag("x");
                row33.setClickable(false);
            }
        }
    }
}