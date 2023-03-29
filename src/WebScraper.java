package src;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScraper {
    public static final String LINK_REGEX = "<a.+href\\s*=\\s*\"([^\"]+)\"";
    public static final Integer MAX_NIVEL = 4;
    public static final ThreadPoolExecutor POOL = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);


    public static void main(String[] args) throws Exception {
        for (String link : getLinks("https://pt.wikipedia.org/wiki/Campina_Grande")) {
            System.out.println(link);
        }
    }

    private static boolean ehAbsoluto(String link) {
        return link.startsWith("http");
    }

    private static boolean ehRelativo(String link) {
        return !ehAbsoluto(link);
    }

    private static URL getUrlAbsoluta(URL pai, String linkRelativo) throws Exception {
        return new URL(pai.getProtocol(), pai.getHost(), linkRelativo);
    }

    public static Set<String> encontradorLinks(String urlOrigem) throws Exception {
        Set<String> linksDescobertos = new HashSet<>();
        Pattern linkPattern = Pattern.compile(LINK_REGEX, Pattern.CASE_INSENSITIVE);

        Scanner leitor;

        try {
            URL u = new URL(urlOrigem); // crio um objeto URL
            leitor = new Scanner(u.openStream()); // abro um strem de leitura para a URL


            while (leitor.hasNextLine()) { // enquanto tem linhas para ler
                String linha = leitor.nextLine(); // leio uma linha do stream
                Matcher linkMatcher = linkPattern.matcher(linha); // crio um matcher para a regex de links

                while (linkMatcher.find()) { // para cada link encontrado
                    String link = linkMatcher.group(1); // retorna o HREF dele

                    if (ehRelativo(link)) { // transformar em absoluto
                        URL linkAbsoluto = getUrlAbsoluta(u, link);
                        linksDescobertos.add(linkAbsoluto.toString());
                    } else if (ehAbsoluto(link)) { // eh absoluto
                        linksDescobertos.add(link);
                    }
                }
            }
        } catch (IOException exception) {
            return linksDescobertos;
        }

        leitor.close(); // fecho o stream
        return linksDescobertos;
    }

    public static Set<String> getLinks(String urlOrigem) throws Exception {
        return encontradorLinks(urlOrigem);
    }

    public static void getLinksRecursivo(String urlOrigem, int nivel, Set<String> linksVisitados) throws Exception {
        if (linksVisitados.contains(urlOrigem) || nivel > MAX_NIVEL) {
            return;
        } else {
            linksVisitados.add(urlOrigem);
        }
        System.out.println("Link Descoberto:" + urlOrigem + " no nivel:" + nivel);

        Set<String> linksDescobertos = encontradorLinks(urlOrigem);

        for (String linkDescoberto : linksDescobertos) {
            getLinksRecursivo(linkDescoberto, nivel + 1, linksVisitados);
        }
    }

    public static void getLinksRecursivoComThread(String urlOrigem, int nivel, Set<String> linksVisitados) {
        Thread thread = new Thread(new WebScraperThread(urlOrigem, nivel, linksVisitados));
        thread.start();
    }

    public static void getLinksRecursivoComThreadLimitado(String urlOrigem, int nivel, Set<String> linksVisitados) throws Exception {
        if (linksVisitados.contains(urlOrigem) || nivel > MAX_NIVEL) {
            return;
        } else {
            linksVisitados.add(urlOrigem);
        }
        System.out.println("Link:" + urlOrigem + " Nivel:" + nivel + " THREADS ATIVAS: " + POOL.getActiveCount());

        Set<String> linksDescobertos = encontradorLinks(urlOrigem);

        for (String linkDescoberto : linksDescobertos) {
            POOL.submit(() -> {
                try {
                    getLinksRecursivoComThreadLimitado(linkDescoberto, nivel + 1, linksVisitados);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
