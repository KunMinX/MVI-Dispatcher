package com.kunminx.purenote.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.binding_recyclerview.adapter.SimpleDataBindingAdapter;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.databinding.AdapterNoteListBinding;

/**
 * Create by KunMinX at 2022/7/3
 */
public class NoteAdapter extends SimpleDataBindingAdapter<Note, AdapterNoteListBinding> {

  public NoteAdapter(Context context) {
    super(context, R.layout.adapter_note_list, new DiffUtil.ItemCallback<Note>() {
      @Override
      public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
        return oldItem.id.equals(newItem.id);
      }
      @Override
      public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
        return oldItem.title.equals(newItem.title) &&
                oldItem.content.equals(newItem.content) &&
                oldItem.type == newItem.type;
      }
    });
  }

  @Override
  protected void onBindItem(AdapterNoteListBinding binding, Note note, RecyclerView.ViewHolder holder) {
    binding.setNote(note);
    int position = holder.getBindingAdapterPosition();
    binding.cl.setOnClickListener(v -> {
      if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v.getId(), note, position);
    });
    binding.btnMark.setOnClickListener(v -> {
      note.toggleType(Note.TYPE_MARKED);
      notifyItemChanged(position);
      notifyItemRangeChanged(position, 1);
      if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v.getId(), note, position);
    });
    binding.btnTopping.setOnClickListener(v -> {
      note.toggleType(Note.TYPE_TOPPING);
      if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v.getId(), note, position);
    });
    binding.btnDelete.setOnClickListener(v -> {
      notifyItemRemoved(position);
      getCurrentList().remove(position);
      notifyItemRangeRemoved(position, getCurrentList().size() - position);
      if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v.getId(), note, position);
    });
  }
}
