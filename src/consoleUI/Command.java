package consoleUI;

public enum Command {
    BEGIN_GAME("Εκκίνηση παιχνιδιού","Ξεκινάς το παιχνίδι"),
    EXIT_GAME("Έξοδος","Κλείνεις το παιχνίδι"),
    MANAGE_PLAYERS("Διαχείριση παίχτη", "Εισέρχεσαι στο μενού επιλογών Παίχτη"),
    HELP("Βοήθεια", "Εμφανίζονται εκτενέστερα οι διαθέσιμες ενέργειες"),
    DELETE_PLAYER("Διαγραφή Παίχτη", "Διαγράφεται η εγγραφή ενός παίχτη απο το παιχνίδι"),
    CREATE_PLAYER("Δημιουργία παίχτη", "Δημιουργείται ένας νέος παίχτης στο παιχνίδι"),
    RENAME_PLAYER("Μετονομασία παίχτη","Αλλάζεις το όνομα του παίχτη"),
    SCOREBOARD("Εμφάνιση πίνακα σκόρ", "Εμφανίζεται ο πίνακας με το τρέχον και μέγιστο σκόρ κάθε παίχτη"),
    //,SCORE_PLAYER("Εμφάνιση σκορ παίχτη", "Εμφανίζεται το τρέχον ατομικό σκορ του παίχτη")
    MAIN_MENU("Κύριο μενού", "Επιστρέφει στο κύριο μενού επιλογών του παιχνιδιού")
    ;

    public final String greekName;
    public final String description;

    public static Command valueOfGreek(String name){
        name = name.toLowerCase();

        switch (name){
            case "εκκίνηση παιχνιδιού":
            case "εκκινηση παιχνιδιου": return BEGIN_GAME;

            case "έξοδος":
            case "εξοδος": return EXIT_GAME;

            case "διαχείριση παίχτη":
            case "διαχειριση παιχτη": return MANAGE_PLAYERS;

            case "διαγραφή παίχτη":
            case "διαγραφη παιχτη": return DELETE_PLAYER;

            case "δημιουργία παίχτη":
            case "δημιουργια παιχτη": return CREATE_PLAYER;

            case "μετονομασία παίχτη":
            case "μετονομασια παιχτη": return RENAME_PLAYER;

            case "εμφάνιση πίνακα σκόρ":
            case "εμφανιση πινακα σκορ": return SCOREBOARD;


            case "κύριο μενού":
            case "κυριο μενου": return MAIN_MENU;



            default: return null;
        }
    }

    Command(String greekName, String description) {
        this.greekName = greekName;
        this.description = description;
    }
}
