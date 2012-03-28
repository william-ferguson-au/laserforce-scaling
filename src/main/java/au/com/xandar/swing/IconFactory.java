package au.com.xandar.swing;

import javax.swing.*;
import java.net.URL;

/**
 * Responsible for finding and constructing an icon resource.
 * <p/>
 * User: William
 * Date: 27/05/2010
 * Time: 8:07:51 PM
 */
public final class IconFactory {

    public ImageIcon createImageIcon(Class loadingClass, String resourcePath) {
        final URL imgURL = loadingClass.getResource(resourcePath);
        return (imgURL != null) ? new ImageIcon(imgURL) : null;
    }
}
