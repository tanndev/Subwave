package com.tanndev.subwave.client.ui.gui;

import com.tanndev.subwave.client.core.SubwaveClient;

/**
 * Created by jtanner on 7/3/2015.
 */
public class ConversationElement implements Comparable<ConversationElement> {
   public final int connectionID;
   public final int conversationID;

   private boolean newMessageFlag = false;

   public ConversationElement(int connectionID, int conversationID) {
      this.connectionID = connectionID;
      this.conversationID = conversationID;
   }

   @Override
   public String toString() {
      return SubwaveClient.getName(connectionID, conversationID);
   }


   @Override
   public int compareTo(ConversationElement o) {
      int result = connectionID - o.connectionID;
      if (result == 0) result = conversationID - o.conversationID;
      return result;
   }

   @Override
   public boolean equals(Object o) {
      return compareTo((ConversationElement) o) == 0;
   }

   public boolean hasNewMessage() {return newMessageFlag;}

   public void setNewMessageFlag(boolean newMessageFlag) {this.newMessageFlag = newMessageFlag;}
}
