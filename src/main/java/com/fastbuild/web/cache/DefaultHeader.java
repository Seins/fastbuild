package com.fastbuild.web.cache;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

/**
 * 类名：com.fastbuild.web.cache.DefaultHeader
 * 创建者： 邓风森 .
 * 创建时间：2016/1/26
 */
public class DefaultHeader implements Header {
    private String value;
    private String name;

    public DefaultHeader(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public DefaultHeader(String nameAndValue) throws Exception {
        String[] nav = nameAndValue.split(":");
        if (nav.length < 2) {
            throw new Exception("错误的header");
        }
        this.name = nav[0];
        this.value = nameAndValue.substring(nav[0].length() + 1, nameAndValue.length());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        return new HeaderElement[0];
    }
}
