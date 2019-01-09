package com.example.tiantian.myapplication.data.wxarticle;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class Chapters implements Parcelable {
    /**
     * children : []
     * courseId : 13
     * id : 408
     * name : 鸿洋
     * order : 190000
     * parentChapterId : 407
     * userControlSetTop : false
     * visible : 1
     */

    private int courseId;
    private int id;
    private String name;
    private int order;
    private int parentChapterId;
    private boolean userControlSetTop;
    private int visible;
    private List<Chapters> children;

    public Chapters() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public boolean isUserControlSetTop() {
        return userControlSetTop;
    }

    public void setUserControlSetTop(boolean userControlSetTop) {
        this.userControlSetTop = userControlSetTop;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public List<Chapters> getChildren() {
        return children;
    }

    public void setChildren(List<Chapters> children) {
        this.children = children;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courseId);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(order);
        dest.writeInt(parentChapterId);
        dest.writeInt(userControlSetTop ? 1 : 0);
        dest.writeInt(visible);
        dest.writeTypedList(children);
    }

    public static final Parcelable.Creator<Chapters> CREATOR = new Creator<Chapters>() {
        @Override
        public Chapters createFromParcel(Parcel source) {
            return new Chapters(source);
        }

        @Override
        public Chapters[] newArray(int size) {
            return new Chapters[size];
        }
    };

    private Chapters(Parcel source) {
        courseId = source.readInt();
        id = source.readInt();
        name = source.readString();
        order = source.readInt();
        parentChapterId = source.readInt();
        userControlSetTop = source.readInt() == 1;
        visible = source.readInt();
        children = source.createTypedArrayList(CREATOR);
    }
}