public class Main{
    public static void main(String[] args) {
        StartMenu start = new StartMenu();
    }
    public static void startGame(Pokemon starter) {
        GameMenu game = new GameMenu(new Player(starter));
    }
}