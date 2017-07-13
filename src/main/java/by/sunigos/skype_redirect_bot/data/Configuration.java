package by.sunigos.skype_redirect_bot.data;

/**
 * Configuration for skype redirect bot.
 */
public class Configuration {
    private String inputChat;
    private String outputChat;
    private String username;
    private String password;

    /**
     * Constructor.
     *
     * @param inputChat  Chat for input messages.
     * @param outputChat Chat for output messages.
     * @param username   Skype username.
     * @param password   Skype password.
     */
    public Configuration(String inputChat, String outputChat, String username, String password) {
        this.inputChat = inputChat;
        this.outputChat = outputChat;
        this.username = username;
        this.password = password;
    }

    public String getInputChat() {
        return inputChat;
    }

    public String getOutputChat() {
        return outputChat;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
