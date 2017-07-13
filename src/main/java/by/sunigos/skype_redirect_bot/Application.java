package by.sunigos.skype_redirect_bot;

import by.sunigos.skype_redirect_bot.data.Configuration;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageEvent;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.events.chat.message.MessageSentEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NoPermissionException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.internal.chat.ChatGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Main application class.
 */
class Application {
    private Skype skype;
    private GroupChat inputChat;
    private GroupChat outpuChat;

    /**
     * Constructor.
     */
    Application() {
        Configuration configuration = readConfiguration();
        initSkype(configuration);
        setConsoleControl();
    }

    /**
     * Read configuration from console.
     *
     * @return Configuration.
     */
    private Configuration readConfiguration() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Skype username:");
        String username = in.nextLine();
        System.out.println("Enter Skype password:");
        String password = in.nextLine();
        System.out.println("Enter group chat ID for input (type '/get name' in the chat to get its ID):");
        String inputChat = in.nextLine();
        System.out.println("Enter group chat ID for output (type '/get name' in the chat to get its ID):");
        String outputChat = in.nextLine();
        return new Configuration(inputChat, outputChat, username, password);
    }

    /**
     * Initialize Skype bot and connect.
     *
     * @param configuration Configuration.
     */
    private void initSkype(Configuration configuration) {
        skype = new SkypeBuilder(configuration.getUsername(), configuration.getPassword()).withAllResources().build();
        try {
            skype.login();
        } catch (ConnectionException e) {
            System.err.println("Connection failed");
            emergencyExit();
        } catch (InvalidCredentialsException e) {
            System.err.println("Invalid Skype credentials");
            System.exit(2);
        } catch (NotParticipatingException e) {
            e.printStackTrace();
            System.err.println("Authentication problem. Something wrong with chat");
            System.exit(3);
        }
        System.out.println("Logged in as: " + configuration.getUsername());
        skype.getEventDispatcher().registerListener(new Listener() {
            @EventHandler
            public void onMessageReceived(MessageReceivedEvent event) throws ConnectionException {
                redirectMessageToOutputChat(event);
            }
        });
        skype.getEventDispatcher().registerListener(new Listener() {
            @EventHandler
            public void onMessageSend(MessageSentEvent event) throws ConnectionException {
                redirectMessageToOutputChat(event);
            }
        });
        try {
            skype.subscribe();
        } catch (ConnectionException e) {
            e.printStackTrace();
            System.err.println("Skype subscribe connection problem");
            System.exit(4);
        }
        try {
            outpuChat = skype.joinChat(configuration.getOutputChat());
            inputChat = skype.joinChat(configuration.getInputChat());
        } catch (NoPermissionException e) {
            System.err.println("Skype has no permissions to join chat");
            emergencyExit();
        } catch (ChatNotFoundException e) {
            System.err.println("Skype chat not found. Check your chat ID with '/get name'");
            emergencyExit();
        } catch (ConnectionException e) {
            e.printStackTrace();
            System.err.println("Connection exception while connecting to chat. Make sure you're using GROUP chat");
            emergencyExit();
        }
        System.out.println("Redirect bot is now online");
        sendMessage("Redirect bot is now online");
    }

    /**
     * Set bot to console control.
     */
    private void setConsoleControl() {
        Scanner in = new Scanner(System.in);
        System.out.println("Type 'exit' to close the program");
        while (true) {
            String line = in.nextLine();
            if ("exit".equals(line)) {
                break;
            }
        }
        close();
    }

    /**
     * Redirect message to outputChat.
     *
     * @param event Chat event.
     */
    private void redirectMessageToOutputChat(MessageEvent event) {
        if (event.getMessage().getChat().getClass() == ChatGroup.class
                && !((ChatGroup) event.getMessage().getChat()).getTopic().equals(inputChat.getTopic())) {
            return;
        }
        String chatName = ((ChatGroup) event.getMessage().getChat()).getTopic();
        String sender = "UNABLE TO GET SENDER NAME";
        try {
            sender = event.getMessage().getSender().getDisplayName();
        } catch (ConnectionException e) {
            System.err.println("Unable to get sender name");
            e.printStackTrace();
        }
        String message = event.getMessage().getContent().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(event.getMessage().getSentTime()));
        sendMessage(String.format("----- %s %s -----\n-- %s -->\n%s", chatName, date, sender, message));
    }

    /**
     * Exit program.
     */
    private void close() {
        sendMessage("Redirect bot is now offline");
        try {
            skype.logout();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Close the program keeping console open.
     */
    private void emergencyExit() {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.exit(1);
    }

    /**
     * Function for sending message to the Skype.
     *
     * @param message Message.
     */
    private void sendMessage(String message) {
        try {
            outpuChat.sendMessage(message);
        } catch (ConnectionException e) {
            e.printStackTrace();
            System.err.println("Unable to send message");
            emergencyExit();
        }
    }
}
