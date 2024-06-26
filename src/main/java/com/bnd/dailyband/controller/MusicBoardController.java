package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.mboard.MusicBoardServiceImpl;
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
public class MusicBoardController {

    private static final Logger log = LoggerFactory.getLogger(MusicBoardController.class);
    private final MusicBoardServiceImpl boardService; // BoardServiceImpl 주입

    @Autowired
    public MusicBoardController(MusicBoardServiceImpl boardService) { // BoardServiceImpl 주입
        this.boardService = boardService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
        if (member != null) {
            model.addAttribute("profilePhoto", member.getProfilePhoto());
            model.addAttribute("username", member.getUsername());
        }
    }

    @RequestMapping(value="/music/list", method=RequestMethod.GET)
    public ModelAndView boardList(
            @RequestParam(value="page", defaultValue="1") int page,
            ModelAndView mv) {
        mv.addObject("current", "gBoard");
        mv.addObject("current_show", "gBoard");
        mv.addObject("current_drop", "gBoardMusic");
        int limit = 10; // 한 화면에 출력할 로우 갯수

        List<Board> boardlist = boardService.getBoardList(page, limit); // 리스트를 받아옴
        int listcount = boardService.getBoardListCount(); // 총 리스트 수를 받아옴

        mv.setViewName("gboard/mboard_list");
        mv.addObject("boardlist", boardlist);
        mv.addObject("listcount", listcount);
        mv.addObject("limit", limit);
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
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardMusic");
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
        log.info(board.getBBS_CN());


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

    @PostMapping("/music/unlike/{id}")
    public String unlikeBoard(@PathVariable("id") int id) {
        boardService.unlikeBoard(id);
        return "redirect:/gboard/music/detail/" + id;
    }

    @PostMapping("/music/undislike/{id}")
    public String undislikeBoard(@PathVariable("id") int id) {
        boardService.undislikeBoard(id);
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
        ArrayList<Ctgry> Genrelist = boardService.getCtgryList(1);
        model.addAttribute("board", board);
        model.addAttribute("Genrelist", Genrelist); // 속성 이름을 Genrelist로 변경
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardMusic");
        return "gboard/mboard_modify";
    }
    @PostMapping("/music/modify/{id}")
    public String editBoard(@PathVariable("id") int id,Board updatedBoard) {
        boardService.updateBoard(id, updatedBoard);
        return "redirect:/gboard/music/detail/" + id;
    }


}

