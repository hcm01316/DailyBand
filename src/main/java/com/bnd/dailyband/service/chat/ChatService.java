package com.bnd.dailyband.service.chat;

import com.bnd.dailyband.domain.ChatRoom;
import com.bnd.dailyband.domain.Chatlist;

import java.util.List;

public interface ChatService {

    public void chatinsert(String id,String cn,int num);

    public List<Chatlist> chatlist(int id);

    public String myprofl(String id);

    public int chatck(String id);

    public List<ChatRoom> ChatRoomList(String id);

    public void ChatJoin(String id,int num);

    public void ChatExit(String id, int num);

    public void ChatRoomDelete(int num);

    public int IsChat(String id, String myid);

    public String MyName(String id);

    public void ChatCreate(String name,String myname);
}
