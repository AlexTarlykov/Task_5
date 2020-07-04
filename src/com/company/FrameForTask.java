package com.company;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FrameForTask {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Task_5");
        frame.setSize(400, 420);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(table());
        panel.add(buttonForBuild(panel));
        panel.add(buttonForSaveList(panel));
        panel.add(textFieldForNameFile(panel));
        panel.add(buttonForOpenList(panel));
        panel.add(buttonForAddColumn(panel));
        panel.add(buttonForRemoveColumn(panel));
        panel.add(buttonForCreate(panel));
        frame.add(panel);
        frame.setVisible(true);
    }

    static JScrollPane table() {
        JTable table = new JTable(1, 3);
        table.setTableHeader(null);
        table.setCellSelectionEnabled(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setBorder(new EtchedBorder());
        table.setRowHeight(30);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(new EtchedBorder());
        pane.setSize(200, 130);
        pane.setLocation(160, 10);
        return pane;
    }

    static JTextField textFieldForNameFile(JPanel panel) {
        JLabel label = new JLabel("Input name file:");
        label.setLocation(25, 310);
        label.setSize(90, 30);
        panel.add(label);
        JTextField textField = new JTextField();
        textField.setLocation(10, 340);
        textField.setSize(110, 30);
        return textField;
    }

    static JButton buttonForBuild(JPanel panel) {
        JButton button = new JButton("Build tree");
        button.setLocation(10, 10);
        button.setSize(110, 50);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JScrollPane pane = (JScrollPane) panel.getComponentAt(160, 10);
                    JTable table = (JTable) pane.getViewport().getView();
                    Tree<Integer> tree = new Tree<>((first, second) -> first < second);
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        tree.add(Integer.parseInt(String.valueOf(table.getValueAt(0, i))));
                    }
                    Tree.TreePaint<Integer> paint = new Tree.TreePaint<>(tree);
                    JFrame frame = new JFrame("Tree");
                    frame.setSize(400, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                    frame.add(paint);
                } catch (Exception ignored) {
                }
            }
        });
        return button;
    }

    static JButton buttonForCreate(JPanel panel) {
        JButton button = new JButton("Create tree");
        button.setLocation(10, 70);
        button.setSize(110, 50);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JScrollPane pane = (JScrollPane) panel.getComponentAt(160, 10);
                    JTable table = (JTable) pane.getViewport().getView();
                    JFrame frame = new JFrame();
                    frame.setSize(200, 200);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                    frame.setLayout(null);
                    JTextField textField = new JTextField();
                    frame.add(textField);
                    textField.setLocation(10, 50);
                    textField.setSize(100, 25);
                    JLabel label = new JLabel("Input N:");
                    frame.add(label);
                    label.setLocation(10, 10);
                    label.setSize(100, 30);
                    JButton button1 = new JButton("Ok");
                    frame.add(button1);
                    button1.setSize(50, 50);
                    button1.setLocation(10, 90);
                    button1.addActionListener(new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Tree<Integer> tree = Tree.intTree(Integer.parseInt(textField.getText()));
                                DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                                dtm.setColumnCount((int) Math.pow(2, tree.height() + 1) - 1);
                                int a = 0;
                                for (int i = 1; i <= Integer.parseInt(textField.getText()); i++) {
                                    for (int j = 0; j < Math.pow(2, i - 1); j++) {
                                        table.setValueAt(i, 0, a++);
                                    }
                                }
                                Tree.TreePaint<Integer> paint = new Tree.TreePaint<>(tree);
                                JFrame frame1 = new JFrame("Tree");
                                frame1.setSize(400, 400);
                                frame1.setLocationRelativeTo(null);
                                frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame1.setVisible(true);
                                frame1.add(paint);
                                frame.dispose();
                            } catch (Exception ignored) {
                            }
                        }
                    });
                } catch (Exception ignored) {
                }
            }
        });
        return button;
    }

    static JButton buttonForSaveList(JPanel panel) {
        JButton button = new JButton("Save");
        button.setLocation(10, 180);
        button.setSize(110, 50);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTextField textField = (JTextField) panel.getComponentAt(10, 340);
                    FileWriter writer = new FileWriter("files\\" + textField.getText() + ".txt");
                    JScrollPane pane = (JScrollPane) panel.getComponentAt(160, 10);
                    JTable table = (JTable) pane.getViewport().getView();
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        builder.append(table.getValueAt(0, i)).append(" ");
                    }
                    writer.write(String.valueOf(builder));
                    textField.setText("");
                    writer.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        return button;
    }

    static JButton buttonForOpenList(JPanel panel) {
        JButton button = new JButton("Open");
        button.setLocation(10, 240);
        button.setSize(110, 50);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTextField textField = (JTextField) panel.getComponentAt(10, 340);
                    File file = new File("files\\" + textField.getText() + ".txt");
                    JScrollPane pane = (JScrollPane) panel.getComponentAt(160, 10);
                    JTable table = (JTable) pane.getViewport().getView();
                    Scanner read = new Scanner(file);
                    textField.setText("");
                    String[] strings = read.nextLine().split(" ");
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    dtm.setColumnCount(strings.length);
                    for (int i = 0; i < strings.length; i++) {
                        table.setValueAt(strings[i], 0, i);
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "File not found");
                }
            }
        });
        return button;
    }

    static JButton buttonForAddColumn(JPanel panel) {
        JButton button = new JButton("Add");
        button.setLocation(205, 180);
        button.setSize(110, 50);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JScrollPane pane = (JScrollPane) panel.getComponentAt(160, 10);
                JTable table = (JTable) pane.getViewport().getView();
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                dtm.setColumnCount(dtm.getColumnCount() + 1);
            }
        });
        return button;
    }

    static JButton buttonForRemoveColumn(JPanel panel) {
        JButton button = new JButton("Remove");
        button.setLocation(205, 240);
        button.setSize(110, 50);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JScrollPane pane = (JScrollPane) panel.getComponentAt(160, 10);
                JTable table = (JTable) pane.getViewport().getView();
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                if (dtm.getColumnCount() > 0) {
                    dtm.setColumnCount(dtm.getColumnCount() - 1);
                }
            }
        });
        return button;
    }
}
