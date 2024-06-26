package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import com.bnd.dailyband.service.vbaord.VideoBoardServiceImpl;
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
public class VideoBoardController {

    private static final Logger log = LoggerFactory.getLogger(VideoBoardController.class);
    private final VideoBoardServiceImpl videoBoardService; // BoardServiceImpl 주입
    private AdminService adminService;

    @Autowired
    public VideoBoardController(VideoBoardServiceImpl videoBoardService, AdminService adminService) { // BoardServiceImpl 주입
        this.videoBoardService = videoBoardService;
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

    @RequestMapping(value="/video/list",method= RequestMethod.GET)
    public ModelAndView boardList(
            @RequestParam(value="page",defaultValue="1") int page,
            ModelAndView mv) {
        mv.addObject("current", "gBoard");
        mv.addObject("current_show", "gBoard");
        mv.addObject("current_drop", "gBoardVideo");
        int limit = 10; // 한 화면에 출력할 로우 갯수

        int listcount = videoBoardService.getBoardListCount(); // 총 리스트 수를 받아옴
        List<Board> boardlist = videoBoardService.getBoardList(page, limit); // 리스트를 받아옴

        mv.setViewName("gboard/vboard_list");
        mv.addObject("listcount",listcount);
        mv.addObject("boardlist",boardlist);
        mv.addObject("limit",limit);
        return mv;
    }

    @GetMapping("/video/count")
    public int getBoardListCount() {
        return videoBoardService.getBoardListCount();
    }

    @GetMapping("/video/write")
    public String showWriteForm(Model model) {
        ArrayList<Ctgry> Genrelist = videoBoardService.getCtgryList(1);
        model.addAttribute("Genrelist", Genrelist);
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardVideo");
        return "gboard/vboard_write";
    }

    @PostMapping("/video/add")
    public String writeBoard(@ModelAttribute Board board, @AuthenticationPrincipal Member member) {

        String currentUserId = member.getUsername();
        board.setMBR_ID(currentUserId);

        videoBoardService.addBoard(board);
        return "redirect:/gboard/video/list";
    }

    @GetMapping("/video/detail/{id}")
    public String showBoardDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardVideo");

        Board board = videoBoardService.getBoardById(id);
        videoBoardService.increaseReadCount(id);



        model.addAttribute("board", board);
        return "gboard/vboard_view";
    }

    @PostMapping("/video/like/{id}")
    public String likeBoard(@PathVariable("id") int id) {
        videoBoardService.likeBoard(id);
        return "redirect:/gboard/video/detail/" + id;
    }

    @PostMapping("/video/dislike/{id}")
    public String dislikeBoard(@PathVariable("id") int id) {
        videoBoardService.dislikeBoard(id);
        return "redirect:/gboard/video/detail/" + id;
    }

    @PostMapping("/video/unlike/{id}")
    public String unlikeBoard(@PathVariable("id") int id) {
        videoBoardService.unlikeBoard(id);
        return "redirect:/gboard/video/detail/" + id;
    }

    @PostMapping("/video/undislike/{id}")
    public String undislikeBoard(@PathVariable("id") int id) {
        videoBoardService.undislikeBoard(id);
        return "redirect:/gboard/video/detail/" + id;
    }

    @PostMapping("/video/delete/{id}")
    public String deleteBoard(@PathVariable("id") int id) {
        videoBoardService.deleteBoard(id);
        return "redirect:/gboard/video/list";
    }
    @GetMapping("/video/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Board board = videoBoardService.getBoardById(id);
        ArrayList<Ctgry> Genrelist = videoBoardService.getCtgryList(1);
        model.addAttribute("board", board);
        model.addAttribute("Genrelist", Genrelist); // 속성 이름을 Genrelist로 변경
        model.addAttribute("current", "gBoard");
        model.addAttribute("current_show", "gBoard");
        model.addAttribute("current_drop", "gBoardVideo");
        return "gboard/vboard_modify";
    }
    @PostMapping("/video/modify/{id}")
    public String editBoard(@PathVariable("id") int id,Board updatedBoard) {
        videoBoardService.updateBoard(id, updatedBoard);
        return "redirect:/gboard/video/detail/" + id;
    }
}

