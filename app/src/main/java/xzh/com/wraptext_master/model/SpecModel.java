package xzh.com.wraptext_master.model;

import java.util.List;

public class SpecModel {

    public String Msg;
    public int Status;
    public String ServerTime;
    public SpecListEntity Result;

    public static class SpecListEntity {
        public List<String> CommendCatalogList;
        public List<String> LastCatalogList;
    }
}
