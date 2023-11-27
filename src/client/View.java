package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game.Board;

/**
 * View class
 * It contains the GUI of the game.
 * It contains the board and the current player.
 * 
 * @author Zhou Zilong
 * @since 2023-11-27
 */
public class View {

    private JFrame frame;
    private JPanel[] panels;

    private JLabel info;

    private JButton submitButton;
    private JTextField inputField;
    private BoardPanel boardPanel;

    /**
     * Constructor
     * Initialize the GUI
     */
    public View() {
        setFrame();
        setInfoPanel();
        setBoardPanel();
        setControlPanel();
    }

    private void setFrame() {
        frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tic Tac Toe");
        frame.setSize(320, 430);
        frame.setVisible(true);
        frame.setResizable(false);

        Container cp = frame.getContentPane();

        panels = new JPanel[3];
        panels[0] = new JPanel();
        panels[1] = new JPanel();
        panels[2] = new JPanel();

        cp.add(panels[0], "North");
        cp.add(panels[1], "Center");
        cp.add(panels[2], "South");
    }

    /**
     * Get the frame
     * 
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }

    private void setInfoPanel() {
        info = new JLabel("-");
        panels[0].add(info);
    }

    private void setBoardPanel() {
        boardPanel = new BoardPanel();
        boardPanel.setPreferredSize(new Dimension(300, 300));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panels[1].add(boardPanel);

    }

    private void setControlPanel() {

        inputField = new JTextField(20);
        submitButton = new JButton("submit");

        JPanel panelsContainer = new JPanel();

        panels[2].setLayout(new BoxLayout(panels[2], BoxLayout.Y_AXIS));
        panelsContainer.setLayout(new BoxLayout(panelsContainer, BoxLayout.X_AXIS));

        panels[2].add(panelsContainer);
        panelsContainer.add(inputField);
        panelsContainer.add(submitButton);
    }

    /**
     * Show the message
     * 
     * @param message
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    /**
     * BoardPanel class
     * It contains the board of the game.
     * 
     * It is a inner class of View.
     * 
     * It extends JPanel.
     * 
     * @author Zhou Zilong
     * @since 2023-11-27
     */
    class BoardPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private Board board;

        /**
         * Constructor
         * Initialize the panel with a game
         */
        public BoardPanel() {
            board = new Board();
        }

        /**
         * Get the board
         * 
         * @return the board
         */
        public void setBoard(int row, int col, char mark) {
            this.board.makeMove(row, col, mark);
        }

        /**
         * Get the board
         * 
         * @return the board
         */
        public void clearBoard() {
            this.board = new Board();
        }

        /**
         * override the repaint method
         * update the board when the round is over
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);
        }

        /**
         * Draw the board
         * 
         * @param g
         */
        private void drawBoard(Graphics g) {
            // draw the board
            g.setColor(Color.black);
            g.drawLine(100, 0, 100, 300);
            g.drawLine(200, 0, 200, 300);
            g.drawLine(0, 100, 300, 100);
            g.drawLine(0, 200, 300, 200);

            // draw the marks
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; ++j) {
                    if (board.getMark(i, j) != '-') {
                        drawMark(g, i, j);
                    }
                }
            }

            // frame.repaint();
        }

        /**
         * Draw the mark
         * 
         * @param g
         * @param row
         * @param col
         */
        private void drawMark(Graphics g, int row, int col) {
            // draw the marks
            if (board.getMark(row, col) == 'x') {
                g.setColor(Color.red);
                g.drawLine(col * 100 + 10, row * 100 + 10, col * 100 + 90, row * 100 + 90);
                g.drawLine(col * 100 + 90, row * 100 + 10, col * 100 + 10, row * 100 + 90);
            } else {
                g.setColor(Color.blue);
                g.drawOval(col * 100 + 10, row * 100 + 10, 80, 80);
            }
        }

    }

    /**
     * Get the submit button
     * 
     * @return the submit button
     */
    public JButton getSubmitButton() {
        return submitButton;
    }

    /**
     * Get the info label
     * 
     * @return the info label
     */
    public JLabel getInfoLabel() {
        return info;
    }

    /**
     * Get the input field
     * 
     * @return the input field
     */
    public JTextField getInputField() {
        return inputField;
    }

    /**
     * Get the board panel
     * 
     * @return the board panel
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
