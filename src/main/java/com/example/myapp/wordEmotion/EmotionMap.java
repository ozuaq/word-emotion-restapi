package com.example.myapp.wordEmotion;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class EmotionMap {
    private HashMap<String, String> wordToEmotion;

    public EmotionMap() {
        wordToEmotion = new HashMap<>();
        createMap();
    }

    private void createMap() {
        Path path = Paths.get("pn.csv.m3.120408.trim");
        
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
            String line=null;
            while((line = reader.readLine()) != null){
                String[] words = line.split("\t");
                wordToEmotion.put(words[0], words[1]);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
        /*
        wordToEmotion.forEach((k, v) -> {
            System.out.println(k+" "+v);
        }); 
         */
        
    }

    public HashMap<String, String> getWordToEmotion(){
        return this.wordToEmotion;
    }

    public void readFile(String filePath) {
        Path path = Paths.get(filePath);
        
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
            String line=null;
            int count = 0;
            while((line = reader.readLine()) != null){
                if(count < 10){
                    System.out.println(line);
                }
                count++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void download(URI uri) throws IOException {
        String path = uri.getPath();
        String name = path.substring(path.lastIndexOf("/") + 1);
        long size = 0;
    
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet(uri))) {

            InputStream is = response.getEntity().getContent();
    
            size = Files.copy(is, Paths.get(name));
        }
    
        System.out.println(name + " - " + size + " bytes");
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        EmotionMap emotionMap = new EmotionMap();
        //emotionMap.readFile("pn.csv.m3.120408.trim");
        //emotionMap.download(new URI("https://www.cl.ecei.tohoku.ac.jp/resources/sent_lex/pn.csv.m3.120408.trim"));
        /*
        Map<String, Double> wordToEmotion = emotionMap.getWordToEmotionalValue();
        Map<String, Double> wordreadingToEmotion = emotionMap.getWordreadingToEmotionalValue();
        //System.out.println("wordと感情値は");
        wordToEmotion.forEach((k, v) -> {
            //System.out.println(k + ":" + v);
        });
        //System.out.println("wordの読みと感情値は");
        wordreadingToEmotion.forEach((k, v) -> {
            //System.out.println(k + ":" + v);
        });

        //emotionMap.download(new URI("http://www.lr.pi.titech.ac.jp/~takamura/pubs/pn_ja.dic"));
        emotionMap.readFile("pn_ja.dic");
         */
        

       
    }
}
