import java.io.*;
import java.util.*;

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
 * Another special feature of the WordLibrary class is if that any inputs from the user library, such as words that
 * are greater than 5 letters, then invalid word! is printed to the terminal and that word is removed from the
 * library so that the library only contains correct words.
 *
 * @author Raunak Agrawal, Lab Sec L06
 *
 * @version October 29, 2022
 */
public class WordLibrary {

    private String[] library;
    private int seed;
    private Random random;
    private String fileName;

    public WordLibrary(String fileName)  {
        try {
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String[] seeding = bufferedReader.readLine().split(" ");
            this.seed = Integer.parseInt(seeding[1]);

            int length = 0;
            ArrayList<String> libraryTemp = new ArrayList<>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                libraryTemp.add(line);
                length++;
            }

            this.library = new String[length];
            for (int i = 0; i < libraryTemp.size(); i++) {
                library[i] = libraryTemp.get(i);
            }

            random = new Random(this.seed);
            try {
                processLibrary();
            } catch(Exception e) {
                e.printStackTrace();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyWord(String word) throws InvalidWordException {
        if (word.length() != 5) {
            throw new InvalidWordException("Invalid word!");
        }
    }

    public void processLibrary() {
        ArrayList<String> cleanLibrary = new ArrayList<>();
        for (int i = 0; i < library.length; i++) {
            try {
                verifyWord(library[i]);
                cleanLibrary.add(library[i]);
            } catch(InvalidWordException e) {
                System.out.println(e.getMessage());
            }
        }
        library = new String[cleanLibrary.size()];
        for (int i = 0; i < cleanLibrary.size(); i++) {
            library[i] = cleanLibrary.get(i);
        }
    }

    public String chooseWord() {
        return library[random.nextInt(library.length)];
    }

    public String[] getLibrary() {
        return library;
    }

    public void setLibrary(String[] library) {
        this.library = library;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

}
