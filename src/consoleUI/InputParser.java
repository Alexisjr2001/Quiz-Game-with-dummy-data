package consoleUI;

import java.util.Scanner;

public class InputParser {
    private Scanner userInput;

    public InputParser(){
        userInput = new Scanner(System.in);
    }

    public String prompt(String userMessage){
        System.out.println(userMessage);
        System.out.print("> ");
        return userInput.nextLine();
    }

    private boolean isAcceptedCommand(String aPossibleCommand, Command[] acceptedCommands){
        Command aCommand = Command.valueOfGreek(aPossibleCommand);
        if (aCommand!=null){
            for(Command c : acceptedCommands){
                if(c == aCommand){
                    return true;
                }
            }
            return false;
        }
        else{
            return false;
        }

    }

    public Command promptCommand(String userMessage, Command[] acceptedCommands){
        boolean isIncomplete;
        String aPrompt = prompt(userMessage);
        isIncomplete = isAcceptedCommand(aPrompt, acceptedCommands);
        while(isIncomplete){
            System.out.println("Λάθος είσοδος! Προσπάθησε ξανά...");
            aPrompt = prompt(userMessage);
            isIncomplete = isAcceptedCommand(aPrompt, acceptedCommands);
        }
        return Command.valueOfGreek(aPrompt);
    }

    public int promptPositiveInt(String userMessage){
        int aPositiveNumber = Integer.parseInt(userMessage);
        while(aPositiveNumber<=0){
            System.out.println("Πρέπει να δώσεις θετικό αριθμό!");
            aPositiveNumber = Integer.parseInt(userMessage);
        }
        return aPositiveNumber;
    }

    public int promptIntInRange(String userMessage,int low, int high){
        int aNumber = Integer.parseInt(userMessage);
        while(aNumber>=low && aNumber<=high ){
            System.out.println("Πρέπει να δώσεις ακέραιο αριθμό εντός [" + low + " ," + high + "]");
            aNumber = Integer.parseInt(userMessage);
        }
        return aNumber;
    }


}
