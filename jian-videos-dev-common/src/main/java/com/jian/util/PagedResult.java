package com.jian.util;

import java.util.List;

/**
 * 封装分页后的数据格式
 */
public class PagedResult {

    private int page; //当前页数
    private int total;//总页数
    private long records;//总数据量
    private List<?> rows;//当前页数的数据

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
