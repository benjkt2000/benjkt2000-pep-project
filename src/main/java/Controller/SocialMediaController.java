package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
       this.accountService = new AccountService();
       this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::postCreateAccountHandler);
        app.post("/login", this::postVerifyAccountHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getRetrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::getRetrieveMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteRemoveMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchModifyMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getRetrieveAllMessagesByUserIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postCreateAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    private void postVerifyAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(ctx.body());
        Account verifiedAccount = accountService.verifyAccount(jsonNode.get("username").asText(), 
                jsonNode.get("password").asText());

        if(verifiedAccount!=null){
            ctx.json(mapper.writeValueAsString(verifiedAccount));
        }else{
            ctx.status(401);
        }
    }

    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getRetrieveAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Message> listOfMessages = messageService.retrieveAllMessages();
        if(listOfMessages!=null)
            ctx.json(mapper.writeValueAsString(listOfMessages));
    }

    private void getRetrieveAllMessagesByUserIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Message> listOfMessages = messageService.retrieveAllMessagesByUserID(Integer.parseInt(ctx.pathParam("account_id")));
      
        if(listOfMessages!=null)
            ctx.json(mapper.writeValueAsString(listOfMessages));
    }

    private void getRetrieveMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = messageService.retrieveMessageByID(Integer.parseInt(ctx.pathParam("message_id")));

        if(message!=null)
            ctx.json(mapper.writeValueAsString(message));
    }

    private void deleteRemoveMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = messageService.removeMessageByID(Integer.parseInt(ctx.pathParam("message_id")));

        if(message!=null)
            ctx.json(mapper.writeValueAsString(message));
    }

    private void patchModifyMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(ctx.body());

        Message message = messageService.modifyMessageByID(Integer.parseInt(ctx.pathParam("message_id")), jsonNode.get("message_text").asText());

        if(message!=null) {
            ctx.json(mapper.writeValueAsString(message));
        }
        else {
            ctx.status(400);
        }    
    }
}