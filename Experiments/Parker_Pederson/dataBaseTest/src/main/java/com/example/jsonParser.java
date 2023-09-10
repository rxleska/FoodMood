package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class jsonParser
{
    public int id;
    public String data;

    // @JsonCreator public jsonParser(@JsonProperty("id") int id,@JsonProperty("data") String data)
    // {
    //     this.id=id;
    //     this.data=data;
    // }

    // @JsonCreator public jsonParser()
    // {
        
    // }

    // public int getId(){return id;}
    // public String getData(){return data;}
}