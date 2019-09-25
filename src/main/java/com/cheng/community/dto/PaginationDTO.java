package com.cheng.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer totalPage;

    private Integer page;

    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalCount, Integer currentPage, Integer size) {
        page = currentPage;
        // 计算总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;


        // 装填pages列表
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页按钮
        if (page == 1) {
            showPrevious = false;
            showFirstPage = false;
        } else {
            showPrevious = true;
            showFirstPage = true;
        }

        // 是否展示下一页按钮
        if (page == totalPage) {
            showEndPage = false;
            showNext = false;
        } else {
            showEndPage = true;
            showNext = true;
        }

        // 是否展示第一页
        showFirstPage = !pages.contains(1);

        // 是否展示最后一页
        showEndPage = !pages.contains(totalPage);
    }
}
