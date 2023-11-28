
import java.io.*;
import java.net.*;


// Server class
class Server {
    public static void main(String[] args)
    {
        ServerSocket server = null;
        System.out.println("----------------------");
        System.out.println("| Server initialized |");
        System.out.println("----------------------");
        try {
            //server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            //running infinite loop for getting client request
            while (true) {
                //socket object to receive incoming client requests
                Socket client = server.accept();

                //Displaying that a new client is connected to server
                System.out.println("New client connected from " + client.getInetAddress().getHostAddress());
                //create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                //This thread will handle the client separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        //Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            BufferedReader urlStream = null;
            try {

                //gets the outputstream of client
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                //gets the inputstream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                String url;
                String code = "";
               while ((line = in.readLine()) != null) {
                   // writing the received message from client
                   System.out.printf(" Sent from a client: %s\n", line);
                   // Conditional statement to check if the url provided is valid (very rudimentary)
                   // Opens a URL stream to pull html context from provided url and outputs HTML content to the client
                   if(line.contains("http://") || line.contains("https://")){
                        urlStream = new BufferedReader(new InputStreamReader(new URL(line).openStream()));
                        while((url = urlStream.readLine())!=null){
                            code+=url;
                        }
                        out.println(code);
                        System.out.println(" Successfully retrieved and sent webpage HTML!");
                        continue;
                    }
                   if(line.equalsIgnoreCase("exit")){
                       System.out.println("Client disconnected.");
                       continue;
                   }
                    out.println("Try submitting a URL instead!");
                }
            }
            catch (MalformedURLException mue) {
                System.out.println("Malformed URL Exception");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                    if (urlStream != null){
                        urlStream.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}