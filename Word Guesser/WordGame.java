import java.util.*;
import java.io.*;

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
public class WordGame {

    public static String welcome = "Ready to play?";
    public static String yesNo = "1.Yes\n2.No";
    public static String noPlay = "Maybe next time!";
    public static String currentRoundLabel = "Current Round: ";
    public static String enterGuess = "Please enter a guess!";
    public static String winner = "You got the answer!";
    public static String outOfGuesses = "You ran out of guesses!";
    public static String solutionLabel = "Solution: ";
    public static String incorrect = "That's not it!";
    public static String keepPlaying = "Would you like to make another guess?";
    public static String fileNameInput = "Please enter a filename";

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println(fileNameInput);
        String fileName = s.nextLine();
        WordLibrary newLibrary = new WordLibrary(fileName);
        System.out.println(welcome + "\n" + yesNo);
        int start = s.nextInt();
        s.nextLine();
        if (start == 2) {
            System.out.println(noPlay);
            return;
        }
        ArrayList<String> guesses = new ArrayList<>();
        String[] allGuesses = new String[0];
        int continuePlaying;
        while (start == 1) {
            String solution = newLibrary.chooseWord();
            WordGuesser wordGuesser = new WordGuesser(solution);
            System.out.println(currentRoundLabel + wordGuesser.getRound());
            wordGuesser.printField();
            continuePlaying = 1;
            while (continuePlaying == 1) {
                if (wordGuesser.getRound() != 1) {
                    System.out.println(currentRoundLabel + wordGuesser.getRound());
                    wordGuesser.printField();
                }
                System.out.println(enterGuess);
                String guess = s.nextLine();
                guesses.add(guess);
                try {
                    if (wordGuesser.checkGuess(guess)) {
                        System.out.println(winner);
                        wordGuesser.printField();
                        allGuesses = new String[guesses.size()];
                        for (int i = 0; i < allGuesses.length; i++) {
                            allGuesses[i] = guesses.get(i);
                        }
                        updateGameLog(solution, allGuesses, true);
                        System.out.println(welcome + "\n" + yesNo);
                        start = s.nextInt();
                        s.nextLine();
                        if (start == 2) {
                            break;
                        } else {
                            allGuesses = new String[0];
                            guesses = new ArrayList<>();
                            continuePlaying = 2;
                        }
                    } else {
                        if (wordGuesser.getRound() == 5) {
                            boolean solved = false;
                            System.out.println(outOfGuesses);
                            System.out.println(solutionLabel + solution);
                            wordGuesser.printField();
                            allGuesses = new String[guesses.size()];
                            for (int i = 0; i < allGuesses.length; i++) {
                                allGuesses[i] = guesses.get(i);
                            }
                            updateGameLog(solution, allGuesses, solved);
                            System.out.println(welcome + "\n" + yesNo);
                            start = s.nextInt();
                            s.nextLine();
                            if (start == 2) {
                                break;
                            } else {
                                allGuesses = new String[0];
                                guesses = new ArrayList<>();
                                continuePlaying = 2;
                            }
                        } else {
                            System.out.println(incorrect);
                            wordGuesser.setRound(wordGuesser.getRound() + 1);
                            System.out.println(keepPlaying + "\n" + yesNo);
                            continuePlaying = s.nextInt();
                            s.nextLine();
                            if (continuePlaying == 2) {
                                wordGuesser.printField();
                                allGuesses = new String[guesses.size()];
                                for (int i = 0; i < allGuesses.length; i++) {
                                    allGuesses[i] = guesses.get(i);
                                }
                                updateGameLog(solution, allGuesses, false);
                                System.out.println(welcome + "\n" + yesNo);
                                start = s.nextInt();
                                s.nextLine();
                                if (start == 2) {
                                    break;
                                } else {
                                    allGuesses = new String[0];
                                    guesses = new ArrayList<>();
                                }
                            }
                        }
                    }
                } catch (InvalidGuessException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println(noPlay);

    }

    public static void updateGameLog(String solution, String[] guesses, boolean solved) {
        File file = new File("gamelog.txt");
        String solvedIt;
        try {
            if (file.createNewFile()) {
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
                int gamesCompleted = 1;
                printWriter.printf("Games Completed: %d\n", gamesCompleted);
                printWriter.printf("Game %d\n", gamesCompleted);
                printWriter.printf("- Solution: %s\n", solution);
                String guessString = "";
                for (int i = 0; i < guesses.length; i++) {
                    if (i == guesses.length - 1) {
                        guessString += guesses[i];
                    } else {
                        guessString += guesses[i] + ",";
                    }
                }
                printWriter.printf("- Guesses: %s\n", guessString);
                if (solved) {
                    solvedIt = "Yes";
                } else {
                    solvedIt = "No";
                }
                printWriter.printf("- Solved: %s\n", solvedIt);
                printWriter.close();
            } else {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                ArrayList<String> fileLines = new ArrayList<>();
                String[] arguments = bufferedReader.readLine().split(" ");
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    fileLines.add(line);
                }

                int gamesCompleted = Integer.parseInt(arguments[2]) + 1;
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, false));
                printWriter.printf("Games Completed: %d\n", gamesCompleted);
                for (String line : fileLines) {
                    printWriter.println(line);
                }
                printWriter.printf("Game %d\n", gamesCompleted);
                printWriter.printf("- Solution: %s\n", solution);
                String guessString = "";
                for (int i = 0; i < guesses.length; i++) {
                    if (i == guesses.length - 1) {
                        guessString += guesses[i];
                    } else {
                        guessString += guesses[i] + ",";
                    }
                }
                printWriter.printf("- Guesses: %s\n", guessString);
                if (solved) {
                    solvedIt = "Yes";
                } else {
                    solvedIt = "No";
                }
                printWriter.printf("- Solved: %s\n", solvedIt);
                printWriter.close();
                bufferedReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
