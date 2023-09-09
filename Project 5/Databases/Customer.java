package Databases;

import Databases.Product;
import Databases.User;

import java.io.*;
import java.util.ArrayList;
/**
 * Databases.Customer
 * 
 * Holds all of the data that will be needed for a customer
 * 
 * @author Andrew Li, Mia Sartain, Kyle Phillips, Raunak Agrawal, and Abhi Annabathula
 * 
 * @version November 14, 2022 
 */
public class Customer extends User implements Serializable {

    private ArrayList<ShoppingItem> cart;
    private ArrayList<Sale> purchased;

    /**
     * @param userID The email of the customer
     * @param password The password of the customer
     */
    public Customer(String userID, String password) {
        super(userID, password);
        this.cart = new ArrayList<>();
        this.purchased = new ArrayList<>();
    }

    synchronized public void addToCart(ShoppingItem shoppingItem) {
        shoppingItem.product().addCustomersHolding(this);
        cart.add(shoppingItem);
    }

    synchronized public boolean buyoutCart() {
        for (ShoppingItem item : cart) {
            if(!buyItem(item)){
                return false;
            }
        }
        return true;
    }

    /* Very funny implementation
    A sale is created to be added to both the customer and the seller's lists
    However, if the item is to be overbought out, it can't happen, which is why the check is performed
    The check does the selling and the checking at the same time though
    So if the product is sold out instead of overbought, there won't be a store associated to it anymore
    since it essentially disappears from the market
    That's why through the sale itself it gets the store from the declaration at the top, and
    adds itself to that store with it's own method
     */
    synchronized public boolean buyItem(ShoppingItem item) {
        Sale sale = new Sale(this, item.product(), item.product().getPrice(),
                item.quantity(), item.product().getAssociatedStore());
        if (!item.product().sellProduct(item.quantity(), this)) {
            return false;
        }
        sale.getStore().addSale(sale);
        purchased.add(sale);
        return true;
    }

    /**
     * @return The full cart of this customer in the form of
     */
    synchronized public ArrayList<ShoppingItem> getCart() {
        return cart;
    }

    /**
     * @return Gets all of the items that this customer has purchased
     */
    synchronized public ArrayList<Sale> getPurchased() {
        return purchased;
    }

    /**
     * @param product The product that should no longer be in cart
     */
    synchronized public void removeFromCart(Product product) {
        product.removeCustomersHolding(this);
        cart.remove(product);
    }

    synchronized public boolean exportPurchases(String filename) {
        File f = new File(filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
        } catch (IOException e){
            return false;
        }
        PrintWriter pw = new PrintWriter(fw, true);
        for (int i = 0; i < purchased.size(); i++) {
            for (Sale sale : getPurchased()) {
                pw.write(purchased.get(i).toString());
            }
        }
        return true;
    }
}
