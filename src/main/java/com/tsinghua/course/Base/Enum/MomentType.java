package com.tsinghua.course.Base.Enum;

/**
 * @描述 动态类型枚举
 **/
public enum MomentType {
    TEXT("文本"),
    PICTURE("图片"),
    VIDEO("视频")
    ;

    MomentType(String name) {
        this.name = name;
    }

    String name;

    public String getName() {
        return name;
    }
}
