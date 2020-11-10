/**
 *
 * @author bmayr
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LinkFinder implements Runnable {

    private String url;
    private ILinkHandler linkHandler;
    private static boolean logged = false;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, ILinkHandler handler) {
        this.url = url;
        this.linkHandler = handler;
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        //System.out.println(url);

        if(linkHandler.size()>500&&!logged){
            logged = true;
            System.out.println(((System.nanoTime()-t0)/1000)+" ms");
        }

        if(!linkHandler.visited(url)){
            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");
                linkHandler.addVisited(url);

                links.stream()
                        .forEach(link -> {
                            try {
                                if(link.attr("href").startsWith("http")){
                                    linkHandler.queueLink(link.attr("href"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

            } catch (IOException e) {

            }
        }
    }
}

