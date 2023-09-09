package Databases;

import Databases.Customer;
import Databases.Product;
import Databases.Seller;

import java.io.*;
import java.util.ArrayList;

/**
 * Databases.Database
 * 
 * Cool trick to take every seller and store them in a file along with all their products so everythings easier
 * 
 * @author Andrew Li, Mia Sartain, Kyle Phillips, Raunak Agrawal, and Abhi Annabathula
 * 
 * @version November 14, 2022
 */
public class Database implements Serializable {
    private volatile ArrayList<User> users;

     /**
     * Instantiates every ArrayList that we need for the Databases.Database code
     */
    public Database() {
        users = new ArrayList<>();
    }

    /**
     * @param username The email that is being checked
     * @return TRUE if the email is invalid or taken or FALSE is the username is valid and ready to go
     */

    synchronized public boolean checkUsername(String username) {
        for (int i = 0; i < users.size(); i++) {
            if(username.equals(users.get(i).getUserID())) {
                return true;
            }
        }
        return false;
    }

    synchronized public boolean checkPassword(String username, String password) {
        for (User user : users) {
            if (user.getUserID().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param user The user to add to the user ArrayList
     */
    synchronized public void addUser(User user) {
        users.add(user);
    }

    /**
     * Update the allProducts ArrayList with every product in order of the sellers index in the sellers ArrayList
     */
    synchronized public ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Seller) {
                for (Store store : ((Seller)user).getStores()) {
                    allProducts.addAll(store.getProducts());
                }
            }
        }
        return allProducts;
    }

    /**
     * @return All of the customers in an ArrayList
     */
    synchronized public ArrayList<User> getUsers() {
        return users;
    }
    synchronized public ArrayList<Seller> getSellers() {
        ArrayList<Seller> sellers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Seller) {
                sellers.add((Seller)user);
            }
        }
        return sellers;
    }

    synchronized public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }
        return customers;
    }

    public synchronized void unloadDatabase() {
        try {
            FileOutputStream fileOut = new FileOutputStream("Database.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The current market that was loaded from Database.ser if it exists or creates a new Database.ser
     */
    public synchronized Database loadDatabase() {
        Database database = null;
        try {
            File f = new File("Database.ser");
            FileInputStream fileIn = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            database = (Database) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            return new Database();
        }
        return database;
    }

    synchronized public ArrayList<Product> sortPrice() {
        ArrayList<Product> allProducts = getAllProducts();
        int length = allProducts.size();
        if (length < 2) {
            return allProducts;
        }
        for (int i = 0; i < length-1; i++) {
            // Find the minimum element in unsorted array
            int minimum = i;
            for (int j = i + 1; j < length; j++) {
                if (allProducts.get(i).getPrice() < allProducts.get(minimum).getPrice()) {
                    minimum = j;
                }
            }
            // Swap the found minimum element with the first
            // element
            Product temp = allProducts.get(minimum);
            allProducts.set(minimum, allProducts.get(i));
            allProducts.set(i, temp);
        }
        return allProducts;
    }

    synchronized public ArrayList<Product> sortQuantity() {
        ArrayList<Product> allProducts = getAllProducts();
        int length = allProducts.size();
        if (length < 2) {
            return allProducts;
        }
        for (int i = 0; i < length-1; i++) {
            // Find the minimum element in unsorted array
            int minimum = i;
            for (int j = i + 1; j < length; j++) {
                if (allProducts.get(i).getQuantity() < allProducts.get(minimum).getQuantity()) {
                    minimum = j;
                }
            }
            // Swap the found minimum element with the first
            // element
            Product temp = allProducts.get(minimum);
            allProducts.set(minimum, allProducts.get(i));
            allProducts.set(i, temp);
        }
        return allProducts;
    }
}
