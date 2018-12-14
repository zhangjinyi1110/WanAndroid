package com.example.tiantian.myapplication.data.wxarticle;

import java.util.List;

public class Article {
    /**
     * curPage : 3
     * datas : []
     * offset : 40
     * over : false
     * pageCount : 32
     * size : 20
     * total : 640
     */

    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    private List<ArticleData> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ArticleData> getDatas() {
        return datas;
    }

    public void setDatas(List<ArticleData> datas) {
        this.datas = datas;
    }
}
