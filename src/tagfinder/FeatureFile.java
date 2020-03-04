package tagfinder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author OlegFrolOff
 */
public class FeatureFile {

    private final Path filePath;
    private final List<String> content; //всё содержимое файла в виде списка строк
    private List<String> tagStrings; //все строки с тегами кроме самой первой строки, относящейся к файлу
    private final String fileTagString; //строка тегов, относящаяся к всему файлу

    //переменные служат для дальнейшего поиска
    private boolean fileTagSearch; //переменной присваивается значение true, если искомый тег находится в строке, относящейся ко всему файлу
    private List<String> tagStringsWithDesireTag; //список строк, в которые входит искомый тег

    {
        fileTagSearch = false;
        tagStringsWithDesireTag = new ArrayList<>();
    }

    public FeatureFile(Path filePath, List<String> content) {
        this.filePath = filePath;
        this.content = content;
        this.tagStrings = new ArrayList<>();
        this.content.forEach(ts -> {
            if (ts.trim().startsWith("@")) {
                tagStrings.add(ts);
            }
        });
        fileTagString = tagStrings.isEmpty() ? "" : this.tagStrings.remove(0); // если в файлах не предполагается наличие общей для всего файла строки тега, закомментировать эту строку
    }

    //далее геттеры и сеттеры
    public List<String> getContent() {
        return content;
    }

    public Path getFilePath() {
        return filePath;
    }

    public List<String> getTagStrings() {
        return tagStrings;
    }

    public String getFileTagString() {
        return fileTagString;
    }

    public boolean isFileTagSearch() {
        return fileTagSearch;
    }

    public void setFileTagSearch(boolean fileTagSearch) {
        this.fileTagSearch = fileTagSearch;
    }

    public List<String> getTagStringsWithDesireTag() {
        return tagStringsWithDesireTag;
    }

    public void setTagStringsWithDesireTag(List<String> tagStringsWithDesireTag) {
        this.tagStringsWithDesireTag = tagStringsWithDesireTag;
    }
}
