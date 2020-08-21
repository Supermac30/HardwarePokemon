import java.util.ArrayList;

public class AI {
    // Stores the functions which decide what the enemy will do
    // Has multiple difficulty levels
    Pokemon player;
    int type;
    /*
    0 - Plays random moves
    1 - Plays move with the largest stamina it can afford
    2 - Plays greedily according to damage
    3 - Plays the highest stun move until the opponent is stunned, then plays the highest damage move
     */
    boolean isEnemyStunned = false;
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

    private ArrayList<Integer> findPoss() {
        ArrayList<Integer> possibilities = new ArrayList();
        for (int i = 0; i < player.attackStats.length; i++) {
            if (player.stamina >= player.attackStats[i][1]) {
                possibilities.add(i);
            }
        }
        return possibilities;
    }

    private double[] random() {
        ArrayList<Integer> possibilities = findPoss();
        if (possibilities.isEmpty()) {
            player.defend();
            return new double[] {-1, -1};
        }
        int pick = (int)(possibilities.size()*Math.random());
        int choose = possibilities.get(pick);
        player.attack(choose);
        return new double[] {player.attackStats[choose][0], player.isStunned(player.attackStats[choose][2]) ? 1:0};
    }

    private double[] wealthy() {
        ArrayList<Integer> possibilities = findPoss();
        if (possibilities.isEmpty()) {
            player.defend();
            return new double[] {-1, -1};
        }
        int largest = possibilities.get(0);
        for (int poss: possibilities) {
            if (player.attackStats[poss][1] > player.attackStats[largest][1]) {
                largest = poss;
            }
        }
        player.attack(largest);
        return new double[] {player.attackStats[largest][0], player.isStunned(player.attackStats[largest][2]) ? 1:0};
    }

    private double[] greedy() {
        ArrayList<Integer> possibilities = findPoss();
        if (possibilities.isEmpty()) {
            player.defend();
            return new double[] {-1, -1};
        }
        int largest = possibilities.get(0);
        for (int poss: possibilities) {
            if (player.attackStats[poss][0] > player.attackStats[largest][0]) {
                largest = poss;
            }
        }
        player.attack(largest);
        return new double[] {player.attackStats[largest][0], player.isStunned(player.attackStats[largest][2]) ? 1:0};
    }

    private double[] smart() {
        ArrayList<Integer> possibilities = findPoss();
        if (possibilities.isEmpty()) {
            player.defend();
            return new double[] {-1, -1};
        }
        if (!isEnemyStunned) {
            // Finds the attack with the greatest stun chance
            int largest = possibilities.get(0);
            for (int poss: possibilities) {
                if (player.attackStats[poss][2] > player.attackStats[largest][2]) {
                    largest = poss;
                }
            }
            player.attack(largest);
            return new double[] {player.attackStats[largest][0], player.isStunned(player.attackStats[largest][2]) ? 1:0};
        }
        // Finds the attack with the greatest damage
        int largest = possibilities.get(0);
        for (int poss: possibilities) {
            if (player.attackStats[poss][0] > player.attackStats[largest][0]) {
                largest = poss;
            }
        }
        player.attack(largest);
        return new double[] {player.attackStats[largest][0], player.isStunned(player.attackStats[largest][2]) ? 1:0};
    }
}
