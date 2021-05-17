package es.fdi.stickerlab;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

class Sticker implements Parcelable {
    final String imageFileName;
    long size;

    Sticker(String imageFileName) {
        this.imageFileName = imageFileName;
    }
  
  private Sticker(Parcel in) {
        imageFileName = in.readString();
        size = in.readLong();
    }

   public void setSize(long size) {
        this.size = size;
    }
  
   public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        @Override
        public Sticker createFromParcel(Parcel in) {
            return new Sticker(in);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };
  
  @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageFileName);
        //dest.writeStringList(emojis);
        dest.writeLong(size);
    }
}
