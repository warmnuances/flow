package under.hans.com.flow.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Hans on 2/3/2018.
 */

public class Transaction implements Parcelable, Comparable<Transaction> {

    //TimeLine Needed, uri,name,day,category

    public String name,day,category,month;
    public int amount,timeMillis,type;
    public Uri mUri;


    public Transaction() {
    }

    public Transaction(String name, String day, String category, String month, int amount, int timeMillis, int type, Uri mUri) {
        this.name = name;
        this.day = day;
        this.category = category;
        this.month = month;
        this.amount = amount;
        this.timeMillis = timeMillis;
        this.type = type;
        this.mUri = mUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(int timeMillis) {
        this.timeMillis = timeMillis;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Uri getmUri() {
        return mUri;
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    public static Creator<Transaction> getCREATOR() {
        return CREATOR;
    }

    protected Transaction(Parcel in) {
        name = in.readString();
        day = in.readString();
        category = in.readString();
        month = in.readString();
        amount = in.readInt();
        timeMillis = in.readInt();
        type = in.readInt();
        mUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(day);
        parcel.writeString(category);
        parcel.writeString(month);
        parcel.writeInt(amount);
        parcel.writeInt(timeMillis);
        parcel.writeInt(type);
        parcel.writeParcelable(mUri, i);
    }

    @Override
    public int compareTo(Transaction transaction) {
        int timeMillis = ((Transaction)transaction).getTimeMillis();

        return timeMillis - this.timeMillis;
    }

    /**
     * public int compareTo(Fruit compareFruit) {

     int compareQuantity = ((Fruit) compareFruit).getQuantity();

     //ascending order
     return this.quantity - compareQuantity;

     //descending order
     //return compareQuantity - this.quantity;

     }
     */
}
