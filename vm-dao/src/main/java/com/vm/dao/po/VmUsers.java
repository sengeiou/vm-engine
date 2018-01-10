package com.vm.dao.po;

import com.vm.base.utils.ByteConstantVar;
import com.vm.base.utils.VmProperties;

public class VmUsers extends BasePo {
    //注册用户时填入的默认img_url前缀
    public static final String USER_IMG_URL_PREFIX = VmProperties.VM_USER_IMG_URL_PREFIX;

    private Long id;

    private String username;

    private String password;

    private Byte sex;

    private Integer birthday;

    private String description;

    private Byte status;

    private Integer createTime;

    private Integer updateTime;

    private String imgUrl;

    /**
     * 状态
     */
    public enum Sex {
        //性别，1为男，1为女，3未设置
        MEN(ByteConstantVar.ONE, "男"),
        WOMEN(ByteConstantVar.TWO, "女"),
        UNKNOWN(ByteConstantVar.THREE, "未设置");

        Byte code;

        String msg;

        Sex(Byte code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Byte getCode() {
            return code;
        }

        public void setCode(Byte code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public VmUsers() {
    }
}