package consoleUI;

import internals.player.PlayerController;
import internals.question.QuestionLibrary;
import internals.round.RightAnswer;
import internals.round.Bet;
import internals.round.Round;
import internals.round.RoundController;


/**
 * Η κλάση που μοντελοποιεί την αλληλεπίδραση της εφαρμογής με τον χρήστη (με διεπαφή κονσόλας).
 * Ουσιαστικά, διαχειρίζεται και χρησιμοποιεί αντικείμενα κλάσεων τύπου Controller και υλοποιεί την λογική του παιχνιδιού,
 * ενω χειρίζεται την είσοδο / έξοδο  απο / προς τον χρήστη.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.3
 */
public class ConsoleInteraction {
    private InputParser parser; // Χρησιμοποιούμε το parser για είσοδο απο τον χρήστη
    private PlayerController playerController; // Χρησιμοποιούμε το playerController για διαχείριση των δεδομένων των παιχτών
    private QuestionLibrary questionLibrary; // Χρησιμοποιούμε το questionLibrary για διαχείριση των δεδομένων των ερωτήσεων
    private RoundController roundController; // Χρησιμοποιούμε το roundController για τυχαία επιλογή των τύπων γύρων και απόκτηση δεδομένων και διαδικασιών που υλοποιούν αντικείμενα τύπου Round

    /**
     * Αρχικοποιεί τα δεδομένα της κλάσης.
     * Για τα αντικείμενα των πεδίων, όταν είναι δυνατό, ενεργοποιείται η λειτουργία "αυτόματου ανακατέματος" που αποσκοπεί στο να περιοριστεί η επανεμφάνιση ίδιων
     * κατηγοριών / ερωτήσεων / γύρων στην ίδια συνεδρία παιχνιδιού (δηλαδή για παράδειγμα άν έχω αποθηκευμένες 100 ερωτήσεις στο σύνολο σε 100 ξεχωριστές κατηγορίες, αν τελειώσει ένα παιχνίδι
     * όπου έχουν χρησιμοποιηθεί 5, αυτές οι 5 δεν θα ξανά εμφανιστούν σε επόμενο παιχνίδι μέχρι να "Χρησιμοποιηθούν" οι υπόλοιπες 95).
     */
    public ConsoleInteraction(){
        parser = new InputParser();
        playerController = new PlayerController();
        questionLibrary = new QuestionLibrary(true);
        roundController = new RoundController(true, questionLibrary);
    }

    /**
     * Ξεκινάει την εκτέλεση της εφαρμογής.
     * Υλοποιεί την λογική του κύριου μενού που χειρίζεται την επιλογή του χρήστη, που μπορεί είναι μίας απο τις ενέργειες:
     * Μεταφορά στο μενού εκκίνησης παιχνιδιού, Έξοδο απο την εφαρμογή, Εμφάνιση λεπτομερειών διαθέσιμων ενεργειών και μεταφορά στο μενού διαχείρισης παιχτών.
     */
    public void beginApp(){
        Command[] availableCommands = {Command.BEGIN_GAME, Command.MANAGE_PLAYERS, Command.EXIT_GAME, Command.HELP}; // Οι επιτρεπτές / διαθέσιμες ενέργειες χρήστη σε αυτό το μενού

        boolean executionContinues = true; // Μεταβλητή που ρυθμίζει την έξοδο απο το παιχνίδι. Αρχικά, θεωρώ ότι δεν γίνεται έξοδος.

        System.out.println("============================================================"); // Εισαγωγικό γραφικό μήνυμα
        System.out.println("|            Καλώς ήρθες στο παιχνίδι γνώσεων Buzz         |");
        System.out.println("============================================================");
        System.out.println();

        while(executionContinues){ // Η επανάληψη εκτελείται μέχρι ο χρήστης να επιλέξει την ενέργεια "Έξοδος"
            UserAssistingMessages.printCommandMenu(availableCommands); // Τυπώνω τις διαθέσιμες ενέργειες χρήστη
            Command userAnswer = parser.promptCommand("Επίλεξε μία απο τις παραπάνω ενέργειες για συνέχεια ή πάτησε \"Βοήθεια\" για λεπτομέρειες σχετικά με τις ενέργειες",
                    availableCommands); // Ζητάω επιλογή απο τις ενέργειες.
            switch (userAnswer){ // Εκτέλεση επιλεγμένης ενέργειας
                case BEGIN_GAME: // Επέλεξε "Εκκίνηση παιχνιδιού"
                    beginGame(); // Μεταφορά εκτέλεσης σε αντίστοιχο (αρμόδιο) τμήμα κώδικα
                    System.out.println();
                    break;
                case EXIT_GAME: // Επέλεξε "Έξοδος"
                    executionContinues =false; // Αλλάζω την μεταβλητή ελέγχου ώστε να τερματίσει η επανάληψη
                    System.out.println();
                    break;
                case HELP: // Επέλεξε "Βοήθεια"
                    UserAssistingMessages.printHelpMenu(availableCommands); // Εκτυπώνω λεπτομέρειες διαθέσιμων εντολών.
                    parser.prompt("Πάτησε οτιδήποτε για συνέχεια...");
                    System.out.println();
                    break;
                case MANAGE_PLAYERS: // Επίλεξε "Διαχείριση παίχτη"
                    playerManagement();  // Μεταφορά εκτέλεσης σε αντίστοιχο (αρμόδιο) τμήμα κώδικα
                    break;
                default: // Δεν επέλεξε επιτρεπτή εντολή, συνεχίζει η επανάληψη
                    System.out.println();
                    break;
            }
        }
    }


