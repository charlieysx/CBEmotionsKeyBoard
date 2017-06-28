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
    private String iconUri;
    private String iconType;

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

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
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
}
