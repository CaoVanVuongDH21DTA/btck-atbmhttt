package com.java.common;

public class PageInfoOrder {
    private int currentPage;
    private int totalPages;

    public PageInfoOrder(int currentPage, int totalPages) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }
}
