package com.codebear.keyboard.data;

import com.codebear.keyboard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Created by CodeBear on 2017/6/28.
 */

public class EmoticonsBean {
    public static final String DEL = "del";

    private String parentId;
    private String id;
    private String name;
    private Object iconUri;
    private String iconType;
    private boolean showName = false;
    private boolean bigEmoticon = false;

    private int rol = -1;
    private int row = -1;

    private boolean showDel = false;

    private List<EmoticonsBean> emoticonsBeanList = new ArrayList<>();

    public EmoticonsBean() {
    }

    public EmoticonsBean(boolean isDel) {
        if(isDel) {
            id = DEL;
            iconUri = R.mipmap.icon_del;
        }
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

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public boolean isBigEmoticon() {
        return bigEmoticon;
    }

    public void setBigEmoticon(boolean bigEmoticon) {
        this.bigEmoticon = bigEmoticon;
        if (rol == -1 || row == -1) {
            if (bigEmoticon) {
                rol = 4;
            } else {
                rol = 7;
            }
            row = 3;
        }
    }

    @Override
    public String toString() {
        return "EmoticonsBean{" + "parentId='" + parentId + '\'' + ", id='" + id + '\'' + ", name='" + name + '\'' +
                ", iconUri=" + iconUri + ", iconType='" + iconType + '\'' + ", showName=" + showName + ", " +
                "bigEmoticon=" + bigEmoticon + ", rol=" + rol + ", row=" + row + ", showDel=" + showDel + ", " +
                "emoticonsBeanList=" + emoticonsBeanList + '}';
    }
}
