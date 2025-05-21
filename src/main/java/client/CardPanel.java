package client;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CardPanel extends JPanel 
{
    private static final long serialVersionUID = 1L;
    //expanding upon GUI
    private JLabel statusLabel;
    private JLabel playerValueLabel;
    private JLabel dealerValueLabel;


    private JButton hitButton;
    private JButton standButton;
    private JButton restartButton;

    private List<Card> dealerCards = new ArrayList<>();
    private List<Card> playerCards = new ArrayList<>();
    private Map<Card, ImageIcon> cardImages;
    //private Random random;

    public CardPanel(JButton hitButton, JButton standButton, JButton restartButton, Map<Card, ImageIcon> cardImages)
    {
        this.hitButton = hitButton;
        this.standButton = standButton;
        this.restartButton=restartButton;
        this.cardImages = cardImages;

        // null layout manager is absolute positioning
        setLayout(null);
        setBackground(Color.GREEN.darker());

        // Initialize labels
        statusLabel = new JLabel("Game in Progress");
        playerValueLabel = new JLabel("Player: 0");
        dealerValueLabel = new JLabel("Dealer: 0");

        // Position labels
        statusLabel.setBounds(400, 50, 200, 30);
        playerValueLabel.setBounds(50, 450, 100, 30);
        dealerValueLabel.setBounds(50, 50, 100, 30);

        // Add labels to panel
        add(statusLabel);
        add(playerValueLabel);
        add(dealerValueLabel);

        //loadCards();

        // add a hit and stand button
        // the actual click handler is defined in the BlackjackGUI class
        hitButton.setBounds(50, 600, 100, 60);
        add(hitButton);
        
        standButton.setBounds(200, 600, 100, 60);
        add(standButton);

        restartButton.setBounds(350, 600, 100, 60);
        add(restartButton);

    }

    public void clearCards() {
        dealerCards.clear();
        playerCards.clear();
    }

    public void addDealerCard(Card card) {
        dealerCards.add(card);
        repaint();
    }

    public void addPlayerCard(Card card) {
        playerCards.add(card);
        repaint();
    }

    public void updateScores(int playerScore, int dealerScore) {
        playerValueLabel.setText("Player: " + playerScore);
        dealerValueLabel.setText("Dealer: " + dealerScore);
        repaint();
    }

    public void updateStatus(String status) {
        statusLabel.setText(status);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 100;
        int y = 100;
        // dealer cards
        for (Card card : dealerCards) {
            // Draw dealer cards
            //System.out.println("Drawing dealer card: " + card);
            ImageIcon cardImage = cardImages.get(card);
            if (cardImage != null) {
                System.out.println("Drawing dealer card: " + card);
                g.drawImage(cardImage.getImage(), x, y, null);
                x += cardImage.getIconWidth() + 10; 
            }
        }

        // player cards
        x = 100; 
        y = 500;
        for (Card card : playerCards) {
            System.out.println("Drawing player card: " + card);
            // Draw player cards
            ImageIcon cardImage = cardImages.get(card);
            if (cardImage != null) {
                g.drawImage(cardImage.getImage(), x, y, null);
                x += cardImage.getIconWidth() + 10; 
            }
        }
    }        
    
}

