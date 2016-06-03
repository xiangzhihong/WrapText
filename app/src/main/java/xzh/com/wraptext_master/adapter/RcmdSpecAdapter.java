package xzh.com.wraptext_master.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import xzh.com.wraptext_master.R;
import xzh.com.wraptext_master.base.BasicAdapter;
import xzh.com.wraptext_master.model.SpecEntity;


public class RcmdSpecAdapter extends BasicAdapter<SpecEntity> {
    private final int SPEC_MAX_COUNT =20;
    private final int RCMD_MAX_COUNT = 20;
    private SpecAdapter mSpecAdapter = null;

    public RcmdSpecAdapter(Context context, SpecAdapter specAdapter) {
        super(context);
        mSpecAdapter = specAdapter;
    }

    @Override
    public int getCount() {
        int realCount = super.getCount();
        return realCount < RCMD_MAX_COUNT ? realCount : RCMD_MAX_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBox specCheckBox = inflate(R.layout.item_rcmd_spec_layout);
        SpecEntity data = getItem(position);
        specCheckBox.setText(data.SpecName);
        specCheckBox.setTag(data);
        specCheckBox.setOnClickListener(clickListener);
        checkPrevSpecState(data, specCheckBox);
        return specCheckBox;
    }

    private void checkPrevSpecState(SpecEntity data, CheckBox specCheckBox) {
        for (SpecEntity specEntity : mSpecAdapter.getList()) {
            if (specEntity.equals(data)) {
                data = specEntity;
                specCheckBox.setChecked(true);
            }
        }
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        private SpecEntity data = null;
        public void onClick(View v) {
            data = (SpecEntity) v.getTag();
            CompoundButton compoundButton = (CompoundButton)v;
            boolean bool = compoundButton.isChecked() && mSpecAdapter.getCount() < SPEC_MAX_COUNT;
            if (bool) {
                mSpecAdapter.add(data);
            } else{
                mSpecAdapter.remove(data);
            }
            compoundButton.setChecked(bool);
        }
    };
}
