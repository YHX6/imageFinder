package com.eulerity.hackathon.services;

import org.jsoup.Jsoup;
import  org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class WebCrawlerService {
    public final String userAgentChromeWindows = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36";
//    public final String userAgentChromeMacOS = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36";
//    public final String userAgentChromeAndroid = "Mozilla/5.0 (Linux; Android 10; SM-A505FN) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.61 Mobile Safari/537.36";

    public ProxyService proxyService;
    public final int maxAttempts = 3; // for trying to crawl an image if proxy failed, up to three times
    private final Logger logger = Logger.getLogger(WebCrawlerService.class.getName());


    public WebCrawlerService() {
        this.proxyService = new ProxyService();
        try {
            FileHandler fileHandler = new FileHandler("crawl.log", true); // Append to the log file
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (SecurityException | IOException e) {
            logger.warning("Failed to initialize logger file handler: " + e.getMessage());
        }
    }

    // method for crawling images and alt given url:
    public List<String[]> crawlImage(String url){
        List<String[]> imageURLs = new ArrayList<>();  // result

        for (int i=0; i<maxAttempts; i++){
            try{
                // get DOM
                Document document = Jsoup
                        .connect(url)
                        .proxy(proxyService.getHttpsProxy())
                        .userAgent(userAgentChromeWindows)
                        .get();

                // get all images in the doc
                Elements images = document.select("img[src]");
                for(Element ele : images){
                    String src =  ele.absUrl("src");
                    String alt = ele.attr("alt");

                    if(!src.trim().isEmpty()){ // in case some src is empty string
                        String[] pair = new String[] {src, alt};
                        imageURLs.add(pair);
                    }
                }
                break; // if success, stop attempts
            }catch (IOException e) {
                System.out.println("Attempt " + i + " failed: " + e.getMessage());
                if (i == maxAttempts - 1){
                    logger.severe("Maximum attempts reached. Web crawl failed: " + url);
                }
            }
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
