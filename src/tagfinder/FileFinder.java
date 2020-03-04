
package tagfinder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author OlegFrolOff
 */
public class FileFinder {
    private final String FILES_SIGN = ".feature"; //признак имени файла, по которому отбираются файлы в коллекцию
    private final Path dir;                        //директория, в которой производится поиск файлов
    private ArrayList<Path> featureFiles;
    
    public FileFinder(String path){
        this.dir = Paths.get(path);
        featureFiles = new ArrayList<>();
        findTheFeatures(dir.toFile());        
    }
    
    
    private void findTheFeatures(File dir){
        File[] containedFiles = dir.listFiles();
        if(containedFiles != null){
            for(File file : containedFiles){
                if(file.isFile() && file.getName().contains(FILES_SIGN)){
                    featureFiles.add(file.toPath());
                } else if(file.isDirectory()){
                    findTheFeatures(file);
                }
            }
        }
    }

    public ArrayList<Path> getFeatureFiles() {
        return featureFiles;
    }    
}
