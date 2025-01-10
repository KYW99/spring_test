package com.example.article.service;


import com.example.article.dto.ArticleDTO;
import com.example.article.dto.PageRequestDTO;
import com.example.article.dto.PageResponseDTO;
import com.example.article.entity.Article;
import com.example.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public int insertArticle(ArticleDTO articleDTO){
        Article article = modelMapper.map(articleDTO, Article.class);
        log.info(article);

        Article savedArticle = articleRepository.save(article);

        return savedArticle.getNo();
    }

    public void deleteArticle(int no){
        Article article = articleRepository.findById((long) no).orElse(null);

        assert article != null;

        articleRepository.deleteById((long) no);
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll(); // 모든 Article 엔터티를 데이터베이스에서 가져오기

        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class)) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public void modifyArticle(Long id, String title, String content) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        article.setTitle(title);
        article.setContent(content);
        articleRepository.save(article);  // 수정된 내용 저장
    }


    public PageResponseDTO<ArticleDTO> selectAllArticles(PageRequestDTO pageRequestDTO) {
        // Pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("rdate");

        // 데이터 조회 (페이징 처리)
        Page<Article> result = articleRepository.findAll(pageable);

        // Article을 ArticleDTO로 변환
        List<ArticleDTO> dtoList = result.stream()
                .map(article -> new ArticleDTO(
                        article.getNo(),
                        article.getTitle(),
                        article.getContent(),
                        article.getAuthor(),
                        article.getRdate()))
                .collect(Collectors.toList());

        // PageResponseDTO 반환
        return new PageResponseDTO<>(pageRequestDTO, dtoList, (int) result.getTotalElements());
    }
}
