package forms;

import disk.scheduling.Direction;
import utils.Process;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DiskScan extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField currentPosition;
    private JTextField trackSize;
    private JTextField numberOfRequest;
    private JTextField seekRate;
    private JPanel requestPanel;
    private JLabel currentPositionLabel;
    private JLabel resultLabel;
    private JPanel requestSection;
    private JScrollPane scrollPane;
    private int CURRENT_POSITION;
    private int TRACK_SIZE;
    private int NUMBER_OF_REQUEST;
    private int SEEK_RATE;
    private Direction DIRECTION;

    public DiskScan() {
        setContentPane(contentPane);
        setTitle("Disk Scan");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }


    private void updateCurrentPosition() {
        this.CURRENT_POSITION = Integer.parseInt(currentPosition.getText());
    }

    private void updateTrackSize() {
        this.TRACK_SIZE = Integer.parseInt(trackSize.getText());
    }

    private void updateSeekRate() {
        this.SEEK_RATE = Integer.parseInt(seekRate.getText());
    }

    private void updateNumberOfRequest() {
        this.NUMBER_OF_REQUEST = Integer.parseInt(numberOfRequest.getText());
    }

    private void onOK() {
        requestSection.removeAll();
        requestPanel.removeAll();
        updateCurrentPosition();
        updateTrackSize();
        updateSeekRate();
        updateNumberOfRequest();
        JButton leftButton = new JButton("LEFT");
        JButton rightButton = new JButton("RIGHT");
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        JPanel buttonSection = new JPanel(new GridLayout(1, 2));
        JLabel directionLabel = new JLabel("Direction: ");
        buttonSection.add(directionLabel);
        buttonSection.add(buttonPanel);
        requestSection.setLayout(new BorderLayout());
        requestSection.add(buttonSection, BorderLayout.PAGE_END);
        requestSection.add(requestPanel, BorderLayout.PAGE_START);
//        requestSection.setVisible(true);

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onScan(Direction.LEFT);
            }
        });

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onScan(Direction.RIGHT);
            }
        });


        requestPanel.setLayout(new GridLayout(NUMBER_OF_REQUEST, 2));
        for(int i = 0; i < NUMBER_OF_REQUEST; i++) {
            String label = "Loc " + String.valueOf(i + 1);
            requestPanel.add(new JLabel(label));
            requestPanel.add(new JTextField());
        }
        revalidate();
        repaint();
    }


    private void onScan(Direction direction) {
        ArrayList<JTextField> textFields = new ArrayList<>();
        for (Component component: requestPanel.getComponents()) {
            if (component instanceof JTextField) {
                textFields.add((JTextField) component);
            }
        }
        ArrayList<Integer> request = new ArrayList<>();
        requestPanel.removeAll();
        for (JTextField field: textFields) {
            System.out.println(field.getText());
            request.add(Integer.parseInt(field.getText()));
        }
        disk.scheduling.DiskScan ds = new disk.scheduling.DiskScan(TRACK_SIZE, SEEK_RATE);
        int[] sequence = new int[request.size()];
        for (int i = 0; i < request.size(); i++) {
            sequence[i] = request.get(i);
        }
        int seekCount = ds.scan(sequence, CURRENT_POSITION, direction);
        ArrayList<Integer> dsResult = ds.getSeekSequence();
        JLabel resultLabel = new JLabel("Seek Time: " + seekCount);
        JLabel resultTitle = new JLabel("Result");
        StringBuilder seekSequenceString = new StringBuilder("Seek Sequence: ");
        ArrayList<Integer> seekSequence = ds.getSeekSequence();
        for (Integer i: seekSequence) {
            seekSequenceString.append(i);
        }
        JLabel resultSeekSequence = new JLabel(String.valueOf(seekSequenceString));


        requestSection.removeAll();
        requestSection.add(resultTitle, BorderLayout.PAGE_START);
        requestSection.add(resultLabel, BorderLayout.CENTER);
        requestSection.add(resultSeekSequence, BorderLayout.PAGE_END);
        requestSection.revalidate();
        requestSection.repaint();
        requestPanel.removeAll();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    
    public static void main(String[] args) {
        DiskScan dialog = new DiskScan();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
