package com.atguigu.tiankuo.videoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.R;
import com.atguigu.tiankuo.videoplayer.activity.ShowImageAndGifActivity;
import com.atguigu.tiankuo.videoplayer.domain.NetAudioBean;
import com.atguigu.tiankuo.videoplayer.utils.Utils;
import com.squareup.picasso.Picasso;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class NetRecyclerAdapter extends RecyclerView.Adapter{

    private final Context mContext;
    private final List<NetAudioBean.ListBean> listDatas;

    public NetRecyclerAdapter(Context mContext, List<NetAudioBean.ListBean> listDatas) {
        this.mContext = mContext;
        this.listDatas = listDatas;
    }

    private static final int TYPE_VIDEO = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_TEXT = 2;
    private static final int TYPE_GIF = 3;
    private static final int TYPE_AD = 4;

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        NetAudioBean.ListBean listBean = listDatas.get(position);
        String type = listBean.getType();//得到类型
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;//广播
        }
        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return initViewHolder(viewType);
    }

    private RecyclerView.ViewHolder initViewHolder(int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View convertView = null;
        switch (viewType) {
            case TYPE_VIDEO:
                convertView = View.inflate(mContext, R.layout.all_video_item, null);
                viewHolder = new VideoHoder(convertView);
                break;
            case TYPE_IMAGE:
                convertView = View.inflate(mContext, R.layout.all_image_item, null);
                viewHolder = new ImageHolder(convertView);
                break;
            case TYPE_TEXT:
                convertView = View.inflate(mContext, R.layout.all_text_item, null);
                viewHolder = new TextHolder(convertView);
                break;
            case TYPE_GIF:
                convertView = View.inflate(mContext, R.layout.all_gif_item, null);
                viewHolder = new GifHolder(convertView);

                break;
            case TYPE_AD:
                convertView = View.inflate(mContext, R.layout.all_ad_item, null);
                viewHolder = new ADHolder(convertView);
                break;
        }
        Log.e("TAG", "viewholder===" + viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemtype = getItemViewType(position);
        if (getItemViewType(position) == TYPE_VIDEO) {
            VideoHoder videoHoder = (VideoHoder) holder;
            videoHoder.setData(listDatas.get(position));
        } else if (getItemViewType(position) == TYPE_IMAGE) {
            ImageHolder imageHolder = (ImageHolder) holder;
            imageHolder.setData(listDatas.get(position));
        } else if (getItemViewType(position) == TYPE_TEXT) {
            TextHolder textHolder = (TextHolder) holder;
            textHolder.setData(listDatas.get(position));
        } else if (getItemViewType(position) == TYPE_GIF) {
            GifHolder gifHolder = (GifHolder) holder;
            gifHolder.setData(listDatas.get(position));
        } else {
            ADHolder adHolder = (ADHolder)holder;
        }
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHeadpic;
        TextView tvName;
        TextView tvTimeRefresh;
        ImageView ivRightMore;
        ImageView ivVideoKind;
        TextView tvVideoKindText;
        TextView tvShenheDingNumber;
        TextView tvShenheCaiNumber;
        TextView tvPostsNumber;
        LinearLayout llDownload;

        public BaseViewHolder(View convertView) {
            super(convertView);

            ivHeadpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvTimeRefresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
            ivRightMore = (ImageView) convertView.findViewById(R.id.iv_right_more);

            ivVideoKind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
            tvVideoKindText = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
            tvShenheDingNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
            tvShenheCaiNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
            tvPostsNumber = (TextView) convertView.findViewById(R.id.tv_posts_number);
            llDownload = (LinearLayout) convertView.findViewById(R.id.ll_download);

            //设置点击事件
            itemView.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetAudioBean.ListBean listEntity = listDatas.get(getLayoutPosition());
                    if (listEntity != null) {
                        //3.传递视频列表
                        Intent intent = new Intent(mContext, ShowImageAndGifActivity.class);
                        if (listEntity.getType().equals("gif")) {
                            String url = listEntity.getGif().getImages().get(0);
                            intent.putExtra("url", url);
                            mContext.startActivity(intent);
                        } else if (listEntity.getType().equals("image")) {
                            String url = listEntity.getImage().getThumbnail_small().get(0);
                            intent.putExtra("url", url);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }

        public void setData(NetAudioBean.ListBean mediaItem) {

            if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
                x.image().bind(ivHeadpic, mediaItem.getU().getHeader().get(0));
            }
            if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
                tvName.setText(mediaItem.getU().getName() + "");
            }

            tvTimeRefresh.setText(mediaItem.getPasstime());

            List<NetAudioBean.ListBean.TagsBean> tagsEntities = mediaItem.getTags();
            if (tagsEntities != null && tagsEntities.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < tagsEntities.size(); i++) {
                    buffer.append(tagsEntities.get(i).getName() + " ");
                }
                tvVideoKindText.setText(buffer.toString());
            }
            tvShenheDingNumber.setText(mediaItem.getUp());
            tvShenheCaiNumber.setText(mediaItem.getDown() + "");
            tvPostsNumber.setText(mediaItem.getForward() + "");
        }
    }

    class VideoHoder extends BaseViewHolder {
        Utils utils;
        TextView tvContext;
        JCVideoPlayerStandard jcvVideoplayer;
        TextView tvPlayNums;
        TextView tvVideoDuration;
        ImageView ivCommant;
        TextView tvCommantContext;

        VideoHoder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            utils = new Utils();
            tvPlayNums = (TextView) convertView.findViewById(R.id.tv_play_nums);
            tvVideoDuration = (TextView) convertView.findViewById(R.id.tv_video_duration);
            ivCommant = (ImageView) convertView.findViewById(R.id.iv_commant);
            tvCommantContext = (TextView) convertView.findViewById(R.id.tv_commant_context);
            jcvVideoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);
        }
        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());

            boolean setUp = jcvVideoplayer.setUp(
                    mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");
            if (setUp) {
                Picasso.with(mContext)
                        .load(mediaItem.getVideo().getThumbnail().get(0))
                        .placeholder(R.drawable.video_default)
                        .error(R.drawable.video_default)
                        .into(jcvVideoplayer.thumbImageView);
            }
            tvPlayNums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
            tvVideoDuration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

        }
    }

    class ImageHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageIcon;

        ImageHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());

            ivImageIcon.setImageResource(R.drawable.bg_item);
            if (mediaItem.getImage() != null && mediaItem.getImage() != null && mediaItem.getImage().getSmall() != null) {
                Picasso.with(mContext)
                        .load(mediaItem.getImage().getDownload_url().get(0))
                        .placeholder(R.drawable.video_default)
                        .error(R.drawable.video_default)
                        .into(ivImageIcon);
            }
        }
    }

    class TextHolder extends BaseViewHolder {
        TextView tvContext;
        TextHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
        }

        public void setData(NetAudioBean.ListBean listBean) {
            super.setData(listBean);
            tvContext.setText(listBean.getText() + "_" + listBean.getType());
            Log.e("TAG","--------wenzi ----------");
        }
    }

    class GifHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageGif;
        private ImageOptions imageOptions;

        GifHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageGif = (ImageView) convertView.findViewById(R.id.iv_image_gif);

            imageOptions = new ImageOptions.Builder()
                    .setSize(ViewGroup.LayoutParams.WRAP_CONTENT, -2)
                    .setIgnoreGif(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.video_default)
                    .setFailureDrawableId(R.drawable.video_default)
                    .build();
        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
            if (mediaItem.getGif() != null && mediaItem.getGif() != null && mediaItem.getGif().getImages() != null) {
                x.image().bind(ivImageGif, mediaItem.getGif().getImages().get(0), imageOptions);
            }

        }
    }

    class ADHolder extends RecyclerView.ViewHolder {
        TextView tvContext;
        ImageView ivImageIcon;
        Button btnInstall;

        ADHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            btnInstall = (Button) convertView.findViewById(R.id.btn_install);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
        }

    }


}
