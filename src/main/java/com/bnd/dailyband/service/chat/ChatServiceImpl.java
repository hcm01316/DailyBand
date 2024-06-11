package com.bnd.dailyband.service.chat;

import com.bnd.dailyband.mybatis.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String myprofl(String id) {
        return chatMapper.myprofl(id);
    }
}
