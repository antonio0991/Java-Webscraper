package src.chamadas;

import src.WebScraper;

import java.util.HashSet;
import java.util.Set;

public class ChamadasRecursivas extends WebScraper {

    public static void main(String[] args) throws Exception {
        Set<String> linksEncontrados = new HashSet<>();
        WebScraper.getLinksRecursivo("https://pt.wikipedia.org/wiki/Campina_Grande",0 , linksEncontrados);
        linksEncontrados.forEach(System.out::println);
    }

}
