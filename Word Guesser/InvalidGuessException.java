/**
 * Project 3 - WordGame
 *
 * This class is what we use to create an invalid guess exception and print that out to the terminal if the user
 * enters an invalid guess.
 *
 * @author Raunak Agrawal, Lab Section L06
 *
 * @version October 29, 2022
 */
public class InvalidGuessException extends Exception {
    public InvalidGuessException(String message) {
        super(message);
    }
}
