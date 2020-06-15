import java.util.ArrayList;

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

    public double[] action() {
        // Returns the damage and whether or not the player is now stunned.
        // The pokemon's stamina is decreased
        // Returns [-1, -1] if the player is defending
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

    private double[] random() {
        ArrayList<Integer> possibilities = new ArrayList();
        for (int i = 0; i < player.attackStats.length; i++) {
            if (player.stamina >= player.attackStats[i][1]) {
                possibilities.add(i);
            }
        }
        if (possibilities.isEmpty()) {
            player.defend();
            return new double[] {-1, -1};
        }
        int pick = (int)(possibilities.size()*Math.random());
        player.attack(possibilities.get(pick));
        return new double[] {player.attackStats[possibilities.get(pick)][0], player.isStunned(player.attackStats[possibilities.get(pick)][2]) ? 1:0};
    }

    private double[] wealthy() {
        return null;
    }

    private double[] greedy() {
        return null;
    }

    private double[] smart() {
        return null;
    }
}
