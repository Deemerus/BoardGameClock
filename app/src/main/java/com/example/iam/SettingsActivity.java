package com.example.iam;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iam.classes.Player;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private long DEFAULT_TIMEBANK = 10;
    private long DEFAULT_TIME_PER_MOVE = 30;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void onSubmit(View view){
        long timeBank;
        long timePerMove;
        try{
            timeBank = Long.valueOf(((EditText)findViewById(R.id.timeBank)).getText().toString());
        } catch (NumberFormatException e) {
            timeBank = DEFAULT_TIMEBANK;
        }

        try{
            timePerMove = Long.valueOf(((EditText)findViewById(R.id.timePerMove)).getText().toString());
        } catch (NumberFormatException e) {
            timePerMove = DEFAULT_TIME_PER_MOVE;
        }


        Player.createPlayers();
        Player.setTimeBanks(timeBank*60);
        Player.timeForMove = timePerMove;

        boolean player1 = ((CheckBox)findViewById(R.id.checkBox1)).isChecked();
        boolean player2 = ((CheckBox)findViewById(R.id.checkBox2)).isChecked();
        boolean player3 = ((CheckBox)findViewById(R.id.checkBox3)).isChecked();
        boolean player4 = ((CheckBox)findViewById(R.id.checkBox4)).isChecked();
        boolean player5 = ((CheckBox)findViewById(R.id.checkBox5)).isChecked();
        boolean player6 = ((CheckBox)findViewById(R.id.checkBox6)).isChecked();

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
