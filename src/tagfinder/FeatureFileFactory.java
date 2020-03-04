package tagfinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OlegFrolOff
 */
public class FeatureFileFactory {

    private final ArrayList<Path> featuresList;
    private ArrayList<FeatureFile> featureObjects;

    public FeatureFileFactory(ArrayList<Path> featuresList) {
        this.featuresList = featuresList;
        this.featureObjects = new ArrayList<>();
        if (this.featuresList != null && !this.featuresList.isEmpty()) {
            this.featuresList.forEach(feature -> {
                List<String> fileLines = new ArrayList<>();
                try {
                    fileLines = Files.readAllLines(feature);
                } catch (IOException ex) {
                    Logger.getLogger(FeatureFileFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
                featureObjects.add(new FeatureFile(feature, fileLines));
            });
        }
    }

    public ArrayList<FeatureFile> getFeatureObjects() {
        return featureObjects;
    }

}
