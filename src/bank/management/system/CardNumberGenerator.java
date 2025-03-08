package bank.management.system;
import java.util.Random;



public class CardNumberGenerator {
    public static String generateCardNumber() {
        Random rand = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(rand.nextInt(10)); // Generate a random digit (0-9)
        }
        return cardNumber.toString();
    }
}
