package snake;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Importation des constantes
import static snake.Constants.*;

/**
 * Contient les paramètres et la gestion des paramètres
 */
public class GameSettings {
    /**
     * Paramètres possibles pour le jeu
     */
    public enum Settings {
        WALLS("Murs autour du plateau", false);

        // Nom en français du paramètre
        private final String settingName;

        // Est-ce que le menu est activé
        private boolean isActivated;

        /**
         * Construit un paramètre
         *
         * @param settingName Nom du paramètre
         */
        Settings(String settingName, boolean isActivated) {
            this.settingName = settingName;
            this.isActivated = isActivated;
        }

        /**
         * @return le nom d'un paramètre
         */
        public String getSettingName() {
            return this.settingName;
        }

        /**
         * @return l'activation d'un paramètre
         */
        public boolean isActivated() {
            return isActivated;
        }

        /**
         * Modifie l'activation d'une option
         * @param activated Nouvelle option du paramètre
         */
        public void setActivated(boolean activated) {
            isActivated = activated;
        }

        /**
         * Inverse l'activation d'un paramètre
         */
        public void toggleOption() {
            isActivated = !isActivated;
        }

        // Liste de tous les paramètres
        private static final List<Settings> SETTINGS_LIST = Collections.unmodifiableList(Arrays.asList(values()));

        /**
         * @return la liste des paramètres
         */
        public static List<Settings> getSettingsList() {
            return SETTINGS_LIST;
        }

        public static Settings getFromSettingsList(int index) {
            return SETTINGS_LIST.get(index);
        }
    }

    /**
     * Ecrit un paramètre dans le fichier de configuration
     * @param settingsName Nom du paramètre
     * @param settingValue Valeur de ce paramètre
     */
    public void writeInFile(String settingsName, String settingValue) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SETTINGS_PATH, true))) {
            bw.write(settingsName + "=" + settingValue + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ecrit tous les paramètres du jeu dans le fichier de configuration
     */
    public void writeAllInFile() {
        File settingsFile = new File(SETTINGS_PATH);
        if (settingsFile.delete()) {
            for (Settings setting : Settings.SETTINGS_LIST) {
                writeInFile(setting.toString(), Boolean.toString(setting.isActivated()));
            }
        }
    }

    /**
     * Lis le contenu du fichier de configuration
     * @return le contenu de ce fichier
     * @throws IOException IOException
     */
    public List<String> readFromFile() throws IOException {
        return Files.readAllLines(Paths.get(SETTINGS_PATH));
    }

    /**
     * Inscrit les valeurs du fichier dans la liste de paramètres du jeu
     */
    public void getFromFile() {
        try {
            List<String> settingsList = readFromFile();

            int settingsIndex = 0;
            for (String setting: settingsList) {
                // On découpe la ligne au niveau du "="
                String[] segments = setting.split("=");

                // On récupère le nom de l'option
                String optionName = segments[0];
                // On converti l'option en type option
                Settings settingsName = null;
                try {
                    settingsName = Settings.valueOf(optionName);
                } catch (Exception ignored) {}

                if (settingsName != null) {
                    // On récupère la dernière partie
                    String optionValue = segments[segments.length - 1];
                    // On converti la valeur en booléen
                    boolean settingsValue = Boolean.parseBoolean(optionValue);

                    // On modifie l'option dans le jeu
                    if (settingsIndex < Settings.SETTINGS_LIST.size()) {
                        Settings.SETTINGS_LIST.get(Settings.SETTINGS_LIST.indexOf(settingsName)).setActivated(settingsValue);
                    }
                }

                settingsIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
