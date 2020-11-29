package consoleUI;

import internals.round.Round;

public class UserAssistingMessages {
    public static void printCommandMenu(Command[] commands){
        System.out.println("-----   Διαθέσιμες Ενέργειες   -----");
        for(Command c : commands){
            System.out.println("\t* " + c.greekName);
        }
        System.out.println("------------------------------------");
    }

    public static void printHelpMenu(Command[] commands){
        System.out.println("-----   Διαθέσιμες Ενέργειες   -----");
        for(Command c : commands){
            System.out.println("\t* " + c.greekName + ":\t" + c.description);
        }
        System.out.println("------------------------------------");
    }

    public static void printGain(int gain, String rightAnswer){
        if (gain>0) {
            System.out.println("Σωστή Απάντηση!");
            System.out.printf("Κέρδισες %d πόντους!%n", gain);
        }
        else {
            System.out.println("Λάθος απάντηση...");
            System.out.printf("Η σωστή ήταν η \"%s\"%n", rightAnswer);
        }
    }

    public static void printRoundInfo(Round aRound){
        System.out.println("Αυτος ο γύρος είναι ο:");
        System.out.println(aRound.getRoundName());
        System.out.println();
        System.out.println("Περιγραφή:");
        System.out.println(aRound.getRoundDescription());
    }

    public static void printQuestion(String roundQuestion, String[] validAnswers){
        System.out.println(roundQuestion);
        System.out.println();
        for (int i=0; i<validAnswers.length; i++){
            System.out.println((i+1)+"." +validAnswers[i]);

        }
    }

    public static void printList(String listLabel, String[] listContents){
        System.out.printf("-----   %s   -----%n", listLabel);
        for (String aListElement : listContents) {
            System.out.println("\t* " + aListElement);
        }
        System.out.printf("--------%s--------%n", "-".repeat(listLabel.length()));
    }

}
