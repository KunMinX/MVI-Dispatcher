package com.kunminx.purenote.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.databinding.AdapterNoteListBinding;

/**
 * Create by KunMinX at 2022/7/3
 */
public class NoteAdapter extends BaseAdapter<Note, AdapterNoteListBinding> {

  public NoteAdapter() {
    super();
  }

  @Override
  protected void onBindingData(BaseHolder<AdapterNoteListBinding> holder, Note note, int position) {
    holder.getBinding().tvTitle.setText(note.title);
    holder.getBinding().cl.setClipToOutline(true);
    holder.getBinding().btnMark.setImageResource(note.isMarked() ? R.drawable.icon_star : R.drawable.icon_star_board);
    holder.getBinding().tvTime.setText(note.getModifyDate());
    holder.getBinding().tvTopped.setVisibility(note.isTopping() ? View.VISIBLE : View.GONE);
    holder.getBinding().tvTitle.setOnClickListener(v -> {
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
    holder.getBinding().btnMark.setOnClickListener(v -> {
      note.toggleType(Note.TYPE_MARKED);
      notifyItemChanged(position);
      notifyItemRangeChanged(position, 1);
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
    holder.getBinding().btnTopping.setOnClickListener(v -> {
      note.toggleType(Note.TYPE_TOPPING);
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
    holder.getBinding().btnDelete.setOnClickListener(v -> {
      notifyItemRemoved(position);
      getData().remove(position);
      notifyItemRangeRemoved(position, getData().size() - position);
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
  }

  @Override
  protected AdapterNoteListBinding onBindingView(ViewGroup viewGroup) {
    return AdapterNoteListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
  }
}
