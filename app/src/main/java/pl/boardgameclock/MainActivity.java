package pl.boardgameclock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import pl.boardgameclock.classes.Player;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private long timeForMove;

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

        timeForMove = Player.timeForMove;
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
                        Player.dropSecondActivePlayer();
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
        for(int i = 0; i<6; i++){
            TextView timeBankDisplay = timeBankDisplays.get(i);
            TextView timeBankBackgroundDisplay = timeBankDisplayBackgrounds.get(i);
            timeBankDisplay.setText(Player.getPlayer(i).getTimeBankStr());
            if(Player.activePlayer == i) {
                timeBankDisplay.setTextColor(getResources().getColor(R.color.active_player_text));
                timeBankBackgroundDisplay.setBackgroundResource(R.color.active_player);
            } else if(Player.getPlayer(i).passed) {
                timeBankDisplay.setTextColor(getResources().getColor(R.color.passed_player_text));
                timeBankBackgroundDisplay.setBackgroundResource(R.color.passed_player);
            } else {
                timeBankDisplay.setTextColor(getResources().getColor(R.color.idle_player_text));
                timeBankBackgroundDisplay.setBackgroundResource(R.color.idle_player);
            }
        }


    }


    public void nextPlayer(View view) {
        isPaused = !Player.nextPlayer();
        if(isPaused) {
            Player.resetPassed();
        }
        resetTimeForMove();
        refreshTextViews();
    }

    private void resetTimeForMove() {
        timeForMove = Player.timeForMove;
    }

    public void changePause(View view) {
        isPaused = !isPaused;
    }

    public void pass(View view) {
        Player.passActivePlayer();
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
                    Player.reset();
                    startActivity(new Intent(context, SettingsActivity.class));
                })
                .setNegativeButton(R.string.restartConfirmationNo, null).show();
    }

}