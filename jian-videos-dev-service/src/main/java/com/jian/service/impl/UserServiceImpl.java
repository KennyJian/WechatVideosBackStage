package com.jian.service.impl;

import com.jian.service.UserService;
import com.jian.mapper.UsersMapper;
import com.jian.model.Users;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);

        Users result = usersMapper.selectByUserName(username);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users users) {

        users.setId(sid.nextShort());
        usersMapper.insert(users);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Users result = usersMapper.selectByUserName(username);
        if (result != null) {
            if (!password.equals(result.getPassword())) {
                result = null;
            }
        }
        return result;
    }

    @Override
    public void updateUserInfo(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
    }

    @Override
    public Users queryUserInfo(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }
}
