package com.jerry.mapper;

import com.jerry.entity.Location;
import com.jerry.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface UserMapper {
    public List<User> getAllUser();

    public int addUser(User user);

    public int deleteUser(int user_id);

    public int updateUser(User user);

    public int updateUserInfo(int user_id, String type, String value);

    public int changeUserState(int user_id, int state);

    public User getUserById(int user_id);

    public User getUserByTel(long tel);

    public List<User> getUserByName(String name);

    public List<User> getUserByGender(String Gender);

    public int getId();

    public int updateUserLocation(Location location);

    public String getIcon(int user_id);
    public String getName(int user_id);

}
