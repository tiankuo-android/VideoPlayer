package com.atguigu.tiankuo.videoplayer.pager;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.R;
import com.atguigu.tiankuo.videoplayer.activity.ShowImageAndGifActivity;
import com.atguigu.tiankuo.videoplayer.adapter.NetAudioFragmentAdapter;
import com.atguigu.tiankuo.videoplayer.domain.NetAudioBean;
import com.atguigu.tiankuo.videoplayer.fragment.BaseFragment;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NetAudioPager extends BaseFragment {

    private static final String TAG = NetAudioPager.class.getSimpleName();
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;
    private NetAudioFragmentAdapter myAdapter;
    private List<NetAudioBean.ListBean> datas;
    private MaterialRefreshLayout materialRefreshLayout;
    private boolean isLoadMore = false;
    private String NET_AUDIO_URL = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";

    @Override
    public View initView() {
        Log.e(TAG, "网络音频UI被初始化了");
        View view = View.inflate(context, R.layout.fragment_net_audio, null);
        ButterKnife.bind(this, view);

        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        materialRefreshLayout.setWaveColor(0xffffffff);
        materialRefreshLayout.setIsOverLay(false);
        materialRefreshLayout.setWaveShow(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                isLoadMore = false;
                getDataFromNet();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
//                isLoadMore = true;
//                getMoreData();
            }
        });


        //设置点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                NetAudioBean.ListBean listEntity = datas.get(position);
                if (listEntity != null) {
                    //3.传递视频列表
                    Intent intent = new Intent(context, ShowImageAndGifActivity.class);
                    if (listEntity.getType().equals("gif")) {
                        String url = listEntity.getGif().getImages().get(0);
                        intent.putExtra("url", url);
                        context.startActivity(intent);
                    } else if (listEntity.getType().equals("image")) {
                        String url = listEntity.getImage().getBig().get(0);
                        intent.putExtra("url", url);
                        context.startActivity(intent);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "网络音频数据初始化了");
//        String saveJson = CacheUtils.getString(context,NET_AUDIO_URL);
//        if(!TextUtils.isEmpty(NET_AUDIO_URL)){
//            processData(NET_AUDIO_URL);
//        }

        getDataFromNet();

    }

    private void getDataFromNet() {
        RequestParams reques = new RequestParams(NET_AUDIO_URL);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

//                CacheUtils.putString(mContext,NET_AUDIO_URL,result);
//                CacheUtils.putString(mContext,NET_AUDIO_URL,result);
                LogUtil.e("onSuccess==" + result);
                processData(result);
                materialRefreshLayout.finishRefresh();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }

    private void processData(String result) {
        NetAudioBean netAudioBean = new Gson().fromJson(result, NetAudioBean.class);
//        NetAudioBean netAudioBean = paraseJson(result);
        LogUtil.e(netAudioBean.getList().get(0).getText() + "-----------");

        datas = netAudioBean.getList();

        if (datas != null && datas.size() > 0) {
            //有视频
            tvNomedia.setVisibility(View.GONE);
            //设置适配器
            myAdapter = new NetAudioFragmentAdapter(context, datas);
            listview.setAdapter(myAdapter);
        } else {
            //没有视频
            tvNomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);

    }

    private List<NetAudioBean.ListBean> parsedJson(String json) {
        NetAudioBean netAudioBean = new Gson().fromJson(json, NetAudioBean.class);
        return netAudioBean.getList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
