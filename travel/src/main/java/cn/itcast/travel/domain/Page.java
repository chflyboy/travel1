package cn.itcast.travel.domain;

import java.util.List;

public class Page<T> {
    private int pageNumber;//页码
    private int allPage;//总页码
    private int countForPage;//每页条目
    private int amount;//符合条件的总条目
    private List<Route> routes;//每页展示的内容

    public Page() {
    }

    public Page(int pageNumber, int allPage, int countForPage, int amount, List<Route> routes) {
        this.pageNumber = pageNumber;
        this.allPage = allPage;
        this.countForPage = countForPage;
        this.amount = amount;
        this.routes = routes;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public int getCountForPage() {
        return countForPage;
    }

    public void setCountForPage(int countForPage) {
        this.countForPage = countForPage;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
