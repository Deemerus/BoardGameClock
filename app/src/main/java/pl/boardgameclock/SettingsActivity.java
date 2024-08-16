package pl.boardgameclock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pl.boardgameclock.classes.BoardState;

public class SettingsActivity extends AppCompatActivity {
    private BoardState boardState;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(getSupportActionBar()!=null){
            this.getSupportActionBar().hide();
        }
        boardState = BoardState.getInstance();
        boardState.initializePlayers();
    }

    public void onSubmit(View view){

        boardState.getPlayer(0).setInPlay(((CheckBox)findViewById(R.id.checkBox1)).isChecked());
        boardState.getPlayer(1).setInPlay(((CheckBox)findViewById(R.id.checkBox2)).isChecked());
        boardState.getPlayer(2).setInPlay(((CheckBox)findViewById(R.id.checkBox3)).isChecked());
        boardState.getPlayer(3).setInPlay(((CheckBox)findViewById(R.id.checkBox4)).isChecked());
        boardState.getPlayer(4).setInPlay(((CheckBox)findViewById(R.id.checkBox5)).isChecked());
        boardState.getPlayer(5).setInPlay(((CheckBox)findViewById(R.id.checkBox6)).isChecked());

        if(boardState.getNumberOfPlayers() < 2) {
            Toast toast = Toast.makeText(this, R.string.notEnoughPlayersError, Toast.LENGTH_LONG);
            toast.show();
        } else {
            long timeBank;
            long timePerMove;
            try{
                timeBank = Long.parseLong(((EditText)findViewById(R.id.timeBank)).getText().toString());
            } catch (NumberFormatException e) {
                timeBank = 10; //Default Value
            }

            try{
                timePerMove = Long.parseLong(((EditText)findViewById(R.id.timePerMove)).getText().toString());
            } catch (NumberFormatException e) {
                timePerMove = 30;  //Default Value
            }

            boardState.setTimeBankForAllPlayers(timeBank*60);
            boardState.setTimeForMove(timePerMove);
            boardState.initiateFirstActivePlayer();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
