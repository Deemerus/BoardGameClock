package pl.boardgameclock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import pl.boardgameclock.classes.BoardState;

public class SettingsActivity extends AppCompatActivity {
    private BoardState boardState;
    private List<Button> colorButtons;
    private List<CheckBox> isInPlayCheckBoxes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(getSupportActionBar()!=null){
            this.getSupportActionBar().hide();
        }
        boardState = BoardState.getInstance();
        boardState.initializePlayers();

        isInPlayCheckBoxes = new LinkedList<>();
        isInPlayCheckBoxes.add(findViewById(R.id.checkBox1));
        isInPlayCheckBoxes.add(findViewById(R.id.checkBox2));
        isInPlayCheckBoxes.add(findViewById(R.id.checkBox3));
        isInPlayCheckBoxes.add(findViewById(R.id.checkBox4));
        isInPlayCheckBoxes.add(findViewById(R.id.checkBox5));
        isInPlayCheckBoxes.add(findViewById(R.id.checkBox6));

        colorButtons = new LinkedList<>();
        colorButtons.add(findViewById(R.id.color1));
        colorButtons.add(findViewById(R.id.color2));
        colorButtons.add(findViewById(R.id.color3));
        colorButtons.add(findViewById(R.id.color4));
        colorButtons.add(findViewById(R.id.color5));
        colorButtons.add(findViewById(R.id.color6));
        setColorButtonsVisibility();
        setButtonColors();
    }

    private void setButtonColors() {
        for(int playerId=0; playerId<=5; playerId++) {
            setButtonColor(playerId);
        }
    }

    private void setButtonColor(int playerId) {
        colorButtons.get(playerId).setBackgroundColor(boardState.getPlayer(playerId).getColor());
        setButtonTextColor(playerId);
    }

    private void setButtonTextColor(int playerId) {
        int textColor = Color.BLACK;
        boolean isBackgroundDark = IntStream.of(getResources().getIntArray(R.array.playerColors_dark))
                .anyMatch(x -> x == boardState.getPlayer(playerId).getColor());
            if(isBackgroundDark) {
                textColor = Color.WHITE;
        }
        colorButtons.get(playerId).setTextColor(textColor);
    }

    private void setColorButtonsVisibility(){
        for(int playerId=0; playerId<=5; playerId++) {
            setColorButtonVisibility(playerId, boardState.isCustomColors());
        }
    }

    private void setColorButtonVisibility(int playerId, boolean visible) {
        int visibility = View.INVISIBLE;
        if(visible && boardState.getPlayer(playerId).isInPlay()){
            visibility = View.VISIBLE;
        }
        colorButtons.get(playerId).setVisibility(visibility);
    }

    private void setPlayersInPlay() {
        for(int playerId=0; playerId<=5; playerId++) {
            boardState.getPlayer(playerId).setInPlay(isInPlayCheckBoxes.get(playerId).isChecked());
        }
    }

    public void onSubmit(View view){
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

    public void onChangeCustomColors(View view) {
        boolean customColorsSetting = ((CheckBox)findViewById(R.id.customColors)).isChecked();
        boardState.setCustomColors(customColorsSetting);
        setColorButtonsVisibility();
    }

    public void onChangePlayerActive(View view) {
        setPlayersInPlay();
        setColorButtonsVisibility();
    }

    public void onSelectColoPlayer1(View view) {
        onSelectColor(view, 0);
    }

    public void onSelectColoPlayer2(View view) {
        onSelectColor(view, 1);
    }

    public void onSelectColoPlayer3(View view) {
        onSelectColor(view, 2);
    }

    public void onSelectColoPlayer4(View view) {
        onSelectColor(view, 3);
    }

    public void onSelectColoPlayer5(View view) {
        onSelectColor(view, 4);
    }

    public void onSelectColoPlayer6(View view) {
        onSelectColor(view, 5);
    }

    private void onSelectColor(View view, int playerID) {

        new MaterialColorPickerDialog
                .Builder(this)
                .setTitle("Pick Color")
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._300)
                .setColorRes(getResources().getIntArray(R.array.playerColors))
                .setTickColorPerCard(true)
                .setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NonNull String colorHex) {
                        boardState.getPlayer(playerID).setColor(color);
                        setButtonColor(playerID);
                    }
                })
                .show();
    }
}
