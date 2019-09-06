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
	private boolean shouldCleanHtml = false;
	private boolean shouldSaveToCsv;
	private String[] lineOfContent;
	private List<String[]> allContent;

	private ContentCrawlerCLI() {
		this.allUrls = new ArrayList<String>();
		this.fileReader = new FileReader();
		this.csvWriter = new CsvWriter();
		this.cleaner = new Cleaner(Whitelist.basic());
		this.allContent = new ArrayList<String[]>();
	}

	/*
	 * Creates a connection with each site, collects and formats each site's links,
	 * removes the header / footer / sidebar from each site's html, uses a cleaner
	 * to remove inline styles, and prints to a csv file.
	 */
	private void run() {

		// Gets all the urls from the input file
		allUrls = fileReader.scanFile(inputFile);
		
		// Loops through each url from the file
		for (String url : allUrls) {

			try {
				// Makes the connection to the url provided
				Document doc = Jsoup.connect(url).get();
				
				// Gets every <a> from the site and adds them to a String
				String formattedLinks = "";
				formatSiteLinksAndAddToString(formattedLinks, doc);

				// Removes styles and shared information if the --cleanHTML command was used in startup
				if (shouldCleanHtml) {
					doc = removeHeaderFooterAndSidebar(doc);
					doc = cleaner.clean(doc);
				}

				// Formats the desired content as a String array and adds that array to a list
				lineOfContent = new String[] { url, doc.select("title").text(), doc.body().toString(), formattedLinks };
				allContent.add(lineOfContent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Writes the entire list of content to csv if the csv format was requested in the startup command
		if (shouldSaveToCsv) {
			csvWriter.writeToCSV(outputFile, allContent);
		}

	}

	/*
	 * Takes each link from the list and puts both the url and the visible text into
	 * the desired format
	 */
	private void formatSiteLinksAndAddToString(String formattedLinks, Document doc) {
		Elements allLinks = doc.select("a");
		for (Element link : allLinks) {
			formattedLinks += "[" + link.text() + "](" + link.attr("href") + ")";
		}
	}

	/*
	 * Searches the html for specific tags, classes, and ids and removes elements
	 * that containt them
	 */
	private Document removeHeaderFooterAndSidebar(Document doc) {
		doc.select("header").remove();
		doc.select("aside").remove();
		doc.select("footer").remove();
		doc.select("#footer").remove();
		Elements elements = doc.body().select("div");

		for (Element element : elements) {

			if (element.className().contains("sidebar") || element.className().contains("footer")
					|| element.className().contains("comment") || element.className().contains("related")
					|| element.id().contains("sidebar") || element.id().contains("footer")
					|| element.id().contains("related") || element.id().contains("comment")) {
				Elements children = element.children();
				for (Element child : children) {
					if (child.className().contains("sidebar") || child.className().contains("footer")
							|| child.className().contains("comment") || child.className().contains("related")
							|| child.id().contains("sidebar") || child.id().contains("footer")
							|| child.id().contains("related") || child.id().contains("comment")) {
						child.remove();
					}
				}
			}
		}
		return doc;
	}

	/*
	 * Takes the file paths and assigns them to the variables used for the input
	 * file and output file
	 */
	private void setFileNames(String inputFileName, String outputFileName) {
		inputFile = new File(inputFileName);
		outputFile = outputFileName;
	}

	/*
	 * Sets the boolean that determines whether or not to save the information to
	 * csv based off of user input when run
	 */
	private void setShouldSaveCsv(String arg) {
		if (arg.equals("csv")) {
			this.shouldSaveToCsv = true;
		} else {
			this.shouldSaveToCsv = false;
		}
	}

	/*
	 * Loops through the arguments and assigns the input file path, output file
	 * path, whether or not to clean the HTML, and whether to save the file to csv
	 */
	private void setArgumentValues(String[] args) {
		String inputName = "";
		String outputName = "";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--inputFileName")) {
				inputName = args[i + 1];
			}
			if (args[i].equals("--outputFileName")) {
				outputName = args[i + 1];
			}
			if (args[i].equals("--cleanHTML")) {
				this.shouldCleanHtml = true;
			}
			if (args[i].equals("--outputFormat")) {
				setShouldSaveCsv(args[i + 1]);
			}
		}

		setFileNames(inputName, outputName);
	}

	/*
	 * Takes the input file and output file as terminal arguments and assigns them
	 * before running the main method (run())
	 */
	public static void main(String[] args) {
		ContentCrawlerCLI conCrawler = new ContentCrawlerCLI();
		conCrawler.setArgumentValues(args);
		conCrawler.run();

	}

}
