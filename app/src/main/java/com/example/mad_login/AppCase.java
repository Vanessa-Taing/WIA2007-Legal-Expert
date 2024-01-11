package com.example.mad_login;
public class AppCase {
    private String keywords;
    private String summary;
    private String desc;
    private String casetype;
    private String url;

    // Required default constructor for Firebase
    public AppCase() {
        // Default constructor required for calls to DataSnapshot.getValue(Case.class)
    }

    public AppCase(String keywords,String summary, String desc, String casetype, String url) {
        this.keywords = keywords;
        this.summary = summary;
        this.desc = desc;
        this.casetype = casetype;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getCasetype() {
        return casetype;
    }

    public void setCasetype(String casetype) {
        this.casetype = casetype;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {this.summary = summary;}
}
