import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Battle extends JFrame implements KeyListener {
    // Creates the battles for the player
    JLabel[][] rootMenu = {
            {new JLabel("ATTACK"), new JLabel("Perform an attack that damages or stuns your opponent")},
            {new JLabel("DEFEND"), new JLabel("Opt not to attack this turn to regain your stamina and health")},
            {new JLabel("RETREAT"), new JLabel("Run away, and live to fight another day")},
            {new JLabel("ITEM"), new JLabel("Use an item")}
    };
    JLabel[][] attackMenu;
    JLabel[][] itemsMenu = new JLabel[3][2];
    JLabel[][][] menus; // Stores the menus the user can navigate through in battle
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
    GameMenu source;
    int location;

    public Battle(Player trainerStart, Pokemon playerStart, Pokemon enemyStart, AI ai, GameMenu gameMenu, int locationStart) {
        player = playerStart;
        enemy = enemyStart;
        enemyAI = ai;
        trainer = trainerStart;
        enemyAI.player = enemy;
        source = gameMenu;
        location = locationStart;

        staminaMessage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        // Adds the attacks of the pokemon to the attackMenu
        attackMenu = new JLabel[player.attacks.length][2];
        for (int i = 0; i < attackMenu.length; i++) {
            attackMenu[i][0] = new JLabel(player.attacks[i]);
            double[][] stats = player.attackStats;
            attackMenu[i][1] = new JLabel("Damage: " + stats[i][0] + "  Stun Chance: " + (stats[i][2]*100) + "%  Stamina Cost: " + stats[i][1]);
        }

        // Adds the player's items to the itemMenu
        for (int i = 0; i < itemsMenu.length; i++) {
            itemsMenu[i][0] = new JLabel(trainer.items[i].name + " x" + trainer.items[i].amount);
            itemsMenu[i][1] = new JLabel(trainer.items[i].description);
        }

        menus = new JLabel[][][] {rootMenu, attackMenu, null, null, itemsMenu};
        setTitle("BATTLE!");
        for (JLabel[] option: rootMenu) {
            option[0].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
            option[0].setBorder(new EmptyBorder(10,10,10,10));
            option[0].setOpaque(true);
            option[0].setBackground(null);

            option[1].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
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

        Icon imgIcon = new ImageIcon(getClass().getResource(player.sprite));
        playerSprite.setIcon(imgIcon);
        playerSprite.setBorder(new EmptyBorder(0, 85, 0, 0));

        imgIcon = new ImageIcon(getClass().getResource(enemy.sprite));
        enemySprite.setIcon(imgIcon);
        enemySprite.setBorder(new EmptyBorder(0, 85, 0, 0));

        playerInfo.setText("HEALTH: " + player.health + "    STAMINA: " + player.stamina);
        enemyInfo.setText("HEALTH: " + enemy.health + "    STAMINA: " + enemy.stamina);
        playerInfo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        enemyInfo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

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

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void updateMenu(int goToMenu) {
        // Changes the layer the menu is on and updates which button currently has a yellow background
        if (menus[goToMenu] == null) {
            return;
        }
        JLabel[][] menu = menus[goToMenu];
        if (goToMenu != currentMenu) {
            choices.removeAll();
            for (JLabel[] option : menu) {
                option[0].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
                option[0].setBorder(new EmptyBorder(10,10,10,10));
                option[0].setOpaque(true);
                option[0].setBackground(null);
                option[1].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
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
        // Updates the health and stamina of the player and enemy
        playerInfo.setText("HEALTH: " + player.health + "    STAMINA: " + player.stamina);
        enemyInfo.setText("HEALTH: " + enemy.health + "    STAMINA: " + enemy.stamina);
    }

    public void addMessage(String message, boolean stopReplacement) {
        // Adds a message to the description
        description.removeAll();
        JLabel mess = new JLabel(message);
        mess.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        description.add(mess);
        addedMessage = stopReplacement;
        validate();
        repaint();
    }

    public void attackStart(int picks) {
        // Creates a question for the user to answer
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
        // Called by the question class, attacks if the player answered correctly
        question.setVisible(false);
        boolean isStunned = false;
        if (question.right) {
            if (enemy.attacked(amount)) {
                String message = "You win gaining " + (location+1) * 10 + " money";
                if (location == trainer.level) {
                    message += " and unlocking the next stage!";
                }
                addMessage( message, true);
                source.endBattle(true);
                return;
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
        }
    }

    public void defend() {
        // Adds health and stamina to the pokemon and updates the screen accordingly
        player.defend();
        addMessage("You defended gaining " + player.defendIncrease + " health and stamina!", false);
        updatePanel();
        AITurn();
    }

    public void retreat() {
        // Ends the battle early
        addMessage("You ran away!", true);
        source.endBattle(false);
    }

    public void updateItems() {
        // Updates the number of items left in the itemMenu
        for (int i = 0; i < itemsMenu.length; i++) {
            itemsMenu[i][0].setText(trainer.items[i].name + " x" + trainer.items[i].amount);
        }
        revalidate();
        repaint();
    }

    public void useItem(int index) {
        // Handles an item being used
        if (trainer.items[index].amount == 0) {
            addMessage("You don't have any of this item", true);
            return;
        }
        // Handles the pokeball being used
        // The player can only catch pokemon that are in the jungle
        if (index == 0) {
            if (location == 1) {
                if (enemy.health <= 30) {
                    String message = "You captured the PokeHardWareMon, gaining " + (location+1) * 10 + " money";
                    if (location == trainer.level) {
                        message += " and unlocking the next stage!";
                    }
                    addMessage(message, true);
                    trainer.items[index].use(trainer, player, enemy);
                    source.endBattle(true);
                } else {
                    addMessage("You need to get the pokemon to 30HP or under", true);
                }
            } else {
                addMessage("You can't capture PokeHardWareMon outside the jungle", true);
            }
        } else {
            trainer.items[index].use(trainer, player, enemy);
            updatePanel();
            updateItems();
        }
    }

    public void AITurn() {
        // Waits a while to make play more organic, then calls AIPlay
        ActionListener AITurn = evt -> {
            AIPlay();
        };
        Timer timer = new Timer(800 ,AITurn);
        timer.setRepeats(false);
        timer.start();
    }

    public void AIPlay() {
        // Handles the AI's moves
        double[] action = enemyAI.action();
        if (action[0] == -1 && action[1] == -1) {
            updatePanel();
            addMessage("Enemy Defends gaining " + enemy.defendIncrease + " health and stamina!", false);
        }
        else if (player.attacked(action[0])) {
            updatePanel();
            addMessage("You Lose!", true);
            source.endBattle(false);
        } else {
            updatePanel();
            String message = "Enemy attacked dealing " + action[0] + " damage";
            if (action[1] == 1) {
                message += " and stunning you!";
                addMessage(message, false);
                enemyAI.isEnemyStunned = true;
                AITurn();
            } else {
                addMessage(message, false);
                enemyAI.isEnemyStunned = false;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Calls the appropriate functions when a button is pressed
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
                        } else if (pointer == 2) {
                            retreat();
                        }
                        goToMenu = pointer + 1;
                        break;
                    case 1:
                        attackStart(pointer);
                        break;
                    case 4:
                        useItem(pointer);
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
