import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Questions extends JFrame implements KeyListener {
    // Creates a question for the user to answer from Questions.txt and returns
    // whether or not they answered correctly
    int numberOfQuestions = 10; // Important - This should be updated when new questions are added to Questions.txt
    int questionNum = (int)(Math.random() * numberOfQuestions);
    JLabel question = new JLabel();
    JPanel answerPanel = new JPanel();
    String[] data;
    JLabel[] answers;
    int correct;
    int pointer = 0;
    public boolean right;
    Battle battle;
    double amount;

    public Questions(Battle source, double value) {
        battle = source;
        amount = value;

        setTitle("ANSWER THIS!");
        // Takes a question from Questions.txt
        Scanner scanner = new Scanner(new InputStreamReader(getClass().getResourceAsStream("Questions.txt")));
        for (int i = 0; i < questionNum; i++) {
            scanner.nextLine();
        }
        data = scanner.nextLine().split("  ");
        scanner.close();

        answers = new JLabel[data.length-2];
        question.setText(data[0]);
        correct = Integer.parseInt(data[data.length-1]);
        for (int i = 0; i < answers.length; i++) {
            answers[i] = new JLabel(data[i+1]);
            answers[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            answers[i].setBorder(new EmptyBorder(10,10,10,10));
            answers[i].setOpaque(true);
            answers[i].setBackground(null);
            answerPanel.add(answers[i]);
        }
        answers[0].setBackground(Color.YELLOW);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 10, 10);

        question.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        question.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(question, c);

        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
        answerPanel.setAlignmentX(CENTER_ALIGNMENT);
        c.gridy = 1;
        add(answerPanel, c);

        addKeyListener(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void updateMenu() {
        // Changes which possible answer is currently highlighted
        for (int i = 0; i < answers.length; i++) {
            answers[i].setBackground(null);
        }
        answers[pointer].setBackground(Color.YELLOW);
    }

    public void answer(double choice) {
        // Calls the attackComplete function from the battle object it was called
        // from returning whether or not the answer was correct
        right = choice == correct;
        battle.attackComplete(amount);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                answer(pointer);
                break;
            case KeyEvent.VK_UP:
                pointer = Math.max(pointer-1, 0);
                break;
            case KeyEvent.VK_DOWN:
                pointer = Math.min(pointer+1, answers.length-1);
                break;
        }
        updateMenu();
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }
}
