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

/* This class takes the content from the websites listed in a text file, removes the site's shared information and inline styling, and
 * prints the information to a csv file in the order URL, Title, Body, and Links found.
 */

public class ContentCrawlerCLI {

	private List<String> allUrls;
	private FileReader fileReader;
	private CsvWriter csvWriter;
	private Cleaner cleaner;
	private File inputFile;
	private String outputFile;
	private String[] lineOfContent;
	private List<String[]> allContent;

	private ContentCrawlerCLI() {
		this.allUrls = new ArrayList<String>();
		this.fileReader = new FileReader();
		this.csvWriter = new CsvWriter();
		this.cleaner = new Cleaner(Whitelist.basic());
		this.allContent = new ArrayList<String[]>();
	}

	/* Creates a connection with each site, collects and formats each site's links, removes the header / footer / sidebar from each site's html,
	 * uses a cleaner to remove inline styles, and prints to a csv file.
	 */
	private void run() {

		allUrls = fileReader.scanFile(inputFile);

		for (String url : allUrls) {

			try {
				Document doc = Jsoup.connect(url).get();
				String formattedLinks = "";

				formatSiteLinksAndAddToString(formattedLinks, doc);

				doc = removeHeaderFooterAndSidebar(doc);

				doc = cleaner.clean(doc);

				lineOfContent = new String[] { url, doc.select("title").text(), doc.body().toString(), formattedLinks };
				allContent.add(lineOfContent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		csvWriter.writeToCSV(outputFile, allContent);

	}

	/* Takes each link from the list and puts both the url and the visible text into the desired format */
	private void formatSiteLinksAndAddToString(String formattedLinks, Document doc) {
		Elements allLinks = doc.select("a");
		for (Element link : allLinks) {
			formattedLinks += "[" + link.text() + "](" + link.attr("href") + ")";
		}
	}
	
	/* Searches the html for specific tags, classes, and ids and removes elements that containt them */
	private Document removeHeaderFooterAndSidebar(Document doc) {
		doc.select("header").remove();
		doc.select("aside").remove();
		doc.select("footer").remove();
		doc.select("#footer").remove();
		Elements elements = doc.body().select("div");

		for (Element element : elements) {

			if (element.className().contains("sidebar") || element.className().contains("footer") || element.className().contains("comment")
					|| element.className().contains("related") || element.id().contains("sidebar")
					|| element.id().contains("footer") || element.id().contains("related") || element.id().contains("comment")) {
				Elements children = element.children();
				for (Element child : children) {
					if (child.className().contains("sidebar") || child.className().contains("footer") || child.className().contains("comment")
							|| child.className().contains("related") || child.id().contains("sidebar")
							|| child.id().contains("footer") || child.id().contains("related") || child.id().contains("comment")) {
						child.remove();
					}
				}
			}
		}
		return doc;
	}

	/* Takes the file paths and assigns them to the variables used for the input file and output file */
	private void setFileNames(String inputFileName, String outputFileName) {
		inputFile = new File(inputFileName);
		outputFile = outputFileName;
	}
	

			
	/* Takes the input file and output file as terminal arguments and assigns them before running the main method (run()) */
	public static void main(String[] args) {
		ContentCrawlerCLI conCrawler = new ContentCrawlerCLI();
		conCrawler.setFileNames(args[0], args[1]);
		conCrawler.run();

	}

}
