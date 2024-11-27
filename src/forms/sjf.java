package forms;

import cpu.scheduling.ShortestJobFirst;
import utils.Process;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class sjf extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JButton compute;

    // List to hold references to dynamically created input panels
    private List<JPanel> dynamicPanels = new ArrayList<>();
    private List<JTextField> arrivalTimeFields = new ArrayList<>();
    private List<JTextField> burstTimeFields = new ArrayList<>();
    private ShortestJobFirst sjf;
    private ArrayList<utils.Process> processes = new ArrayList<>();

    public sjf() {
        setContentPane(contentPane);
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

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Add a scroll pane for the contentPane to make it scrollable
        JScrollPane scrollPane = new JScrollPane(contentPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show the vertical scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Never show horizontal scrollbar
        setContentPane(scrollPane); // Set the scrollPane as the main content pane
    }

    private void onOK() {
        try {
            int numberOfFields = Integer.parseInt(textField1.getText()); // Get number of rows (pairs of fields)

            // Clear the dynamically added input fields and table if any
            for (JPanel panel : dynamicPanels) {
                contentPane.remove(panel); // Remove each dynamically added panel
            }
            dynamicPanels.clear();
            arrivalTimeFields.clear();
            burstTimeFields.clear();

            // Check if the table already exists and remove it
            for (Component component : contentPane.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel panel = (JPanel) component;
                    if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
                        if (scrollPane.getViewport().getView() instanceof JTable) {
                            // Remove the existing table
                            contentPane.remove(panel);
                            break;
                        }
                    }
                }
            }

            // Keep the existing layout (BoxLayout) with compact spacing for dynamic fields
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)); // Keep current layout with vertical stacking

            // Add some gap before the new fields
            contentPane.add(Box.createVerticalStrut(10));

            // Add input fields dynamically below the existing components
            for (int i = 0; i < numberOfFields; i++) {
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // Change to BoxLayout for horizontal alignment

                // Create Arrival Time and Burst Time input fields
                JLabel arrivalTimeLabel = new JLabel("AT " + (i + 1));
                JTextField arrivalTimeField = new JTextField(); // Don't specify the number of columns to let it expand
                arrivalTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // Let the field expand horizontally

                JLabel burstTimeLabel = new JLabel("BT " + (i + 1));
                JTextField burstTimeField = new JTextField(); // Don't specify the number of columns to let it expand
                burstTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // Let the field expand horizontally

                // Add labels and text fields to the panel
                inputPanel.add(arrivalTimeLabel);
                inputPanel.add(Box.createHorizontalStrut(10)); // Add space between label and text field
                inputPanel.add(arrivalTimeField);
                inputPanel.add(Box.createHorizontalStrut(10)); // Add space between text fields
                inputPanel.add(burstTimeLabel);
                inputPanel.add(Box.createHorizontalStrut(10)); // Add space between label and text field
                inputPanel.add(burstTimeField);
                // Add the panel to the content pane
                contentPane.add(inputPanel);

                // Store the fields in their respective lists
                arrivalTimeFields.add(arrivalTimeField);
                burstTimeFields.add(burstTimeField);

                // Store the panel in the dynamicPanels list for later removal
                dynamicPanels.add(inputPanel);
            }

            // Create the button panel only once and add the buttons to it
            if (compute == null) {
                compute = new JButton("Simulate");
                compute.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        computeSJF();  // Compute SJF when button is clicked
                    }
                });
            }

            compute.setMaximumSize(new Dimension(Integer.MAX_VALUE, compute.getPreferredSize().height));

            // Create a new panel for buttons, ensuring it stays fixed
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.add(compute);

            // Ensure the button panel is added once after fields are updated
            contentPane.add(buttonPanel);

            // Revalidate the layout and repaint it to update the display
            contentPane.revalidate();
            contentPane.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }



    private void computeSJF() {
        try {
            // Check if the table already exists
            JTable existingTable = null;
            for (Component component : contentPane.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel panel = (JPanel) component;
                    if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
                        if (scrollPane.getViewport().getView() instanceof JTable) {
                            existingTable = (JTable) scrollPane.getViewport().getView();
                            break;
                        }
                    }
                }
            }

            // Proceed with SJF computation only if the table doesn't exist
            int numProcesses = arrivalTimeFields.size();
            int[] arrivalTimes = new int[numProcesses];
            int[] burstTimes = new int[numProcesses];

            // Retrieve the arrival and burst times from the text fields
            for (int i = 0; i < numProcesses; i++) {
                arrivalTimes[i] = Integer.parseInt(arrivalTimeFields.get(i).getText());
                burstTimes[i] = Integer.parseInt(burstTimeFields.get(i).getText());
            }

            // Combine the arrival time, burst time, and index into a list of processes
            for (int i = 0; i < numProcesses; i++) {
                this.processes.add(new utils.Process(i + 1, (double) arrivalTimes[i], (double) burstTimes[i]));
            }

            sjf = new ShortestJobFirst(processes);
            ArrayList<utils.Process> result = sjf.simulate();


            // Prepare data for JTable
            String[] columns = {"Process", "AT", "BT", "CT", "TAT", "WT"};
            Object[][] data = new Object[numProcesses + 2][6]; // +1 for the averages row

            int PROCESS_ID = 0;
            int AT = 1;
            int BT = 2;
            int CT = 3;
            int TAT = 4;
            int WT = 5;

            double totalCT = 0;
            double totalTAT = 0;
            double totalWT = 0;

            for (int i = 0; i < numProcesses; i++) {
                utils.Process process = result.get(i);

                data[i][PROCESS_ID] = "P" + process.getID();
                data[i][AT] = process.getArrivalTime();
                data[i][BT] = process.getBurstTime();
                data[i][CT] = process.getCompletionTime();
                data[i][TAT] = process.getTurnAroundTime();
                data[i][WT] = process.getWaitingTime();

            }
            totalCT = sjf.getTotalCompletionTime();
            totalTAT = sjf.getTotalTurnAroundTime();
            totalWT = sjf.getTotalWaitingTime();

            // Calculate averages
            double avgCT = Math.round(totalCT / numProcesses * 100.0) / 100.0;
            double avgTAT = Math.round(totalTAT / numProcesses * 100.0) / 100.0;
            double avgWT = Math.round(totalWT / numProcesses * 100.0) / 100.0;

            // Add average row to the table
            data[numProcesses][PROCESS_ID] = "Total";
            data[numProcesses][CT] = totalCT;
            data[numProcesses][TAT] = totalTAT;
            data[numProcesses][WT] = totalWT;

            data[numProcesses + 1][PROCESS_ID] = "Average";
            data[numProcesses + 1][CT] = avgCT;
            data[numProcesses + 1][TAT] = avgTAT;
            data[numProcesses + 1][WT] = avgWT;

            // If the table exists, update it
            if (existingTable != null) {
                // Update the existing table's model with the new data
                existingTable.setModel(new DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // All cells are non-editable
                    }
                });
            } else {
                // If the table doesn't exist, create a new one (this is a fallback)
                JTable table = new JTable(new DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // All cells are non-editable
                    }
                });
                table.setPreferredScrollableViewportSize(new Dimension(500, 100));
                table.setFillsViewportHeight(true);

                // Create a JScrollPane for the table
                JScrollPane tableScrollPane = new JScrollPane(table);

                // Create a panel for the table and add it to the content pane
                JPanel tablePanel = new JPanel();
                tablePanel.setName("table");
                tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
                tablePanel.add(tableScrollPane);
                for (Component component: contentPane.getComponents()) {
                    if (component instanceof JPanel) {
                        if (component.getName() != null) {
                            contentPane.remove(component);
                        }
                    }
                }
                contentPane.add(tablePanel);
            }

            this.processes.clear();
            contentPane.revalidate();  // Revalidate the layout
            contentPane.repaint();  // Repaint to reflect changes
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.");
        }
    }



    private void onCancel() {
        setVisible(false); // Hide this dialog
        MainMenu mainMenu = new MainMenu(); // Show MainMenu again
        mainMenu.pack();
        mainMenu.setVisible(true);

    }

    public static void main(String[] args) {
        sjf dialog = new sjf();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
