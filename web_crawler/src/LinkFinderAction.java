import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

/**
 *
 * @author bmayr
 */

// Recursive Action for forkJoinFramework from Java7

public class LinkFinderAction extends RecursiveAction {

    private String url;
    private ILinkHandler cr;
    private static boolean logged = false;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();

    public LinkFinderAction(String url, ILinkHandler cr) {
        this.url = url;
        this.cr = cr;
    }

    @Override
    public void compute() {
        //System.out.println(url);

        if(cr.size()>500&&!logged){
            logged = true;
            System.out.println(((System.nanoTime()-t0)/1000)+" ms");
        }

        if(!cr.visited(url)){
            try {
                Document doc = Jsoup.connect(url).ignoreContentType(true).get();
                Elements links = doc.select("a[href]");
                cr.addVisited(url);

                List<LinkFinderAction> subfinders = links.stream()
                        .map(link -> {
                            if(link.attr("href").startsWith("http")){
                                return new LinkFinderAction(link.attr("href"),cr);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                invokeAll(subfinders);

            } catch (IOException e) {

            }
        }

    }
}

