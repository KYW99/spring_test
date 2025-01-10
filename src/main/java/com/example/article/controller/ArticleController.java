package com.example.article.controller;

import com.example.article.dto.ArticleDTO;
import com.example.article.dto.PageRequestDTO;
import com.example.article.dto.PageResponseDTO;
import com.example.article.entity.Article;
import com.example.article.repository.ArticleRepository;
import com.example.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/article/write")
    public String write() {

        return "/article/write";
    }

    @PostMapping("/article/write")
    public String write(ArticleDTO articleDTO) {

        articleService.insertArticle(articleDTO);

        return "redirect:/article/list";

    }

//    @GetMapping("/article/list")
//    public String list(Model model) {
//        List<ArticleDTO> articles = articleService.getAllArticles();
//        model.addAttribute("articles", articles);
//        return "article/list";
//    }

    @GetMapping(value={"/","/index","/article/list"})
    public String list(PageRequestDTO pageRequestDTO, Model model,
                       @RequestParam(value = "pg", defaultValue = "1") int pg) {

        pageRequestDTO.setPg(pg); // 페이지 번호 설정

        PageResponseDTO<ArticleDTO> pageResponseDTO = articleService.selectAllArticles(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO); // 페이징 정보 전달
        model.addAttribute("articles", pageResponseDTO.getDtoList()); // 게시글 리스트 전달

        return "article/list";
    }


    @GetMapping("/article/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article/modify";
    }

    @PostMapping("/article/modify")
    public String modify(@RequestParam("no") Long no, @RequestParam("title") String title, @RequestParam("content") String content) {
        articleService.modifyArticle(no, title, content);
        return "redirect:/article/list";
    }

    @GetMapping("/article/delete")
    public String delete(@RequestParam("no") int no) {

        articleService.deleteArticle(no);

        return "redirect:/article/list";
    }

    @GetMapping("/article/view/{id}")
    public String view(@PathVariable int id, Model model) {
        Article article = articleService.getArticleById((long) id);
        model.addAttribute("article", article);
        return "article/view";
    }

}
