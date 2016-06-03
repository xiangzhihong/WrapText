package xzh.com.wraptext_master.model;

import java.io.Serializable;

public class AttrEntity implements Serializable {
    public AttrEntity(String attr) {
        this.Attr = attr;
    }
    public AttrEntity(String specName, String attr) {
        this.Catalog = specName;
        this.Attr = attr;
    }

    public String Catalog;//属性名称
    public String Attr;//属性名称
    public String ImageUrl;//属性图片

    @Override
    public String toString() {
        return Attr + "-" + ImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        return hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return (Catalog + Attr).hashCode();
    }
}
