package xzh.com.wraptext_master.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.ButterKnife;
import butterknife.InjectView;
import xzh.com.wraptext_master.R;
import xzh.com.wraptext_master.base.BasicAdapter;
import xzh.com.wraptext_master.model.SpecEntity;

/**
 * Created by wangzheng on 2016/4/1.
 */
public class SpecAdapter extends BasicAdapter<SpecEntity> {
    public SpecAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflate(R.layout.item_spec_layout);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SpecEntity data = getItem(position);
        viewHolder.specNameView.setText(data.SpecName);
        viewHolder.deleteButton.setTag(data);
        viewHolder.deleteButton.setOnClickListener(deleteSpecClickListener);
        return convertView;
    }

    private View.OnClickListener deleteSpecClickListener = new View.OnClickListener() {
        private SpecEntity data = null;

        public void onClick(View v) {
            data = (SpecEntity) v.getTag();
            remove(data);
        }
    };

    static class ViewHolder {
        @InjectView(R.id.spec_name_view)
        TextView specNameView;
        @InjectView(R.id.delete_button)
        ImageView deleteButton;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
            view.setTag(this);
        }
    }
}
