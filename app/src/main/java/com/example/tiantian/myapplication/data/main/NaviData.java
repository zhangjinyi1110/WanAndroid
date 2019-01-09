package com.example.tiantian.myapplication.data.main;

import com.example.tiantian.myapplication.data.wxarticle.ArticleData;

import java.util.List;

public class NaviData {
    /**
     * articles : [{},{},{},{}]
     * cid : 281
     * name : 公司博客
     */

    private int cid;
    private String name;
    private List<ArticleData> articles;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticleData> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleData> articles) {
        this.articles = articles;
    }
}
