package com.yk.example.rongCloud.models.response;


import com.yk.example.rongCloud.models.Result;
import com.yk.example.rongCloud.models.user.UserModel;
import com.yk.example.rongCloud.util.GsonUtil;

/**
 * @author RongCloud
 */
public class WhiteListResult extends Result {

    private UserModel[] members;

    public WhiteListResult(Integer code, String msg, UserModel[] members) {
        super(code, msg);
        this.members = members;
    }

    public WhiteListResult(UserModel[] members) {
        this.members = members;
    }

    public UserModel[] getMembers() {
        return this.members;
    }

    public void setMembers(UserModel[] members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this, WhiteListResult.class);
    }

}
