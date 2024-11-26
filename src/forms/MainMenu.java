package forms;

import javax.swing.*;
import java.awt.event.*;

public class MainMenu extends JDialog {
    private JPanel contentPane;
    private JButton diskScanButton;
    private JButton exitButton;
    private JButton shortestJobFirstButton;
    private JButton roundRobinButton;

    public MainMenu() {
        setContentPane(contentPane);
        setTitle("Operating System Scheduling System v1.0");
        setModal(true);

        shortestJobFirstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSJF();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });
    }

    private void onSJF() {
        setVisible(false); // Hide this dialog
        sjf sjfDialog = new sjf(); // Show sjf dialog
        sjfDialog.pack();
        sjfDialog.setVisible(true);

    }

    private void onExit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        MainMenu dialog = new MainMenu();
        dialog.pack();
        dialog.setVisible(true);
    }
}
