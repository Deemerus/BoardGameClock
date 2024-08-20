package pl.boardgameclock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import pl.boardgameclock.classes.BoardState;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    private long timeForMove;
    private BoardState boardState;

    ArrayList<TextView> timeBankDisplays;
    ArrayList<TextView> timeBankDisplayBackgrounds;

    private TextView timeForMoveDisplay;

    private CountDownTimer cTimer;

    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(getSupportActionBar()!=null){
            this.getSupportActionBar().hide();
        }
        boardState = BoardState.getInstance();
        boardState.initializePlayers();

        isPaused = true;

        timeBankDisplays = new ArrayList<>();
        timeBankDisplays.add(findViewById(R.id.timeBank1));
        timeBankDisplays.add(findViewById(R.id.timeBank2));
        timeBankDisplays.add(findViewById(R.id.timeBank3));
        timeBankDisplays.add(findViewById(R.id.timeBank4));
        timeBankDisplays.add(findViewById(R.id.timeBank5));
        timeBankDisplays.add(findViewById(R.id.timeBank6));

        timeBankDisplayBackgrounds = new ArrayList<>();
        timeBankDisplayBackgrounds.add(findViewById(R.id.timeBankBackground1));
        timeBankDisplayBackgrounds.add(findViewById(R.id.timeBankBackground2));
        timeBankDisplayBackgrounds.add(findViewById(R.id.timeBankBackground3));
        timeBankDisplayBackgrounds.add(findViewById(R.id.timeBankBackground4));
        timeBankDisplayBackgrounds.add(findViewById(R.id.timeBankBackground5));
        timeBankDisplayBackgrounds.add(findViewById(R.id.timeBankBackground6));
        //add

        timeForMove = boardState.getTimeForMove();
        timeForMoveDisplay = findViewById(R.id.nextPlayer);
        timeForMoveDisplay.setText(String.valueOf(timeForMove));

        createCountDownTimer();
        cTimer.start();
    }

    @Override
    protected void onDestroy() {
        if(cTimer != null){
            cTimer.cancel();
        }
        super.onDestroy();
    }

    void createCountDownTimer() {
        if(cTimer != null){
            cTimer.cancel();
        }

        cTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            public void onTick(long millisUntilFinished) {
                if(!isPaused){
                    if(timeForMove>0){
                        timeForMove--;
                    } else {
                        boardState.dropSecondActivePlayer();
                    }
                }
                refreshTextViews();
            }

            public void onFinish() {
            }
        };
    }

    public void refreshTextViews() {
        timeForMoveDisplay.setText(String.valueOf(timeForMove));
        for (int i = 0; i < 6; i++) {
            refreshTextView(i);
        }
        if(boardState.isCustomColors()){
            int playerColor = boardState.getActivePlayer().getColor();
            timeForMoveDisplay.setBackgroundColor(playerColor);
            int textColor = getColorFromContext(R.color.lightBgTextColor);
            boolean isBackgroundDark = IntStream.of(getResources().getIntArray(R.array.playerColors_dark))
                    .anyMatch(x -> x == playerColor);
            if (isBackgroundDark) {
                textColor = getColorFromContext(R.color.darkBgTextColor);
            }
            timeForMoveDisplay.setTextColor(textColor);
        }
    }

    private void refreshTextView(int playerId) {
        TextView timeBankDisplay = timeBankDisplays.get(playerId);
        TextView timeBankBackgroundDisplay = timeBankDisplayBackgrounds.get(playerId);
        timeBankDisplay.setText(boardState.getPlayer(playerId).getTimeBankStr());

        int textColor;
        int bgColor;
        if(boardState.isCustomColors()){
            if (!boardState.getPlayer(playerId).isInPlay()) {
                return;
            }
            int playerColor = boardState.getPlayer(playerId).getColor();
            bgColor = playerColor;
            textColor = getColorFromContext(R.color.lightBgTextColor);
            boolean isBackgroundDark = IntStream.of(getResources().getIntArray(R.array.playerColors_dark))
                    .anyMatch(x -> x == playerColor);
            if (isBackgroundDark) {
                textColor = getColorFromContext(R.color.darkBgTextColor);
            }
            if (boardState.getPlayer(playerId).isPassed()) {
                bgColor = getColorFromContext(R.color.passed_player);
            }
        } else {
            // Default color functionality
            if (boardState.getActivePlayerId() == playerId) {
                textColor = getColorFromContext(R.color.active_player_text);
                bgColor = getColorFromContext(R.color.active_player);
            } else if (boardState.getPlayer(playerId).isPassed()) {
                textColor = getColorFromContext(R.color.passed_player_text);
                bgColor = getColorFromContext(R.color.passed_player);
            } else {
                textColor = getColorFromContext(R.color.idle_player_text);
                bgColor = getColorFromContext(R.color.idle_player);
            }
        }
        timeBankDisplay.setTextColor(textColor);
        timeBankBackgroundDisplay.setBackgroundColor(bgColor);

    }

    private int getColorFromContext(int colorId) {
        return ContextCompat.getColor(this, colorId);
    }





    public void nextPlayer(View view) {
        isPaused = !boardState.nextPlayer();
        if(isPaused) {
            boardState.resetPassed();
        }
        resetTimeForMove();
        refreshTextViews();
    }

    private void resetTimeForMove() {
        timeForMove = boardState.getTimeForMove();
    }

    public void changePause(View view) {
        isPaused = !isPaused;
    }

    public void pass(View view) {
        boardState.passActivePlayer();
        nextPlayer(view);
    }

    public void restart(View view) {
        Context context = this;
        new AlertDialog.Builder(this)
                .setMessage(R.string.restartConfirmation)
                .setPositiveButton(R.string.restartConfirmationYes, (dialogInterface, i) -> {
                    if(cTimer != null){
                        cTimer.cancel();
                    }
                    boardState.reset();
                    startActivity(new Intent(context, SettingsActivity.class));
                })
                .setNegativeButton(R.string.restartConfirmationNo, null).show();
    }

}