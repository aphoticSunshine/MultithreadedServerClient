import java.io.*;
import java.net.*;
import java.util.*;
// Client class
class Client {

    // driver code
    public static void main(String[] args)
    {
        // establish a connection by providing host and port number
        try (Socket socket = new Socket("localhost", 1234)) {

            // writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter fileWriter = null;
            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null;
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("| Please enter a URL to be converted into an html file, preceded by http:// or https:// |");
            System.out.println("-----------------------------------------------------------------------------------------");
            while (!"exit".equalsIgnoreCase(line)) {
                // reading from userf
                line = sc.nextLine();
                // sending the user input to server
                out.println(line);
                out.flush();
                //If the user submits a valid url, takes output from server and asks user for the file name for it to be
                //saved under.
                if (line.contains("http://") || line.contains("https://")) {
                    String code = in.readLine();
                    if(code != null){
                        //Takes user input name and automatically creates and stores new HTML file to current user directory
                        System.out.println(" Please enter the name for your file");
                        String name = sc.nextLine();
                        String userdir = System.getProperty("user.dir");
                        File file = new File(userdir + "\\" + name + ".html");
                        file.createNewFile();
                        fileWriter = new BufferedWriter(new FileWriter(file, true), 100000);
                        fileWriter.write(code);
                        fileWriter.flush();
                        System.out.println(" Successfully saved the webpage HTML at " + file);
                    }
                }
                else if(!"exit".equalsIgnoreCase(line)){
                    //Default response from server asking for url
                    System.out.println(" Server replied: " + in.readLine());
                }
            }
            System.out.println("Disconnected from server.");

            // closing the scanner and buffered writer objects
            sc.close();
            if(fileWriter!=null){
                fileWriter.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}