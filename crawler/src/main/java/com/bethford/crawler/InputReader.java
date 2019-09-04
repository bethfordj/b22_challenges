package com.bethford.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputReader {

	public String getFileNames() {
		String line = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

			while (true) {
				if ((line = reader.readLine()) != null) {
					System.out.println("echo>> " + line);
				} else {
					// input finishes
					break;
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return line;
	}

}
