package xzh.com.wraptext_master.ui;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import xzh.com.wraptext_master.R;
import xzh.com.wraptext_master.adapter.RcmdSpecAdapter;
import xzh.com.wraptext_master.adapter.SpecAdapter;
import xzh.com.wraptext_master.controller.SpecController;
import xzh.com.wraptext_master.model.SpecModel;
import xzh.com.wraptext_master.utils.JsonUtil;
import xzh.com.wraptext_master.utils.Utils;
import xzh.com.wraptext_master.view.SearchView;
import xzh.com.wraptext_master.view.wrap.WrapTextView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.spec_flow_view)
    WrapTextView specFlowView;
    @InjectView(R.id.recommend_spec_flow_view)
    WrapTextView recommendSpecFlowView;
    @InjectView(R.id.title_text_view)
    TextView titleTextView;

   private static int MAX_SPEC_COUNT=4;
    private SpecAdapter mSpecAdapter = null;
    private RcmdSpecAdapter mWantToSpecAdapter = null;
    private SpecController mSpecController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        mSpecController=new SpecController(this);

        mSpecAdapter = new SpecAdapter(this);
        mSpecAdapter.registerDataSetObserver(dataSetObserver);
        specFlowView.setAdapter(mSpecAdapter);

        //猜你想的商品规格列表
        mWantToSpecAdapter = new RcmdSpecAdapter(this, mSpecAdapter);
        recommendSpecFlowView.setAdapter(mWantToSpecAdapter);
    }

    private void initData() {
        String data = Utils.getAssertData(this, "spec.txt");
        SpecModel model = JsonUtil.parseJson(data, SpecModel.class);
        if (model != null) {
            mSpecController.structuralData(model.Result);
            mWantToSpecAdapter.setList(mSpecController.getWantToSpecList());
        }
    }

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        public void onChanged() {
            titleTextView.setText("选择规格" + mSpecAdapter.getCount() + "/" + MAX_SPEC_COUNT);
            specFlowView.setVisibility(mSpecAdapter.isEmpty() ? View.GONE : View.VISIBLE);
            if (mWantToSpecAdapter != null) mWantToSpecAdapter.notifyDataSetChanged();
        }
    };
}
