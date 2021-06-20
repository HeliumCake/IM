package com.tsinghua.course.Base.Enum;

/**
 * @描述 动态类型枚举
 **/
public enum FileType {
    PICTURE("图片"),
    VIDEO("视频"),
    AUDIO("音频")
    ;

    FileType(String name) {
        this.name = name;
    }

    String name;

    public String getName() {
        return name;
    }
}
