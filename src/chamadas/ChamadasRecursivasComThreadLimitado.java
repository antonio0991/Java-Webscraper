package src.chamadas;

import src.WebScraper;

import java.util.HashSet;
import java.util.Set;

public class ChamadasRecursivasComThreadLimitado extends WebScraper {

    public static void main(String[] args) throws Exception {
        Set<String> linksEncontrados = new HashSet<>();
        WebScraper.getLinksRecursivoComThreadLimitado("https://pt.wikipedia.org/wiki/Campina_Grande",0 , linksEncontrados);
    }

}
