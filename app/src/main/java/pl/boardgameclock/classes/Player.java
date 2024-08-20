package pl.boardgameclock.classes;

import android.graphics.Color;

public class Player {
    private long timeBank;
    private boolean passed = false;
    private boolean inPlay = false;
    private int color = Color.WHITE;

    public Player () {}

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }

    public void setTimeBank(long timeBank) {
        this.timeBank = timeBank;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void dropSecond() {
        if(timeBank>0){
            timeBank--;
        }
    }

    public String getTimeBankStr() {
        if(inPlay) {
            return formatTime(timeBank);
        } else {
            return " ";
        }
    }

    private String formatTime(long time) {
        long seconds = time % 60;
        String formatted = time / 60 + ":";
        if (seconds < 10) {
            formatted = formatted + "0";
        }
        return formatted + seconds;
    }

}
