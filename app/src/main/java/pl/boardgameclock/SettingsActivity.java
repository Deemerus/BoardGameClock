package pl.boardgameclock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pl.boardgameclock.classes.Player;

public class SettingsActivity extends AppCompatActivity {

    private long DEFAULT_TIMEBANK = 10;
    private long DEFAULT_TIME_PER_MOVE = 30;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(getSupportActionBar()!=null){
            this.getSupportActionBar().hide();
        }

    }

    public void onSubmit(View view){
        boolean player1 = ((CheckBox)findViewById(R.id.checkBox1)).isChecked();
        boolean player2 = ((CheckBox)findViewById(R.id.checkBox2)).isChecked();
        boolean player3 = ((CheckBox)findViewById(R.id.checkBox3)).isChecked();
        boolean player4 = ((CheckBox)findViewById(R.id.checkBox4)).isChecked();
        boolean player5 = ((CheckBox)findViewById(R.id.checkBox5)).isChecked();
        boolean player6 = ((CheckBox)findViewById(R.id.checkBox6)).isChecked();
        int playerCount = 0;
        if(player1) playerCount++;
        if(player2) playerCount++;
        if(player3) playerCount++;
        if(player4) playerCount++;
        if(player5) playerCount++;
        if(player6) playerCount++;

        if(playerCount < 2) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.notEnoughPlayersError, Toast.LENGTH_LONG);
            toast.show();
        } else {
            long timeBank;
            long timePerMove;
            try{
                timeBank = Long.parseLong(((EditText)findViewById(R.id.timeBank)).getText().toString());
            } catch (NumberFormatException e) {
                timeBank = DEFAULT_TIMEBANK;
            }

            try{
                timePerMove = Long.parseLong(((EditText)findViewById(R.id.timePerMove)).getText().toString());
            } catch (NumberFormatException e) {
                timePerMove = DEFAULT_TIME_PER_MOVE;
            }


            Player.createPlayers();
            Player.setTimeBanks(timeBank*60);
            Player.timeForMove = timePerMove;



            Player.getPlayer(0).inPlay = player1;
            Player.getPlayer(1).inPlay = player2;
            Player.getPlayer(2).inPlay = player3;
            Player.getPlayer(3).inPlay = player4;
            Player.getPlayer(4).inPlay = player5;
            Player.getPlayer(5).inPlay = player6;

            Player.initiateFirstActivePlayer();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
