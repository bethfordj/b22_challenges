package com.bethford.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
	
	/* This class takes a file as input and adds each line of the file into an array as a string. */
	
	public List<String> scanFile(File file) {

		List<String> allUrls = new ArrayList<String>();
		String line = "";

		try (Scanner fileScanner = new Scanner(file)) {
			while (fileScanner.hasNextLine()) {

				line = fileScanner.nextLine();
				allUrls.add(line);
			}

		} catch (FileNotFoundException e) {
			System.out.println("File " + file.getAbsolutePath() + "not found!");
			e.printStackTrace();
		} catch (NullPointerException e1) {
			System.out.println("Null Pointer Exception");
			e1.printStackTrace();
		}

		return allUrls;
	}

}
