package searchengine.text;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Lemmatize {

    public static HashSet<String> getWordsLemmas(String inputText) throws IOException {

        LuceneMorphology luceneMorph = new RussianLuceneMorphology();

        String textForProcessing = getTextForProcessing(inputText);
        HashSet <String> wordLemmas = new HashSet<>();
        for (String word: textForProcessing.split(" ")) {
            word = word.trim();
            if (word.isEmpty()) {
                continue;
            }
            List<String> morphInfo = luceneMorph.getMorphInfo(word);
            if(isServiceWord(morphInfo)) {
                continue;
            }
            List<String> normalForms = luceneMorph.getNormalForms(word);
            wordLemmas.addAll(normalForms);
        }

        return wordLemmas;
    }

    public static HashMap<String, Integer> getWordsLemmasWithQty(String inputText) throws IOException {

        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        HashMap <String, Integer> wordLemmas = new HashMap<>();
        String textForProcessing = getTextForProcessing(inputText);
        for (String word: textForProcessing.split(" ")) {
            word = word.trim();
            if (word.isEmpty()) {
                continue;
            }
            List<String> morphInfo = luceneMorph.getMorphInfo(word);
            if(isServiceWord(morphInfo)) {
                continue;
            }
            List<String> normalForms = luceneMorph.getNormalForms(word);
            for (String normalForm: normalForms) {
                int count = wordLemmas.getOrDefault(normalForm, 0);
                wordLemmas.put(normalForm, count + 1);
            }
        }

        return wordLemmas;
    }

    private static String getTextForProcessing(String inputText) {

        String textForProcessing = inputText.replaceAll("[^[А-Яа-я\\-]]", " ");
        textForProcessing = textForProcessing.toLowerCase();

        return textForProcessing;
    }

    private static boolean isServiceWord(List<String> morphInfo) {
        for (String info: morphInfo) {
            boolean isServiceWord =
                    info.contains("|n") ||
                    info.contains("|o") ||
                    info.contains("|p") ||
                    info.contains("|l");
            if (isServiceWord) return true;
        }
        return false;
    }

}
