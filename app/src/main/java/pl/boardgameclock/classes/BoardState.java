package pl.boardgameclock.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardState {
    private static final BoardState instance = new BoardState();
    private List<Player> players;
    private int activePlayer;
    private long timeForMove;

    public int getActivePlayer() {
        return activePlayer;
    }

    public long getTimeForMove() {
        return timeForMove;
    }

    public void setTimeForMove(long timeForMove) {
        this.timeForMove = timeForMove;
    }

    public static BoardState getInstance() {
        return instance;
    }

    public int getNumberOfPlayers() {
        int noOfPlayers = 0;
        for(Player player : players){
            if(player.isInPlay()){
                noOfPlayers++;
            }
        }
        return noOfPlayers;
    }


    public void initializePlayers() {
        if(players == null){
            players = new LinkedList<>();
            players.add(new Player());
            players.add(new Player());
            players.add(new Player());
            players.add(new Player());
            players.add(new Player());
            players.add(new Player());
        }
    }

    public Player getPlayer(int id){
        return players.get(id);
    }

    public void dropSecondActivePlayer() {
        players.get(activePlayer).dropSecond();
    }

    public void passActivePlayer() {
        players.get(activePlayer).setPassed(true);
    }

    public boolean nextPlayer(){
        for(int i=0;i<6;i++){
            activePlayer++;
            if(activePlayer>5){
                activePlayer=0;
            }
            if(players.get(activePlayer).isInPlay() && !players.get(activePlayer).isPassed()){
                return true;
            }
        }
        return false;
    }

    public void resetPassed() {
        for(int i=0;i<6;i++){
            players.get(i).setPassed(false);
        }
    }

    public void initiateFirstActivePlayer() {
        for(int i = 0; i<6;i++) {
            if(players.get(i).isInPlay()) {
                activePlayer = i;
                return;
            }
        }
    }

    public void reset() {
        players = new ArrayList<>();
        activePlayer = 0;
        timeForMove = 0;
    }

    public void setTimeBankForAllPlayers(long timeBank) {
        for(Player player : players) {
            player.setTimeBank(timeBank);
        }
    }

}
