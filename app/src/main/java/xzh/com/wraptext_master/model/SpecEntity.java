package xzh.com.wraptext_master.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpecEntity implements Serializable {
    @SerializedName("Catalog")
    public String SpecName;//规格名称
    public boolean IsRefPicture;//是否关联图片
    private List<AttrEntity> Attrs;//规格的属性集合

    public SpecEntity(String specName) {
        this.SpecName = specName;
    }

    public boolean add(AttrEntity attr) {
        if (Attrs == null) {
            Attrs = new ArrayList<>();
        }
        return Attrs.add(attr);
    }

    public boolean isEmpty() {
        return Attrs == null || Attrs.isEmpty();
    }

    public List<AttrEntity> getAttrs() {
        if (Attrs == null) {
            Attrs = new ArrayList<>();
        }
        return Attrs;
    }

    @Override
    public boolean equals(Object o) {
        SpecEntity data = (SpecEntity) o;
        return SpecName.equals(data.SpecName);
    }
}