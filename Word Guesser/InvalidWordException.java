/**
 * Project 3 - WordGame
 *
 * This class is what we use to create an invalid word exception and print that out to the terminal if the user
 * enters an invalid word into their input file.
 *
 * @author Raunak Agrawal, Lab Section L06
 *
 * @version October 29, 2022
 */
public class InvalidWordException extends Exception {
    public InvalidWordException(String message) {
        super(message);
    }
}
