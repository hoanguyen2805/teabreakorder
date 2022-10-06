package com.nta.teabreakorder.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Pageable {

    private int page = 0;
    private int pageSize = 10;
    private long total = 0;
    private Map<String, String> search;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String searchData;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sortData;

    private Map<String, String> sort;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String fields;
    private List<String> fieldList;

    public Pageable() {
    }

    public Pageable(int page, int pageSize, String searchData, String sortData, String fields) {
        this.page = page;
        this.pageSize = pageSize;
        this.searchData = searchData;
        this.sortData = sortData;
        this.fields = fields;
    }

    public Pageable ofDefault() {
        return new Pageable();
    }


    public static Pageable ofValue(Integer page, Integer pageSize, String searchData, String sortData, String fields) {
        return new Pageable(page != null ? page : 0, pageSize != null ? pageSize : 10, searchData, sortData, fields);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Map<String, String> getSearch() {
        return search;
    }

    public void setSearch(Map<String, String> search) {
        this.search = search;
    }

    public String getSearchData() {
        return searchData;
    }


    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public String getSortData() {
        return sortData;
    }



    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
