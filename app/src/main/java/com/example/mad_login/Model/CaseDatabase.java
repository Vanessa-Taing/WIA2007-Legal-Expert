package com.example.mad_login.Model;

public class CaseDatabase {
    String casetype, keywords , desc , summary, url;

    CaseDatabase(){

    }
    public CaseDatabase(String casetype, String keywords, String desc, String summary, String url) {
        this.casetype = casetype;
        this.keywords = keywords;
        this.desc = desc;
        this.summary = summary;
        this.url = url;
    }


    public String getCasetype() {
        return casetype;
    }

    public void setCasetype(String casetype) {
        this.casetype = casetype;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
