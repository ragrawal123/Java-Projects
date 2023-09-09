package Databases;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable {

    private String name;
    private ArrayList<Product> products;
    private Seller seller;
    private ArrayList<Sale> sales;

    /**
     * Store constructor
     * @param name
     * @param products
     * @param seller
     */
    public Store(String name, ArrayList<Product> products, Seller seller){

        this.name = name;
        this.products = products;

        if (products == null) {

            this.products = new ArrayList<>();

        }

        this.seller = seller;
        sales = new ArrayList<>();
    }

    synchronized public boolean equals(Object o) {
        Store compared = (Store)o;
        if (compared.getName().equals(this.getName())) {
            return true;
        }
        return false;
    }

    synchronized public void addSale(Sale sale) {
        sales.add(sale);
    }

    synchronized public ArrayList<Sale> getSales() {
        return sales;
    }

    synchronized public void addProduct(Product product){

        this.products.add(product);

    }

    synchronized public void removeProduct(Product product){
        for(int i = 0; i < this.products.size(); i++){
            if(product.equals(products.get(i))){
                products.remove(i);
                break;
            }
        }
    }

    synchronized public String getName(){
        return this.name;
    }

    synchronized public void setName(String Name){
        this.name = name;
    }

    synchronized public Seller getSeller() {
        return seller;
    }

    synchronized public void setSeller(Seller seller) {
        this.seller = seller;
    }

    synchronized public ArrayList<Product> getProducts(){
        return this.products;
    }

}
