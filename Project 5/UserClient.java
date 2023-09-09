import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.stream.*;

import Databases.*;
import Records.*;
import Enums.*;

import javax.swing.*;

/**
 * UserClient Class
 * Contains all the GUI and user side commands
 * Intended to communicate with server but not store any information on this side
 */
class UserClient {

    public static Login Login(Icon icon) {
        String[] roles = {"Customer", "Seller"};
        String role = "";
        switch (JOptionPane.showOptionDialog(null, "Select Role", "GeologyRocks",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon, roles, roles[0])) {
            case 0:
                role = "Customer";
                break;
            case 1:
                role = "Seller";
                break;
            default:
                return null;
        }
        RequestType requestType;
        switch (JOptionPane.showOptionDialog(null, "Do you have an account?",
                "GeologyRocks", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon,
                null, null)) {
            case 0:
                requestType = RequestType.LOGIN;
                break;
            case 1:
                requestType = RequestType.CREATE_ACCOUNT;
                break;
            default:
                return null;
        }
        String username = (String) JOptionPane.showInputDialog(null, "Enter Email:",
                "GeologyRocks", JOptionPane.QUESTION_MESSAGE, icon, null, null);
        if (username == null) {
            return null;
        }
        String password = (String) JOptionPane.showInputDialog(null, "Enter Password",
                "GeologyRocks", JOptionPane.QUESTION_MESSAGE, icon, null, null);
        if (password == null) {
            return null;
        }
        return new Login(role, username, password, requestType);
    }

    public static int displayProduct(Product product, Icon icon) {
        return JOptionPane.showOptionDialog(null, product.toString(), "Geological Inspection",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                icon, new String[]{"Add to cart", "Buy immediately", "Exit"}, null);
    }

