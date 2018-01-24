package com.exz.zjb.bean;

import java.util.List;

/**
 * Created by 史忠文
 * on 2018/1/23.
 */

public class test {
    /**
     * title : 神钢桩机
     * carImageUrl : ["http://xxx/img1.png","http://xxx/img2.png","http://xxx/img3.png"]
     * provinceCity : 江苏徐州
     * date : 2018-01-18
     * factoryYear : 2017
     * modelName : 神钢SK135R
     * description : 车主描述
     * nickname : 昵称
     * authenticationState : 状态：0审核中，1已通过 2未通过
     * starLevel : 星级，返回数字（1 ~ 5）
     * headImg : http://123.png
     * company : 公司名称
     * companyAddress : 公司地址
     * mobile : 联系电话
     * isCollection : 0
     */

    private String title;
    private String provinceCity;
    private String date;
    private String factoryYear;
    private String modelName;
    private String description;
    private String nickname;
    private String authenticationState;
    private String starLevel;
    private String headImg;
    private String company;
    private String companyAddress;
    private String mobile;
    private String isCollection;
    private List<String> carImageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFactoryYear() {
        return factoryYear;
    }

    public void setFactoryYear(String factoryYear) {
        this.factoryYear = factoryYear;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAuthenticationState() {
        return authenticationState;
    }

    public void setAuthenticationState(String authenticationState) {
        this.authenticationState = authenticationState;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public List<String> getCarImageUrl() {
        return carImageUrl;
    }

    public void setCarImageUrl(List<String> carImageUrl) {
        this.carImageUrl = carImageUrl;
    }
}
