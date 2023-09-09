import java.util.Arrays;

/**
 * Project 3 - WordGame
 *
 * In this program, we are conducting a word game. We are letting a user input a txt file containing a seed that
 * determines a random number as well as a list of words that will make up our library. From there, a library object
 * is created that will store the words in a library and choose a random word from that library as our solution to
 * the word game. From there, the user is prompted whether they are ready to play or not. If they are, a wordguesser
 * object is created that has multiple functions such as checking the guess while updating the playing field and then
 * printing the field for the user to see. Each round, before the user enters a guess, the current round number as
 * well as the current playing field are printed for the user to see. The user can make a guess, if they are right
 * then they win and the gamelog txt file is updated. If they are wrong and choose to continue to guess, then they
 * can keep guessing for 5 rounds total. If they exceed that, then they lose without getting the answer and the
 * gamelog txt file is updated. They also lose and the game log txt file is updated but don't see the solution if they
 * choose to quit the game before finishing 5 rounds and they got a guess wrong. The user is then prompted whether
 * they will like to continue playing until they choose not to.
 *
 *
 * @author Raunak Agrawal, Lab Sec L06
 *
 * @version October 29, 2022
 */
public class WordGuesser {
    private String[][] playingField;
    private int round;
    private String solution;

    public WordGuesser(String solution) {
        this.solution = solution;
        this.round = 1;
        this.playingField = new String[5][5];
        for (String[] strings : playingField) {
            Arrays.fill(strings, " ");
        }
    }

    public String[][] getPlayingField() {
        return playingField;
    }

    public void setPlayingField(String[][] playingField) {
        this.playingField = playingField;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public boolean checkGuess(String guess) throws InvalidGuessException {
        if (guess.length() != 5) {
            throw new InvalidGuessException("Invalid Guess!");
        }
        if (guess.equalsIgnoreCase(this.solution)) {
            for (int i = 0; i < guess.length(); i++) {
                playingField[this.round - 1][i] = "'" + guess.charAt(i) + "'";
            }
            return true;
        } else {
            for (int i = 0; i < guess.length(); i++) {
                if (this.solution.contains("" + guess.charAt(i))) {
                    if (solution.charAt(i) == guess.charAt(i)) {
                        playingField[this.round - 1][i] = "'" + guess.charAt(i) + "'";
                    } else {
                        playingField[this.round - 1][i] = "*" + guess.charAt(i) + "*";
                    }
                } else {
                    playingField[this.round - 1][i] = "{" + guess.charAt(i) + "}";
                }
            }
            return false;
        }
    }

    public void printField() {
        for (String[] strings : this.playingField) {
            for (int j = 0; j < strings.length; j++) {
                if (j == 4) {
                    System.out.println(strings[j]);
                } else {
                    System.out.print(strings[j] + " | ");
                }
            }
        }
    }
}