    public static int deleteProduct(ArrayList<Product> products) {
        StringBuilder productString = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            productString.append(i + 1).append(products.get(i).toString()).append("\n");
        }
        boolean isInteger = false;
        int productNum = 0;
        do {
            try {
                productNum = Integer.parseInt(JOptionPane.showInputDialog(null,
                        productString.toString() + "\n\n" + "What product would you like to delete?",
                        "Geology Rocks", JOptionPane.QUESTION_MESSAGE));
                if (productNum < 1 || productNum > products.size()) {
                    isInteger = true;
                    JOptionPane.showMessageDialog(null, "Please enter a valid product number!",
                            "Geology Rocks", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                isInteger = true;
                JOptionPane.showMessageDialog(null, "Please enter a valid product number!",
                        "Geology Rocks", JOptionPane.ERROR_MESSAGE);
            }
        } while (isInteger);
        return productNum;
    }

    public static int editProduct(ArrayList<Product> products) {
        StringBuilder productString = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            productString.append(i + 1).append(products.get(i).toString()).append("\n");
        }
        boolean isInteger = false;
        int productNum = 0;
        do {
            try {
                productNum = Integer.parseInt(JOptionPane.showInputDialog(null,
                        productString.toString() + "\n\n" + "What product would you like to edit?",
                        "Geology Rocks", JOptionPane.QUESTION_MESSAGE));
                if (productNum < 1 || productNum > products.size()) {
                    isInteger = true;
                    JOptionPane.showMessageDialog(null, "Please enter a valid product number!",
                            "Geology Rocks", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                isInteger = true;
                JOptionPane.showMessageDialog(null, "Please enter a valid product number!",
                        "Geology Rocks", JOptionPane.ERROR_MESSAGE);
            }
        } while (isInteger);
        return productNum;
    }

    public static void viewProducts(ArrayList<Product> products) {
        StringBuilder productString = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            productString.append(i + 1).append(products.get(i).toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, productString, "Geology Rocks",
                JOptionPane.PLAIN_MESSAGE);
    }

    // driver code
    public static void main(String[] args) {
        ImageIcon icon = new ImageIcon();
        ImageIcon error = new ImageIcon();
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;

        Product product;
        Database database = new Database();
        String productName = "";
        String productDescription = "";
        Store productStore;
        String search = "";
        Store[] storeList;
        int productQuantity = 0;
        double productPrice = 0.00;
        ArrayList<Product> products = new ArrayList<Product>();

        //establishing connection
        try (Socket socket = new Socket("localhost", 3000)) {

            outputStream = new ObjectOutputStream(socket.getOutputStream());

            inputStream = new ObjectInputStream(socket.getInputStream());

            JOptionPane.showMessageDialog(null, "Welcome to GeologyRocks!", "GeologyRocks",
                    JOptionPane.PLAIN_MESSAGE, icon);

            Login login;

            //Logging in
            while (true) {
                login = Login(icon);

                if (login == null) {
                    return;
                }
                outputStream.writeObject(login);
                outputStream.flush();
                try {
                    LoginResponse response = (LoginResponse) inputStream.readObject();
                    JOptionPane.showMessageDialog(null, response.reason(), "Geology Rocks",
                            JOptionPane.PLAIN_MESSAGE, icon);
                    if (response.successful()) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            application_loop:
            while (true) {
                if (login.role().equals("Customer")) {
                    Customer customer = new Customer(login.email(), login.password());
                    try {
                        RequestType[] options = new RequestType[]{RequestType.VIEW_MARKET, RequestType.SEARCH,
                                RequestType.EXPORT_PURCHASES, RequestType.VIEW_CART, RequestType.DELETE_ACCOUNT,
                                RequestType.SAVE_AND_QUIT};
                        switch ((RequestType) (JOptionPane.showInputDialog(null,
                                "Customer actions: ", "Geology Rocks", JOptionPane.PLAIN_MESSAGE,
                                icon, options, RequestType.VIEW_MARKET))) {
                            case VIEW_MARKET:
                                switch (JOptionPane.showOptionDialog(null, "Sort market by?",
                                        "Geology Rocks", JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.PLAIN_MESSAGE, icon,
                                        new RequestType[]{RequestType.SORT_PRICE, RequestType.SORT_QUANTITY,
                                                RequestType.VIEW_MARKET}, RequestType.SORT_PRICE)) {
                                    case 0:
                                        outputStream.writeObject(new Request(RequestType.SORT_PRICE, null));
                                        break;
                                    case 1:
                                        outputStream.writeObject(new Request(RequestType.SORT_QUANTITY, null));
                                        break;
                                    default:
                                        outputStream.writeObject(new Request(RequestType.VIEW_MARKET, null));
                                        break;
                                }
                                products =
                                        ((ViewMarketResponse) inputStream.readObject()).products();
                                try {
                                    product = (Product) JOptionPane.showInputDialog(null,
                                            "Dug up products from rock bottom",
                                            "Rock Marketplace", JOptionPane.PLAIN_MESSAGE, icon,
                                            products.toArray(new Product[0]), products.toArray(new Product[0])[0]);
                                    switch (displayProduct(product, icon)) {
                                        case 0:
                                            int quantity = Integer.parseInt((String) JOptionPane.showInputDialog(
                                                    null, "Quantity?",
                                                    "Rock Marketplace", JOptionPane.PLAIN_MESSAGE, icon,
                                                    Arrays.toString(IntStream.range(1,
                                                            product.getQuantity()).toArray()).split("[\\[\\]]")[1].split(", "), "0"));
                                            outputStream.writeObject(new Request(RequestType.ADD_TO_CART,
                                                    new ShoppingItem(quantity, product)));
                                            break;
                                        case 1:
                                            outputStream.writeObject(new Request(RequestType.BUY_PRODUCT, product));
                                            break;
                                        default:
                                            break;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    JOptionPane.showMessageDialog(null, "Oh pebbles!" +
                                                    " The marketplace is empty, please check back later.",
                                            "Geology Rocks",
                                            JOptionPane.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                                break;
                            case SEARCH:
                                //search for products, in try catch to show error when nothing is found
                                try {
                                    search = JOptionPane.showInputDialog(null,
                                            "Please enter your search below:", "Geology Rocks",
                                            JOptionPane.PLAIN_MESSAGE);
                                    ArrayList<Product> allProducts = database.getAllProducts();
                                    ArrayList<Product> results = new ArrayList<>();
                                    for (int h = 0; h < allProducts.size(); h++) {
                                        if (allProducts.get(h).toString().contains(search)) {
                                            results.add(allProducts.get(h));
                                        }
                                    }

                                    JOptionPane.showMessageDialog(null, "Your search found:" +
                                            results.toString(), "Geology Rocks", JOptionPane.INFORMATION_MESSAGE);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    JOptionPane.showMessageDialog(null, "Your search did not " +
                                            "find any results.", "Geology Rocks", JOptionPane.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                                break;
                            case EXPORT_PURCHASES:
                                int confirm = JOptionPane.showConfirmDialog(null, "Do you want " +
                                                "to export the file?", "Geology Rocks", JOptionPane.YES_NO_OPTION,
                                        JOptionPane.PLAIN_MESSAGE, icon);
                                if (confirm == 0) {
                                    //download file
                                    boolean confirmation = customer.exportPurchases("purchase_history.txt");
                                    if (confirmation) {
                                        JOptionPane.showMessageDialog(null, "Your rocking file" +
                                                        " was downloaded successfully!", "Geology Rocks",
                                                JOptionPane.PLAIN_MESSAGE, error);
                                        break;
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Your file was not " +
                                                "downloaded :(", "Geology Rocks", JOptionPane.ERROR_MESSAGE, error);
                                    }
                                }
                                break;
                            case VIEW_CART:
                                //pull up cart
                                JOptionPane.showMessageDialog(null, "Your cart contains:",
                                        "Geology Rocks" + customer.getCart().toString(), JOptionPane.INFORMATION_MESSAGE, icon);
                                break;
                            case DELETE_ACCOUNT:
                                int choice = JOptionPane.showConfirmDialog(null, "Are you " +
                                                "sure you want to delete your account?", "Geology Rocks",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
                                if (choice == 0) {
                                    //remove account
                                    customer.setUserID("");
                                    customer.setPassword("");
                                    JOptionPane.showMessageDialog(null, "Your account has been " +
                                            "deleted.", "Geology Rocks", JOptionPane.INFORMATION_MESSAGE);
                                }
                                break;
                            case SAVE_AND_QUIT:
                                JOptionPane.showMessageDialog(null, "Thanks for " +
                                                "rocking with us!", "Geology Rocks",
                                        JOptionPane.PLAIN_MESSAGE);
                                break;
                            default:
                                break;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                } else if (login.role().equals("Seller")) {
                    Seller seller = new Seller(login.email(), login.password());
                    String storesStr = JOptionPane.showInputDialog(null,
                            "Please add the stores you manage. List them separated by a " +
                                    "comma like 'Store1,Store2'",
                            "Geology Rocks", JOptionPane.PLAIN_MESSAGE);
                    String[] storeListStr;
                    if (storesStr.contains(",")) {
                        storeListStr = storesStr.split(",");
                        for (int g = 0; g < storeListStr.length; g++) {
                            Store store = new Store(storeListStr[g], products, seller);
                            seller.addStore(store);
                        }
                    } else {
                        Store store1 = new Store(storesStr, products, seller);
                        seller.addStore(store1);
                    }

                    try {
                        RequestType[] sellerOptions = new RequestType[]{RequestType.CREATE_PRODUCT,
                                RequestType.EDIT_PRODUCT, RequestType.DELETE_PRODUCT, RequestType.VIEW_PRODUCTS,
                                RequestType.VIEW_STATISTICS, RequestType.EXPORT_PRODUCTS,
                                RequestType.VIEW_ITEMS_IN_CARTS, RequestType.SAVE_AND_QUIT};
                        boolean loopForSeller = true;
                        do {
                            ArrayList<Store> stores = seller.getStores();
                            Store[] storeOptions = new Store[stores.size()];
                            for (int f = 0; f < stores.size(); f++) {
                                storeOptions[f] = stores.get(f);
                            }
                            switch ((RequestType) JOptionPane.showInputDialog(null, "Seller " +
                                            "Actions",
                                    "Geology Rocks", JOptionPane.PLAIN_MESSAGE, icon, sellerOptions,
                                    RequestType.CREATE_PRODUCT)) {
                                case CREATE_PRODUCT:
                                    boolean createAnother = false;
                                    do {
                                        productName = JOptionPane.showInputDialog(null,
                                                "What is the name of the product?", "Geology Rocks",
                                                JOptionPane.PLAIN_MESSAGE);
                                        productStore = (Store) JOptionPane.showInputDialog(null,
                                                "What store is this product being sold from?",
                                                "Geology Rocks", JOptionPane.PLAIN_MESSAGE, icon,
                                                storeOptions, storeOptions[0]);

                                        productDescription = JOptionPane.showInputDialog(null,
                                                "What is the product's description?", "Geology Rocks",
                                                JOptionPane.PLAIN_MESSAGE);
                                        boolean okValue;
                                        do {
                                            String quantityStr = JOptionPane.showInputDialog(null,
                                                    "How much of the product is in stock?", "Geology Rocks",
                                                    JOptionPane.PLAIN_MESSAGE);
                                            try {
                                                productQuantity = Integer.parseInt(quantityStr);
                                                okValue = false;
                                            } catch (Exception e) {
                                                JOptionPane.showMessageDialog(null, "Please " +
                                                                "enter a valid quantity!",
                                                        "Geology Rocks", JOptionPane.ERROR_MESSAGE, error);
                                                okValue = true;
                                            }
                                        } while (okValue);
                                        do {
                                            String priceStr = JOptionPane.showInputDialog(null,
                                                    "Please enter the price of the product.",
                                                    "Geology Rocks", JOptionPane.PLAIN_MESSAGE);
                                            try {
                                                productPrice = Double.parseDouble(priceStr);
                                                okValue = false;
                                            } catch (Exception e) {
                                                JOptionPane.showMessageDialog(null, "Please " +
                                                                "enter a valid price!",
                                                        "Geology Rocks", JOptionPane.ERROR_MESSAGE, error);
                                                okValue = true;
                                            }
                                        } while (okValue);
                                        boolean createdProduct = seller.createProduct(productName, productStore,
                                                productDescription, productQuantity, productPrice, 0);
                                        if (createdProduct) {
                                            //needs to create product object
                                            int option = JOptionPane.showConfirmDialog(null,
                                                    "Your product was made successfully! Would you like to " +
                                                            "create another?",
                                                    "Geology Rocks", JOptionPane.YES_NO_OPTION,
                                                    JOptionPane.PLAIN_MESSAGE);
                                            if (option == 1) {
                                                createAnother = true;
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Your product " +
                                                            "was not created.", "Geology Rocks",
                                                    JOptionPane.ERROR_MESSAGE, error);
                                        }
                                    } while (createAnother);
                                    break;
                                case EDIT_PRODUCT:
                                    products =
                                            ((ViewMarketResponse) inputStream.readObject()).products();
                                    boolean editAnother = false;
                                    do {
                                        int productNum = editProduct(products);
                                        Product editingProduct = products.get(productNum - 1);
                                        RequestType[] options = new RequestType[]{RequestType.EDIT_NAME,
                                                RequestType.EDIT_STORE
                                                , RequestType.EDIT_DESCRIPTION, RequestType.EDIT_QUANTITY,
                                                RequestType.EDIT_PRICE};
                                        boolean okValue;
                                        switch ((RequestType) (JOptionPane.showInputDialog(null,
                                                "What would you like to edit?",
                                                "Geology Rocks", JOptionPane.PLAIN_MESSAGE, null, options,
                                                RequestType.VIEW_MARKET))) {
                                            case EDIT_NAME:
                                                productName = JOptionPane.showInputDialog(null,
                                                        "Please enter the new name:", "Geology Rocks",
                                                        JOptionPane.PLAIN_MESSAGE);
                                                //modifyName with editingProduct and new name
                                                break;
                                            case EDIT_STORE:
                                                String productStoreStr = JOptionPane.showInputDialog(null,
                                                        "Please enter the new store:",
                                                        "Geology Rocks", JOptionPane.PLAIN_MESSAGE);
                                                break;
                                            case EDIT_DESCRIPTION:
                                                productDescription = JOptionPane.showInputDialog(null,
                                                        "Please enter the new description", "Geology Rocks",
                                                        JOptionPane.PLAIN_MESSAGE);
                                                break;
                                            case EDIT_QUANTITY:
                                                okValue = false;
                                                do {
                                                    String quantityStr = JOptionPane.showInputDialog(null,
                                                            "Please enter the new stock quantity:",
                                                            "Geology Rocks", JOptionPane.PLAIN_MESSAGE);
                                                    try {
                                                        productQuantity = Integer.parseInt(quantityStr);
                                                        okValue = false;
                                                    } catch (Exception e) {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Please enter a valid quantity!",
                                                                "Geology Rocks", JOptionPane.ERROR_MESSAGE, error);
                                                        okValue = true;
                                                    }
                                                } while (okValue);
                                                break;
                                            case EDIT_PRICE:
                                                okValue = false;
                                                do {
                                                    String priceStr = JOptionPane.showInputDialog(null,
                                                            "Please enter the new price:",
                                                            "Geology Rocks", JOptionPane.PLAIN_MESSAGE);
                                                    try {
                                                        productPrice = Double.parseDouble(priceStr);
                                                    } catch (Exception e) {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Please enter a valid price!",
                                                                "Geology Rocks", JOptionPane.ERROR_MESSAGE, error);
                                                        okValue = true;
                                                    }
                                                } while (okValue);
                                                break;
                                        }
                                        int moreEditing = JOptionPane.showConfirmDialog(null,
                                                "Your product has been " +
                                                        "changed to:\n" + editingProduct.toString() +
                                                        "\nWould you like to edit " +
                                                        "another one?", "Geology Rocks", JOptionPane.YES_NO_OPTION,
                                                JOptionPane.PLAIN_MESSAGE);
                                        if (moreEditing == 1) {
                                            editAnother = true;
                                        }
                                    } while (editAnother);
                                    //need to call modify____ for each one.
                                    break;
                                case DELETE_PRODUCT:
                                    //need to get products arraylist;

                                    boolean deleteAnother = false;
                                    products =
                                            ((ViewMarketResponse) inputStream.readObject()).products();
                                    do {
                                        int productNum = deleteProduct(products);
                                        int sureDelete = JOptionPane.showConfirmDialog(null,
                                                "Are you sure you want to " +
                                                        "delete\n" + products.get(productNum - 1).getName(),
                                                "Geology Rocks",
                                                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                                        if (sureDelete == 0) {
                                            String[] noOptions = new String[]{"Delete", "Return"};
                                            int newOption = JOptionPane.showOptionDialog(null,
                                                    "Do you want to delete a " +
                                                            "product or return to menu?", "Geology Rocks",
                                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null
                                                    , noOptions, noOptions[0]);
                                            if (newOption == 0) {
                                                deleteAnother = true;
                                            } else if (newOption == 1) {
                                                deleteAnother = false;
                                            }
                                        } else if (sureDelete == 1) {
                                            //then need to call deleteProduct method in class with productNum as input and
                                            // product Array list
                                            int oneMore = JOptionPane.showConfirmDialog(null,
                                                    "Your product has been " +
                                                            "deleted. Would you like to delete one more?",
                                                    "Geology Rocks",
                                                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                                            if (oneMore == 0) {
                                                deleteAnother = false;
                                            } else if (oneMore == 1) {
                                                deleteAnother = true;
                                            }
                                        }
                                    } while (deleteAnother);

                                    break;
                                case VIEW_PRODUCTS:
                                    products =
                                            ((ViewMarketResponse) inputStream.readObject()).products();
                                    viewProducts(products);
                                    break;
                                case VIEW_STATISTICS:
                                    //seller.viewStats();
                                    break;
                                case EXPORT_PRODUCTS:
                                    String fileName = JOptionPane.showInputDialog(null, "Please" +
                                                    " enter the file name of which you wish to export to:",
                                            "Geology Rocks", JOptionPane.PLAIN_MESSAGE);
                                    boolean exportSuccess = seller.exportProducts(fileName);
                                    if (exportSuccess) {
                                        JOptionPane.showMessageDialog(null, "Export successful!",
                                                "Geology Rocks",
                                                JOptionPane.PLAIN_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Error in exporting",
                                                "Geology Rocks",
                                                JOptionPane.PLAIN_MESSAGE, error);
                                    }
                                    break;
                                case VIEW_ITEMS_IN_CARTS:
                                    //no is in shopping cart var anymore
                                case SAVE_AND_QUIT:
                                    JOptionPane.showMessageDialog(null, "Thanks for " +
                                                    "rocking with us!", "Geology Rocks",
                                            JOptionPane.PLAIN_MESSAGE);
                                    loopForSeller = false;
                                    //need to unload anything here?
                                    break;
                            }
                        } while (loopForSeller);
                        //Biggest issue, how to access seller methods and how to get product Arraylist. And more
                        // functionality stuff that I need to try and figure out.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            // closing the input stream and output stream(see server for more details on unintentionally open sockets)
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
