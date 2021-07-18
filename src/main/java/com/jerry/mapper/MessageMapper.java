package com.jerry.mapper;

import com.jerry.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface MessageMapper {
    public int getId();
    public List<Message> getUnreadMessage(int to_id);
    public int insertMessage(Message message);
    public int insertUnreadMessage(int message_id,int user_id);
    public int deleteUnreadMessage(int message_id,int user_id);

    List<Integer> getGroupUserId(int task_id);

}
