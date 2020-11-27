package consoleUI;

import internals.player.PlayerController;
import internals.question.QuestionLibrary;
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
            printCommandMenu(availableCommands);
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
                    printHelpMenu(availableCommands);
                    break;
                case MANAGE_PLAYERS:
                    playerManagement();
                    break;
                default:
                    break;
            }
        }
    }

    private void printCommandMenu(Command[] commands){
        System.out.println("-----   Διαθέσιμες Ενέργειες   -----");
        for(Command c : commands){
            System.out.println("\t* " + c.greekName);
        }
        System.out.println("------------------------------------");
    }

    private void printHelpMenu(Command[] commands){
        System.out.println("-----   Διαθέσιμες Ενέργειες   -----");
        for(Command c : commands){
            System.out.println("\t* " + c.greekName + ":\t" + c.description);
        }
        System.out.println("------------------------------------");
    }

    /**
     * Κομμάτι του μενού που διαχειρίζεται τους παίχτες
     */
    private void playerManagement(){
        Command[] availableCommands = {Command.CREATE_PLAYER,Command.DELETE_PLAYER,Command.RENAME_PLAYER,Command.SCOREBOARD, Command.HELP};

        boolean executionContinues = true;

        while(executionContinues){
            printCommandMenu(availableCommands);
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
                    userResponce = parser.prompt("Δώσε όνομα παίχτη που θέλεις να αλλάξεις:");
                    userSecondResponce = parser.prompt("Δώσε νέο όνομα");
                    System.out.println(playerController.createPlayer(userResponce) + "!");
                    break;

                case SCOREBOARD:
                    String[][] scoreboard = playerController.getScoreboard();
                    if(scoreboard.length==0){
                        System.out.println("Δεν υπάρχουν καταχωρημένοι παίχτες!");
                        break;
                    }

                    System.out.printf("| %s ||%10s | %10s |%n", "Όνομα παίχτη", "Τρέχον σκόρ", "Μέγιστο σκόρ");
                    for (String[] aStringArray : scoreboard){
                        System.out.printf("| %s ||%10s |%10s |%n", aStringArray[0], aStringArray[1], aStringArray[2]);
                    }
                    break;

                case HELP:
                    printHelpMenu(availableCommands);
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
            printCommandMenu(availableCommands);
            Command userAnswer = parser.promptCommand(
                    String.format("Επίλεξε \"Εκκίνηση παιχνιδιού\" για να ξεκινήσεις το παιχνίδι ή \"Κύριο μενού\" για επιστροφή στο κύριο μενού %n" +
                            "Διαφορετικά επίλεξε \"Βοήθεια\" για λεπτομέρειες σχετικά αυτές τις ενέργειες"),
                    availableCommands);
            switch (userAnswer){
                case BEGIN_GAME:
                    executionContinues=gameProcess();
                    break;
                case MAIN_MENU:
                    executionContinues = false;
                    break;
                case HELP:
                    printHelpMenu(availableCommands);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean gameProcess(){
        int roundNumber = parser.promptPositiveInt("Πόσους γύρους θες να παίξεις;");
        roundController.setPlayerNumber(1);
        int questionNumber = parser.promptPositiveInt("Πόσες ερωτήσεις θες να έχει ο γύρος;");
        roundController.setNumberOfQuestionsPerRound(questionNumber);

        while(roundNumber--!=0){
            Round currentRound = roundController.getRandomRoundType();

        }


    }


    private void printQuestion(String questionCategory, String roundQuestion, String[] validAnswers){
        System.out.println("Category: "+ questionCategory);
        System.out.println();
        System.out.println(roundQuestion);
        System.out.println();
        for (int i=0; i<validAnswers.length; i++){
            System.out.println((i+1)+"." +validAnswers[i]);

        }
    }

}