    /**
     * Κομμάτι του μενού που διαχειρίζεται τους παίχτες.
     * Χειρίζεται την επιλογή του χρήστη, που μπορεί είναι μίας απο τις ενέργειες:
     * Δημιουργία παίχτη, Διαγραφή παίχτη, Αλλαγή ονόματος παίχτη, Εμφάνιση πίνακα με σκόρ,
     * Επιστροφή (μεταφορά πίσω) στο κύριο μενού και Εμφάνιση λεπτομερειών διαθέσιμων ενεργειών.
     */
    private void playerManagement(){
        Command[] availableCommands = {Command.CREATE_PLAYER,Command.DELETE_PLAYER,Command.RENAME_PLAYER,Command.SCOREBOARD, Command.HELP, Command.MAIN_MENU}; // Οι επιτρεπτές / διαθέσιμες ενέργειες χρήστη σε αυτό το μενού

        boolean executionContinues = true; // Μεταβλητή που ρυθμίζει την έξοδο απο το μενού και επιστροφή στο προηγούμενο. Αρχικά, θεωρώ ότι δεν γίνεται επιστροφή.

        while(executionContinues){ // Η επανάληψη εκτελείται μέχρι ο χρήστης να επιλέξει την ενέργεια "Κύριο Μενού"
            UserAssistingMessages.printCommandMenu(availableCommands); // Τυπώνω τις διαθέσιμες ενέργειες χρήστη
            Command userAnswer = parser.promptCommand("Επίλεξε μία απο τις παραπάνω ενέργειες για συνέχεια ή πάτησε \"Βοήθεια\" για λεπτομέρειες σχετικά με τις ενέργειες",
                    availableCommands); // Ζητάω επιλογή απο τις ενέργειες.
            String userResponce, userSecondResponce; // Μεταβλητές που αποθηκεύουν την πρώτη και (αν χρειαστεί) την δεύτερη απάντηση του χρήστη
            switch (userAnswer){ // Εκτέλεση επιλεγμένης ενέργειας
                case CREATE_PLAYER: // Επέλεξε "Δημιουργία Παίχτη"
                    userResponce = parser.prompt("Δώσε όνομα παίχτη για προσθήκη:"); // Διαβάζω όνομα παίχτη
                    System.out.println(playerController.createPlayer(userResponce) + "!"); // Προσπάθεια δημιουργίας νέου παίχτη και εκτύπωση μηνύματος με το αποτέλεσμα της προσπάθειας αυτής.
                    System.out.println();
                    break;
                case DELETE_PLAYER: // Επέλεξε "Διαγραφή Παίχτη"
                    if (playerController.getNumberOfPlayers()==0){ // Έλεγχος αν υπάρχουν παίχτες. Αν δεν υπάρχουν, δεν υπάρχει λόγος να ζητήσω στην συνέχεια όνομα παίχτη
                        System.out.println("Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να διαγράψεις κάποιον...");
                        System.out.println();
                        break; // Δεν υπάρχει κανένας αποθηκευμένος παίχτης, επιστροφή στο προηγούμενο μενού.
                    }
                    userResponce = parser.prompt("Δώσε όνομα παίχτη για διαγραφή:"); // Διαβάζω όνομα παίχτη
                    System.out.println(playerController.removePlayer(userResponce) + "!"); // Προσπάθεια διαγραφής παίχτη και εκτύπωση μηνύματος με το αποτέλεσμα της προσπάθειας αυτής.
                    System.out.println();
                    break;
                case RENAME_PLAYER: // Επέλεξε "Μετονομασία παίχτη"
                    if (playerController.getNumberOfPlayers()==0){ // Έλεγχος αν υπάρχουν παίχτες. Αν δεν υπάρχουν δεν υπάρχει λόγος να ζητήσω στην συνέχεια παλιό και νέο όνομα παίχτη
                        System.out.println("Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να κάνεις κάποια μετονομασία...");
                        System.out.println();
                        break; // Δεν υπάρχει κανένας αποθηκευμένος παίχτης, επιστροφή στο προηγούμενο μενού.
                    }
                    userResponce = parser.prompt("Δώσε όνομα παίχτη που θέλεις να αλλάξεις:"); // Διαβάζω παλιό όνομα παίχτη
                    userSecondResponce = parser.prompt("Δώσε νέο όνομα"); // Διαβάζω νέο όνομα παίχτη
                    System.out.println(playerController.changePlayerName(userResponce, userSecondResponce) + "!"); // Προσπάθεια μετονομασίας παίχτη και εκτύπωση μηνύματος με το αποτέλεσμα της προσπάθειας αυτής.
                    System.out.println();
                    break;

                case SCOREBOARD: // Επέλεξε "Εμφάνιση πίνακα σκόρ"
                    String[][] scoreboard = playerController.getScoreboard(); // Αποθηκεύω τον πίνακα με τα σκόρ
                    if(scoreboard.length==0){ // Αν ο πίνακας έχει μήκος 0, τότε δεν υπάρχουν παίκτες των οποίων τα σκορ μπορώ να ελέγξω
                        System.out.println("Δεν υπάρχουν καταχωρημένοι παίχτες!");
                        System.out.println();
                        break;
                    }

                    System.out.printf("| %-25s ||%15s | %18s |%n", "Όνομα παίχτη", "Τρέχον σκόρ", "Μέγιστο σκόρ"); // Τυπώνω (με μορφοποίηση) τις επικεφαλίδες των στηλών τον πίνακα με τα σκόρ
                    for (String[] aStringArray : scoreboard){ // Τυπώνω όνομα παίχτη, τρέχον σκορ παίχτη, μέγιστο σκορ παίχτη για κάθε παίχτη του πίνακα
                        System.out.printf("| %-25s ||%15s | %18s |%n", aStringArray[0], aStringArray[1], aStringArray[2]);
                    }
                    System.out.println();
                    break;

                case HELP: // Επέλεξε "Βοήθεια"
                    UserAssistingMessages.printHelpMenu(availableCommands); // Εκτύπωση ονομάτων και περιγραφών των διαθέσιμων ενεργειών (εντολών)
                    System.out.println();
                    break;

                case MAIN_MENU: // Επέλεξε "Κύριο Μενού"
                    executionContinues=false; // Τερματίζω την επανάληψη ώστε η εκτέλεση να επιστρέψει στο σημείο κλήσης της μεθόδου.
                    break;
                default: // Δεν επέλεξε επιτρεπτή εντολή, συνεχίζει η επανάληψη
                    break;
            }
        }
    }

