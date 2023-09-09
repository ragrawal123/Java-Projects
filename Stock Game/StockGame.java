import java.io.*;

/**
 * Homework 10 -- Concurrency
 *
 * In this program, we are creating a Stock Game that will take in a number of users given in the main method. For
 * example, our test case that we run creates two StockGame objects with two different stock traders and their
 * respective files that contain their buy-sell order list. The key to this program is that both users actions in
 * their buy-sell order list are run simultaneously. This is possible due to Threads and that threads can run at the
 * same time in programs. This shows more realistic expectations to what will happen in actual stock trading versus just
 * running the trading commands for one user and then running the trading commands for the second user and so on.
 * This program can have different outputs as different things can happen each time. The way we make sure that the
 * two actions don't overlap and are not printed on top of each other is through synchronization. What
 * synchronization does is make sure that when a single thread is accessing something that will be shared by all
 * threads, no other thread can also that in that instant. They have to wait till that single thread is done. In this
 * program we are synchronizing the processes of getting the command and the number of shares, and then doing the
 * processes of either buying or selling an x number of shares or printing an error if there are insufficient shares
 * owned or shares available or the input is just invalid. From there we run the game and print out the different
 * iterations of what happens in the game as well as the different stock prices, shares available, name, and trade
 * number.
 *
 *
 * @author Raunak Agrawal, Lab Sec L06
 *
 * @version October 28, 2022
 */
public class StockGame extends Thread {
    private static final Object TEST_OBJECT = new Object();
    private static double stockPrice;
    private static int availableShares;
    private static int tradeCount = 1;
    private final String name;
    private int numShares;
    private final String fileName;

    public StockGame(String name, String fileName) {
        stockPrice = 100.0;
        availableShares = 100;
        this.name = name;
        this.fileName = fileName;
    }

    public void run() {
        try {
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                synchronized (TEST_OBJECT) {
                    String[] information = line.split(", ");
                    if (information[0].equals("BUY") || information[0].equals("SELL")) {
                        try {
                            int changeShares = Integer.parseInt(information[1]);
                            if (changeShares > 0) {

                                System.out.println("----------");
                                System.out.printf("Stock Price: %.2f\n", stockPrice);
                                System.out.printf("Available Shares: %d\n", availableShares);
                                System.out.printf("Trade Number: %d\n", tradeCount);
                                System.out.printf("Name: %s\n", this.name);

                                if (information[0].equals("BUY")) {
                                    if (changeShares <= availableShares) {
                                        System.out.printf("Purchasing %d shares at %.2f\n",
                                                changeShares, stockPrice);
                                        this.numShares += changeShares;
                                        availableShares -= changeShares;
                                        stockPrice += (1.5 * changeShares);
                                        tradeCount++;

                                    } else {
                                        System.out.println("Insufficient shares available, cancelling order...");
                                    }
                                } else {
                                    if (changeShares <= this.numShares) {
                                        System.out.printf("Selling %d shares at %.2f\n",
                                                changeShares, stockPrice);
                                        numShares -= changeShares;
                                        availableShares += changeShares;
                                        stockPrice -= (2.0 * changeShares);
                                        tradeCount++;
                                    } else {
                                        System.out.println("Insufficient owned shares, cancelling order...");
                                    }
                                }

                            }
                        } catch (Exception e) {
                            System.out.println("----------");
                            System.out.println("Error, invalid input!");
                        }
                    } else {
                        System.out.println("----------");
                        System.out.println("Error invalid input!");
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("----------");
            System.out.println("Error, invalid input!");
        }

    }

}
