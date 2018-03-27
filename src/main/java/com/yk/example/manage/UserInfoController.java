package com.yk.example.manage;

import com.yk.example.dao.UserDao;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.User;
import com.yk.example.service.UserService;
import com.yk.example.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by yk on 2018/3/27.
 */
@ApiIgnore
@Controller
public class UserInfoController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;


    /**
     * @param model
     * @param user
     * @param pageCurrent 当前页
     * @param pageSize    每页个数
     * @param pageCount   总共多少页
     * @return
     */
    @RequestMapping(value = "/admin/userList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, User user, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if(pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        Page<User> userPage =  userService.findAll(user,new PageRequest(pageCurrent-1, pageSize));
        model.addAttribute("userList", userPage.getContent());
        String pageHTML = PageUtils.getPageContent("userList_{pageCurrent}_{pageSize}_{pageCount}?phone=" + user.getPhone() + "&nickName=" + user.getNickName(), pageCurrent, pageSize, userPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("user", user);
        return "user/userManage";
    }

    /**
     * 用户编辑
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value = "/admin/userEdit")
    public String userEdit(User user,Model model){
        if(StringUtils.isNotBlank(user.getUserId())){
            User newUser = userService.findOne(user.getUserId());
            model.addAttribute("user", newUser);
        }
        return "user/userEdit";
    }

    /**
     * 删除用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/admin/updateUserState")
    @ResponseBody
    public ControllerResult updateUserState(User user) {
        int i = userService.updateIsValidByUserId(user.getUserId(), user.getIsValid());
        return new ControllerResult().setRet_code(0).setMessage("删除成功");
    }
}
