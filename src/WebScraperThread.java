package src;

import java.util.HashSet;
import java.util.Set;

import static src.WebScraper.MAX_NIVEL;
import static src.WebScraper.encontradorLinks;

public class WebScraperThread implements Runnable {

    private final String urlOrigem;
    private final int nivel;
    private final Set<String> linksVisitados;

    public WebScraperThread(String urlOrigem, int nivel, Set<String> linksVisitados){
        this.urlOrigem = urlOrigem;
        this.nivel = nivel;
        this.linksVisitados = linksVisitados;
    }

    @Override
    public void run() {
        if(linksVisitados.contains(urlOrigem) || nivel > MAX_NIVEL){
            return;
        }else {
            linksVisitados.add(urlOrigem);
        }
        System.out.println("Link:" + urlOrigem + "  Nivel:" + nivel);

        Set<String> linksDescobertos;
        try {
            linksDescobertos = encontradorLinks(urlOrigem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Size: " + linksDescobertos.size());

        for (String linkDescoberto:linksDescobertos) {
            Thread newThread = new Thread(new WebScraperThread(linkDescoberto, nivel + 1, linksVisitados));
            newThread.start();
        }
    }
}
