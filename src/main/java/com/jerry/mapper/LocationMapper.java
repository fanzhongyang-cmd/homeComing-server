package com.jerry.mapper;


import com.jerry.entity.Location;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface LocationMapper {
    public List<Location> queryAllLocation();

    public List<Location> queryGroupLocation(int task_id);

    public List<Location> queryLocationById(int task_id, int user_id);

    public void uploadLocation(Location location);

    public void deleteGroupLocation(int task_id);

    public void deleteGroupUserLocation(int task_id, int user_id);

    public void deleteUserLocation(int user_id);
}
