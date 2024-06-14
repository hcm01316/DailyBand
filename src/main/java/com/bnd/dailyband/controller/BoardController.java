package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.board.BoardServiceImpl;
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
public class BoardController {

    private final BoardServiceImpl boardService; // BoardServiceImpl 주입

    @Autowired
    public BoardController(BoardServiceImpl boardService) { // BoardServiceImpl 주입
        this.boardService = boardService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
        if (member != null) {
            model.addAttribute("profilePhoto", member.getProfilePhoto());
            model.addAttribute("username", member.getUsername());
        }
    }

    @RequestMapping(value="/music/list",method= RequestMethod.GET)
    public ModelAndView boardList(
            @RequestParam(value="page",defaultValue="1") int page,
            ModelAndView mv) {
        mv.addObject("current", "gBoard");
        mv.addObject("current_show", "gBoard");
        mv.addObject("current_drop", "gBoardMusic");
        int limit = 10; // 한 화면에 출력할 로우 갯수

        int listcount = boardService.getBoardListCount(); // 총 리스트 수를 받아옴

        // 총 페이지 수
        int maxpage = (listcount + limit - 1) / limit;

        // 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등...)
        int startpage = ((page - 1) / 10) * 10 + 1;

        // 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등...)
        int endpage = startpage + 10 - 1;

        if (endpage > maxpage)
            endpage = maxpage;

        List<Board> boardlist = boardService.getBoardList(page, limit); // 리스트를 받아옴

        mv.setViewName("gboard/mboard_list");
        mv.addObject("page",page);
        mv.addObject("maxpage",maxpage);
        mv.addObject("startpage",startpage);
        mv.addObject("endpage",endpage);
        mv.addObject("listcount",listcount);
        mv.addObject("boardlist",boardlist);
        mv.addObject("limit",limit);
        return mv;
    }

    @GetMapping("/count")
    public int getBoardListCount() {
        return boardService.getBoardListCount();
    }

    @GetMapping("/music/write")
    public String showWriteForm(Model model) {
        ArrayList<Ctgry> Genrelist = boardService.getCtgryList(1);
        model.addAttribute("Genrelist", Genrelist);
        model.addAttribute("current","Board");
        return "gboard/mboard_write";
    }

    @PostMapping("/add")
    public String writeBoard(@ModelAttribute Board board, @AuthenticationPrincipal Member member) {

        String currentUserId = member.getUsername();
        board.setMBR_ID(currentUserId);

        boardService.addBoard(board);
        return "redirect:/gboard/music/list";
    }

    @GetMapping("/music/detail/{id}")
    public String showBoardDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardMusic");

        Board board = boardService.getBoardById(id);
        boardService.increaseReadCount(id);

        model.addAttribute("board", board);
        return "gboard/mboard_view";
    }

    @PostMapping("/music/like/{id}")
    public String likeBoard(@PathVariable("id") int id) {
        boardService.likeBoard(id);
        return "redirect:/gboard/music/detail/" + id;
    }

    @PostMapping("/music/dislike/{id}")
    public String dislikeBoard(@PathVariable("id") int id) {
        boardService.dislikeBoard(id);
        return "redirect:/gboard/music/detail/" + id;
    }

    @PostMapping("/music/delete/{id}")
    public String deleteBoard(@PathVariable("id") int id) {
        boardService.deleteBoard(id);
        return "redirect:/gboard/music/list";
    }
    @GetMapping("/music/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Board board = boardService.getBoardById(id);
        ArrayList<Ctgry> Genrelist = boardService.getCtgryList(1); // 장르 목록을 가져옵니다. 여기서 1은 장르 카테고리 ID일 것입니다.
        model.addAttribute("board", board);
        model.addAttribute("Genrelist", Genrelist); // 속성 이름을 Genrelist로 변경
        return "gboard/mboard_modify";
    }
    @PostMapping("/music/modify/{id}")
    public String editBoard(@PathVariable("id") int id,Board updatedBoard) {
        boardService.updateBoard(id, updatedBoard);
        return "redirect:/gboard/music/detail/" + id;
    }
}

