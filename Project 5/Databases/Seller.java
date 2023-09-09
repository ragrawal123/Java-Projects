package Databases;

import java.io.*;
import java.util.*;
/**
 * Databases.Seller
 *
 * A class to hold the Databases.Seller's name, password, stores, and products
 *
 * @author Andrew Li, Mia Sartain, Kyle Phillips, Raunak Agrawal, and Abhi Annabathula
 *
 * @version November 14, 2022
 */
public class Seller extends User implements Serializable {
    private ArrayList<Store> stores;

    /**
     * Constructs Seller
     * @param userID Username
     * @param password Password
     */
    public Seller(String userID, String password) {
        super(userID, password);
        this.stores = new ArrayList<>();

    }

    /**
     *
     * @param productName The name of the product
     * @param associatedStore The store that the new product is in
     * @param description The description of the product
     * @param quantity How much of the item is in stock
     * @param price The price of the item
     * @param quantitySold How many times this item has sold
     */
    synchronized public boolean createProduct(String productName, Store associatedStore, String description,
                                                     int quantity, double price, int quantitySold) {
        Product product = new Product(this, productName, associatedStore, description,
                quantity, price, quantitySold);
        if (!checkProductName(product)){
            return false;
        }
        associatedStore.addProduct(product);
        return true;
    }

    /**
     *
     * @param product
     * @return
     */
    synchronized public boolean checkProductName(Product product) {
        for (Product products : product.getAssociatedStore().getProducts()) {
            if (products.equals(product)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param store
     * @param product
     * @return
     */
    synchronized public boolean deleteProduct(Store store, Product product) {
        for (Product products : store.getProducts()) {
            if (products.equals(product)) {
                store.removeProduct(product);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param filename
     * @return
     */
    synchronized public boolean importProducts(String filename) {
        File f = new File(filename);
        FileReader fr = null;
        try {
            fr = new FileReader(f);
        } catch (IOException e){
            return false;
        }
        BufferedReader bfr = new BufferedReader(fr);
        String toBeRead = "";
        try {
            while ((toBeRead = bfr.readLine()) != null){
                String[] splitInput = toBeRead.split(",");
                for (Store store : stores) {
                    if(store.getName().equals(splitInput[1])) {
                        createProduct(splitInput[0], store, splitInput[2],
                                Integer.parseInt(splitInput[3]), Double.parseDouble(splitInput[4]),
                                Integer.parseInt(splitInput[5]));
                    }
                }
            }
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param filename
     * @return
     */
    synchronized public boolean exportProducts(String filename) {
        File f = new File(filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
        } catch (IOException e){
            return false;
        }
        PrintWriter pw = new PrintWriter(fw, true);
        for (Store store : stores) {
            for (Product product : store.getProducts()) {
                pw.write(String.format("%s,%s,%s,%d,%f,%d\n", product.getName(),
                        product.getAssociatedStore(), product.getDescription(),
                        product.getQuantity(), product.getPrice(), product.getQuantitySold()));
            }
        }
        return true;
    }

    /**
     *
     * @param store
     * @param product
     * @param newProductName
     * @return
     */
    synchronized public boolean modifyName(Store store, Product product, String newProductName) {
        for (Product p : store.getProducts()) {
            if (p.getName().equals(newProductName)) {
                return false;
            }
        }
        product.setName(newProductName);
        return true;
    }

    /**
     * @param product The product that is having its store changed
     * @param newStore The new store that the product is in
     */
    synchronized public void modifyStore(Product product, Store newStore) {
        for (Store store : stores) {
            if (store.equals(newStore)) {
                store.addProduct(product);
                product.setAssociatedStore(store);
            }
        }
    }

    /**
     * @param product The product that is having its description changed
     * @param newDescription The new description that the product is getting
     */
    synchronized public void modifyDescription(Product product, String newDescription) {
        product.setDescription(newDescription);
    }

    /**
     * @param product The product that is having its quantity changed
     * @param newQuantity The new quantity that the product has
     */
    synchronized public void modifyQuantity(Product product, int newQuantity) {
        product.setQuantity(newQuantity);
    }

    /**
     * @param product The product that is having its price changed
     * @param newPrice The new price that the product has
     */
    synchronized public void modifyPrice(Product product, double newPrice) {
        product.setPrice(newPrice);
    }


    /**
     * @return The seller's stores in an ArrayList
     */
    synchronized public ArrayList<Store> getStores() {
        return stores;
    }

    /**
     * @param stores The new stores ArrayList for this seller
     */
    synchronized public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    synchronized public void addStore(Store store) {
        stores.add(store);
    }
}
