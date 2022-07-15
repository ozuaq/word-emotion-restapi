package com.example.myapp.models.recentsearch;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResultSearch {

@SerializedName("data")
@Expose
public List<Datum> data = null;
@SerializedName("meta")
@Expose
public Meta meta;

}