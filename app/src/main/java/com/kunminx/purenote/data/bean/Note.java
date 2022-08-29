package com.kunminx.purenote.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.kunminx.architecture.utils.TimeUtils;
import com.kunminx.purenote.R;

/**
 * Created by KunMinX on 2015/7/31.
 */
@Entity
public class Note implements Parcelable {
  public final static int TYPE_TOPPING = 0x0001;
  public final static int TYPE_MARKED = 0x0002;

  @PrimaryKey
  @NonNull
  private String id = "";

  private String title = "";

  private String content = "";

  @ColumnInfo(name = "create_time")
  private long createTime;

  @ColumnInfo(name = "modify_time")
  private long modifyTime;

  private int type;

  @Ignore
  public String getCreateDate() {
    return TimeUtils.getTime(createTime, TimeUtils.YYYY_MM_DD_HH_MM_SS);
  }

  @Ignore
  public String getModifyDate() {
    return TimeUtils.getTime(modifyTime, TimeUtils.YYYY_MM_DD_HH_MM_SS);
  }

  @Ignore
  public boolean isMarked() {
    return (type & TYPE_MARKED) != 0;
  }

  @Ignore
  public boolean isTopping() {
    return (type & TYPE_TOPPING) != 0;
  }

  @Ignore
  public void toggleType(int param) {
    if ((type & param) != 0) {
      type = type & ~param;
    } else {
      type = type | param;
    }
  }

  @Ignore
  public int markIcon() {
    return isMarked() ? R.drawable.icon_star : R.drawable.icon_star_board;
  }

  @Ignore
  public Note() {
  }

  public Note(@NonNull String id, String title, String content, long createTime, long modifyTime, int type) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createTime = createTime;
    this.modifyTime = modifyTime;
    this.type = type;
  }

  @NonNull
  public String getId() {
    return id;
  }
  public String getTitle() {
    return title;
  }
  public String getContent() {
    return content;
  }
  public long getCreateTime() {
    return createTime;
  }
  public long getModifyTime() {
    return modifyTime;
  }
  public int getType() {
    return type;
  }

  protected Note(Parcel in) {
    id = in.readString();
    title = in.readString();
    content = in.readString();
    createTime = in.readLong();
    modifyTime = in.readLong();
    type = in.readInt();
  }

  public static final Creator<Note> CREATOR = new Creator<Note>() {
    @Override
    public Note createFromParcel(Parcel in) {
      return new Note(in);
    }

    @Override
    public Note[] newArray(int size) {
      return new Note[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(title);
    dest.writeString(content);
    dest.writeLong(createTime);
    dest.writeLong(modifyTime);
    dest.writeInt(type);
  }
}
