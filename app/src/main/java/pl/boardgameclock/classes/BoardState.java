package pl.boardgameclock.classes;

import java.util.LinkedList;
import java.util.List;

public class BoardState {
    private static final BoardState instance = new BoardState();
    private List<Player> players;
    private int activePlayerId;
    private long timeForMove;
    private boolean customColors = false;

    public int getActivePlayerId() {
        return activePlayerId;
    }

    public Player getActivePlayer() {
        return players.get(this.activePlayerId);
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

    public boolean isCustomColors() {
        return customColors;
    }

    public void setCustomColors(boolean customColors) {
        this.customColors = customColors;
    }

    public Player getPlayer(int id){
        return players.get(id);
    }

    public void dropSecondActivePlayer() {
        players.get(activePlayerId).dropSecond();
    }

    public void passActivePlayer() {
        players.get(activePlayerId).setPassed(true);
    }

    public boolean nextPlayer(){
        for(int i=0;i<6;i++){
            activePlayerId++;
            if(activePlayerId >5){
                activePlayerId =0;
            }
            if(players.get(activePlayerId).isInPlay() && !players.get(activePlayerId).isPassed()){
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
                activePlayerId = i;
                return;
            }
        }
    }

    public void reset() {
        players = null;
        initializePlayers();
        activePlayerId = 0;
        timeForMove = 0;
        customColors = false;
    }

    public void setTimeBankForAllPlayers(long timeBank) {
        for(Player player : players) {
            player.setTimeBank(timeBank);
        }
    }

}
