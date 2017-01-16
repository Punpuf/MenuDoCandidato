package com.punpuf.uem_menudocandidato.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.punpuf.uem_menudocandidato.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private Context mContext;
    private ArrayList<String> mTitleList;
    private ArrayList<String> mContentList;
    private int lastPosition;

    RecyclerViewAdapter(Context context, ArrayList<String> titleList,
                               ArrayList<String> contentList){
        mContext = context;
        mTitleList = titleList;
        mContentList = contentList;
        lastPosition = -1;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.home_list_item, parent, false);
        return new RecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.title.setText(mTitleList.get(position));
        holder.content.setText(mContentList.get(position));

        /*String buttonText = mButtonList.get(position);
        if(buttonText != null) holder.button.setText(buttonText);
        else*/ holder.button.setVisibility(View.GONE);

        if(lastPosition < position){
            Timber.d("last pos is bigger than current");
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_enter);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    void updateData(ArrayList<String> titleList, ArrayList<String> contentList){
        mTitleList = titleList;
        mContentList = contentList;
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.list_item_title)
        TextView title;
        @BindView(R.id.list_item_content)
        TextView content;
        @BindView(R.id.list_item_btn)
        Button button;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
