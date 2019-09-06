package com.bethford.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class ContentCrawlerCLI {

	private List<String> allUrls;
	private FileReader fileReader;
	private CsvWriter csvWriter;
	private Cleaner cleaner;
	private File inputFile = new File("demo-input.txt");
	private String outputFile = "output.csv";
	private String[] lineOfContent;
	private List<String[]> allContent;

	private ContentCrawlerCLI() {
		this.allUrls = new ArrayList<String>();
		this.fileReader = new FileReader();
		this.csvWriter = new CsvWriter();
		this.cleaner = new Cleaner(Whitelist.basic());
		this.allContent = new ArrayList<String[]>();
	}

	private void run() {

		allUrls = fileReader.scanFile(inputFile);

		for (String url : allUrls) {

			try {
				Document doc = Jsoup.connect(url).get();
				String formattedLinks = "";

				formatSiteLinksAndAddToString(formattedLinks, doc);

				doc.select("header").remove();
				doc.select("aside").remove();
				doc.select("footer").remove();
				doc.select("#footer").remove();
				doc.select(".sidebar").remove();


				doc = cleaner.clean(doc);

				lineOfContent = new String[] { url, doc.select("title").text(), doc.body().toString(), formattedLinks };
				allContent.add(lineOfContent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		csvWriter.writeToCSV(outputFile, allContent);

	}

	private void formatSiteLinksAndAddToString(String formattedLinks, Document doc) {
		Elements allLinks = doc.select("a");
		for (Element link : allLinks) {
			formattedLinks += "[" + link.text() + "](" + link.attr("href") + ")";
		}
	}

	private void setFileNames(String inputFileName, String outputFileName) {
		inputFile = new File(inputFileName);
		outputFile = outputFileName;
	}

	public static void main(String[] args) {
		ContentCrawlerCLI conCrawler = new ContentCrawlerCLI();
//		conCrawler.setFileNames(args[0], args[1]);
		conCrawler.run();

	}

}
