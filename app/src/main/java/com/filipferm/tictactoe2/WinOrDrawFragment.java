package com.filipferm.tictactoe2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Filip on 2015-04-02.
 *
 * The dialog fragment
 *
 * tells if who won or if there is a draw.
 * gets the win or draw from PlayField class.
 *
 * Recreates the playfield with the last number of players
 * that it gets from PlayField class.
 */
public class WinOrDrawFragment extends DialogFragment {
    String winOrDrawString;
    String players;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder winOrDraw = new AlertDialog.Builder(getActivity());
        winOrDrawString = getArguments().getString("winOrDrawString");
        players = Integer.toString(getArguments().getInt("numberOfPlayers"));

        winOrDraw.setTitle(winOrDrawString);
        setCancelable(false);

        winOrDraw.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent playAgain = new Intent(getActivity(), PlayField.class);
                playAgain.putExtra("players",players);
                getActivity().finish();
                getActivity().startActivity(playAgain);
            }
        });

        winOrDraw.setNegativeButton("Main menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        return winOrDraw.create();
    }
}
