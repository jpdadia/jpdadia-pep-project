package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Message;
import Model.Account;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    public Javalin startAPI() {
        Javalin app = Javalin.create();
            app.get("/messages", this::getAllMessagesHandler);
            app.get("/messages/{id}", this::getMessageByIdHandler);
            app.get("/accounts/{id}/messages", this::getMessageFromUserHandler);
            app.post("/messages", this::createNewMessageHandler);
            app.delete("/messages/{id}", this::deleteMessageByIdHandler);
            app.patch("/messages/{id}", this::updateMessageHandler);

            app.post("/register", this::registerHandler);
            app.post("/login",this::loginHandler);
       
        app.get("example-endpoint", this::exampleHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = MessageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    private void getMessageByIdHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message message = MessageService.getMessageById(id);

        if (message != null){
            ctx.status(200).json(message);
        }else {
            ctx.status(200).result("");
        }
        }
    
    private void getMessageFromUserHandler(Context ctx){
        int userId = Integer.parseInt(ctx.pathParam("id"));
        List<Message> message = MessageService.getMessageFromUserMessages(userId);
        ctx.status(200).json(message);
        }
    
        private void createNewMessageHandler(Context ctx){
            Message message = ctx.bodyAsClass(Message.class);
            

            if (message.getMessage_text().isBlank() == true || message.getMessage_text().length() > 255
                || !AccountService.accountExist(message.getPosted_by()) ){
                ctx.status(400).result("");
                return;
            }
            Message newMessage = MessageService.createNewMessage(message);
            ctx.status(200).json(newMessage);
            
        }

        private void deleteMessageByIdHandler(Context ctx){
            int id = Integer.parseInt(ctx.pathParam("id"));
            Message message = MessageService.deleteMessageById(id);

            if (message != null){
                ctx.status(200).json(message);
            }
            else{
                ctx.status(200).result("");
            }
        
        }

        private void updateMessageHandler(Context ctx){
            int id = Integer.parseInt(ctx.pathParam("id"));
            Message message = MessageService.getMessageById(id);

            if (message == null){
                ctx.status(400).result("");
                return;
            }
            Message upMess = ctx.bodyAsClass(Message.class);
            if(upMess.getMessage_text().isBlank() || upMess.getMessage_text()
            .length()>255){
                ctx.status(400).result("");
                return;
            }
            message.setMessage_text(upMess.getMessage_text());
            Message updMess = MessageService.updateMessage(message);
            ctx.status(200).json(updMess);

        }
        

    private void exampleHandler(Context context) {
        context.json("sample text");
    }


    private void registerHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            ctx.status(400).result("");
            return;
        }
        if (AccountService.duplicateUsername(account.getUsername())){
            ctx.status(400).result("");
            return;
        }

        Account createdAccount = AccountService.createNewAccount(account);
        ctx.status(200).json(createdAccount);
    }

    public void loginHandler(Context ctx){
        Account acc = ctx.bodyAsClass(Account.class);
        Account acc2 = AccountService.logIn(acc.getUsername(),acc.getPassword());

        if (acc2 != null){
            ctx.status(200).json(acc2);
        }else{
            ctx.status(401).result("");
        }
    }
}