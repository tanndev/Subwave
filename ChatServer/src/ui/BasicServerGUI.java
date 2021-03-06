package com.tanndev.subwave.server.ui;

import com.tanndev.subwave.common.Defaults;
import com.tanndev.subwave.common.ErrorHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by James Tanner on 7/2/2015.
 */
public class BasicServerGUI extends JPanel {
   private static JTextArea textOutput;

   public BasicServerGUI() {
      // Set up the layout manager
      BorderLayout layout = new BorderLayout();
      layout.setVgap(2);
      setLayout(layout);

      // Create text area
      textOutput = new JTextArea(20, 50);
      textOutput.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(textOutput);
      scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(2, 3, 2, 2)));

      // Create button
      JButton shutdownButton = new JButton("Shutdown Server");
      shutdownButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            System.exit(0);
         }
      });
      shutdownButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));

      //Add Components to this panel.
      add(scrollPane, BorderLayout.CENTER);
      add(shutdownButton, BorderLayout.SOUTH);
      setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Server Messages:"),
            BorderFactory.createEmptyBorder(1, 1, 1, 1)));
   }

   public static void createAndShowGUI() {
      // Make the runnable
      Runnable task = new Runnable() {
         public void run() {
            //Create and set up the window.
            JFrame frame = new JFrame("Subwave Server");
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            //Add contents to the window.
            frame.add(new BasicServerGUI());

            //Display the window.
            frame.pack();
            frame.setVisible(true);

            // Redirect output to the UI.
            System.setOut(new OutputPrinter(System.out));

            synchronized (this) {
               this.notifyAll();
            }
         }
      };

      // Schedule a new thread to create the UI.
      javax.swing.SwingUtilities.invokeLater(task);

      // Wait on the task.
      try {
         synchronized (task) {
            task.wait(Defaults.DEFAULT_UI_LAUNCH_WAIT);
         }
      } catch (InterruptedException e) {
         ErrorHandler.logError("Exception thrown while waiting for UI.", e);
      }
   }

   private static class OutputPrinter extends PrintStream {
      public OutputPrinter(OutputStream out) {
         super(out, true);
      }

      @Override
      public void println(String output) {
         if (textOutput == null) return;
         textOutput.append(output + "\n");
         textOutput.setCaretPosition(textOutput.getDocument().getLength());
      }

      @Override
      public void print(String output) {
         if (textOutput == null) return;
         textOutput.append(output);
         textOutput.setCaretPosition(textOutput.getDocument().getLength());
      }
   }
}