package internals.question;

import javax.swing.*;

/**
 * Η κλάση {@code ImageQuestion} επεκτείνει την κλάση {@code Question}
 * και μοντελοποιεί την οντότητα μίας ερώτησης  με εικόνα που πρέπει να απαντηθεί κατά την διάρκεια ενός γύρου.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.31
 */
public class ImageQuestion extends Question{
    private ImageIcon imageIcon; //Η εικόνα της ερώτησης

    /**
     * Ο τυπικός κατασκευαστής της κλάσης που αρχικοποιεί τα δεδομένα της σύμφωνα με τα ορίσματα.
     * @param question Η εκφώνηση της ερώτησης.
     * @param answers Οι πιθανές απαντήσεις της ερώτησης.
     * @param rightAnswer Η σωστή απάντηση.
     * @param category Το όνομα της κατηγορίας στην οποία ανήκει η ερώτηση.
     * @param imageFileName Το όνομα της εικόνας που συνδέεται με την ερώτηση
     */
    public ImageQuestion(String question, String[] answers, String rightAnswer, String category, String imageFileName) {
        super(question, answers, rightAnswer, category);
        imageIcon = new ImageIcon(imageFileName);
    }

    /**
     * Επιστρέφει αναφορά στην εικόνα της ερώτησης
     * @return Την εικόνα της ερώτησης
     */
    public ImageIcon getImageIcon() {
        return imageIcon;
    }
}
