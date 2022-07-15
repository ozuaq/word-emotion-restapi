package com.example.myapp.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.myapp.models.wordEmotion.NegaPosiRequest;
import com.example.myapp.models.wordEmotion.NegaPosiResult;
import com.example.myapp.twitterhandler.RecentSearch;
import com.example.myapp.wordEmotion.TweetAnalyzer;

@RestController
public class RestAPIController {
    @PostMapping("/emotion")
    public NegaPosiResult wordEmotionGet(@RequestBody NegaPosiRequest negaPosiRequest) throws IOException, URISyntaxException {
        ArrayList<String> tweets = RecentSearch.getTweets(negaPosiRequest);
        return new TweetAnalyzer().getNegaPosiResult(tweets);
    }
}
