import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class Layer {
    JLabel title;
    JLabel[] buttons;
    JLabel text = null;
    public Layer(JLabel initTitle, JLabel[] initButtons) {
        title = initTitle;
        buttons = initButtons;
    }
    public Layer(JLabel initTitle, JLabel[] initButtons, JLabel initText) {
        title = initTitle;
        buttons = initButtons;
        text = initText;
    }
}

interface MenuInterface {
    void handleMenu(boolean pressedEnter, int layerNumber, int pointer);
}

public class Menu <E extends MenuInterface> extends JFrame implements KeyListener{
    Layer[] layers;
    JPanel menuPanel = new JPanel();
    int currentLayer = 0;
    int pointer = 0;
    E source;

    public Menu(E initSource, Layer[] initLayers) {
        // Requires a handleMenu function in the source object to function properly
        layers = initLayers;
        source = initSource;

        addKeyListener(this);

        buildLayer(currentLayer);
    }

    public void buildLayer(int layerNumber) {
        layers[layerNumber].title.setFont(new Font("Courier", Font.BOLD, 30));
        layers[layerNumber].title.setBorder(new EmptyBorder(10,100,30,100));

        menuPanel.setLayout(new GridBagLayout());
        menuPanel.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;

        menuPanel.add(layers[layerNumber].title, c);

        c.gridy = 1;
        if (layers[layerNumber].buttons != null) {
            for (JLabel button : layers[layerNumber].buttons) {
                button.setFont(new Font("Courier", Font.BOLD, 30));
                button.setBorder(new EmptyBorder(30, 30, 30, 30));
                button.setOpaque(true);
                button.setBackground(null);
                menuPanel.add(button, c);
                c.gridy++;
            }

            layers[layerNumber].buttons[pointer].setBackground(Color.YELLOW);
        }

        if (layers[layerNumber].text != null) {
            c.fill = GridBagConstraints.HORIZONTAL;
            layers[layerNumber].text.setFont(new Font("Courier", Font.PLAIN, 20));
            menuPanel.add(layers[layerNumber].text, c);
        }

        add(menuPanel);
        pack();
    }

    public void updateMenu() {
        if (layers[currentLayer].buttons == null) {
            return;
        }
        for (JLabel button : layers[currentLayer].buttons) {
            button.setBackground(null);
        }
        layers[currentLayer].buttons[pointer].setBackground(Color.YELLOW);
        revalidate();
        repaint();
    }

    public void changeLayer(int layerNumber) {
        pointer = 0;
        currentLayer = layerNumber;
        buildLayer(currentLayer);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                source.handleMenu(true, currentLayer, pointer);
                break;
            case KeyEvent.VK_BACK_SPACE:
                source.handleMenu(false, currentLayer, pointer);
                break;
            case KeyEvent.VK_UP:
                pointer = Math.max(pointer-1, 0);
                break;
            case KeyEvent.VK_DOWN :
                pointer = Math.min(pointer+1, layers[currentLayer].buttons.length-1);
                break;
        }
        updateMenu();
    }


    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}