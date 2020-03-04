package tagfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author OlegFrolOff
 */
public class Searcher {

    private int numOfEntries;

    //метод для поиска файлов FeatureFile, содержащих искомый тег    
    public List<FeatureFile> searchFiles(List<FeatureFile> featureObjects, String tag) {
        numOfEntries = 0;
        List<FeatureFile> found = new ArrayList<>();
        featureObjects.forEach((featureObject) -> {
            int entries = 0;
            
            //поиск тега в строке, относящейся ко всему файлу            
            boolean tagFoundInMainString = searchTag(featureObject.getFileTagString(), tag);            
            if (tagFoundInMainString) {
                entries++;
                featureObject.setFileTagSearch(true);
            } else {
                featureObject.setFileTagSearch(false);
            }
            
            //поиск тега в строках, относящихся к кейсам            
            List<String> checkStrings = featureObject.getTagStringsWithDesireTag().isEmpty()
                    ? featureObject.getTagStrings() : featureObject.getTagStringsWithDesireTag();
            List<String> foundStrings = new ArrayList<>();
            for (String string : checkStrings) {
                boolean tagFound = searchTag(string, tag);
                if (tagFound) {
                    foundStrings.add(string);
                    entries++;
                }
            }
            featureObject.setTagStringsWithDesireTag(foundStrings);
            if (entries > 0) {
                numOfEntries += entries;
                found.add(featureObject);
            }
        });

        return found;
    }

    //метод для поиска файлов FeatureFile, не содержащих искомый тег
    public List<FeatureFile> searchFilesWithoutTag(List<FeatureFile> featureObjects, String tag) {
        numOfEntries = 0;
        List<FeatureFile> found = new ArrayList<>();

        featureObjects.forEach((featureObject) -> {
            int entries = 0;
            
            //поиск тега в строке, относящейся ко всему файлу
            //ищем исключающий тег, если при предыдущем поиске был найден тег в этой строке
            if (featureObject.isFileTagSearch()) {
                boolean tagFoundInMainString = searchTag(featureObject.getFileTagString(), tag);                
                if (tagFoundInMainString) {
                    featureObject.setFileTagSearch(false);
                } else {
                    entries++;
                    featureObject.setFileTagSearch(true);
                }
            }
            
            //поиск тега в строках, относящихся к кейсам            
            List<String> checkStrings = featureObject.getTagStringsWithDesireTag();
            List<String> foundStrings = new ArrayList<>();
            for (String string : checkStrings) {
                boolean tagFound = searchTag(string, tag);                
                if (!tagFound) {
                    foundStrings.add(string);
                    entries++;
                }
            }
            featureObject.setTagStringsWithDesireTag(foundStrings);
            if (entries > 0) {
                numOfEntries += entries;
                found.add(featureObject);
            }
        });

        return found;
    }

    //поиск тега в строке
    private boolean searchTag(String string, String tag) {
        boolean tagFound = false;
        List<String> splittedTagString = new ArrayList<>(Arrays.asList(string.trim().split("@")));
        splittedTagString.removeIf(str -> str.isEmpty());
        for (String tagString : splittedTagString) {
            if (tagString.trim().equalsIgnoreCase(tag)) {
                tagFound = true;
            }
        }
        return tagFound;
    }

    public int getNumOfEntries() {
        return numOfEntries;
    }
}
