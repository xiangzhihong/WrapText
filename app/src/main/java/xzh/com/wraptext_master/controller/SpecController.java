package xzh.com.wraptext_master.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import xzh.com.wraptext_master.model.SpecEntity;
import xzh.com.wraptext_master.model.SpecModel.SpecListEntity;

/**
 * Created by xiangzhihong on 2016/6/2 on 18:02.
 */
public class SpecController {

    private Context mContext;
    private List<SpecEntity> mSpecList = new ArrayList<>();
    private List<SpecEntity> mWantToSpecList = new ArrayList<>();
    private List<SpecEntity> mUsedSpecList = new ArrayList<>();

    public SpecController(Context context) {
        this.mContext = context;
    }

    public void structuralData(SpecListEntity specListEntity) {
        for (String spec : specListEntity.CommendCatalogList) {
            mWantToSpecList.add(new SpecEntity(spec));
        }
        for (String spec : specListEntity.LastCatalogList) {
            mUsedSpecList.add(new SpecEntity(spec));
        }
        mSpecList.addAll(mWantToSpecList);
        mSpecList.addAll(mUsedSpecList);
    }


    public List<SpecEntity> getUsedSpecList() {
        return mUsedSpecList;
    }

    public List<SpecEntity> getWantToSpecList() {
        return mWantToSpecList;
    }

}
