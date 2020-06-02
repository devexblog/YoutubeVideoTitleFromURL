package com.example.youtubevideotitlefromurl.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;


public class TitleCallable implements Callable<String> {
    private String url;

    public TitleCallable(String url) {
        this.url = url;
    }

    @Override
    public String call(){
        try {
            if (url != null) {
                Document document = Jsoup.connect(url).get();
                Elements metaTags = document.getElementsByTag("meta");
                for (Element metaElement : metaTags) {
                    String property = metaElement.attr("property");
                    if (property.equals("og:title")) {
                        String title = metaElement.attr("content");
                        return title;
                    }
                }
                return null;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}