package com.jian.service;


import com.jian.model.Users;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 注册用户
     * @param users
     */
    public void saveUser(Users users);

    /**
     * 验证用户登陆
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username,String password);

    /**
     * 用户修改信息
     * @param users
     */
    public void updateUserInfo(Users users);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);
}
