package com.example.myapp.models.wordEmotion;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


public class NegaPosiRequest implements Serializable{
    @JsonProperty("word")
    public String word;
    @JsonProperty("max_results")
    public String maxResults;
    @JsonProperty("max_queries")
    public int maxQueries;
    @JsonProperty("start_time")
    public String startTime;
    @JsonProperty("end_time")
    public String endTime;
}
