package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.fboard.FreeBoardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/gboard")
public class FreeBoardController {

    private static final Logger log = LoggerFactory.getLogger(FreeBoardController.class);
    private final FreeBoardServiceImpl freeBoardService; // BoardServiceImpl 주입

    @Autowired
    public FreeBoardController(FreeBoardServiceImpl freeBoardService) { // BoardServiceImpl 주입
        this.freeBoardService = freeBoardService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
        if (member != null) {
            model.addAttribute("profilePhoto", member.getProfilePhoto());
            model.addAttribute("username", member.getUsername());
        }
    }

    @RequestMapping(value="/free/list",method= RequestMethod.GET)
    public ModelAndView boardList(
            @RequestParam(value="page",defaultValue="1") int page,
            ModelAndView mv) {
        mv.addObject("current", "gBoard");
        mv.addObject("current_show", "gBoard");
        mv.addObject("current_drop", "gBoardFree");
        int limit = 10; // 한 화면에 출력할 로우 갯수

        int listcount = freeBoardService.getBoardListCount(); // 총 리스트 수를 받아옴

        // 총 페이지 수
        int maxpage = (listcount + limit - 1) / limit;

        // 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등...)
        int startpage = ((page - 1) / 10) * 10 + 1;

        // 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등...)
        int endpage = startpage + 10 - 1;

        if (endpage > maxpage)
            endpage = maxpage;

        List<Board> boardlist = freeBoardService.getBoardList(page, limit); // 리스트를 받아옴

        mv.setViewName("gboard/fboard_list");
        mv.addObject("page",page);
        mv.addObject("maxpage",maxpage);
        mv.addObject("startpage",startpage);
        mv.addObject("endpage",endpage);
        mv.addObject("listcount",listcount);
        mv.addObject("boardlist",boardlist);
        mv.addObject("limit",limit);
        return mv;
    }

    @GetMapping("/free/count")
    public int getBoardListCount() {
        return freeBoardService.getBoardListCount();
    }

    @GetMapping("/free/write")
    public String showWriteForm(Model model) {
        ArrayList<Ctgry> Genrelist = freeBoardService.getCtgryList(1);
        model.addAttribute("Genrelist", Genrelist);
        model.addAttribute("current","Board");
        return "gboard/fboard_write";
    }

    @PostMapping("/free/add")
    public String writeBoard(@ModelAttribute Board board, @AuthenticationPrincipal Member member) {

        String currentUserId = member.getUsername();
        board.setMBR_ID(currentUserId);

        freeBoardService.addBoard(board);
        return "redirect:/gboard/free/list";
    }

    @GetMapping("/free/detail/{id}")
    public String showBoardDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardFree");

        Board board = freeBoardService.getBoardById(id);
        freeBoardService.increaseReadCount(id);
        log.info(board.getBBS_CN());


        model.addAttribute("board", board);
        return "gboard/fboard_view";
    }

    @PostMapping("/free/like/{id}")
    public String likeBoard(@PathVariable("id") int id) {
        freeBoardService.likeBoard(id);
        return "redirect:/gboard/free/detail/" + id;
    }

    @PostMapping("/free/dislike/{id}")
    public String dislikeBoard(@PathVariable("id") int id) {
        freeBoardService.dislikeBoard(id);
        return "redirect:/gboard/free/detail/" + id;
    }

    @PostMapping("/free/delete/{id}")
    public String deleteBoard(@PathVariable("id") int id) {
        freeBoardService.deleteBoard(id);
        return "redirect:/gboard/free/list";
    }
    @GetMapping("/free/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Board board = freeBoardService.getBoardById(id);
        ArrayList<Ctgry> Genrelist = freeBoardService.getCtgryList(1); // 장르 목록을 가져옵니다. 여기서 1은 장르 카테고리 ID일 것입니다.
        model.addAttribute("board", board);
        model.addAttribute("Genrelist", Genrelist); // 속성 이름을 Genrelist로 변경
        return "gboard/fboard_modify";
    }
    @PostMapping("/free/modify/{id}")
    public String editBoard(@PathVariable("id") int id,Board updatedBoard) {
        freeBoardService.updateBoard(id, updatedBoard);
        return "redirect:/gboard/free/detail/" + id;
    }


}

