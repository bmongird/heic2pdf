/*
 * Created Date: Saturday, September 16th 2023, 2:29:57 pm
 * Author: Ben Mongirdas
 * 
 * Description: Create and manage the window
 *
 * 
 * Copyright (c) 2023 Your Company
 */

package com.mongirdas;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;





public class Window extends JFrame implements ActionListener { // Add destructor if needed
    private int windowWidth, windowHeight;
    private boolean deleteState = false;
    private String windowName;
    private JFrame frame;
    private JButton convertButton, selectFileButton;
    private JToggleButton deleteOriginalFileToggleButton;
    private JFileChooser chooser;
    private JLabel statusText;
    Converter converter = new Converter();


    Window(int width, int height, String name){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        frame = this;
        //Add buttons
        convertButton = new JButton("Convert", null);
        convertButton.addActionListener(this);

        selectFileButton = new JButton("Select File(s)", null);
        selectFileButton.addActionListener(this);

        deleteOriginalFileToggleButton = new JToggleButton("Delete original files?");
        deleteOriginalFileToggleButton.addActionListener(this);

        this.add(convertButton);
        this.add(selectFileButton);
        this.add(deleteOriginalFileToggleButton);
        
        //Add Text Display
        statusText = new JLabel("Status: Select Files");
        JPanel panel = new JPanel(new GridLayout());
        panel.add(statusText);
        frame.add(panel);
        windowWidth = width;
        windowHeight = height;
        windowName = name;

        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2,3));
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        
    }

    public int getWidth(){
        return windowWidth;
    }

    public int getHeight(){
        return windowHeight;
    }

    public String getName(){
        return windowName;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == convertButton){
            System.out.println("Convert pressed");
            statusText.setText("Status: Converting...");

            //Puts the conversion on a separate Thread in order to not freeze the UI
            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                public Void doInBackground() {
                    converter.ConvertHEICToPdf(chooser.getSelectedFiles(), deleteState);
                    return null;
                }
                @Override
                public void done(){
                    statusText.setText("Status: Conversion Complete");
                }
            };
            worker.execute();
        }
        else if (e.getSource() == selectFileButton){
            chooser = new JFileChooser();
            chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
            chooser.addChoosableFileFilter(new FileNameExtensionFilter(".HEIC files", "heic"));
            chooser.setMultiSelectionEnabled(true);
            chooser.showOpenDialog(null);
            statusText.setText("Status: Press Convert");
        }
        else if (e.getSource() == deleteOriginalFileToggleButton){
            deleteState = deleteOriginalFileToggleButton.isSelected();
            System.out.println(deleteState);
        }
    }

}
