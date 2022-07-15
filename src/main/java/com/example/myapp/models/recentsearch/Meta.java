package com.example.myapp.models.recentsearch;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Meta {

@SerializedName("newest_id")
@Expose
public String newestId;
@SerializedName("oldest_id")
@Expose
public String oldestId;
@SerializedName("result_count")
@Expose
public Integer resultCount;
@SerializedName("next_token")
@Expose
public String nextToken;

}