    /**
     * Κομμάτι του μενού που ξεκινάει το παιχνίδι.
     * Χειρίζεται την επιλογή του χρήστη, που μπορεί είναι μίας απο τις ενέργειες:
     * Εκκίνηση παιχνιδιού, Επιστροφή στο κύριο μενού και Εμφάνιση λεπτομερειών διαθέσιμων ενεργειών.
     */
    private void beginGame(){
        Command[] availableCommands = {Command.BEGIN_GAME, Command.MAIN_MENU, Command.HELP}; // Οι επιτρεπτές / διαθέσιμες ενέργειες χρήστη σε αυτό το μενού

        boolean executionContinues = true; // Η μεταβλητή που χειρίζεται την έξοδο απο τον βρόχο

        while(executionContinues){ // Η επανάληψη επιλογής θα τερματίσει όταν ο χρήστης επιλέξει να επιστρέψει στο κύριο μενού ή η εκκίνηση του παιχνιδιού να τερματίσει με αποτυχία.
            UserAssistingMessages.printCommandMenu(availableCommands); // Τυπώνω τις διαθέσιμες εντολές του χρήστη
            Command userAnswer = parser.promptCommand(
                    String.format("Επίλεξε \"Εκκίνηση παιχνιδιού\" για να ξεκινήσεις το παιχνίδι ή \"Κύριο μενού\" για επιστροφή στο κύριο μενού %n" +
                            "Διαφορετικά επίλεξε \"Βοήθεια\" για λεπτομέρειες σχετικά αυτές τις ενέργειες"),
                    availableCommands); // Ζητάω απο τον χρήστη να επιλέξει μία ενέργεια
            switch (userAnswer){ // Επιλογή ενέργειας
                case BEGIN_GAME: // Επέλεξε "Εκκίνηση Παιχνιδιού"
                    executionContinues = gameProcess(); // Μετάβαση στην μέθοδο που χειρίζεται την εκτέλεση του παιχνιδιού, αποθηκεύω την κατάσταση επιτυχίας (αν κατάφερε να ξεκινήσει το παιχνίδι)
                    if (executionContinues){ // Άν δεν έγινε εκκίνηση παιχνιδιού δεν πρέπει να τυπωθεί το μήνυμα
                        System.out.println();
                        System.out.println("--------------------------------"); // Εκτύπωση μηνύματος που σηματοδοτεί το τέλος όλων των γύρων.
                        System.out.println("-----   Τέλος Παιχνιδιού   -----");
                        System.out.println("--------------------------------");
                        System.out.println();
                    }
                    break;
                case MAIN_MENU: // Επέλεξε "Κύριο Μενού"
                    executionContinues = false; // Τερματίζω το βρόχο για να επιστρέψω στο προηγούμενο μενού
                    break;
                case HELP: // Επέλεξε "Βοήθεια"
                    UserAssistingMessages.printHelpMenu(availableCommands); // Εκτύπωση ονομάτων και περιγραφών των διαθέσιμων ενεργειών (εντολών)
                    System.out.println();
                    break;
                default: // Δεν επέλεξε επιτρεπτή ενέργεια
                    break;
            }
        }
    }

