package com.kunminx.purenote.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.ui.adapter.BaseBindingAdapter;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.databinding.AdapterNoteListBinding;

import java.util.List;

/**
 * Create by KunMinX at 2022/7/3
 */
public class NoteAdapter extends BaseBindingAdapter<Note, AdapterNoteListBinding> {

  public NoteAdapter(List<Note> list) {
    super(list);
  }

  @Override
  protected int getLayoutResId(int viewType) {
    return R.layout.adapter_note_list;
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
      getList().remove(position);
      notifyItemRangeRemoved(position, getList().size() - position);
      if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v.getId(), note, position);
    });
  }

  @Override
  public int getItemCount() {
    return getList().size();
  }
}
