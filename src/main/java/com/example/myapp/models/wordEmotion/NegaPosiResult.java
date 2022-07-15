package com.example.myapp.models.wordEmotion;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NegaPosiResult {
    @JsonProperty("tweets")
    public List<ExtractedTweet> tweets;
    @JsonProperty("nega_num")
    public int negaNum;
    @JsonProperty("posi_num")
    public int posiNum;
    @JsonProperty("neutral_num")
    public int neutralNum;
    @JsonProperty("target_tweets")
    public int targetTweets;
    @JsonProperty("hit_tweets")
    public int hitTweets;
}
