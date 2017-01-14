package com.example.test.listviewscrollnoload;

/**
 * 作者：Chris
 * 创建时间: 2017/1/14 17:44
 * 邮箱：395932265@qq.com
 * 描述:
 * TODO
 */
public class UserEnity {
    String name;
    String icon;

    public UserEnity(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "UserEnity{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
