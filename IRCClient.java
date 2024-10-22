import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IRCClient {
private static final String SERVER = "irc.freenode.net"; 
private static final int PORT = 6667; 
private static final String CHANNEL = "#testchannel"; 
private static final String NICKNAME = "CJ";


public static void main(String[] args) {
    try (Socket socket = new Socket(SERVER, PORT);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

 
        out.println("NICK " + NICKNAME);
        out.println("USER " + NICKNAME + " 0 * :" + NICKNAME);
        out.println("JOIN " + CHANNEL);

 
        new Thread(() -> {
            String response;
            try {
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                  
                    if (response.startsWith("PING")) {
                        out.println("PONG " + response.split(" ")[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        String message;
        while ((message = userInput.readLine()) != null) {
            out.println("PRIVMSG " + CHANNEL + " :" + message);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}