import javax.swing.*;
import java.io.*;
import java.net.*;

/**
 * Homework 11 -- Search Engine
 *
 * In this program, we are creating a Search Engine that will take user input, such as a keyword, and return a list of
 * things the user might be looking for. For example, if the user were to enter the word Purdue, we are returning a
 * title from the searchDatabase.txt that has the word Purdue in it or in its description. The point of this program
 * is to basically act as a mini search engine for a specific set of stuff from a database txt. In this file
 * specifically, SearchClient, we are handling all of the stuff that the user will actually interact with using
 * JDialogs. We let the user enter the server they would like to connect to and then enter the words or text they
 * would like to search until they don't want to anymore.
 *
 * Use Host: localhost
 * PortNumber: 4204
 *
 *
 * @author Raunak Agrawal, Lab Sec L06
 *
 * @version November 7, 2022
 */
public class SearchClient {
    public static void main(String[] args) throws IOException {
        int openingMessage = JOptionPane.showConfirmDialog(null, "Welcome to the search engine!", "Search Engine",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (openingMessage == JOptionPane.CANCEL_OPTION || openingMessage == JOptionPane.CLOSED_OPTION) {
            return;
        }
        String hostName = JOptionPane.showInputDialog(null, "Enter the host name", "Search Engine",
                JOptionPane.QUESTION_MESSAGE);
        if (hostName == null) {
            return;
        }
        //if it's a string, then it will return null
        boolean isNumber = false;
        String portNumber;
        int number = 0;
        do {
            portNumber = JOptionPane.showInputDialog(null, "Enter the port number", "Search Engine",
                    JOptionPane.QUESTION_MESSAGE);
            if (portNumber == null) {
                return;
            }
            try {
                number = Integer.parseInt(portNumber);
                //if integer, will return CLOSED Or CANCEL Option, -1
                isNumber = true;
            } catch (Exception e) {
                //TA said these showMessageDialogs don't need to be handled.
                JOptionPane.showMessageDialog(null, "Please enter a valid port number", "Search Engine",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!isNumber);
        BufferedReader reader;
        PrintWriter writer;
        Socket clientSocket;
        try {
            clientSocket = new Socket(hostName, number);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server!", "Search Engine",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(null, "Server Connected!", "Search Engine",
                JOptionPane.INFORMATION_MESSAGE);

        boolean searchAgain;

        do {
            String searchText = JOptionPane.showInputDialog(null, "Please enter the text you would like to search:",
                    "Search Engine", JOptionPane.QUESTION_MESSAGE);
            if (searchText == null) {
                return;
            }
            writer.write(searchText);
            writer.println();
            writer.flush();


            String[] titles = reader.readLine().split(",");
            if (titles[0].equalsIgnoreCase("No results found")) {
                JOptionPane.showMessageDialog(null, "There were no available searches!", "Search Engine",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                String index = (String) JOptionPane.showInputDialog(null, "Please choose one from the menu:",
                        "Search Engine", JOptionPane.QUESTION_MESSAGE, null, titles, null);
                if (index == null) {
                    return;
                }
                writer.write(index);
                writer.println();
                writer.flush();

                String description = reader.readLine();
                JOptionPane.showMessageDialog(null, description, "Search Engine", JOptionPane.INFORMATION_MESSAGE);
            }

            int againOption = JOptionPane.showConfirmDialog(null, "Would you like to search again?", "Search Engine",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (againOption == JOptionPane.NO_OPTION || againOption == JOptionPane.CLOSED_OPTION) {
                return;
            }
            searchAgain = againOption == 0;
        } while (searchAgain);
        JOptionPane.showMessageDialog(null, "Thank you for using the Search Engine!", "Search Engine",
                JOptionPane.PLAIN_MESSAGE);
        reader.close();
        writer.close();
    }
}
