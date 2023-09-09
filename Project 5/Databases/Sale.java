package Databases;

import java.io.Serializable;

public class Sale implements Serializable {
    private Customer customer;
    private Product product;
    private double cost;
    private double revenue;
    private int quantitySold;
    private Store store;

    public Sale(Customer customer, Product product, double cost,
                int quantitySold, Store store) {
        this.customer = customer;
        this.product = product;
        this.revenue = quantitySold * cost;
        this.cost = cost;
        this.quantitySold = quantitySold;
        this.store = store;
    }

    synchronized public Store getStore() {
        return store;
    }

    synchronized public void setStore(Store store) {
        this.store = store;
    }

    synchronized public double getCost() {
        return cost;
    }

    synchronized public void setCost(double cost) {
        this.cost = cost;
    }

    synchronized public Customer getCustomer() {
        return customer;
    }

    synchronized public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    synchronized public Product getProduct() {
        return product;
    }

    synchronized public void setProduct(Product product) {
        this.product = product;
    }

    synchronized public double getRevenue() {
        return revenue;
    }

    synchronized public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    synchronized public int getQuantitySold() {
        return quantitySold;
    }

    synchronized public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
}
