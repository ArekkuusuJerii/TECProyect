package Principal;

import Resources.ResourceHelper;
import Utils.CleanupDone;
import java.awt.Color;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Descripción: Inicio del Programa
 *
 */
@CleanupDone
public class Launch {

    private static boolean isLoaded;

    /**
     * Uso: Llama el método de Textura y carga una nueva ventana Iniciar Sesión.
     *
     * Descripción: Si isloaded es false manda un aviso a la vista inicio.
     *
     * @param args // Carga el Programa
     */
    public static void main(String[] args) {
        isLoaded = loadTexture();
        Vista.VIniciarSesion IniciarSesion = new Vista.VIniciarSesion();
        IniciarSesion.setLocationRelativeTo(null);
        IniciarSesion.setVisible(true);
        if (!isLoaded) {
            IniciarSesion.JIngresar.setToolTipText("La Textura del Programa no se ha podido cargar, proceda con normalidad");
            IniciarSesion.JIngresar.setForeground(Color.RED);
        }
    }

    /**
     * Uso: Carga la Textura del programa.
     *
     * Descripción: Devuelve un true si encuentra el look and feel, false si
     * ocurre un error.
     *
     */
    private static boolean loadTexture() {
        try {
            Properties props = new Properties();
            props.put("textureSet", "Custom");
            Icon texture = ResourceHelper.cargarImagen("WindowTexture.jpg");
            if (texture != null) {
                props.put("windowTexture", texture);
            }
            texture = ResourceHelper.cargarImagen("BackgroundTexture.jpg");
            if (texture != null) {
                props.put("backgroundTexture", texture);
            }
            texture = ResourceHelper.cargarImagen("AlterBackgroundTexture.jpg");
            if (texture != null) {
                props.put("alterBackgroundTexture", texture);
            }
            texture = ResourceHelper.cargarImagen("RolloverTexture.jpg");
            if (texture != null) {
                props.put("rolloverTexture", texture);
            }
            texture = ResourceHelper.cargarImagen("SelectedTexture.jpg");
            if (texture != null) {
                props.put("selectedTexture", texture);
            }
            texture = ResourceHelper.cargarImagen("PressedTexture.jpg");
            if (texture != null) {
                props.put("pressedTexture", texture);
            }
            texture = ResourceHelper.cargarImagen("DisabledTexture.jpg");
            if (texture != null) {
                props.put("disabledTexture", texture);
            }
            texture = ResourceHelper.cargarImagen("MenubarTexture.jpg");
            if (texture != null) {
                props.put("menubarTexture", texture);
            }

            props.setProperty("backgroundColor", "240 240 240");
            props.setProperty("backgroundColorLight", "220 220 220");
            props.setProperty("backgroundColorDark", "200 200 200");
            props.setProperty("alterBackgroundColor", "180 180 180");

            props.setProperty("frameColor", "164 164 164");
            props.setProperty("gridColor", "196 196 196");

            props.setProperty("disabledForegroundColor", "96 96 96");
            props.setProperty("disabledBackgroundColor", "240 240 240");

            props.setProperty("rolloverColor", "160 160 160");
            props.setProperty("rolloverColorLight", "230 230 230");
            props.setProperty("rolloverColorDark", "210 210 210");

            props.setProperty("controlBackgroundColor", "248 248 248");
            props.setProperty("controlShadowColor", "160 160 160");
            props.setProperty("controlDarkShadowColor", "110 110 110");
            props.setProperty("controlColorLight", "248 248 248");
            props.setProperty("controlColorDark", " 210 210 210");

            props.setProperty("buttonColorLight", "255 255 255");
            props.setProperty("buttonColorDark", "230 230 230");

            props.setProperty("menuBackgroundColor", "64 64 64");
            props.setProperty("menuColorLight", "96 96 96");
            props.setProperty("menuColorDark", "48 48 48");
            props.setProperty("menuSelectionBackgroundColor", "48 48 48");
            props.setProperty("toolbarBackgroundColor", "64 64 64");
            props.setProperty("toolbarColorLight", "96 96 96");
            props.setProperty("toolbarColorDark", "48 48 48");
            props.setProperty("desktopColor", "220 220 220");

            com.jtattoo.plaf.texture.TextureLookAndFeel.setCurrentTheme(props);

            UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
