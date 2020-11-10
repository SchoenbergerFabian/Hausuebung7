/**
 *
 * @author bmayr
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LinkFinder implements Runnable {

    private String url;
    private ILinkHandler linkHandler;
    private static boolean logged1500 = false;
    private static boolean logged3000 = false;
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

        if(linkHandler.size()>1500&&!logged1500){
            logged1500 = true;
            System.out.println(((System.nanoTime()-t0)/1000000000)+" s");
        }

        if(linkHandler.size()>3000&&!logged3000){
            logged3000 = true;
            System.out.println(((System.nanoTime()-t0)/1000000000)+" s");
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

