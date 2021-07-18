package com.jerry.mapper;

import com.jerry.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface AccountMapper {
    public Account query(int user_id);

    public int countTel(long telephone);

    public int addAccount(Account account);

    public int deleteAccount(int user_id);

    public int updateAccount(Account account);
    //public int logout(int user_id);   


}
