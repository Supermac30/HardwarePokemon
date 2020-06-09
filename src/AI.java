public class AI {
    Pokemon player;
    int type;
    /*
    0 - Plays random moves
    1 - Plays move with the largest stamina it can afford
    2 - Plays greedily according to damage
    3 - Plays the highest stun move until the opponent is stunned, then plays the highest damage move twice.
     */
    public AI(Pokemon pokemon, int t) {
        player = pokemon;
        type = t;
    }

    public int[] action() {
        // Returns the damage and whether or not the player is now stunned. The pokemon's stamina is decreased
        switch(type) {
            case 0:
                return random();
            case 1:
                return wealthy();
            case 2:
                return greedy();
            case 3:
                return smart();
        }
        return null;
    }

    private int[] random() {
        return null;
    }

    private int[] wealthy() {
        return null;
    }

    private int[] greedy() {
        return null;
    }

    private int[] smart() {
        return null;
    }
}