    /**
     * Χειρίζεται την εκτέλεση του παιχνιδιού ζητώντας απο τον χρήστη επιλογές για τις ρυθμίσεις του παιχνιδιού και επιστρέφει την επιτυχία εκκίνησης του παιχνιδιού.
     *
     * Η εκκίνηση παιχνιδιού είναι επιτυχής εφόσον υπάρχει τουλάχιστον ένας παίχτης αποθηκευμένος (που έχει δημιουργηθεί εκ των προτέρων της εκκίνησης).
     *
     * Δίνει την δυνατότητα στον χρήστη να διαβάσει τις οδηγίες του παιχνιδιού και του ζητάει να ορίσει τις ρυθμίσεις του παιχνιδιού (αριθμό γύρων και ερωτήσεων).
     * Επιλέγει τύπο γύρου με τυχαίο τρόπο και μεταβαίνει στην αντίστοιχη μέθοδο για διαχείριση του γύρου εφόσον κάθε γύρος έχει τις ιδιαιτερότητες του.
     *
     * @return επιτυχία εκκίνησης. true αν υπάρχει τουλάχιστον ένας αποθηκευμένος παίχτης, false σε διαφορετική περίπτωση
     */
    private boolean gameProcess(){
       String selectedPlayer = selectPlayer(); // Γίνεται επιλογή παίχτη
       if (selectedPlayer == null) { return false; } // Άν δεν υπάρχουν παίχτες τερματίζω με αποτυχία (false)

        String userResponce =  parser.prompt("Θέλεις να διαβάσεις τις οδηγίες του παιχνιδιού; Απάντησε με Ναι/Όχι").toLowerCase(); // Διαβάζει απάντηση χρήστη
        if (userResponce.equals("ναι") || userResponce.equals("ναί")){ // Η απάντηση χρήστη με ναί ή ναι (ανεξαρτήτως κεφαλαίων / πεζών χαρακτήρων) θεωρείται ως θετική απάντηση. Κάθε άλλη απάντηση αντιστοιχεί σε Όχι.

            // Εκτύπωση οδηγιών παιχνιδιού
            System.out.println("Το παιχνίδι αποτελείται απο έναν αριθμό γύρων όπου κάθε ένας έχει έναν αριθμό ερωτήσεων.");
            System.out.println("Κάθε ερώτηση έχει 4 πιθανές απαντήσεις με την αρίθμηση 1, 2, 3, 4 όπου μόνο μία είναι σωστή.");
            System.out.println("Καλείσαι να απαντήσεις σε αυτές τις ερωτήσεις δίνοντας τον αριθμό που αντιστοιχεί στην απάντηση που θέλεις να επιλέξεις " +
                    "(πρέπει να δώσεις τον αριθμό 1, 2, 3 ή 4 αντίστοιχα).");
            System.out.println("Σημειώνεται ότι κάθε γύρος ίσως να έχει κάποιες παραλλαγές σε σχέση με αυτές τις οδηγίες, οι οποίες εξηγούνται προτού αρχίσει ο συγκεκριμένος γύρος.");
            System.out.println();
        }

        roundController.setPlayerNumber(1); // Ορίζω αριθμό παιχτών = 1, απο ζητούμενα εφαρμογής

        int roundNumber = parser.promptIntInRange("Πόσους γύρους θες να παίξεις; (δώσε αριθμο απο 1 μέχρι 20)", 1 ,20); // Αποθηκεύω αριθμό γύρων
        int questionNumber = parser.promptIntInRange("Πόσες ερωτήσεις θες να έχει ο γύρος; (δώσε αριθμο απο 1 μέχρι 10)", 1 ,10); // Αποθηκεύω αριθμό ερωτήσεων ανα γύρο.
        roundController.setNumberOfQuestionsPerRound(questionNumber); // Ορίζω αριθμό ερωτήσεων ανα γύρο στην roundController για να επιστρέφει αντίστοιχα αντικείμενα γύρων


        int gamePointSum = 0; // Μεταβλητή που αποθηκεύει σύνολο πόντων που συγκέντρωσε ο παίχτης κατά την διάρκεια όλων των γύρων.

        while(roundNumber--!=0){ // Όταν η μεταβλητή roundNumber γίνει 0, σημαίνει ότι ο χρήστης έχει παίξει τον αριθμό των γύρων που αυτός επέλεξε
            Round currentRound = roundController.getRandomRoundType(); // "Επιλέγω" τυχαίο τύπο γύρου

            // Μετάβαση στην αντίστοιχη μέθοδο για διαχείριση του αντίστοιχου γύρου και προσαύξηση μεταβλητής συνολικών κερδισμένων πόντων
            if (currentRound instanceof RightAnswer){
                gamePointSum += playRightAnswer((RightAnswer) currentRound, selectedPlayer);
            } else if (currentRound instanceof  Bet){
                gamePointSum += playBet((Bet) currentRound, selectedPlayer);
            }
            System.out.println();

        }

        // Εκτύπωση στατιστικών παιχνιδιού στον χρήστη
        System.out.println("Σύνολο πόντων που αποκτήθηκαν αυτό το παιχνίδι: " + gamePointSum);
        System.out.println("Τρέχον σκόρ παίχτη " + selectedPlayer + " : " + playerController.getPlayerScore(selectedPlayer));
        System.out.println("Μέγιστο σκόρ παίχτη (highscore) " + selectedPlayer + " : " + playerController.getPlayerHighScore(selectedPlayer));

        return true; // Επιτυχία εκκίνησης
    }

