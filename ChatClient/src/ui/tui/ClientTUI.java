package com.tanndev.subwave.client.ui.tui;

import com.tanndev.subwave.client.ui.ClientUIFramework;
import com.tanndev.subwave.common.Connection;
import com.tanndev.subwave.common.Message;
import com.tanndev.subwave.common.MessageType;
import com.tanndev.subwave.common.Settings;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by James Tanner on 6/28/2015.
 */
public class ClientTUI extends ClientUIFramework {

   protected Connection serverConnection;

   private int lastConversationID = 0;

   public ClientTUI() {
      serverConnection = openConnection(Settings.DEFAULT_SERVER_ADDRESS, Settings.DEFAULT_SERVER_PORT, null);
      if (serverConnection == null) {
         System.err.println("No server connection for TUI to use.");
         System.exit(0);
      }

      // Switch off the local connection message printing.
      serverConnection.setPrintMessages(false);
      System.out.println("Hiding TX/RX messages.");
   }

   public static void displayHelp(Command command) {
      // TODO display a help file.
      System.out.println("Malformed command.");
   }

   public void run() {
      // Start a new input listeners.
      new UserListener(this).start();
      new ServerListener(this).start();
   }

   public void shutdown() {
      closeConnection(serverConnection);
      System.exit(0);
   }

   protected void handleUserInput(String input) {
      if (input == null || input.length() < 1) return;

      MessageType messageType = MessageType.DEBUG_MESSAGE;
      int conversationID = 0;
      int clientID = serverConnection.getClientID();
      String messageText = input;

      if (isCommand(input)) {
         String[] tokens = tokenizeCommand(input);
         String commandToken = tokens[0];
         Command command = Command.parseCommandToken(commandToken);
         int messageStartIndex = commandToken.length();

         switch (command) {
            case MESSAGE:
               messageType = MessageType.CHAT_MESSAGE;
               if (tokens.length < 3) {
                  displayHelp(Command.MESSAGE);
                  return;
               }
               messageText = recombineTokensAfter(tokens, input, 2);
               break;

            case EMOTE:
               messageType = MessageType.CHAT_EMOTE;
               if (tokens.length < 3) {
                  displayHelp(Command.MESSAGE);
                  return;
               }
               messageText = recombineTokensAfter(tokens, input, 2);
               break;

            case REPLY:
               conversationID = lastConversationID;
               messageType = MessageType.CHAT_MESSAGE;
               break;

            case CONVERSATION_NEW:
               messageType = MessageType.CONVERSATION_NEW;
               break;

            case CONVERSATION_INVITE:
               // TODO conversation invite.
               messageType = MessageType.CONVERSATION_INVITE;
               break;

            case CONVERSATION_JOIN:
               // TODO conversation join.
               messageType = MessageType.CONVERSATION_JOIN;
               break;

            case CONVERSATION_LEAVE:
               // TODO conversation leave.
               messageType = MessageType.CONVERSATION_LEAVE;
               break;

            case NAME_UPDATE:
               // TODO name update.
               messageType = MessageType.NAME_UPDATE;
               break;

            case ACKNOWLEDGE:
               // TODO acknowledge.
               messageType = MessageType.ACKNOWLEDGE;
               break;

            case REFUSE:
               // TODO refuse.
               messageType = MessageType.REFUSE;
               break;

            case DEBUG_MESSAGE:
               // TODO debug
               break;

            case QUIT:
               shutdown();
               break;

            default:
               System.err.println("Unrecognised command token: " + tokens[0]);
               return;
         }
         // Trim command off sending message.
         messageText = messageText.substring(messageStartIndex);

      } else {
         // No command. If there is an ongoing conversation, assume this is a reply. Otherwise, show help.
         if (lastConversationID > 0) {
            messageType = MessageType.CHAT_MESSAGE;
            conversationID = lastConversationID;
         } else displayHelp(null);
      }

      Message message = new Message(messageType, conversationID, clientID, messageText);
      serverConnection.send(message);
   }

   private boolean isCommand(String input) {
      return input.startsWith("\\");
   }

   private String[] tokenizeCommand(String input) {
      Scanner tokenizer = new Scanner(input);
      ArrayList<String> tokens = new ArrayList<String>();
      while (tokenizer.hasNext()) tokens.add(tokenizer.next());
      return tokens.toArray(new String[tokens.size()]);
   }

   private String recombineTokensAfter(String[] tokens, String original, int lastUsedIndex) {
      if (lastUsedIndex > tokens.length) return null;
      return original.substring(original.indexOf(tokens[lastUsedIndex]));
   }

   protected void handleServerInput(Message message) {
      if (message == null) return;

      // TODO switch on server input.
      System.out.println("RECEIVED > " + message.toString());

      // Set reply conversation, for messages tagged with a conversation.
      int conversationID = message.conversationID;
      if (conversationID > 0) lastConversationID = conversationID;
   }

}
