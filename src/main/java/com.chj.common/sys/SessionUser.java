package com.chj.common.sys;

import com.chj.common.mybatis.DataPermission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenhj
 * @since 2018-3-24
 */
@Data
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 1L;
    //目前userId直接存放处
    private Long userId;
    private String userCode;
    private String userName;
    private String userType;
    private String language;
    private String timeZone;
    private String roleCode;
    private String tokenId;
    private String password;
    //用户名称
    private String nickName;
    private String[]  roleId;
    private String roleName;
    private String isFirstLogin;
    /**
     * 登录类型0：pc 1:rf*/
    private String loginType;
    /**
     * 登录人级别*/
    private String loginUserType;
    /**
     * 是否需要分库校验 非分库情况下为false*/
    private boolean isCheckedObject;
    /**
     * 分库代号 wms以仓库分库 值为3;待补充*/
    private String objectType="3";
    /**
     * 所属系统*/
    private String sys ;
    /**
     * 分库标示，返回分库ID*/
    private Long shard;
    //登录用户的数据权限
    private DataPermission dataPermission;
    /**
     * 各系统预留字段
     */
    private Object userInfo;
}
