package Records;

import Databases.Product;

import java.io.Serializable;
import java.util.ArrayList;

public record ViewMarketResponse(ArrayList<Product> products) implements Serializable {
}
