import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    JLabel playerInfo = new JLabel();
    JLabel enemyInfo = new JLabel();
    JLabel playerSprite = new JLabel();
    JLabel enemySprite = new JLabel();

    JPanel playerSide = new JPanel();
    JPanel enemySide = new JPanel();
    JPanel field = new JPanel();

    int choice;

    int pointer = 0;
    int currentMenu = 0;
    boolean wait = false;
    Questions question;
    boolean addedMessage = false; // If true "You don't have enough stamina" is displayed
    JLabel staminaMessage = new JLabel("You don't have enough stamina");

    Pokemon player;
    Pokemon enemy;
    Player trainer;
    AI enemyAI;

    boolean turn;

    public Battle(Player trainerStart, Pokemon playerStart, Pokemon enemyStart, AI ai) {
        player = playerStart;
        enemy = enemyStart;
        enemyAI = ai;
        trainer = trainerStart;
        enemyAI.player = enemy;

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
        GridBagConstraints c0 = new GridBagConstraints();
        c0.gridx = 0;
        c0.gridy = 0;
        c0.fill = GridBagConstraints.BOTH;
        c0.weightx = 1.0;
        c0.weighty = 1.0;
        add(Box.createGlue(), c0);
        c0.gridx = 0;
        c0.gridy = 2;
        c0.fill = GridBagConstraints.HORIZONTAL;
        c0.weightx = 0.0;
        c0.weighty = 0.0;

        description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        description.add(rootMenu[0][1]);
        add(description, c0);
        c0.gridy = 3;
        add(choices, c0);
        addKeyListener(this);

        Icon imgIcon = new ImageIcon(player.sprite);
        playerSprite.setIcon(imgIcon);
        playerSprite.setBorder(new EmptyBorder(0, 85, 0, 0));

        imgIcon = new ImageIcon(enemy.sprite);
        enemySprite.setIcon(imgIcon);
        enemySprite.setBorder(new EmptyBorder(0, 85, 0, 0));

        playerInfo.setText("HEALTH: " + player.health + "    STAMINA: " + player.stamina);
        enemyInfo.setText("HEALTH: " + enemy.health + "    STAMINA: " + enemy.stamina);
        playerInfo.setFont(new Font("Courier", Font.BOLD, 20));
        enemyInfo.setFont(new Font("Courier", Font.BOLD, 20));

        playerSide.setLayout(new BorderLayout());
        enemySide.setLayout(new BorderLayout());
        enemySide.add(enemySprite, BorderLayout.CENTER);
        playerSide.add(playerSprite, BorderLayout.CENTER);
        playerSide.add(playerInfo, BorderLayout.SOUTH);
        enemySide.add(enemyInfo, BorderLayout.SOUTH);

        playerSide.setBorder(new EmptyBorder(10, 50, 10, 50));
        enemySide.setBorder(new EmptyBorder(10, 50, 10, 50));

        field.setLayout(new BorderLayout());
        field.add(playerSide, BorderLayout.WEST);
        field.add(enemySide, BorderLayout.EAST);

        c0.gridx = 0;
        c0.gridy = 1;
        c0.fill = GridBagConstraints.HORIZONTAL;
        c0.weightx = 0.0;
        c0.weighty = 0.0;

        add(field, c0);

        setSize(800, 400);
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

    void updatePanel() {
        playerInfo.setText("HEALTH: " + player.health + "    STAMINA: " + player.stamina);
        enemyInfo.setText("HEALTH: " + enemy.health + "    STAMINA: " + enemy.stamina);
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

    public void attackStart(int picks) {
        choice = picks;
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
        boolean isStunned = false;
        if (question.right) {
            if (enemy.attacked(amount)) {
                // You win
            }
            String message = "You answered correctly and attacked dealing " + amount + " damage";
            if (player.isStunned(player.attackStats[choice][2])) {
                isStunned = true;
                message += " and stunning your opponent!";
            }
            addMessage(message, false);
        } else {
            addMessage("You answered incorrectly", false);
        }
        question = null;
        wait = false;
        updatePanel();
        if (!isStunned) {
            AITurn();
            turn = !turn;
        }
    }

    public void defend() {
        player.defend();
        addMessage("You defended gaining " + player.defendIncrease + " health and stamina!", false);
        updatePanel();
        turn = !turn;
        AITurn();
    }

    public void AITurn() {
        ActionListener AITurn = evt -> {
            if (turn) {
                AIPlay();
                turn = !turn;
            }
        };
        Timer timer = new Timer(800 ,AITurn);
        timer.setRepeats(false);
        timer.start();
    }

    public void AIPlay() {
        double[] action = enemyAI.action();
        if (action[0] == -1 && action[1] == -1) {
            addMessage("Enemy Defends gaining " + enemy.defendIncrease + " health and stamina!", false);
        }
        else if (player.attacked(action[0])) {
            updatePanel();
            addMessage("You Lose!", false);
            // You lose
        } else {
            updatePanel();
            String message = "Enemy attacked dealing " + action[0] + " damage";
            if (action[1] == 1) {
                message += " and stunning you!";
                addMessage(message, false);
                AITurn();
            } else {
                addMessage(message, false);
            }
        }
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
                        if (pointer == 1) {
                            defend();
                        }
                        goToMenu = pointer + 1;
                        break;
                    case 1:
                        attackStart(pointer);
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
