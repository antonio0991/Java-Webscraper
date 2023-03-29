package src.chamadas;

import src.WebScraper;

import java.util.HashSet;
import java.util.Set;

public class ChamadasRecursivasComThread extends WebScraper {

    public static void main(String[] args) throws Exception {
        Set<String> linksEncontrados = new HashSet<>();
        WebScraper.getLinksRecursivoComThread("https://pt.wikipedia.org/wiki/Campina_Grande",0 , linksEncontrados);
    }

}
