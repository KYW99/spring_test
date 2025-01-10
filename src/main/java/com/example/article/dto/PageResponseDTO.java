package com.example.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PageResponseDTO<T> {
    private int pg; // 현재 페이지
    private int size; // 페이지 크기
    private int total; // 전체 게시물 수
    private List<T> dtoList; // 게시물 리스트
    private boolean prev; // 이전 버튼
    private boolean next; // 다음 버튼
    private int start; // 페이지 번호 시작
    private int end; // 페이지 번호 끝

    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<T> dtoList, int total) {
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.dtoList = dtoList;
        this.total = total;

        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) total / size);

        // 페이지 네비게이션 계산
        int pageCount = 10; // 표시할 페이지 번호의 개수
        int currentBlock = (pg - 1) / pageCount;

        start = currentBlock * pageCount + 1;
        end = Math.min(start + pageCount - 1, totalPages);

        prev = pg > 1; // 현재 페이지가 1보다 크면 이전 페이지가 있음
        next = pg < totalPages; // 현재 페이지가 총 페이지 수보다 작으면 다음 페이지가 있음
    }

    // Getter & Setter
}