package tagfinder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OlegFrolOff
 */
public class TagFinder {

    private final String FIRST_STEP = "Тег для поиска или 0 для завершения:";
    private final String SECOND_STEP = "1:Вывод результата\n"
            + "2:Добавить тег\n"
            + "3:Исключить тег";
    private final String LAST_STEP = "1:Вывести результат в файл\n"
            + "2:Повторить поиск\n"
            + "0:Выйти";

    private Scanner scan;
    private List<FeatureFile> featureList;

    //первый уровень меню
    //циклично вызывается из main, пока не вернёт "0"
    //создаётся объект класса FileFinder, он производит поиск файлов с расширением .feature
    //коллекция найденных файлов присваивается переменной this.featureList
    //информация о количестве содержащихся в директории файлов выводится в консоль
    //у пользователя запрашивается ввод тега для поиска или 0 для завершения
    //в цикле вызывается метод secondMenu(), в который передаётся введённый пользователем тег
    //если метод secondMenu() возвращает "0", цикл завершается
    private String firstMenu() {
        String input;
      //  String dir = System.getProperty("user.dir") + "\\src";
        String dir = System.getProperty("user.dir");
        System.out.println("\nРабочая директория: " + dir);
        FileFinder fileFinder = new FileFinder(dir);
        featureList = new FeatureFileFactory(fileFinder.getFeatureFiles()).getFeatureObjects();
        System.out.println("Найдено файлов .feature: " + featureList.size());
        System.out.println(FIRST_STEP);
        input = scan.nextLine();
        if (!input.equals("0")) {
            input = secondMenu(input);
        }
        return input;
    }

    //второй уровень меню
    //циклично вызывается из firstMenu(), пока не вернёт "0"
    //создаётся объект класса Searcher, который возвращает коллекцию объектов FeatureFile, в которых найден искомый тег
    //в цикле: выводится информация о найденных строках
    //          предлагается показать результат, либо добавить ещё один тег для дальнейшего поиска в найденных строках,
    //          либо добавить исключающий тег, который не должен содержаться в результате
    private String secondMenu(String tag) {
        String input;
        Searcher searcher = new Searcher();
        List<FeatureFile> foundFeatureFiles = searcher.searchFiles(featureList, tag);

        while (true) {
            System.out.println("\nНайдено " + searcher.getNumOfEntries() + " строк. Количество файлов: " + foundFeatureFiles.size());
            System.out.println(SECOND_STEP);
            input = scan.nextLine();
            switch (input) {
                case "1":
                    return thirdMenu(foundFeatureFiles);
                case "2":
                    System.out.println("\nТег для поиска: ");
                    tag = scan.nextLine();
                    foundFeatureFiles = searcher.searchFiles(foundFeatureFiles, tag);
                    break;
                case "3":
                    System.out.println("\nИсключить тег: ");
                    tag = scan.nextLine();
                    foundFeatureFiles = searcher.searchFilesWithoutTag(foundFeatureFiles, tag);
                    break;
                default:
                    break;
            }
        }
    }

    //третий уровень меню
    //вызывается из secondMenu(), принимает коллекцию найденных объектов
    //формирует отчёт и выводит в консоль
    //предлагает вывести отчёт в файл, повторить поиск или завершить работу
    //бесконечный цикл используется для обработки ошибочного ввода
    private String thirdMenu(List<FeatureFile> foundFeatureFiles) {
        String input;
        StringBuilder report = new StringBuilder();

        foundFeatureFiles.forEach(file -> {
            report.append("\n").append(file.getFilePath()).append(":\n");
            List<String> strings;
            if (file.isFileTagSearch()) {
                report.append(file.getFileTagString().trim()).append("\n");
                strings = file.getTagStrings();
            } else {
                strings = file.getTagStringsWithDesireTag();
            }

            strings.forEach(string -> {
                report.append("\t").append(string.trim()).append("\n");
            });

        });

        System.out.println(report);
        while (true) {
            System.out.println("\n" + LAST_STEP);
            input = scan.nextLine();
            switch (input) {
                case "1":
                    reportFileCreate(report.toString());
                    return input;
                case "2":
                    return input;
                case "0":
                    return input;
                default:
                    break;
            }
        }

    }

    //вызывается из thirdMenu() и принимает сформированный отчёт
    //записывает отчёт в файл
    private void reportFileCreate(String report) {
        String txtRepPath = System.getProperty("user.dir") + "\\TagSearcherReport.txt";
        Path txtReport = Paths.get(txtRepPath);
        try (FileWriter writer = new FileWriter(txtReport.toFile(), false)) {
            writer.write(report);
        } catch (IOException ex) {
            Logger.getLogger(TagFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //создается новый объект TagFinder, инициализируется this.scan
    //в цикле вызывается метод начального меню, пока он не вернёт строку "0"
    //метод начального меню запускает поиск файлов .fearure
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        TagFinder ts = new TagFinder();
        try (Scanner scan = new Scanner(System.in)) {
            ts.scan = scan;
            while (!ts.firstMenu().equals("0"));
        }
    }

}
