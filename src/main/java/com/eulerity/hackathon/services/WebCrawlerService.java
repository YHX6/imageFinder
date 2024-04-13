package com.eulerity.hackathon.services;

import org.jsoup.Jsoup;
import  org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class WebCrawlerService {
    public final String userAgentChromeWindows = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36";
    public final String userAgentChromeMacOS = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36";
    public final String userAgentChromeAndroid = "Mozilla/5.0 (Linux; Android 10; SM-A505FN) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.61 Mobile Safari/537.36";

    // method for crawling images and alt given url:
    public List<String[]> crawlImage(String url){
        List<String[]> imageURLs = new ArrayList<>();  // result

        try{
            // get DOM
            Document document = Jsoup
                   .connect(url)
                   .userAgent(userAgentChromeWindows)
                   .get();

            // get all images in the doc
            Elements images = document.select("img[src]");
            for(Element ele : images){
                String src =  ele.absUrl("src");
                String alt = ele.attr("alt");

                if(!src.trim().isEmpty()){ // incase some src is empty string
                    String[] pair = new String[] {src, alt};
                    imageURLs.add(pair);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return imageURLs;
    }

    // method for crawling hrefs given url:
    public List<String> crawlHrefs(String url){
        List<String> res = new ArrayList<>();  // result

        try{
            // get DOM
            Document document = Jsoup
                    .connect(url)
                    .userAgent(userAgentChromeWindows)
                    .get();

            // get all hreds in the doc
            Elements links = document.select("a[href]");
            for (Element link : links) {
                res.add(link.attr("href"));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


}
