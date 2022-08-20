package com.kunminx.architecture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
/**
 * Create by KunMinX at 2022/8/20
 */
public abstract class BaseBindingAdapter<M, B extends ViewDataBinding>
        extends RecyclerView.Adapter<BaseBindingAdapter.BaseBindingViewHolder> {

  protected List<M> mList;
  protected OnItemClickListener<M> mOnItemClickListener;
  protected OnItemLongClickListener<M> mOnItemLongClickListener;

  public BaseBindingAdapter(List<M> list) {
    mList = list;
  }

  public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  public void setOnItemLongClickListener(OnItemLongClickListener<M> onItemLongClickListener) {
    mOnItemLongClickListener = onItemLongClickListener;
  }

  @Override
  @NonNull
  public BaseBindingAdapter.BaseBindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), this.getLayoutResId(viewType), parent, false);
    BaseBindingViewHolder holder = new BaseBindingViewHolder(binding.getRoot());
    holder.itemView.setOnClickListener(v -> {
      if (mOnItemClickListener != null) {
        int position = holder.getBindingAdapterPosition();
        mOnItemClickListener.onItemClick(holder.itemView.getId(), mList.get(position), position);
      }
    });
    holder.itemView.setOnLongClickListener(v -> {
      if (mOnItemLongClickListener != null) {
        int position = holder.getBindingAdapterPosition();
        mOnItemLongClickListener.onItemLongClick(holder.itemView.getId(), mList.get(position), position);
        return true;
      }
      return false;
    });
    return holder;
  }

  @Override
  public void onBindViewHolder(BaseBindingAdapter.BaseBindingViewHolder holder, final int position) {
    B binding = DataBindingUtil.getBinding(holder.itemView);
    this.onBindItem(binding, mList.get(position), holder);
    if (binding != null) binding.executePendingBindings();
  }

  protected abstract @LayoutRes
  int getLayoutResId(int viewType);

  protected abstract void onBindItem(B binding, M item, RecyclerView.ViewHolder holder);

  public List<M> getList() {
    return mList;
  }

  public void refresh(List<M> list) {
    mList.clear();
    mList.addAll(list);
    notifyDataSetChanged();
  }

  public void append(List<M> list) {
    mList.addAll(list);
    notifyDataSetChanged();
  }

  public static class BaseBindingViewHolder extends RecyclerView.ViewHolder {
    BaseBindingViewHolder(View itemView) {
      super(itemView);
    }
  }

  public interface OnItemClickListener<M> {
    void onItemClick(int viewId, M item, int position);
  }

  public interface OnItemLongClickListener<M> {
    void onItemLongClick(int viewId, M item, int position);
  }
}
