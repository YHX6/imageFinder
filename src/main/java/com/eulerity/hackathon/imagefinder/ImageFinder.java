package com.eulerity.hackathon.imagefinder;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eulerity.hackathon.services.WebCrawlerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

@WebServlet(
    name = "ImageFinder",
    urlPatterns = {"/main"}
)
public class ImageFinder extends HttpServlet{
	private WebCrawlerService wcService;
	private ExecutorService executor;
	private static final long serialVersionUID = 1L;
	protected static final Gson GSON = new GsonBuilder().create();


	@Override
	public void init() throws ServletException {
		super.init();
		wcService = new WebCrawlerService();
		executor = Executors.newFixedThreadPool(10);
	}



	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/json");
//		resp.setContentType("application/json");
		String url = req.getParameter("url");
		System.out.println(url);

		// 1. get all the hrefs in the page(subpages)
		List<String> hrefs = wcService.crawlHrefs(url);
		System.out.println("Job starts...");
		String mainURL = removeTrailingSlash(url);

		// 2.run image crawl in each page with multitheading
		ConcurrentHashMap<String, List<String>> results = new ConcurrentHashMap<>(); // create a variable to store all the data
		List<Future<?>> futures = new ArrayList<>();

		// Submit tasks and collect their Future objects
		for (int i = 0; i < 2; i++) {
			final int hrefIndex = i;
			String subPageURL = mainURL + hrefs.get(hrefIndex);
			Future<?> future = executor.submit(() -> crawlImagesFromSubPage(subPageURL, results));
			futures.add(future);
		}

		for (Future<?> future : futures) {
			try {
				future.get(); // Blocks until the task represented by this Future completes
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				resp.getWriter().write("Task execution was interrupted.");
				return;
			} catch (ExecutionException e) {
				resp.getWriter().write("Error during task execution.");
				return;
			}
		}

		System.out.println(results.toString());


		// 1.if return an array
		String[] res = convertToArray(results);
		resp.getWriter().print(GSON.toJson(res));


		// 2/if return a hashmap
//		String jsonResponse = GSON.toJson(results);
//		resp.getWriter().write(jsonResponse);
	}

	private String[] convertToArray(ConcurrentHashMap<String, List<String>> results){
		int totalLen = 0;
		for(String key:results.keySet()){
			totalLen += results.get(key).size();
		}

		String[] res = new String[totalLen];
		int index = 0;
		for(String key:results.keySet()){
			for(String url:results.get(key)){
				res[index] = url;
				index ++;
			}
		}

		return res;
	}


	private void crawlImagesFromSubPage(String subPageURL,ConcurrentHashMap<String, List<String>> results){
		try {
			System.out.println(subPageURL);
			if (results.containsKey(subPageURL)) return;

			List<String> imageURLs = wcService.crawlImage(subPageURL);
			synchronized (results) {
				results.put(subPageURL, imageURLs);
			}

			System.out.println("Job Done for subpage: " + subPageURL);
		} catch (Exception e) {
			System.err.println("Failed to crawl subpage: " + subPageURL);
			e.printStackTrace();
		}
	}

	// remove the tailing slash so we can combine it with subpage urls
	private  String removeTrailingSlash(String str) {
		if (str != null && str.endsWith("/")) {
			return str.substring(0, str.length() - 1);
		}
		return str;
	}




	@Override
	public void destroy() {
		if (executor != null) {
			executor.shutdown();
		}
	}

}
