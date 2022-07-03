package com.kunminx.purenote.ui.adapter;

import android.content.Context;
import android.graphics.Color;

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
        return oldItem.equals(newItem);
      }

      @Override
      public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
        return oldItem.getModifyDate().equals(newItem.getModifyDate());
      }
    });
  }

  @Override
  protected void onBindItem(AdapterNoteListBinding binding, Note item, RecyclerView.ViewHolder holder) {
    binding.setNote(item);
    binding.btnMark.setImageResource(item.isMarked() ? R.drawable.ic_baseline_star : R.drawable.ic_baseline_star_border);
    binding.ivTopped.setImageResource(item.isTopping() ? R.drawable.ic_baseline_push_pin : Color.TRANSPARENT);
  }
}