    /**
     * Υλοποιεί την εκτέλεση του γύρου τύπου "Σωστή Απάντηση".
     * @param currentRound αναφορά σε αντικείμενο γύρου που θα παίξει ο χρήστης
     * @param playerName όνομα χρήστη που παίζει
     * @return αριθμό πόντων που κέρδισε ο χρήστης σε αυτό τον γύρο
     */
    private int playRightAnswer(RightAnswer currentRound, String playerName){
        int pointSum = 0; // Αρχικοποίηση μεταβλητής που αποθηκεύει το σύνολο των κερδισμένων πόντων σε αυτό τον γύρο

        UserAssistingMessages.printRoundInfo(currentRound); // Εκτύπωση πληροφοριών γύρου
        parser.prompt("Πάτησε enter για να αρχίσει ο γύρος...");

        while (! currentRound.isOver() ){ // Δίνουμε ερωτήσεις που πρέπει να απαντήσει ο παίκτης μέχρι αυτές να τελειώσουν (Αυτό καθορίζεται απο την μέθοδο isOver() της round)
            System.out.printf("Κατηγορία ερώτησης: %s%n%n", currentRound.getQuestionCategory()); // Εκτύπωση κατηγορίας ερώτησης

            parser.prompt("Πάτησε enter για να εμφανιστεί η ερώτηση...");
            System.out.println();

            UserAssistingMessages.printQuestion(currentRound.getQuestion(), currentRound.getQuestionAnswers()); // Εκτύπωση εκφώνησης ερώτησης και πιθανών απαντήσεων της

            int gain = currentRound.answerQuestion(currentRound.getQuestionAnswers()[parser.promptAnswer()-1]); // Υπολογισμός σκόρ που αποκτήθηκε απο την απάντηση στην ερώτηση
            pointSum += gain; // Προσαύξηση μετρητή πόντων
            playerController.playerCalculateGain(playerName, gain); // Ενημέρωση στατιστικών του παίχτη

            UserAssistingMessages.printGain(gain, currentRound.getRightQuestionAnswer()); // Εκτύπωση του αποτελέσματος της απάντησης

            currentRound.proceed(); // "Φόρτωση" επόμενης ερώτησης
            System.out.println();
        }
        System.out.println("Σύνολο πόντων που αποκτήθηκαν αυτό τον γύρο: " + pointSum);
        System.out.printf("-----   Τέλος Γύρου: Σωστή Ερώτηση   -----%n%n"); // Σηματοδοτεί στον χρήστη το τέλος του γύρου
        return pointSum; // Επιστρέφει σύνολο πόντων που κερδήθηκαν σε αυτό τον γύρο
    }

