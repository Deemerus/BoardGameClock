package pl.boardgameclock.classes;

import java.util.ArrayList;

public class Player {

    public static ArrayList<Player> players = new ArrayList<>();
    public static int activePlayer = 0;
    public static long timeForMove;

    public long timeBank;
    public boolean passed = false;
    public boolean inPlay = true;

    public Player () {
    }

    public static void createPlayers() {
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
    }

    public static void setTimeBanks(long timeBank) {
        for(Player player : players) {
            player.timeBank = timeBank;
        }
    }

    public static Player getPlayer(int id){
        return players.get(id);
    }

    public static void dropSecondActivePlayer() {
        players.get(activePlayer).dropSecond();
    }

    public static void passActivePlayer() {
        players.get(activePlayer).passed = true;
    }

    public static boolean nextPlayer(){
        for(int i=0;i<6;i++){
            activePlayer++;
            if(activePlayer>5){
                activePlayer=0;
            }
            if(players.get(activePlayer).inPlay && !players.get(activePlayer).passed){
                return true;
            }
        }
        return false;
    }

    public static void resetPassed() {
        for(int i=0;i<6;i++){
            players.get(i).passed = false;
        }
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

    public static void initiateFirstActivePlayer() {
        for(int i = 0; i<6;i++) {
            if(players.get(i).inPlay) {
                activePlayer = i;
                return;
            }
        }
    }

}
