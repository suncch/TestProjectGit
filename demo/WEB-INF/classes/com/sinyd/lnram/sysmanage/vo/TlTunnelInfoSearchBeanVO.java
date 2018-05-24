package com.sinyd.lnram.sysmanage.vo;

import com.sinyd.lnram.util.BaseSearchBean;

/*
 * @version 2.0
 */

public class TlTunnelInfoSearchBeanVO extends BaseSearchBean {

    private static final long serialVersionUID = 1L;

    private String isCanEmpty;

    private String tunnel_code;

    public String getTunnel_code() {
        return tunnel_code;
    }

    public void setTunnel_code(String tunnel_code) {
        this.tunnel_code = tunnel_code;
    }

    public String getIsCanEmpty() {
        return isCanEmpty;
    }

    public void setIsCanEmpty(String isCanEmpty) {
        this.isCanEmpty = isCanEmpty;
    }

}