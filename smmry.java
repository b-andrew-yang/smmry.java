//package smmry;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Smmry{
	public static void main(String[] args){
		File article = null;

		// See if file exists, throw exception if it doesn't.
		try{
			String[] sentences = getSentences(args[0]);
			// args[0] is the first argument after you call java files.

			System.out.println("Summary");

			System.out.println(summarizer(sentences));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String summarizer(String[] article){
		System.out.println("Calls summarizer");

		/*
		*	int[] sentPoints keeps track of total points for each sentence
		*	String title is the first two lines of the article
		*	Sring[] titleArr is an array holding each word in the title
		*/

		int[] sentPoints = new int[article.length];
		String title = article[0] + article[1];
		System.out.println("Title: " + title);
		String[] titleArr = title.split(" ");
		int articleLength = 0;
		int cutoff = 0;

		for(int i = 1; i< article.length; i++){
			String[] wordsPerSent = article[i].split(" ");
			articleLength += article[i].length();

			for(String titleWord: titleArr){
				//System.out.println("titleWord: " + titleWord); 
				for(int j = 0; j< wordsPerSent.length; j++){
					//System.out.println(wordsPerSent[j]);
					if(titleWord.toLowerCase().equals(wordsPerSent[j].replaceAll("//s+", "").toLowerCase())){
						System.out.println("Title match");
						sentPoints[i]++;
					}
				}
			}
		}

		String summary = concatSummary(article, sentPoints, cutoff);

		while((double)summary.length()/(double)articleLength > .4){
			cutoff++;
			summary = concatSummary(article, sentPoints, cutoff);
		}
		summary += '\n';
		String reducedBy = String.format("%.2f", (((double)summary.length()/(double)articleLength)*100));

		summary += ("Article summarized by " + reducedBy + " %");
		//ArrayList<String> artSentence = new ArrayList<String>();


		return summary.trim().replaceAll(" +", " ");
	}

	public static String concatSummary(String[] article, int[] sentPoints, int cutoff){
		
		/*
		*	concatSummary chooses the sentences which fit the cutoff criteria for
		*	being related to the title and concatenates them into a String called
		* 	summary.
		*/

		String summary = article[0];
		for(int i = 0; i< article.length; i++){
			if(sentPoints[i]>= cutoff){
				System.out.println("Added sentence to summary");
				summary += article[i] + ".";
			}
		}

		return summary;
	}

	public static String[] getSentences(String article){
		System.out.println("Entered getSentences");
		String[] error = {"File error"};

		try{
			
			/*
			*	This block calls readFileIntoString and initially saves the 
			*	article as oddArticle, which may contain weird characters.
			*	We then replace the unwanted characters and save it as 
			* 	cleanArticle.
			*/

			String oddArticle = readFileIntoString(article);
			// oddString == String with potentially odd characters (Control)
			String cleanArticle = oddArticle.replaceAll("[^\\x00-\\x7F]", " "); 
			// cleanArticle == String after odd characters taken out.
			
			//System.out.println(cleanArticle);

			/*
			*	This block splits the sentences at the punctuations ".", "?",
			*	and "!". Rudimentary for now.
			*
			*	To Do:
			*		Find a better way to split sentences.	
			*/

			String[] sentences = cleanArticle.split("\\.|\\?|\\!");
			return sentences;

		}catch(IOException e){
			e.printStackTrace();
		}

		return error;
	}

	public static String readFileIntoString(String article) throws IOException {

		/*
		*	readFileIntoString reads the article from a file into a single string.
		*/

		BufferedReader br = new BufferedReader(new FileReader(article));

		StringBuffer sb = new StringBuffer();
		String line = null;

		while((line = br.readLine()) != null){
			sb.append(line).append("\n");
		}

		System.out.println("Finish readFileIntoString");
		return sb.toString();
	}
}