    /**
     * Υλοποιεί την εκτέλεση του γύρου τύπου "Ποντάρισμα"
     * @param currentRound αναφορά σε αντικείμενο γύρου που θα παίξει ο χρήστης
     * @param playerName όνομα χρήστη που παίζει
     * @return αριθμό πόντων που κέρδισε ο χρήστης σε αυτό τον γύρο
     */
    private int playBet(Bet currentRound, String playerName){
        int pointSum = 0; // Αρχικοποίηση μεταβλητής που αποθηκεύει το σύνολο των κερδισμένων πόντων σε αυτό τον γύρο

        UserAssistingMessages.printRoundInfo(currentRound);  // Εκτύπωση πληροφοριών γύρου
        System.out.println();

        parser.prompt("Πάτησε enter για να αρχίσει ο γύρος...");
        String userBetState; // Αποθηκεύει την κατάσταση επιτυχίας της προσπάθειας πονταρίσματος


        while (! currentRound.isOver() ){ // Δίνουμε ερωτήσεις που πρέπει να απαντήσει ο παίκτης μέχρι αυτές να τελειώσουν (Αυτό καθορίζεται απο την μέθοδο isOver() της round)
            parser.prompt("Πάτησε enter για να εμφανιστεί η κατηγορία της ερώτησης...");

            System.out.println();

            System.out.printf("Κατηγορία ερώτησης: %s%n%n", currentRound.getQuestionCategory()); // Εκτύπωση κατηγορίας ερώτησης

            do{ // Ζητάω απο τον χρήστη να επιλέξει ποντάρισμα και το "τοποθετώ" στον γύρο
                userBetState = currentRound.placeBet(playerName, parser.promptPositiveInt("Πόσους πόντους θες να ποντάρεις; Επιλογές: 250, 500, 750, 1000."));
                System.out.println(userBetState + "!");
            }while (!userBetState.equals("Επιτυχία")); // Επαναλαμβάνεται το πάνω μέχρι ο χρήστης να δώσει επιτρεπτή τιμή πονταρίσματος

            parser.prompt("Πάτησε enter για να εμφανιστεί η ερώτηση...");
            System.out.println();
            UserAssistingMessages.printQuestion(currentRound.getQuestion(), currentRound.getQuestionAnswers()); // Εκτύπωση εκφώνησης ερώτησης και πιθανών απαντήσεων της

            int gain = currentRound.answerQuestion(currentRound.getQuestionAnswers()[parser.promptAnswer()-1], playerName); // Υπολογισμός σκόρ που αποκτήθηκε απο την απάντηση στην ερώτηση
            pointSum += gain; // Ενημέρωση μετρητή πόντων
            playerController.playerCalculateGain(playerName, gain); // Ενημέρωση στατιστικών αντικειμένου

            UserAssistingMessages.printGain(gain, currentRound.getRightQuestionAnswer()); // Εκτύπωση του αποτελέσματος της απάντησης

            currentRound.proceed(); // "Φόρτωση" επόμενης ερώτησης
            System.out.println();
        }
        System.out.println("Σύνολο πόντων που αποκτήθηκαν αυτό τον γύρο: " + pointSum);
        System.out.printf("-----   Τέλος Γύρου: Ποντάρισμα   -----%n%n"); // Σηματοδοτεί στον χρήστη το τέλος του γύρου
        return pointSum; // Επιστρέφει σύνολο πόντων που κερδήθηκαν σε αυτό τον γύρο
    }

