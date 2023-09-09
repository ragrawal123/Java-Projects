package Databases;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private String userID;
    private String password;

    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    /**
     * @return The Databases.Seller's username
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID What the new username is
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return This seller's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password What to change this seller's password to
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
