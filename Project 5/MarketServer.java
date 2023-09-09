import java.io.*;
import java.net.*;

import Databases.*;
import Records.*;
import Enums.*;

import javax.swing.*;

/**
 * MarketServer class
 * Stores all the information and intended to update on each change
 * Should have methods for data return functionality
 */
class MarketServer {

    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(3000);
            //Annoying thing that happens in python sockets(my experience) is that you have to wait for TIME_WAIT in
            //windows terminal for it to do another connection. This lets it not have that wait time
            server.setReuseAddress(true);

            // Constantly waiting for a client
            while (true) {
                //As soon as it gets a client, immediately makes a thread for it(hence inbuilt synchronization)
                Socket client = server.accept();
                System.out.println("New client connected "
                        + client.getInetAddress()
                        .getHostAddress());
                ClientHandler clientSock
                        = new ClientHandler(client);
                new Thread(clientSock).start();
            }
        }
        // Makes sure to close the server on crash instead of it staying open and being annoying(have to
        //manually close it through terminal)
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // One of these acts as a sub server for each client to deal with them separately
    private static class ClientHandler implements Runnable {
        private Database database = new Database();
        private final Socket clientSocket;
        ObjectInputStream inputStream;

        // Constructor
        public ClientHandler(Socket socket)
                throws IOException {
            this.clientSocket = socket;
            database = database.loadDatabase();
            inputStream = new ObjectInputStream(socket.getInputStream());
        }

        public void run() {
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(
                        clientSocket.getOutputStream());
                //main block
                try {
                    User user = new User(null, null);
                    //login
                    login:
                    while (true) {
                        Login login = (Login) inputStream.readObject();
                        switch (login.requestType()) {
                            case LOGIN:
                                LoginResponse loginResponse = LogIn(login);
                                if (loginResponse.successful()) {
                                    if (login.role().equals("Seller")) {
                                        for (Seller seller : database.getSellers()) {
                                            if (seller.getUserID().equals(login.email())) {
                                                user = seller;
                                            }
                                        }
                                    } else {
                                        for (Customer customer : database.getCustomers()) {
                                            if (customer.getUserID().equals(login.email())) {
                                                user = customer;
                                            }
                                        }
                                    }
                                    outputStream.writeObject(loginResponse);
                                    outputStream.flush();
                                    break login;
                                } else {
                                    outputStream.writeObject(loginResponse);
                                    outputStream.flush();
                                }
                                break;
                            case CREATE_ACCOUNT:
                                LoginResponse createResponse = CreateAccount(login);
                                if (createResponse.successful()) {
                                    if (login.role().equals("Seller")) {
                                        user = new Seller(login.email(), login.password());
                                    } else {
                                        user = new Customer(login.email(), login.password());
                                    }
                                    outputStream.writeObject(createResponse);
                                    outputStream.flush();
                                    break login;
                                } else {
                                    outputStream.writeObject(createResponse);
                                    outputStream.flush();
                                }
                                break;
                        }
                    }

                    while (true) {
                        Request request = (Request) inputStream.readObject();
                        switch (request.type()) {
                            case VIEW_MARKET:
                                outputStream.writeObject(new ViewMarketResponse(database.getAllProducts()));
                                break;
                            case SORT_PRICE:
                                outputStream.writeObject(new ViewMarketResponse(database.sortPrice()));
                                break;
                            case SORT_QUANTITY:
                                outputStream.writeObject(new ViewMarketResponse(database.sortQuantity()));
                                break;
                            case ADD_TO_CART:
                                ((Customer) user).addToCart((ShoppingItem) request.object());
                                break;
                            case BUY_PRODUCT:
                                ((Customer) user).buyItem((ShoppingItem) request.object());
                                break;

                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                        clientSocket.close();
                    }
                    //saves the information
                    database.unloadDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Login functionality on server side
        public LoginResponse LogIn(Login login) {
            if (!login.email().contains("@")) {
                return new LoginResponse(false, "Please enter an email.");
            }
            if (!database.checkUsername(login.email())) {
                return new LoginResponse(false, "Email does not match any accounts.");
            }
            if (!database.checkPassword(login.email(), login.password())) {
                return new LoginResponse(false, "Incorrect password.");
            }
            return new LoginResponse(true, "Successful Login!");
        }

        public LoginResponse CreateAccount(Login login) {
            if (!login.email().contains("@")) {
                return new LoginResponse(false, "Please enter an email.");
            }
            if (database.checkUsername(login.email())) {
                return new LoginResponse(false, "Email already in use.");
            }

            return new LoginResponse(true, "Successful Account Creation!");
        }
    }
}
