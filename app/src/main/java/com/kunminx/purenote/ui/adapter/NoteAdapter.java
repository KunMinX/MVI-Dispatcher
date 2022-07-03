package com.kunminx.purenote.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
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
    holder.getBinding().btnMark.setImageResource(note.isMarked() ? R.drawable.ic_baseline_star : R.drawable.ic_baseline_star_border);
    holder.getBinding().ivTopped.setImageResource(note.isTopping() ? R.drawable.ic_baseline_pin : Color.TRANSPARENT);
    holder.getBinding().tvTitle.setOnClickListener(v -> {
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
    holder.getBinding().btnMark.setOnClickListener(v -> {
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
    holder.getBinding().btnTopping.setOnClickListener(v -> {
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
    holder.getBinding().btnDelete.setOnClickListener(v -> {
      if (listener != null) listener.onItemClick(v.getId(), position, note);
    });
  }

  @Override
  protected AdapterNoteListBinding onBindingView(ViewGroup viewGroup) {
    return AdapterNoteListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
  }
}
