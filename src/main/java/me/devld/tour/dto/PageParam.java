package me.devld.tour.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 页码从 1 开始
 */
public class PageParam {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private Integer page;

    private Integer pageSize;

    private String sort;

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public static PageParam from(int page, int pageSize, String sort) {
        PageParam pageParam = new PageParam();
        pageParam.setPage(page);
        pageParam.setPageSize(pageSize);
        pageParam.setSort(sort);
        return pageParam;
    }

    public static PageParam from(int page, int pageSize) {
        return from(page, pageSize, null);
    }

    public Pageable toPageable(Sort sort) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (pageSize == null || pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        --page;
        if (sort == null) {
            return PageRequest.of(page, pageSize);
        }
        return PageRequest.of(page, pageSize, sort);
    }

    public Pageable toPageable() {
        return toPageable(null);
    }
}
