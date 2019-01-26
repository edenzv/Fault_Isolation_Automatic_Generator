package org.afeka.fi.backend.pojo.commonstructure;

import com.google.gson.annotations.Expose;
import j2html.tags.ContainerTag;

import java.util.Arrays;

public class HtmlObj {

    @Expose(serialize = true, deserialize = true)
    private HtmlData[] htmlData;

    @Expose(serialize = false, deserialize = false)
    public ContainerTag htmlObject;

    public HtmlData[] getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(HtmlData[] htmlData) {
        this.htmlData = htmlData;
    }

    @Override
    public String toString() {
        return "HtmlObj{" +
                "htmlData=" + Arrays.toString(htmlData) +
                '}';
    }
}