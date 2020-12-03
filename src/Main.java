import consoleUI.ConsoleInteraction;

/**
 * Υλοποιεί την μοναδική μέθοδο main της εφαρμογής και αποτελεί σημείο εκκίνησης του προγράμματος.
 */
public class Main {
    /**
     * Δημιουργεί και εκκινεί το περιβάλλον αλληλεπίδρασης του χρήστη
     * @param args Ορίσματα κονσόλας, δεν χρησιμοποιούνται
     */
    public static void main(String[] args) {
        ConsoleInteraction playGame = new ConsoleInteraction(); // Δημιουργία περιβάλλοντος
        playGame.beginApp(); // Εκκίνηση περιβάλλοντος
        System.out.println("Αντίο...");
    }
}
