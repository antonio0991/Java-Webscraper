package src.chamadas;

import src.WebScraper;

import java.util.HashSet;
import java.util.Set;

public class ChamadasMultiplas {

    public static void main(String[] args) throws Exception {
        Set<String> linksEncontrados = new HashSet<>();
        for (String linkDescoberto: WebScraper.getLinks("https://pt.wikipedia.org/wiki/Campina_Grande")) {
            System.out.println("Buscando links da pagina:" + linkDescoberto + "...");
            linksEncontrados.add(linkDescoberto);
            for(String subLinkDescoberto: WebScraper.getLinks(linkDescoberto)){
                System.out.println("\t" + subLinkDescoberto);
                linksEncontrados.add(subLinkDescoberto);
            }
        }

    }
}

