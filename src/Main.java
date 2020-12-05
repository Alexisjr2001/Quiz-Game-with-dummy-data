import consoleUI.ConsoleInteraction;

/**
 * Υλοποιεί την μοναδική μέθοδο main της εφαρμογής και αποτελεί σημείο εκκίνησης του προγράμματος.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.3
 */
public class Main {

    /**
     * Η κλάση αποτελεί σημείο εκκίνησης του προγράμματος υλοποιώντας μέθοδο main, επομένως δεν συνίσταται η δημιουργία αντικειμένου της.
     */
    public Main(){}

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
