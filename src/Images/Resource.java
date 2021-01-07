package Images;

import javax.swing.*;
import java.net.URL;

public class Resource {
    public static URL getURL(String imageFilename) {
        return Resource.class.getResource(imageFilename);
    }

    public static ImageIcon getImageIcon(String imageFilename) {
        return new ImageIcon(getURL(imageFilename));
    }
}
