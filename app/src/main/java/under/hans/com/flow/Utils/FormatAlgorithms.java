package under.hans.com.flow.Utils;

import android.content.Context;
import android.util.Log;

import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.R;

/**
 * Created by Hans on 1/5/2018.
 */

public class FormatAlgorithms {
    private static final String TAG = "FormatAlgorithms";

    /** Contents
     *  Method 1 setFormattedFunds : used in storing value in database
     *  Method 2 getFormattedFunds : used in timeline
     *  Method 3 convertString : used in timeline
     *  Method 4 setFormattedFunds :
     */


    /**
     * You put in 11 bucks becomes 1100 in the database
     * @param funds
     * @return dbfunds
     */
    public static int setFormattedFunds(String funds){
        Double parseMoney = Double.parseDouble(funds);
        Double convertMoney = new Double(parseMoney);
        Double money = convertMoney * 100;

        int dbfunds = money.intValue();

        return dbfunds;
    }

    /** Used in Chart Entries
     *
     */

    public static float getFloatfromInt(int intValue){

        float fltResult;

        fltResult = (float) intValue/100;
        return fltResult;
    }

    /**
     * Database funds record :1100 convert to string 11.00 bucks
     * @param funds
     * @return
     */
    public static String getFormattedFunds(int funds){
        String money = String.valueOf(funds);

        return convertString(money);
    }

    public static String convertString(String text){


        int length = text.length();

        if(length >=3){
            int offset = length - 2;
            String updatedText;

            StringBuilder string = new StringBuilder(text);
            string.insert(offset,".");
            updatedText = string.toString();

            return updatedText;
        }
        else {
            return "0";
        }
    }

    /** Update Balance if user spends
     *  e.g balance - spendings in double
     * @param ttlBalance
     * @param spendings
     * @return
     */
    public static int updateBalanceSpendings(int ttlBalance, int spendings){
        Double totalBalance = 0.00;
        int returnBalance;

        String strBal = String.valueOf(ttlBalance);
        String strSpendings = String.valueOf(spendings);

        Double dblBal = Double.parseDouble(strBal);
        Double dblSpendings = Double.parseDouble(strSpendings);

        Double convertBal = new Double(dblBal);
        Double Balance = convertBal * 100;

        Double convertSpendings = new Double(dblSpendings);
        Double Spendings = convertSpendings * 100;

        totalBalance = Balance - Spendings;
        returnBalance = totalBalance.intValue();

        return returnBalance;
    }

    public static long parseConstantToInt(String constant){
        long day = 86400L;
        long week = 604800L;
        long month = 2592000L;

        if(constant.equals("Day")){
            return day;
        }else if(constant.equals("Week")){
            return week;
        }else if(constant == "Month"){
            return month;
        }else {
            return 0;
        }
    }

    public static int getCategoryPriority(int usage, int priority, boolean isTimePriority){

        int totalPriority = 0;
        int timePriority = 30 ;
        int constPriority = usage*2 + (priority +8);

        if(isTimePriority == true){
            totalPriority = timePriority + constPriority;

        }else {
            totalPriority = timePriority;
        }

        return totalPriority;

    }

    public static Boolean checkPriority(Context context,String name){

        Boolean checkP;

        if(name == getString(context,R.string.Food_and_beverage)){

            switch (DateTimeUtils.getCurrentHour()){

                case 11:checkP = true;
                    break;

                case 12:checkP = true;
                    break;

                case 18:checkP = true;
                    break;

                case 19:checkP = true;
                    break;

                case 20:checkP = true;
                    break;

                default:checkP = false;
                    break;
            }

        }


        else {
            checkP= false;
        }


        return checkP;

    }

    public static String getString(Context context,int intValue){
        String res = context.getResources().getString(intValue);

        return res;
    }

}
