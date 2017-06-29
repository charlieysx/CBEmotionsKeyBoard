package com.codebear.keyboard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Created by CodeBear on 2017/6/28.
 */

public class EmoticonsBean {
    private String parentId;
    private String id;
    private String name;
    private Object iconUri;
    private String iconType;

    private int rol = 3;
    private int row = 4;

    private boolean showDel;

    private List<EmoticonsBean> emoticonsBeanList = new ArrayList<>();

    public EmoticonsBean() {
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getIconUri() {
        return iconUri;
    }

    public void setIconUri(Object iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public List<EmoticonsBean> getEmoticonsBeanList() {
        return emoticonsBeanList;
    }

    public void setEmoticonsBeanList(List<EmoticonsBean> emoticonsBeanList) {
        this.emoticonsBeanList = emoticonsBeanList;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isShowDel() {
        return showDel;
    }

    public void setShowDel(boolean showDel) {
        this.showDel = showDel;
    }
}
