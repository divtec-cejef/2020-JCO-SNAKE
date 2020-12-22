package snake;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// Importation des constantes
import static snake.Constants.*;

/**
 * Contient les paramètres et la gestion des paramètres
 */
public class GameSettings {
    /**
     * Paramètres du jeu
     */
    public enum Settings {
        WALLS("Murs autour du plateau", false),
        SNAKE_RAINBOW_SHEDDING("Mue colorée", false);

        // Nom en français du paramètre
        private final String settingName;

        // Est-ce que l'option est activée
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
        private void setActivated(boolean activated) {
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

        /**
         * @param index Index de l'élèment à récupérer
         * @return un élément de la liste de paramètres
         */
        public static Settings getFromSettingsList(int index) {
            return SETTINGS_LIST.get(index);
        }

        /**
         * @return le nombre d'éléments de la liste de paramètres
         */
        public static int getSettingsCount() {
            return SETTINGS_LIST.size();
        }

        /**
         * @return l'index du dernier élément de la liste
         */
        public static int getLastSettingsIndex() {
            return SETTINGS_LIST.size() - 1;
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

        try {
            settingsFile.createNewFile();
        } catch (IOException exception){
            System.out.println(exception.getMessage());
        }

        // On supprime le contenu de l'ancien fichier
        if (settingsFile.delete()) {
            // On écrit le nouveau contenu
            for (Settings setting : Settings.SETTINGS_LIST)
                writeInFile(setting.toString(), Boolean.toString(setting.isActivated()));
        }
    }

    /**
     * Lis le contenu du fichier de configuration
     * @return le contenu de ce fichier
     * @throws IOException IOException
     */
    public List<String> readFromFile() throws IOException {
        try {
            return Files.readAllLines(Paths.get(SETTINGS_PATH));
        } catch (IOException exception) {
            exception.printStackTrace();
            List<String> liste = new ArrayList<>();
            liste.add("");
            return liste;
        }
    }

    /**
     * Récupère les valeurs du fichier et le inscrits dans la liste de paramètres du jeu
     */
    public void getFromFile() throws IOException {
        File settingsFile = new File(SETTINGS_PATH);
        if (settingsFile.exists()) {
            try {
                List<String> settingsList = readFromFile();

                int settingsIndex = 0;
                for (String setting : settingsList) {
                    // On découpe la ligne au niveau du "="
                    String[] segments = setting.split("=");

                    // On récupère le nom de l'option
                    String optionName = segments[0];

                    // On converti l'option en type option
                    Settings settingsName = null;
                    try {
                        settingsName = Settings.valueOf(optionName);
                    } catch (Exception ignored) {
                    }

                    if (settingsName != null) {
                        // On récupère la valeur de l'option
                        String optionValue = segments[segments.length - 1];

                        // On converti la valeur en booléen
                        boolean settingsValue = Boolean.parseBoolean(optionValue);

                        // On modifie l'option dans le jeu
                        if (settingsIndex < Settings.getSettingsCount())
                            Settings.getFromSettingsList(Settings.SETTINGS_LIST.indexOf(settingsName)).setActivated(settingsValue);
                    }
                    settingsIndex++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            settingsFile.createNewFile();
            getFromFile();
        }
    }
}
