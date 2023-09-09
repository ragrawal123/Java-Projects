package Databases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Databases.Product
 * 
 * The product class that holds all of the stats and information that a product needs to
 * 
 * @author Andrew Li, Mia Sartain, Kyle Phillips, Raunak Agrawal, and Abhi Annabathula
 * 
 * @version November 14, 2022
 */
public class Product implements Serializable {
    private Seller seller;
    private String name;
    private Store associatedStore;
    private String description;
    private int quantity;
    private double price;
    private int quantitySold;
    private ArrayList<Customer> customersHolding;

    /**
     * @param seller the seller
     * @param name The name of the product
     * @param associatedStore The Databases.Store that this object is in
     * @param description The description of the product
     * @param quantity The amount of quantity in stock
     * @param price The price of the product
     * @param quantitySold The amount of this item that was sold
     */
    public Product(Seller seller, String name, Store associatedStore, String description, int quantity,
                   double price, int quantitySold) {
        this.seller = seller;
        this.name = name;
        this.associatedStore = associatedStore;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.quantitySold = quantitySold;
        this.customersHolding = new ArrayList<>();
    }

    /**
     * @param quantitySold The amount that was sold to subtract from the current quantity
     */
    synchronized public boolean sellProduct(int quantitySold, Customer customer) {
        if (quantity - quantitySold < 0) {
            return false;
        }
        quantity -= quantitySold;
        addQuantitySold(quantitySold);
        customersHolding.remove(customer);
        if (quantity == 0) {
            this.getAssociatedStore().removeProduct(this);
        }
        return true;
    }

    //Overwrites equals method such that a product with the same name will return true(more on this in stores
    synchronized public boolean equals(Object o) {
        Product compared = (Product) o;
        if (compared.getName().equals(name) || compared.getAssociatedStore().equals(associatedStore)) {
            return true;
        }
        return false;
    }

    /**
     * @return The products name
     */
    synchronized public String getName() {
        return name;
    }

    synchronized public Seller getSeller() {
        return seller;
    }

    synchronized public void setSeller(Seller seller) {
        this.seller = seller;
    }

    synchronized public ArrayList<Customer> getCustomersHolding() {
        return customersHolding;
    }

    synchronized public void addCustomersHolding(Customer customer) {
        this.customersHolding.add(customer);
    }

    synchronized public void removeCustomersHolding(Customer customer) {
        this.customersHolding.remove(customer);
    }

    /**
     * @return The amount of this item that was sold
     */
    synchronized public int getQuantitySold() {
        return quantitySold;
    }

    /**
     * @param quantityToBeSold How much to increase the global quantitySold by
     */
    synchronized public void addQuantitySold(int quantityToBeSold) {
        this.quantitySold += quantityToBeSold;
    }

    /**
     * @return The Databases.Store that this product is in
     */
    synchronized public Store getAssociatedStore() {
        return associatedStore;
    }

    /**
     * @return The description of this product
     */
    synchronized public String getDescription() {
        return description;
    }

    /**
     * @return The current quantity in stock of this object
     */
    synchronized public int getQuantity() {
        return quantity;
    }

    /**
     * @return The price of this product
     */
    synchronized public double getPrice() {
        return price;
    }

    /**
     * @param name What to set the product name to
     */
    synchronized public void setName(String name) {
        this.name = name;
    }

    /**
     * @param associatedStore What to change the name of the Databases.Store that this product is in
     */
    synchronized public void setAssociatedStore(Store associatedStore) {
        this.associatedStore = associatedStore;
    }

    /**
     * @param description What to change the description of the product to
     */
    synchronized public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param quantity The current stock
     */
    synchronized public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @param price What to set the price of this product to
     */
    synchronized public void setPrice(double price) {
        this.price = price;
    }

    synchronized public String toString() {
        return String.format("%10s|%10s|%30s|%d|%.2f", name, associatedStore.getName(), description, quantity, price);
    }

}
