package Service;

import Model.Message;
import Model.Account;
import DAO.MessageDAO;
import DAO.AccountDAO;

import static org.mockito.ArgumentMatchers.isNotNull;

import java.util.ArrayList;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message) {
        Account verifiedAccount = this.accountDAO.CheckExistingAccountID(message.getPosted_by());

        if(verifiedAccount != null) {
            if(message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255)
                return this.messageDAO.insertMessage(message);
        }

        return null;
    }

    public ArrayList<Message> retrieveAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public ArrayList<Message> retrieveAllMessagesByUserID(int id) {
        return this.messageDAO.getAllMessagesByUserID(id);
    }

    public Message retrieveMessageByID(int id) {
       return this.messageDAO.getMessageByID(id);
    }

    public Message removeMessageByID(int id) {
        Message existingMessage = this.messageDAO.getMessageByID(id);

        if(existingMessage != null) {
            this.messageDAO.deleteMessageByID(id);
            return existingMessage;
        }

        return null;
    }

    public Message modifyMessageByID(int id, String message) {
        Message existingMessage = this.messageDAO.getMessageByID(id);
        
        if(existingMessage != null){
            if(!message.trim().isEmpty() && message.length() <= 255){
                this.messageDAO.updateMessageByID(id, message);
                return this.messageDAO.getMessageByID(id);
            }
        }

        return null;
    }
}
