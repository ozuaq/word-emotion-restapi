package com.example.myapp.wordEmotion;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import com.worksap.nlp.sudachi.Dictionary;
import com.worksap.nlp.sudachi.DictionaryFactory;
import com.worksap.nlp.sudachi.Morpheme;
import com.worksap.nlp.sudachi.Tokenizer;
import com.google.gson.Gson;
import com.example.myapp.models.wordEmotion.*;
import com.example.myapp.twitterhandler.RecentSearch;

/** フィード(RSS) の全 item 要素 の description 要素の内容を Sudachi で形態素解析 */

public class TweetAnalyzer {
	private EmotionMap emotionMap;
	private NegaPosiResult negaPosiResult;

	public static void main(String[] args) throws IOException, URISyntaxException{
		TweetAnalyzer tweetAnalyzer = new TweetAnalyzer();
		
	}

	public TweetAnalyzer(){
		emotionMap = new EmotionMap();
		negaPosiResult = new NegaPosiResult();
	}

	public void addPositive(){
        this.negaPosiResult.posiNum++;
    }
    
    public void addNegative(){
        this.negaPosiResult.negaNum++;
    }

	public void addNeutral(){
		this.negaPosiResult.neutralNum++;
	}

	public NegaPosiResult getNegaPosiResult(ArrayList<String> tweets) {
		Dictionary dictionary = null;
		try {
			dictionary = new DictionaryFactory().create();
		} catch (IOException e) {
			System.err.println("辞書が読み込めません: " + e);
			System.exit(-1);
		}
		Tokenizer tokenizer = dictionary.create();
		
		this.negaPosiResult.tweets = new ArrayList<>();
		for(String tweet : tweets){
			ExtractedTweet extractedTweet = getExtractedTweet(tweet, tokenizer);
			if(extractedTweet != null){
				this.negaPosiResult.tweets.add(extractedTweet);
			}
		}

		this.negaPosiResult.targetTweets = tweets.size();
		this.negaPosiResult.hitTweets = this.negaPosiResult.tweets.size();
		
		return this.negaPosiResult;
		
	}

	public String getNegaPosiResultToJson(ArrayList<String> tweets){
		String resultJson = null;
		NegaPosiResult negaPosiResult = getNegaPosiResult(tweets);
		Gson gson = new Gson();
		resultJson = gson.toJson(negaPosiResult);

		return resultJson;
	}

	private ExtractedTweet getExtractedTweet(String tweet, Tokenizer tokenizer){
		boolean matchEmotionWord = false;
		ExtractedTweet extractedTweet = new ExtractedTweet();
		extractedTweet.morphemes = new ArrayList<>();
		for (List<Morpheme> morphemes : tokenizer.tokenizeSentences(Tokenizer.SplitMode.A, tweet)) {
			for(Morpheme morpheme : morphemes){
				String surface = morpheme.surface();
				HashMap<String, String> wordToEmotion = emotionMap.getWordToEmotion();
				if(wordToEmotion.containsKey(surface)){
					if(!matchEmotionWord){
						matchEmotionWord = true;
						extractedTweet.tweet = tweet;
					}
					String emotion = wordToEmotion.get(surface);
					ExtractedMorpheme extractedMorpheme = new ExtractedMorpheme();
					extractedMorpheme.word = surface;
					extractedMorpheme.emotion = emotion;
					extractedTweet.morphemes.add(extractedMorpheme);
					if(emotion.equals("n")){
						addNegative();
					}else if(emotion.equals("p")){
						addPositive();
					}else{
						addNeutral();
					}
				}
			}
		}
		if(!matchEmotionWord){
			extractedTweet = null;
		}

		return extractedTweet;
	}

}