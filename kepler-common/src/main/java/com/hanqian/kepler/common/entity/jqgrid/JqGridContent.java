package com.hanqian.kepler.common.entity.jqgrid;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class JqGridContent<T> {

    /**
     * 是否使用了分页
     */
    private boolean isPage;

    /**
     * jpa分页对象（如果没有使用分页，则page为null）
     */
    private Page<T> page;

    /**
     * 具体数据内容
     */
    private List<T> list = new ArrayList<>();

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public List<T> getList() {
        return page!=null ? page.getContent() : list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isPage() {
        return isPage;
    }

    public void setPage(boolean page) {
        isPage = page;
    }

    public JqGridContent(boolean isPage, Page<T> page, List<T> list) {
        this.isPage = isPage;
        this.page = page;
        this.list = list;
    }
}
