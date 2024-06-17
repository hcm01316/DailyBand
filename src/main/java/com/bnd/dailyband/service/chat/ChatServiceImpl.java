package com.bnd.dailyband.service.chat;

import com.bnd.dailyband.domain.ChatRoom;
import com.bnd.dailyband.domain.Chatlist;
import com.bnd.dailyband.mybatis.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;

    @Autowired
    public ChatServiceImpl(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    @Override
    public void chatinsert(String id, String cn, int num) {
        chatMapper.chatinsert(id,cn,num);
    }

    @Override
    public List<Chatlist> chatlist(int num) {
        return chatMapper.chatlist(num);
    }

    @Override
    public String myprofl(String id) {
        return chatMapper.myprofl(id);
    }

    @Override
    public int chatck(String id) {
        return chatMapper.chatck(id);
    }

    @Override
    public List<ChatRoom> ChatRoomList(String id) {
       return chatMapper.ChatRoomList(id);
    }

    @Override
    public void ChatJoin(String id, int num) {
        chatMapper.ChatJoin(id,num);
    }

    @Override
    public void ChatExit(String id, int num) {
        chatMapper.ChatExit(id,num);
    }

    @Override
    public void ChatRoomDelete(int num) {
        chatMapper.ChatRoomDelete(num);
    }

    @Override
    public int IsChat(String id, String myid) {
        return chatMapper.IsChat(id,myid);
    }

    @Override
    public String MyName(String id) {
        return chatMapper.MyName(id);
    }

    @Override
    public void ChatCreate(String name, String myname) {
        chatMapper.ChatCreate(name,myname);
    }


}
