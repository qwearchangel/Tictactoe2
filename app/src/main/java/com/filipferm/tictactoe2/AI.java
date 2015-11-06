package com.filipferm.tictactoe2;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Filip on 2015-04-09.
 */
public class AI {

    private ImageView[] listViewArray;
    ImageView randomImageView;

    public void getArray(ImageView[] listViewArray) {
        this.listViewArray = listViewArray;
    }

    public void aiMove() {
        randomImageView();
        randomImageView.setImageResource(R.drawable.tic_x);
        randomImageView.setTag("x");
        randomImageView.setClickable(false);

    }

    private void randomImageView() {
        ArrayList<ImageView> randomArray = new ArrayList();

        for (int i = 0; i <= listViewArray.length - 1; i++) {
            if (listViewArray[i].isClickable()) {
                randomArray.add(listViewArray[i]);
            }
        }
        if (randomArray.size() != 0) {
            Random r = new Random();
            int rand = r.nextInt(randomArray.size());
            randomImageView = randomArray.get(rand);
        }
    }
}
