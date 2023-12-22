package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.util.ArrayList;

import java.sql.*;

public class MessageDAO {

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(? ,? ,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> listOfMessages = new ArrayList<Message>();
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message currentMessage = new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"), 
                                    rs.getLong("time_posted_epoch"));
                listOfMessages.add(currentMessage);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return listOfMessages;
    }

    public Message getMessageByID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message retrievedMessage = new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"), 
                                    rs.getLong("time_posted_epoch"));
                
                return retrievedMessage;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteMessageByID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            
            preparedStatement.executeUpdate();

        }catch(SQLException e){   
            System.out.println(e.getMessage()); 
        }
    }

    public void updateMessageByID(int id, String message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, id);
            
            preparedStatement.executeUpdate();

        }catch(SQLException e){   
            System.out.println(e.getMessage()); 
        }
    }

    public ArrayList<Message> getAllMessagesByUserID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> listOfMessages = new ArrayList<Message>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);
            
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message currentMessage = new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"), 
                                    rs.getLong("time_posted_epoch"));
                listOfMessages.add(currentMessage);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return listOfMessages;
    }
}
