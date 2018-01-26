package com.exz.zjb.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc on 2018/1/25.
 */

public class UpgradeBean {

    /**
     * isUpgrade : 1
     * isMust : 1
     * loadUrl : https//123.aspx
     * tip : 更新内容提示
     */

    @SerializedName("isUpgrade")
    private String isUpgrade;
    @SerializedName("isMust")
    private String isMust;
    @SerializedName("loadUrl")
    private String loadUrl="http://sj.qq.com/myapp/";
    @SerializedName("tip")
    private String tip;

    public String getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(String isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public String getIsMust() {
        return isMust;
    }

    public void setIsMust(String isMust) {
        this.isMust = isMust;
    }

    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
