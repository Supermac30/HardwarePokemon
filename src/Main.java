import javax.swing.*;

public class Main{
    public static void main(String[] args) {
        StartMenu start = new StartMenu();
    }
    public static void startGame(Pokemon starter) {
        GameMenu game = new GameMenu(new Player(starter));
    }
}
/*
TODO:
Add a use item action
Add a retreat action
Add a win/lose screen
Add a start battle screen
Add a wallet and money from battles
Add a forest
Add a final gym battle
Add Music */