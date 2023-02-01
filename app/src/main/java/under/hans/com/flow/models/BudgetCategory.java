package under.hans.com.flow.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class BudgetCategory implements Parcelable,Comparable<BudgetCategory> {

    String CategoryName;
    int CategoryPercentage;

    public BudgetCategory() {
    }

    public BudgetCategory(String categoryName, int categoryPercentage) {
        CategoryName = categoryName;
        this.CategoryPercentage = categoryPercentage;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getCategoryPercentage() {
        return CategoryPercentage;
    }

    public void setCategoryPercentage(int categoryPercentage) {
        CategoryPercentage = categoryPercentage;
    }

    public static Creator<BudgetCategory> getCREATOR() {
        return CREATOR;
    }

    protected BudgetCategory(Parcel in) {
        CategoryName = in.readString();
        CategoryPercentage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CategoryName);
        dest.writeInt(CategoryPercentage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BudgetCategory> CREATOR = new Creator<BudgetCategory>() {
        @Override
        public BudgetCategory createFromParcel(Parcel in) {
            return new BudgetCategory(in);
        }

        @Override
        public BudgetCategory[] newArray(int size) {
            return new BudgetCategory[size];
        }
    };

    @Override
    public int compareTo(@NonNull BudgetCategory budgetCategory) {
        int categoryPercentage = ((BudgetCategory)budgetCategory).getCategoryPercentage();

        return categoryPercentage - this.CategoryPercentage;
    }
}
