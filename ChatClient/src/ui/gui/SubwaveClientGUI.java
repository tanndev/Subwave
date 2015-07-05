package com.tanndev.subwave.client.ui.gui;

import com.tanndev.subwave.client.core.SubwaveClient;
import com.tanndev.subwave.client.ui.ClientUIFramework;
import com.tanndev.subwave.common.ErrorHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Created by James Tanner on 7/2/2015.
 */
public class SubwaveClientGUI extends ClientUIFramework {

   protected static ConversationListPanel conversationListPanel;
   protected static ClientListPanel clientListPanel;
   protected static ChatPanel chatPanel;
   protected static SubwaveClientGUI uiRoot;
   protected static int serverConnectionID;

   public SubwaveClientGUI() {
      // Attempt to open the connection.
      // TODO ask instead of defaults
      serverConnectionID = SubwaveClient.connectToServer(null, 0, null);
      if (serverConnectionID == 0) {
         ErrorHandler.logError("No server connection for GUI to use.");
         System.exit(0);
      }

      // Set the UI root
      uiRoot = this;

      // Make a runnable task to create the conversation list panel
      Runnable createAndShowGUI = new Runnable() {
         public void run() {
            //Create and set up the window.
            JFrame frame = new JFrame("Subwave Client");
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create side bar
            JPanel sideBar = new JPanel(new GridLayout(0, 1));
            clientListPanel = new ClientListPanel(uiRoot);
            sideBar.add(clientListPanel);
            conversationListPanel = new ConversationListPanel(uiRoot);
            sideBar.add(conversationListPanel);

            //Create main panel
            JPanel mainPanel = new JPanel(new BorderLayout());
            chatPanel = new ChatPanel(uiRoot);
            mainPanel.add(sideBar, BorderLayout.LINE_START);
            mainPanel.add(chatPanel, BorderLayout.CENTER);

            // Put the content pane in the frame
            frame.add(mainPanel);

            //Display the window.
            frame.pack();
            frame.setVisible(true);
         }
      };

      // Schedule a thread to run the new task.
      javax.swing.SwingUtilities.invokeLater(createAndShowGUI);
   }

   public void shutdown() {
      // TODO Shutdown GUI gracefully.
      System.exit(0);
   }

   public void commandConversationNew() {

   }

   @Override
   public void handleChatMessage(int connectionID, int conversationID, int sourceClientID, String message) {
      super.handleChatMessage(connectionID, conversationID, sourceClientID, message);
   }

   @Override
   public void handleChatEmote(int connectionID, int conversationID, int sourceClientID, String message) {
      super.handleChatEmote(connectionID, conversationID, sourceClientID, message);
   }

   @Override
   public void handleConversationInvite(int connectionID, int conversationID, int sourceClientID, String conversationName) {
      ConversationElement conversationElement = new ConversationElement(connectionID, conversationID, conversationName);
      conversationListPanel.addConversation(conversationElement);
   }

   @Override
   public void handleConversationJoin(int connectionID, int conversationID, int sourceClientID, String message) {
      super.handleConversationJoin(connectionID, conversationID, sourceClientID, message);
   }

   @Override
   public void handleConversationLeave(int connectionID, int conversationID, int sourceClientID, String message) {
      super.handleConversationLeave(connectionID, conversationID, sourceClientID, message);
   }

   @Override
   public void handleNameUpdate(int connectionID, int conversationID, int sourceClientID, String friendlyName) {
      super.handleNameUpdate(connectionID, conversationID, sourceClientID, friendlyName);
   }

   @Override
   public void handleAcknowledge(int connectionID, int conversationID, int sourceClientID, String message) {
      super.handleAcknowledge(connectionID, conversationID, sourceClientID, message);
   }

   @Override
   public void handleRefuse(int connectionID, int conversationID, int sourceClientID, String message) {
      super.handleRefuse(connectionID, conversationID, sourceClientID, message);
   }

   @Override
   public void handleNetworkConnect(int connectionID, int clientID, String friendlyName) {
      super.handleNetworkConnect(connectionID, clientID, friendlyName);
   }

   @Override
   public void handleNetworkDisconnect(int connectionID, int clientID) {
      super.handleNetworkDisconnect(connectionID, clientID);
   }

   @Override
   public void handleDebug(int connectionID, int conversationID, int clientID, String message) {
      super.handleDebug(connectionID, conversationID, clientID, message);
   }

   @Override
   public void handleUnhandled(int connectionID, int conversationID, int sourceClientID, String message) {
      super.handleUnhandled(connectionID, conversationID, sourceClientID, message);
   }

   @Override
   public void onServerDisconnect(int connectionID) {
      super.onServerDisconnect(connectionID);
   }
}