# content-crawler-challenge
The content crawler takes an text file of urls, connects to each, and saves the url, title, content, and links to a csv file.

## Installation
To run the file with HTML cleaning and save the information to a csv, download the jar file and open it with the following command:

### For Windows

```java -jar content-crawler-1.0.0.jar –-inputFileName C:/files/urls.txt –-outputFileName  C:/files/output.csv --cleanHTML --outputFormat csv```

### For Unix
```java -jar content-crawler-1.0.0.jar –-inputFileName /opt/files/urls.txt –-outputFileName /opt/files/output.csv --cleanHTML --outputFormat csv```

## Dependencies
```Jsoup 1.11.3```
```Apache Commons: commons-csv 1.6```
