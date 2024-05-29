package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Member;
import java.util.List;

public interface AdminService {


  int getSearchListCount(int index, String searchWord);

  List<Member> getSearchList(int index, String searchWord, int page, int limit);
}
