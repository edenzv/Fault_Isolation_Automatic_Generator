package org.afeka.fi.backend.pojo.commonstructure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PG
{
    @Expose(serialize = true,deserialize = true)
    @XmlAttribute(name="dwgID")
    public String dwgID="";
    @Expose(serialize = true,deserialize = true)
    @XmlAttribute(name="doc")
    public String doc;
    @SerializedName("n")
    @Expose(serialize = true,deserialize = true)
    @JsonProperty("n")
    @XmlAttribute(name="n")
    public String _n;
    @Expose(serialize = true,deserialize = true)
    @XmlElement(name = "N")
    public YN N;
    @Expose(serialize = true,deserialize = true)
    @XmlElement(name = "Y")
    public YN Y;
    @Expose(serialize = true,deserialize = true)
    @XmlTransient
    public HtmlObj htmlObj;

    @Override
    public String toString() {
        return "PG{" + "dwgID='" + dwgID + '\'' + ", doc='" + doc + '\'' + ", _n='" + _n + '\'' + ", N=" + N + ", Y=" + Y + ", htmlObj=" + htmlObj + '}';
    }
}