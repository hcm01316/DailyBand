package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import com.bnd.dailyband.service.qboard.QnABoardServiceImpl;
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
public class QnABoardController {

    private static final Logger log = LoggerFactory.getLogger(QnABoardController.class);
    private final QnABoardServiceImpl qnABoardService; // BoardServiceImpl 주입
    private AdminService adminService;

    @Autowired
    public QnABoardController(QnABoardServiceImpl qnABoardService, AdminService adminService) { // BoardServiceImpl 주입
        this.qnABoardService = qnABoardService;
        this.adminService = adminService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
        int resCnt = adminService.resWaitCnt();
        model.addAttribute("resCnt", resCnt);

        if (member != null) {
            model.addAttribute("profilePhoto", member.getProfilePhoto());
            model.addAttribute("username", member.getUsername());
        }
    }

    @RequestMapping(value="/qna/list",method= RequestMethod.GET)
    public ModelAndView boardList(
            @RequestParam(value="page",defaultValue="1") int page,
            ModelAndView mv) {
        mv.addObject("current", "gBoard");
        mv.addObject("current_show", "gBoard");
        mv.addObject("current_drop", "gBoardQnA");
        int limit = 10; // 한 화면에 출력할 로우 갯수

        int listcount = qnABoardService.getBoardListCount(); // 총 리스트 수를 받아옴
        List<Board> boardlist = qnABoardService.getBoardList(page, limit); // 리스트를 받아옴

        mv.setViewName("gboard/qboard_list");
        mv.addObject("listcount",listcount);
        mv.addObject("boardlist",boardlist);
        mv.addObject("limit",limit);
        return mv;
    }

    @GetMapping("/qna/count")
    public int getBoardListCount() {
        return qnABoardService.getBoardListCount();
    }

    @GetMapping("/qna/write")
    public String showWriteForm(Model model) {
        ArrayList<Ctgry> Genrelist = qnABoardService.getCtgryList(1);
        model.addAttribute("Genrelist", Genrelist);
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardQnA");
        return "gboard/qboard_write";
    }

    @PostMapping("/qna/add")
    public String writeBoard(@ModelAttribute Board board, @AuthenticationPrincipal Member member) {

        String currentUserId = member.getUsername();
        board.setMBR_ID(currentUserId);

        qnABoardService.addBoard(board);
        return "redirect:/gboard/qna/list";
    }

    @GetMapping("/qna/detail/{id}")
    public String showBoardDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardQnA");

        Board board = qnABoardService.getBoardById(id);
        qnABoardService.increaseReadCount(id);



        model.addAttribute("board", board);
        return "gboard/qboard_view";
    }

    @PostMapping("/qna/like/{id}")
    public String likeBoard(@PathVariable("id") int id) {
        qnABoardService.likeBoard(id);
        return "redirect:/gboard/qna/detail/" + id;
    }

    @PostMapping("/qna/dislike/{id}")
    public String dislikeBoard(@PathVariable("id") int id) {
        qnABoardService.dislikeBoard(id);
        return "redirect:/gboard/qna/detail/" + id;
    }

    @PostMapping("/qna/unlike/{id}")
    public String unlikeBoard(@PathVariable("id") int id) {
        qnABoardService.unlikeBoard(id);
        return "redirect:/gboard/qna/detail/" + id;
    }

    @PostMapping("/qna/undislike/{id}")
    public String undislikeBoard(@PathVariable("id") int id) {
        qnABoardService.undislikeBoard(id);
        return "redirect:/gboard/qna/detail/" + id;
    }

    @PostMapping("/qna/delete/{id}")
    public String deleteBoard(@PathVariable("id") int id) {
        qnABoardService.deleteBoard(id);
        return "redirect:/gboard/qna/list";
    }
    @GetMapping("/qna/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Board board = qnABoardService.getBoardById(id);
        ArrayList<Ctgry> Genrelist = qnABoardService.getCtgryList(1);
        model.addAttribute("board", board);
        model.addAttribute("Genrelist", Genrelist); // 속성 이름을 Genrelist로 변경
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardQnA");
        return "gboard/qboard_modify";
    }
    @PostMapping("/qna/modify/{id}")
    public String editBoard(@PathVariable("id") int id,Board updatedBoard) {
        qnABoardService.updateBoard(id, updatedBoard);
        return "redirect:/gboard/qna/detail/" + id;
    }


}

