package com.vm.frontend.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.vm.base.utils.ServiceController;
import com.vm.dao.po.CustomVmUsers;
import com.vm.dao.po.VmUsers;
import com.vm.dao.validator.group.VmUsersGroups;
import com.vm.frontend.service.bo.VmUsersBo;
import com.vm.frontend.service.inf.VmUsersService;
import com.vm.frontend.service.vo.VmUsersVo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Created by ZhangKe on 2017/12/28.
 */
@Controller
@RequestMapping("/user")
@Scope("prototype")
public class VmUsersController extends ServiceController<VmUsersService> {
    public static final String KEY_OF_ONLINE_USER = "ONLINE_USER";

    @RequestMapping(value = "/login", method = RequestMethod.PUT)
    @ResponseBody
    public Object userLogin(@Validated(value = {VmUsersGroups.UserLogin.class})
                            @RequestBody VmUsersVo user,
                            BindingResult result) throws Exception {

        VmUsersBo loginUser = service.userLogin(user);

        setSessionAttr(KEY_OF_ONLINE_USER, loginUser);

        return ImmutableMap.of(
                "user", loginUser
        );

    }

    @RequestMapping(value = "/regist", method = RequestMethod.PUT)
    @ResponseBody
    public Object userRegist(@Validated(value = {VmUsersGroups.UserRegist.class})
                             @RequestBody VmUsersVo user,
                             BindingResult result) throws Exception {

        VmUsersBo loginUser = service.userRegist(user);

        getSession().setAttribute(KEY_OF_ONLINE_USER, loginUser);


        return ImmutableMap.of(
                "user", loginUser
        );
    }

    @RequestMapping(value = "/online", method = RequestMethod.GET)
    @ResponseBody
    public Object getOnlineUser() throws Exception {
        Object user = null;

        if (!isNullObject(getSession())) {
            user = getSession().getAttribute(KEY_OF_ONLINE_USER);
        }
        if (isNullObject(user)) {
            return Maps.newHashMap();
        }
        //get db use
        user = service.getUserBasicInfo(((VmUsersBo) user).getId());
        //update session user
        setSessionAttr(KEY_OF_ONLINE_USER, user);

        return ImmutableMap.of(
                "user",user
        );
    }


    @RequestMapping(value = "/logout", method = RequestMethod.PUT)
    public @ResponseBody
    Object userLogout() throws Exception {

        getSession().removeAttribute(KEY_OF_ONLINE_USER);

        getSession().invalidate();

        return response;

    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserBasicInfo(@PathVariable("userId") Long userId) throws Exception {

        return ImmutableMap.of(
                "user", service.getUserBasicInfo(userId)
        );
    }


    @RequestMapping(value = "/online/update", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateOnlineUserBasicInfo(@Validated(value = {VmUsersGroups.UpdateUserBasicInfo.class})
                                            @RequestBody VmUsersVo user,
                                            BindingResult result) throws Exception {

        Object vmUsers = service.updateOnlineUserBasicInfo(user);

        setSessionAttr(KEY_OF_ONLINE_USER, vmUsers);

        return ImmutableMap.of("user", vmUsers);
    }

    /**
     * 获取用户图片
     *
     * @return
     */
    @RequestMapping(value = "/img/{fileId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserImg(
            @PathVariable("fileId") Long fileId,
            Integer width) throws Exception {
        service.sendUserImg(fileId, width, getResponse());
        return response;
    }
//
//    @RequestMapping(value = "/update", method = RequestMethod.PUT)
//    public @ResponseBody
//    Object updateOnlineUserBasicInfo(@Validated(value = {VmUsersGroups.UpdateUserBasicInfo.class}) CustomVmUsers user,
//                               BindingResult result) throws Exception {
//
//        VmUsers vmUsers = service.updateOnlineUserBasicInfo(user);
//
//        response.putData("user",vmUsers);
//
//        return response;
//    }
}

