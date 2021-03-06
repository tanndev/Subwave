package com.tanndev.subwave.client.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by James Tanner on 7/5/2015.
 */
public class ChatPanel extends JPanel {

   private static final String BLANK_CARD_NAME = "BLANK CARD";
   private SubwaveClientGUI parentUI;
   private CardLayout cardLayout;
   private ConcurrentHashMap<String, ChatCard> cardMap = new ConcurrentHashMap<String, ChatCard>();
   private ConversationElement displayedConversation;

   public ChatPanel(SubwaveClientGUI parentUI) {
      super(new CardLayout());
      this.parentUI = parentUI;

      cardLayout = (CardLayout) getLayout();

      // Create blank panel
      JPanel blankCard = new JPanel(new BorderLayout());
      JLabel blankCardLabel = new JLabel("No conversation selected.");
      blankCardLabel.setPreferredSize(new Dimension(500, 300));
      blankCardLabel.setHorizontalAlignment(SwingConstants.CENTER);
      blankCard.add(blankCardLabel, BorderLayout.CENTER);
      add(blankCard, BLANK_CARD_NAME);
   }

   public void displayConversation(ConversationElement conversation) {
      if (conversation == null) return;
      cardLayout.show(this, buildCardName(conversation));
      displayedConversation = conversation;
      conversation.setNewMessageFlag(false);
      parentUI.repaint();
   }

   public void addConversation(ConversationElement conversation) {
      if (conversation == null) return;
      ChatCard chatCard = new ChatCard(parentUI, conversation);
      String cardName = buildCardName(conversation);
      add(chatCard, cardName);
      cardMap.put(cardName, chatCard);
      conversation.setNewMessageFlag(true);
      parentUI.repaint();
   }

   public void removeConversation(ConversationElement conversation) {
      if (conversation == null) return;
      String cardName = buildCardName(conversation);
      ChatCard chatCard = cardMap.get(cardName);
      if (chatCard == null) return;
      remove(chatCard);
      cardLayout.show(this, BLANK_CARD_NAME);
      displayedConversation = null;
      parentUI.repaint();
   }

   public void postMessage(ConversationElement conversation, String message) {
      if (conversation == null) return;
      ChatCard chatCard = cardMap.get(buildCardName(conversation));
      if (chatCard == null) return;
      chatCard.postMessage(message);
      if (displayedConversation != conversation) conversation.setNewMessageFlag(true);
      parentUI.repaint();
   }

   public ConversationElement getDisplayedConversation() {
      return displayedConversation;
   }

   private String buildCardName(ConversationElement conversation) {
      return "connection " + conversation.connectionID + ", conversation " + conversation.conversationID;
   }
}
