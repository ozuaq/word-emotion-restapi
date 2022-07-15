package com.example.myapp.twitterhandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.myapp.models.recentsearch.*;
import com.example.myapp.models.wordEmotion.NegaPosiRequest;
import com.google.gson.Gson;

public class RecentSearch {
  private static ArrayList<String> tweets = new ArrayList<>();
  private static String nextToken ;
  private static int queryCount;

  public static void main(String args[]) throws IOException, URISyntaxException {
    ArrayList<NameValuePair> queryParameters = new ArrayList<>();
    queryParameters.add(new BasicNameValuePair("of", "t-k"));
    queryParameters.add(new BasicNameValuePair("lim", "5"));
    queryParameters.add(new BasicNameValuePair("word", "異世界"));
    // queryParameters.add(new BasicNameValuePair("out", "json"));

    String baseUrl = "https://api.syosetu.com/novelapi/api/";

    RecentSearch recentSearch = new RecentSearch();
    System.out.println(recentSearch.getResponse(baseUrl, queryParameters));

    String bearerToken = System.getenv("BEARER_TOKEN");
  }

  public static void addTweet(String tweet){
    String formattedTweet = tweet.replace("\n", "");
    Pattern pattern = Pattern.compile("^RT");
    Matcher matcher = pattern.matcher(formattedTweet);
    if(!matcher.find()) {
      tweets.add(formattedTweet);
    }
  }
  public static ArrayList<String> getTweets(NegaPosiRequest negaPosiRequest) throws IOException, URISyntaxException {
    tweets.clear();
    nextToken = null;
    queryCount = 1;
    
    System.out.println("recent_search start!");
    String searchString = negaPosiRequest.word;
    String maxResults = negaPosiRequest.maxResults;
    int maxQueries = negaPosiRequest.maxQueries;
    String startTime = negaPosiRequest.startTime;
    String endTime = negaPosiRequest.endTime;
    String bearerToken = System.getenv("BEARER_TOKEN");

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
    ArrayList<NameValuePair> queryParameters;
    queryParameters = new ArrayList<>();
    queryParameters.add(new BasicNameValuePair("query", searchString));
    queryParameters.add(new BasicNameValuePair("max_results", maxResults));
    queryParameters.add(new BasicNameValuePair("start_time", startTime));
    queryParameters.add(new BasicNameValuePair("end_time", endTime));
    uriBuilder.addParameters(queryParameters);

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpGet.setHeader("Content-Type", "application/json");

    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      String responseJson = EntityUtils.toString(entity, "UTF-8");

      Gson gson = new Gson();
      ResultSearch resultSearch = gson.fromJson(responseJson, ResultSearch.class);
      for (Datum datum : resultSearch.data) {
        addTweet(datum.text);
      }

      if(maxQueries > 1){
        nextToken = resultSearch.meta.nextToken;
        System.out.println("next_token: " + nextToken);
        searchTweetsByNextToken(searchString, bearerToken, nextToken, maxQueries);
      }

    }
    System.out.println("recent_search finish! "+"queryCount: "+queryCount);

    return tweets;
  }

  private static void searchTweetsByNextToken(String searchString, String bearerToken, String nextToken, int maxQueries)
      throws IOException, URISyntaxException {
    queryCount++;

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
    ArrayList<NameValuePair> queryParameters;
    queryParameters = new ArrayList<>();
    queryParameters.add(new BasicNameValuePair("query", searchString));
    queryParameters.add(new BasicNameValuePair("next_token", nextToken));
    uriBuilder.addParameters(queryParameters);

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpGet.setHeader("Content-Type", "application/json");

    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      String responseJson = EntityUtils.toString(entity, "UTF-8");
      Gson gson = new Gson();
      ResultSearch resultSearch = gson.fromJson(responseJson, ResultSearch.class);
      for (Datum datum : resultSearch.data) {
        addTweet(datum.text);
      }
      if (queryCount < maxQueries) {
        nextToken = resultSearch.meta.nextToken;
        System.out.println("next_token: " + nextToken);
        searchTweetsByNextToken(searchString, bearerToken, nextToken, maxQueries);
      }

    }
  }

  public String getResponse(String baseUrl, ArrayList<NameValuePair> queryParameters)
      throws IOException, URISyntaxException {
    String resStr = null;
    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder(baseUrl);
    uriBuilder.addParameters(queryParameters);

    HttpGet httpGet = new HttpGet(uriBuilder.build());

    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();

    if (null != entity) {
      resStr = EntityUtils.toString(entity, "UTF-8");

    }

    return resStr;
  }

}
