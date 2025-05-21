//Basic mouse and keyboard control has been implemented in the code! Mousecontrol disables the button when sum exceeds 21, and keyboard control uses "H/h"and "S/s" for hit and stand functionality.
package client;

import java.awt.Dialog;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class BlackjackGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JButton hitButton;
    private JButton standButton;
    private JButton restartButton;


    private String BASE_URL = "http://euclid.knox.edu:8080/api/blackjack";
    private String USERNAME = "dsharma";
    private String PASSWORD = "95928b";
    private ClientConnecter clientConnecter;

    private CardPanel cardPanel;
    private int playerSum=0;
    private int playerAceCount=0;
    private int dealerSum=0;
    private int dealerAceCount=0; //To ensure our sum does not exceed 21 for cards
    private Map<Card, ImageIcon> cardImages;
    private Random random = new Random();
    private UUID sessionId;

    public BlackjackGUI() {
        setTitle("Blackjack Game");
        setSize(1000, 800);
        loadCards();
        // create and pass the buttons to the card panel
        // it will resize them and add them to the panel
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        restartButton = new JButton("Restart");
        cardPanel = new CardPanel(hitButton, standButton, restartButton, cardImages);
        setContentPane(cardPanel);

        // Initial deal
        dealInitialCards();

        // Add restart button functionality
        restartButton.addActionListener(e -> {
            playerSum = 0;
            playerAceCount = 0;
            dealerSum = 0;
            dealerAceCount = 0;
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
            cardPanel.clearCards();
            cardPanel.updateScores(0, 0);
            cardPanel.updateStatus("Game in Progress");
            dealInitialCards();
            repaint();
        });

        // now set the action listeners for the hit/stand buttons
        hitButton.addActionListener(e -> {
            System.out.println("Hit button clicked");
            List<Card> cards = List.of(Card.values());
            Card c = cards.get(random.nextInt(cards.size()));
            cardPanel.addPlayerCard(c);
            //To update the sum
            playerSum += c.getValue();
            if (c.isAce()){
                playerAceCount++;
            }
            playerSum = reducePlayerAce();
            cardPanel.updateScores(playerSum, dealerSum);
            repaint(); 
            //Logic to block out hit and stand button after exceeding 21
            if (playerSum > 21){
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
                cardPanel.updateStatus("You Lose! - Bust");
            }
        });

        standButton.addActionListener(e -> {
            System.out.println("Stand button clicked");
            // Dealer's turn - keep drawing until 17 or higher
            while (dealerSum < 17) {
                List<Card> cards = List.of(Card.values());
                Card d = cards.get(random.nextInt(cards.size()));
                cardPanel.addDealerCard(d);
                dealerSum += d.getValue();
                if(d.isAce()){
                    dealerAceCount++;
                }
                dealerSum = reduceDealerAce();
                cardPanel.updateScores(playerSum, dealerSum);
                repaint();
            }
            
            // Determine winner
            if (dealerSum > 21) {
                cardPanel.updateStatus("You Win! - Dealer Bust");
            } else if (dealerSum > playerSum) {
                cardPanel.updateStatus("You Lose! - Dealer Wins");
            } else if (dealerSum < playerSum) {
                cardPanel.updateStatus("You Win! - Higher Score");
            } else {
                cardPanel.updateStatus("Push - It's a Tie!");
            }
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        });

        // client connecter to make API calls on the server
        clientConnecter = new ClientConnecter(BASE_URL, USERNAME, PASSWORD);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addMenuBar();
        addKeyboardControls(); // Add keyboard controls
    }

    private void dealInitialCards() {
        List<Card> cards = List.of(Card.values());
        
        // Deal two cards to player
        for (int i = 0; i < 2; i++) {
            Card c = cards.get(random.nextInt(cards.size()));
            cardPanel.addPlayerCard(c);
            playerSum += c.getValue();
            if (c.isAce()) {
                playerAceCount++;
            }
        }
        playerSum = reducePlayerAce();
        
        // Deal two cards to dealer
        for (int i = 0; i < 2; i++) {
            Card d = cards.get(random.nextInt(cards.size()));
            cardPanel.addDealerCard(d);
            dealerSum += d.getValue();
            if (d.isAce()) {
                dealerAceCount++;
            }
        }
        dealerSum = reduceDealerAce();
        
        cardPanel.updateScores(playerSum, dealerSum);
        
        // Check for natural blackjack
        if (playerSum == 21) {
            cardPanel.updateStatus("Blackjack! You Win!");
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        }
    }

    public int reducePlayerAce() {
        int tempSum = playerSum;
        int tempAceCount = playerAceCount;
        while (tempSum > 21 && tempAceCount > 0) {
            tempSum -= 10;
            tempAceCount--;
        }
        return tempSum;
    }

    public int reduceDealerAce() {
        int tempSum = dealerSum;
        int tempAceCount = dealerAceCount;
        while (tempSum > 21 && tempAceCount > 0) {
            tempSum -= 10;
            tempAceCount--;
        }
        return tempSum;
    }

    private void addKeyboardControls() {
        // Basic keyboard control
        KeyListener listener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();
                System.out.println("Key pressed: " + key); // Debug print
                if (key == 'h' || key == 'H') {
                    hitButton.doClick();
                }
                if (key == 's' || key == 'S') {
                    standButton.doClick();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {} 
            @Override
            public void keyReleased(KeyEvent e) {}
        };
        
        // Add the listener and make sure we can receive key events
        addKeyListener(listener);
        setFocusable(true);
        requestFocus();
    }

    private void addMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        
        menuBar.add(fileMenu);
        addMenuItem(fileMenu, "Reconnect", () -> {
            System.out.println("Load clicked");
            try {
                List<SessionSummary> sessionSummaryList = clientConnecter.listSessions();
                for (SessionSummary session : sessionSummaryList) {
                    System.out.println("Session ID: " + session.sessionId + ", Balance: " + session.balance);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addMenuItem(fileMenu, "New Game", () -> {
            System.out.println("New Game clicked");
            try {
                GameState state = clientConnecter.startGame();
                System.out.println(state);
                sessionId = state.sessionId;
                // TODO: use the game state to update the UI
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error starting new game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    // convert "THREE OF HEARTS" from server to Card.THREE_OF_HEARTS
    private Card getCard(String cardName) {
        return Card.valueOf(cardName.toUpperCase().replace(' ', '_'));
    }

    private void addMenuItem(JMenu menu, String name, Runnable action) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(e -> action.run());
        menu.add(menuItem);
    }

    private void loadCards() {
        // Load card images and add them to the main panel
        // This is where you would implement the logic to load and display cards
        cardImages = new HashMap<>();
        for (Card card : Card.values()) {
            ImageIcon cardImage = new ImageIcon(getClass().getResource("/assets/" + card.getFilename()));
            cardImages.put(card, cardImage);
        }
    }

    public void showListPopup(String title, java.util.List<String> items) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JList<String> list = new JList<>(new DefaultListModel<>());
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        for (String item : items) {
            model.addElement(item);
        }

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double click to select
                    String selected = list.getSelectedValue();
                    System.out.println("Selected: " + selected);
                    dialog.dispose();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        BlackjackGUI gui = new BlackjackGUI();
        gui.setVisible(true);
    }
}
