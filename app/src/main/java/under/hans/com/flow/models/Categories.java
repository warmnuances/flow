package under.hans.com.flow.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Hans on 3/31/2018.
 */

public class Categories implements Comparable<Categories>,Parcelable {

    public String categoryName;
    public int compareSort;
    public int pathRes;

    public Categories() {
    }

    public Categories(String categoryName, int compareSort, int pathRes) {
        this.categoryName = categoryName;
        this.compareSort = compareSort;
        this.pathRes = pathRes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCompareSort() {
        return compareSort;
    }

    public void setCompareSort(int compareSort) {
        this.compareSort = compareSort;
    }

    public int getPathRes() {
        return pathRes;
    }

    public void setPathRes(int pathRes) {
        this.pathRes = pathRes;
    }

    public static Creator<Categories> getCREATOR() {
        return CREATOR;
    }

    protected Categories(Parcel in) {
        categoryName = in.readString();
        compareSort = in.readInt();
        pathRes = in.readInt();
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeInt(compareSort);
        parcel.writeInt(pathRes);
    }

    @Override
    public int compareTo(@NonNull Categories categories) {
        return 0;
    }
}