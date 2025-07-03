package com.fly.service;

import com.fly.pojo.Account;
import java.util.List;

public interface AccountService {
    /**
     * 查询所有
     * @return
     */
    public List<Account> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Account findById(Integer id);

    /**
     * 保存账户
     * @param account
     */
    public void save(Account account);

    /**
     * 更新账户
     * @param account
     */
    public void update(Account account);

    /**
     * 删除一个账户
     * @param id
     */
    public void del(Integer id);
}
