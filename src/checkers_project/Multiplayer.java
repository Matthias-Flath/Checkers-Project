package checkers_project;
// Creates a client side and server side Java Socket to enable multiplayer.

// Classes and interfaces that control low-level communication.
import java.net.*;

// Provides input and output streams to use while communicating
import java.io.*;

// We need to figure out which port we are going to use for the server socket.

public class Multiplayer {
    private ServerSocket ServerSocket;
    private Socket clientSocket;

    private int Port;
    private String ipAddress;

    public void start(int port) {
        // The server has to specify which port it is using.
        serverSocket = new ServerSocket(6666);

        // The Client needs to know the server's IP address and port number.
        clientSocket = new Socket(ipAddress, Port);
    }

}
