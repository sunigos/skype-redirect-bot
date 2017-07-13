# General information
This bot for Skype that redirects all messages from one group chat (input) to another one (output).

# How to compile and run
To build this application you'll need <a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html">JDK</a> and <a href="https://maven.apache.org/install.html">Apache Maven</a> properly installed.<br>
1. Use command:<br>
`mvn install`
2. In the `target` directory you'll get file `SkypeRedirectBot-1.0-jar-with-dependencies.jar` - this is your executable. But you can't just run it since you need console access.
3. Create file `SkypeRedirectBot.bat` and fill in with this command:
`java -jar SkypeRedirectBot-1.0-jar-with-dependencies.jar`
<br>
Or you can use existing `.bat` file from the repository. Just copy it to the directory with your `.jar` file.

Now you can run your .bat file and enjoy the bot!<br>
After starting you'll see the message in the output chat: `Redirect bot is now online`<br>
To stop the bot just type `exit` in the console.

# What do you need
In order to run the bot you'll need:
1. Skype user name and password.
2. Two group chats (bot works only with group chats). Your Skype user should be present in boths chats.<br>

After running the bot will ask you to type username, password and both chat IDs. You can get chat ID by typing in the Skype chat:<br>
`/get name`

# How it looks like
In the output chat all messages from the input chat will be printed in the following format:
<br>
----- <chat_name> <date_and_time> -----<br>
-- <sender_name> --<br>
<message_text><br>

For example:<br><br>
----- Weekend barbecue 2017-07-13 22:13:40 -----<br>
-- Adam Gontier --<br>
I can take my guitar. What do you think?<br>
----- Weekend barbecue 2017-07-13 22:14:22 -----<br>
-- Bill Gates --<br>
That would be cool!<br>