    /**
     * Επιλογή παίχτη απο τον χρήστη.
     *
     * Τυπώνει μία λίστα με τα ονόματα των παιχτών και ζητάει απο τον χρήστη να επιλέξει ένα απο αυτά και επιστρέφει το όνομα του παίχτη που επιλέχθηκε, άν υπάρχει.
     * Άν ο χρήστης έδωσε όνομα παίχτη που δεν υπάρχει, επαναλαμβάνεται η παραπάνω διαδικασία.
     *
     * Σε περίπτωση που δεν υπάρχει κανένας αποθηκευμένος παίχτης εκτυπώνεται αντίστοιχο μήνυμα και επιστρέφεται null.
     *
     * @return όνομα παίχτη που επέλεξε ο χρήστης ή null αν δεν υπάρχουν αποθηκευμένοι χρήστες
     */
    private String selectPlayer(){
        String[] playerList = playerController.listPlayers(); // Αποθηκεύω την λίστα με τα ονόματα των παιχτών
        if (playerList.length != 0){ // Αν υπάρχει έστω και ένας αποθηκευμένος παίχτης
            String userResponse; // Μεταβλητή που αποθηκεύει την απάντηση του χρήστη

            UserAssistingMessages.printList("Επίλεξε ένα από τα ονόματα παιχτών", playerList);
            userResponse= parser.prompt("");
            String playerExistsResult = playerController.playerExists(userResponse); // Αποθηκεύω την απάντηση του χρήστη

            while (!playerExistsResult.equals("Επιτυχία")) { // Αν δεν υπάρχει ο χρήστης
                System.out.println(playerExistsResult); // Τυπώνω σχετικό σφάλμα
                System.out.println("Προσπάθησε ξανά...");

                // Επαναλαμβάνω παραπάνω διαδικασία
                UserAssistingMessages.printList("Επίλεξε ένα από τα ονόματα παιχτών", playerList);
                userResponse= parser.prompt("");
                playerExistsResult = playerController.playerExists(userResponse);
            }

            return userResponse; // Επιστρέφω όνομα παίχτη που επέλεξε ο χρήστης
        }
        else { // Δεν υπάρχουν αποθηκευμένοι παίχτες
            // Τυπώνω σχετικό μήνυμα λάθους
            System.out.println("Δεν έχει δημιουργηθεί κανένας παίχτης!");
            System.out.println("Πρέπει πρώτα να δημιουργήσεις έναν για να αρχίσεις το παιχνίδι.");
            System.out.println("Στο κεντρικό μενού επίλεξε την ενέργεια \"Διαχείριση παίχτη\" και στη συνέχεια την ενέργεια \"Δημιουργία παίχτη\"");
            System.out.println("Τώρα, επιστρέφεσαι στο κεντρικό μενού...");
            return null; // Δεν υπάρχουν παίχτες άρα δεν μπορεί να επιλεχτεί κανένας, επιστρέφω null
        }
    }


}
