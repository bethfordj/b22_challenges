package com.bethford.crawler;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CsvWriter {

	public void writeToCSV(String outputFile, List<String[]> content) {

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile));  

				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader("URL", "Title", "Body", "Links found"));) {
			for (String[] array : content) {
				csvPrinter.printRecord(array[0], array[1], array[2], array[3]);
			}

			csvPrinter.flush();
		}
	
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

