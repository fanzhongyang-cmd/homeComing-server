package com.jerry.mapper;


import com.jerry.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
//@Repository
public interface TaskMapper {
    public List<Task> getAllTask();
    public List<Task> getAllRunningTask();
    public boolean insertTask(Task task);

    public int deleteTask(int task_id);

    public int completeTask(int task_id, long complete_time);

    public int cancelTask(int task_id, long cancel_time);

    //通过相关信息查找
    public List<Task> getTaskByInfo(String info);

    Task getTaskById(int task_id);

    List<Task> getMyTask(int user_id);

    List<Task> getTaskByName(String name);

    List<Integer> getGroupUserId(int task_id);

    public int getId();

    public int updateTask(Task task);

    public String getTaskFace(int task_id);
    public int setTaskFace(int task_id,String lost_face);

    public int insertFace(int task_id, String face);

    public int deleteFace(int task_id, String face);

    public int updateFace(int task_id, String face);

    public List<String> getFacePath(int task_id);

    public int insertTakenFace(int task_id, int user_id, String path, long time);

    public int joinTask(int user_id, int task_id, long timestamp);

    public int exitTask(int user_id, int task_id);

    public boolean isInTask(int user_id, int task_id);

    public List<Task> getMyPublish(int user_id);
}
