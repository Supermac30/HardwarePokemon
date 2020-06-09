import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Battle extends JFrame implements KeyListener {
    JLabel[][] rootMenu = {
            {new JLabel("ATTACK"), new JLabel("Perform an attack that damages or stuns your opponent")},
            {new JLabel("DEFEND"), new JLabel("Opt not to attack this turn to regain your stamina and health")},
            {new JLabel("RETREAT"), new JLabel("Run away, and live to fight another day")},
            {new JLabel("ITEM"), new JLabel("Use an item")}
    };
    JLabel[][] attackMenu;
    JLabel[][] itemsMenu = {
            {new JLabel("POKEBALL"), new JLabel("Capture the pokemon")}
    };
    JLabel[][][] menus;
    JPanel description = new JPanel();
    JPanel choices = new JPanel();
    int pointer = 0;
    int currentMenu = 0;
    boolean wait = false;
    Questions question;
    boolean addedMessage = false; // If true "You don't have enough stamina" is displayed
    JLabel staminaMessage = new JLabel("You don't have enough stamina");

    Pokemon player;
    Pokemon enemy;

    boolean turn;

    public Battle(Pokemon playerStart, Pokemon enemyStart) {
        player = playerStart;
        enemy = enemyStart;

        staminaMessage.setFont(new Font("Courier", Font.PLAIN, 20));

        attackMenu = new JLabel[player.attacks.length][2];
        for (int i = 0; i < attackMenu.length; i++) {
            attackMenu[i][0] = new JLabel(player.attacks[i]);
            double[][] stats = player.attackStats;
            attackMenu[i][1] = new JLabel("Damage: " + stats[i][0] + "  Stun Chance: " + (stats[i][2]*100) + "%  Stamina Cost: " + stats[i][1]);
        }
        menus = new JLabel[][][] {rootMenu, attackMenu, null, null, itemsMenu};
        setTitle("BATTLE!");
        for (JLabel[] option: rootMenu) {
            option[0].setFont(new Font("Courier", Font.BOLD, 30));
            option[0].setBorder(new EmptyBorder(10,10,10,10));
            option[0].setOpaque(true);
            option[0].setBackground(null);

            option[1].setFont(new Font("Courier", Font.PLAIN, 20));
            option[1].setBorder(new EmptyBorder(10,10,10,10));
            option[1].setOpaque(true);
            option[1].setBackground(null);

            choices.add(option[0]);
        }
        rootMenu[0][0].setBackground(Color.YELLOW);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(Box.createGlue(), c);
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.weighty = 0.0;

        description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        description.add(rootMenu[0][1]);
        add(description, c);
        c.gridy = 3;
        add(choices, c);
        addKeyListener(this);
    }

    public void updateMenu(int goToMenu) {
        if (menus[goToMenu] == null) {
            return;
        }
        JLabel[][] menu = menus[goToMenu];
        if (goToMenu != currentMenu) {
            choices.removeAll();
            for (JLabel[] option : menu) {
                option[0].setFont(new Font("Courier", Font.BOLD, 30));
                option[0].setBorder(new EmptyBorder(10,10,10,10));
                option[0].setOpaque(true);
                option[0].setBackground(null);
                option[1].setFont(new Font("Courier", Font.PLAIN, 20));
                option[1].setBorder(new EmptyBorder(10,10,10,10));
                option[1].setOpaque(true);


                choices.add(option[0]);
            }
            pointer = 0;
        } else {
            for (JLabel[] option : menu) {
                option[0].setBackground(null);
            }
        }
        currentMenu = goToMenu;
        menu[pointer][0].setBackground(Color.YELLOW);
        if (addedMessage) {
            addedMessage = false;
        } else {
            description.removeAll();
            description.add(menu[pointer][1]);
        }
        repaint();
        validate();
    }

    private void addMessage(String message, boolean stopReplacement) {
        description.removeAll();
        JLabel mess = new JLabel(message);
        mess.setFont(new Font("Courier", Font.PLAIN, 20));
        description.add(mess);
        addedMessage = stopReplacement;
        validate();
        repaint();
    }

    public void attackStart(int choice) {
        if (player.attack(choice)) {
            question = new Questions(this, player.attackStats[choice][0]);
            question.setVisible(true);
            question.pack();
            question.setLocationRelativeTo(this);
            wait = true;
        } else {
            addMessage("You don't have enough stamina", true);
        }
    }

    public void attackComplete(double amount) {
        question.setVisible(false);

        if (question.right) {
            if (enemy.attacked(amount)) {
                // You win
            }
            addMessage("You answered correctly and attacked dealing " + amount + " damage!", false);
        } else {
            addMessage("You answered incorrectly", false);
        }
        question = null;
        wait = false;
        turn = !turn;
    }

    public void defend() {
        player.defend();
        turn = !turn;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int goToMenu = currentMenu;
        if (wait) {
            return;
        }
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                switch (currentMenu) {
                    case 0:
                        goToMenu = pointer + 1;
                        break;
                    case 1:
                        attackStart(pointer);
                        break;
                    case 2:
                        defend();
                        break;
                }
                break;
            case KeyEvent.VK_BACK_SPACE:
                if (currentMenu != 0)
                    goToMenu = 0;
                break;
            case KeyEvent.VK_LEFT:
                pointer = Math.max(pointer-1, 0);
                break;
            case KeyEvent.VK_RIGHT :
                pointer = Math.min(pointer+1, menus[currentMenu].length-1);
                break;
        }
        updateMenu(goToMenu);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
