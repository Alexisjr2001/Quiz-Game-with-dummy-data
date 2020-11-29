package consoleUI;

import internals.player.PlayerController;
import internals.question.Question;
import internals.question.QuestionLibrary;
import internals.round.RightAnswer;
import internals.round.Bet;
import internals.round.Round;
import internals.round.RoundController;


/**
 * Η κλάση που μοντελοποιεί την αλληλεπίδραση της εφαρμογής με τον χρήστη (με διεπαφή κονσόλας)
 */
public class ConsoleInteraction {
    private InputParser parser;
    private PlayerController playerController;
    private QuestionLibrary questionLibrary;
    private RoundController roundController;
    private Round runningRound;

    public ConsoleInteraction(){
        parser = new InputParser();
        playerController = new PlayerController();
        questionLibrary = new QuestionLibrary(true);
        roundController = new RoundController(true, questionLibrary);
    }

    /**
     * Ξεκινάει την εκτέλεση της εφαρμογής
     */
    public void beginApp(){
        Command[] availableCommands = {Command.BEGIN_GAME, Command.MANAGE_PLAYERS, Command.EXIT_GAME, Command.HELP};

        boolean executionContinues = true;

        System.out.println("============================================================");
        System.out.println("|            Καλώς ήρθες στο παιχνίδι γνώσεων Buzz         |");
        System.out.println("============================================================");
        System.out.println();

        while(executionContinues){
            UserAssistingMessages.printCommandMenu(availableCommands);
            Command userAnswer = parser.promptCommand("Επίλεξε μία απο τις παραπάνω ενέργειες για συνέχεια ή πάτησε \"Βοήθεια\" για λεπτομέρειες σχετικά με τις ενέργειες",
                    availableCommands);
            switch (userAnswer){
                case BEGIN_GAME:
                    beginGame();
                    break;
                case EXIT_GAME:
                    executionContinues =false;
                    break;
                case HELP:
                    UserAssistingMessages.printHelpMenu(availableCommands);
                    parser.prompt("Πάτησε οτιδήποτε για συνέχεια...");
                    break;
                case MANAGE_PLAYERS:
                    playerManagement();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * Κομμάτι του μενού που διαχειρίζεται τους παίχτες
     */
    private void playerManagement(){
        Command[] availableCommands = {Command.CREATE_PLAYER,Command.DELETE_PLAYER,Command.RENAME_PLAYER,Command.SCOREBOARD, Command.HELP, Command.MAIN_MENU};

        boolean executionContinues = true;

        while(executionContinues){
            UserAssistingMessages.printCommandMenu(availableCommands);
            Command userAnswer = parser.promptCommand("Επίλεξε μία απο τις παραπάνω ενέργειες για συνέχεια ή πάτησε \"Βοήθεια\" για λεπτομέρειες σχετικά με τις ενέργειες",
                    availableCommands);
            String userResponce, userSecondResponce;
            switch (userAnswer){
                case CREATE_PLAYER:
                    userResponce = parser.prompt("Δώσε όνομα παίχτη για προσθήκη:");
                    System.out.println(playerController.createPlayer(userResponce) + "!");
                    break;
                case DELETE_PLAYER:
                    userResponce = parser.prompt("Δώσε όνομα παίχτη για διαγραφή:");
                    System.out.println(playerController.removePlayer(userResponce) + "!");
                    break;
                case RENAME_PLAYER:
                    if (playerController.getNumberOfPlayers()==0){
                        System.out.println("Δεν υπάρχουν παίχτες, οπότε δεν μπορείς να κάνεις κάποια μετονομασία...");
                        break;
                    }
                    userResponce = parser.prompt("Δώσε όνομα παίχτη που θέλεις να αλλάξεις:");
                    userSecondResponce = parser.prompt("Δώσε νέο όνομα");
                    System.out.println(playerController.changePlayerName(userResponce, userSecondResponce) + "!");
                    break;

                case SCOREBOARD:
                    String[][] scoreboard = playerController.getScoreboard();
                    if(scoreboard.length==0){
                        System.out.println("Δεν υπάρχουν καταχωρημένοι παίχτες!");
                        break;
                    }

                    System.out.printf("| %-25s ||%15s | %18s |%n", "Όνομα παίχτη", "Τρέχον σκόρ", "Μέγιστο σκόρ");
                    for (String[] aStringArray : scoreboard){
                        System.out.printf("| %-25s ||%15s | %18s |%n", aStringArray[0], aStringArray[1], aStringArray[2]);
                    }
                    break;

                case HELP:
                    UserAssistingMessages.printHelpMenu(availableCommands);
                    break;

                case MAIN_MENU:
                    executionContinues=false;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Κομμάτι του μενού που ξεκινάει το παιχνίδι
     */
    private void beginGame(){
        Command[] availableCommands = {Command.BEGIN_GAME, Command.MAIN_MENU, Command.HELP};
        System.out.println();

        boolean executionContinues = true;

        while(executionContinues){
            UserAssistingMessages.printCommandMenu(availableCommands);
            Command userAnswer = parser.promptCommand(
                    String.format("Επίλεξε \"Εκκίνηση παιχνιδιού\" για να ξεκινήσεις το παιχνίδι ή \"Κύριο μενού\" για επιστροφή στο κύριο μενού %n" +
                            "Διαφορετικά επίλεξε \"Βοήθεια\" για λεπτομέρειες σχετικά αυτές τις ενέργειες"),
                    availableCommands);
            switch (userAnswer){
                case BEGIN_GAME:
                    executionContinues = gameProcess();
                    System.out.println();
                    System.out.println("--------------------------------");
                    System.out.println("-----   Τέλος Παιχνιδιού   -----");
                    System.out.println("--------------------------------");
                    System.out.println();
                    break;
                case MAIN_MENU:
                    executionContinues = false;
                    break;
                case HELP:
                    UserAssistingMessages.printHelpMenu(availableCommands);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean gameProcess(){
       String selectedPlayer = selectPlayer();
       if (selectedPlayer == null) { return false; }

        String userResponce =  parser.prompt("Θέλεις να διαβάσεις τις οδηγίες του παιχνιδιού; Απάντησε με Ναι/Όχι").toLowerCase();
        if (userResponce.equals("ναι") || userResponce.equals("ναί")){
            System.out.println("Το παιχνίδι αποτελείται απο έναν αριθμό γύρων όπου κάθε ένας έχει έναν αριθμό ερωτήσεων.");
            System.out.println("Κάθε ερώτηση έχει 4 πιθανές απαντήσεις με την αρίθμηση 1, 2, 3, 4 όπου μόνο μία είναι σωστή.");
            System.out.println("Καλείσαι να απαντήσεις σε αυτές τις ερωτήσεις δίνοντας τον αριθμό που αντιστοιχεί στην απάντηση που θέλεις να επιλέξεις " +
                    "(πρέπει να δώσεις τον αριθμό 1, 2, 3 ή 4 αντίστοιχα).");
            System.out.println("Σημειώνεται ότι κάθε γύρος ίσως να έχει κάποιες παραλλαγές σε σχέση με αυτές τις οδηγίες, οι οποίες εξηγούνται προτού αρχίσει ο συγκεκριμένος γύρος.");
        }

        int roundNumber = parser.promptPositiveInt("Πόσους γύρους θες να παίξεις;");
        roundController.setPlayerNumber(1);
        int questionNumber = parser.promptPositiveInt("Πόσες ερωτήσεις θες να έχει ο γύρος;");
        roundController.setNumberOfQuestionsPerRound(questionNumber);


        while(roundNumber--!=0){
            Round currentRound = roundController.getRandomRoundType();

            if (currentRound instanceof RightAnswer){
                playRightAnswer((RightAnswer) currentRound, selectedPlayer);
            } else if (currentRound instanceof  Bet){
                playBet((Bet) currentRound, selectedPlayer);
            }

        }
        return true;
    }

    private void playRightAnswer(RightAnswer currentRound, String playerName){
        UserAssistingMessages.printRoundInfo(currentRound);
        parser.prompt("Πάτησε οποιοδήποτε πλήκτρο για να αρχίσει ο γύρος...");

        while (! currentRound.isOver() ){ // Δίνουμε ερωτήσεις που πρέπει να απαντήσει ο παίκτης μέχρι αυτές να τελειώσουν (Αυτό καθορίζεται απο την μέθοδο isOver() της round)
            System.out.printf("Κατηγορία ερώτησης: %s%n%n", currentRound.getQuestionCategory());

            parser.prompt("Πάτησε οποιοδήποτε πλήκτρο για να εμφανιστεί η ερώτηση...");
            UserAssistingMessages.printQuestion(currentRound.getQuestion(), currentRound.getQuestionAnswers());

            int gain = currentRound.answerQuestion(currentRound.getQuestionAnswers()[parser.promptAnswer()-1]);
            playerController.playerCalculateGain(playerName, gain);

            UserAssistingMessages.printGain(gain, currentRound.getRightQuestionAnswer());

            currentRound.proceed();
        }
        System.out.printf("-----   Τέλος Γύρου: Σωστή Ερώτηση   -----%n%n");
    }

    private void playBet(Bet currentRound, String playerName){
        UserAssistingMessages.printRoundInfo(currentRound);
        parser.prompt("Πάτησε οποιοδήποτε πλήκτρο για να αρχίσει ο γύρος...");
        String userBetState;


        while (! currentRound.isOver() ){ // Δίνουμε ερωτήσεις που πρέπει να απαντήσει ο παίκτης μέχρι αυτές να τελειώσουν (Αυτό καθορίζεται απο την μέθοδο isOver() της round)
            parser.prompt("Πάτησε οποιοδήποτε πλήκτρο για να εμφανιστεί η κατηγορία της ερώτησης...");

            System.out.printf("Κατηγορία ερώτησης: %s%n%n", currentRound.getQuestionCategory());

            do{
                userBetState = currentRound.placeBet(playerName, parser.promptPositiveInt("Πόσους πόντους θες να ποντάρεις; Επιλογές: 250, 500, 750, 1000."));
                System.out.println(userBetState + "!");
            }while (!userBetState.equals("Επιτυχία"));

            parser.prompt("Πάτησε οποιοδήποτε πλήκτρο για να εμφανιστεί η ερώτηση...");
            UserAssistingMessages.printQuestion(currentRound.getQuestion(), currentRound.getQuestionAnswers());

            int gain = currentRound.answerQuestion(currentRound.getQuestionAnswers()[parser.promptAnswer()-1], playerName);
            playerController.playerCalculateGain(playerName, gain);

            UserAssistingMessages.printGain(gain, currentRound.getRightQuestionAnswer());

            currentRound.proceed();
        }
        System.out.printf("-----   Τέλος Γύρου: Ποντάρισμα   -----%n%n");
    }

    private String selectPlayer(){
        String[] playerList = playerController.listPlayers();
        if (playerList.length != 0){
            String userResponse;

            UserAssistingMessages.printList("Επίλεξε ένα από τα ονόματα παιχτών", playerList);
            userResponse= parser.prompt("");
            String playerExistsResult = playerController.playerExists(userResponse);

            while (!playerExistsResult.equals("Επιτυχία")) {
                System.out.println(playerExistsResult);
                System.out.println("Προσπάθησε ξανά...");

                UserAssistingMessages.printList("Επίλεξε ένα από τα ονόματα παιχτών", playerList);
                userResponse= parser.prompt("");
                playerExistsResult = playerController.playerExists(userResponse);
            }

            return userResponse;
        }
        else {
            System.out.println("Δεν έχει δημιουργηθεί κανένας παίχτης!");
            System.out.println("Πρέπει πρώτα να δημιουργήσεις έναν για να αρχίσεις το παιχνίδι.");
            System.out.println("Στο κεντρικό μενού επίλεξε την ενέργεια \"Διαχείριση παίχτη\" και στη συνέχεια την ενέργεια \"Δημιουργία παίχτη\"");
            System.out.println("Τώρα, επιστρέφεσαι στο κεντρικό μενού...");
            return null;
        }
    }


}
