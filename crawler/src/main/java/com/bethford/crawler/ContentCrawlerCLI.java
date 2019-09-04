package com.bethford.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ContentCrawlerCLI {

	private List<String> allUrls;
	private FileReader fileReader;
	private File inputFile = new File("demo-input.txt");
	private File outputFile;

	private ContentCrawlerCLI() {
		this.allUrls = new ArrayList<String>();
		this.fileReader = new FileReader();
	}

	private void run() {

		allUrls = fileReader.scanFile(inputFile);

		for (String url : allUrls) {

			try {
				Document doc = Jsoup.connect(url).get();
				System.out.print(doc);
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
