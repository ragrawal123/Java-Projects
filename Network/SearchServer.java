import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Homework 11 -- Search Engine
 *
 * In this program, we are creating a Search Engine that will take user input, such as a keyword, and return a list of
 * things the user might be looking for. For example, if the user were to enter the word Purdue, we are returning a
 * title from the searchDatabase.txt that has the word Purdue in it or in its description. The point of this program
 * is to basically act as a mini search engine for a specific set of stuff from a database txt. In this file
 * specifically, SearchServer, we are doing all the processing for the stuff that is happening on the Client side
 * such as returning the titles that have the searchedText and returning the description of whatever title the user
 * chooses.
 *
 * Use Host: localhost
 * PortNumber: 4204
 *
 *
 * @author Raunak Agrawal, Lab Sec L06
 *
 * @version November 7, 2022
 */
public class SearchServer {

    public static void main(String[] args) throws IOException {
        ArrayList<String> fileContent = new ArrayList<>();
        File file = new File("searchDatabase.txt");
        ServerSocket serverSocket = new ServerSocket(4204);
        System.out.println("Connecting server...");
        Socket socket = serverSocket.accept();
        System.out.println("Successful connection!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                fileContent.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] content = new String[fileContent.size()];
        for (int i = 0; i < fileContent.size(); i++) {
            content[i] = fileContent.get(i);
        }
        while (true) {
            try {
                boolean empty = false;
                String searchText = reader.readLine();
                ArrayList<String> result = new ArrayList<>();
                for (String line : content) {
                    if (line.toLowerCase().contains(searchText.toLowerCase())) {
                        result.add(line);
                    }
                }
                if (result.size() == 0) {
                    writer.write("No results found,");
                    empty = true;
                } else {
                    for (String line : result) {
                        writer.write(line.split(";")[1] + ",");
                    }
                }
                writer.println();
                writer.flush();
                if (!empty) {
                    String index = reader.readLine();
                    String description = "";
                    for (String s : content) {
                        if ((s.split(";"))[1].equalsIgnoreCase(index)) {
                            description = (s.split(";"))[2];
                        }
                    }
                    writer.write(description);
                    writer.println();
                    writer.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
