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

        diskScanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDiskScan();
            }
        });

        roundRobinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRoundRobin();
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

    private void onDiskScan() {
        setVisible(false); // Hide this dialog
        DiskScan dsDialog = new DiskScan(); // Show sjf dialog
        dsDialog.pack();
        dsDialog.setVisible(true);
    }


    private void onRoundRobin() {
        setVisible(false); // Hide this dialog
        round_robin rrDialog = new round_robin(); // Show sjf dialog
        rrDialog.pack();
        rrDialog.setVisible(true);
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
