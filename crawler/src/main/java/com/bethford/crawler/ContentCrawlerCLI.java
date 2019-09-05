package com.bethford.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ContentCrawlerCLI {

	private List<String> allUrls;
	private FileReader fileReader;
	private CsvWriter csvWriter;
	private File inputFile = new File("demo-input.txt");
	private File outputFile;
	private String[] lineOfContent;
	private List<String[]> allContent;

	private ContentCrawlerCLI() {
		this.allUrls = new ArrayList<String>();
		this.fileReader = new FileReader();
		this.csvWriter = new CsvWriter();
		this.allContent = new ArrayList<String[]>();
	}

	private void run() {

		allUrls = fileReader.scanFile(inputFile);

		for (String url : allUrls) {

			try {
				Document doc = Jsoup.connect(url).get();
				String formattedLinks = "";
				
				while(doc.selectFirst("a") != null) {
					Element link = doc.selectFirst("a");
					formattedLinks += "[" + link.text() + "](" + link.attr("href") + ")";
					doc.selectFirst("a").remove();
				}
				
				lineOfContent = new String[] {url, doc.select("title").text(), doc.body().text(), formattedLinks};
				allContent.add(lineOfContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void getAndSetFileNames() {

	}

	public static void main(String[] args) {
		ContentCrawlerCLI conCrawler = new ContentCrawlerCLI();
		conCrawler.run();

	}

}
