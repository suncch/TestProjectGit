package com.sinyd.lnram.util;

import com.sinyd.platform.uivo.vo.GridParamBean;

@SuppressWarnings("serial")
public class BaseSearchBean extends GridParamBean {

    private String office_code;

    private String maintain_code;

    /**
     * @return the office_code
     */
    public String getOffice_code() {
        return office_code;
    }

    /**
     * @param office_code the office_code to set
     */
    public void setOffice_code(String office_code) {
        this.office_code = office_code;
    }

    public String getMaintain_code() {
        return maintain_code;
    }

    public void setMaintain_code(String maintain_code) {
        this.maintain_code = maintain_code;
    }

}
