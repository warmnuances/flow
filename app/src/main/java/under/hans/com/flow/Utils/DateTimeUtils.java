package under.hans.com.flow.Utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import under.hans.com.flow.Adapters.CategoryAdapter;

/**
 * Created by Hans on 3/5/2018.
 */

public class DateTimeUtils {

    /** Contents
     * Method 1 getCurrentSecMillis : (Algorithm) change current time(hour,minute,second) into timeMillis
     * Method 2 formatDatetoSec: (Database) store time and date in timeMillis format in database
     * Method 3 formatTimetoString: (UI) return timeMillis to Normal Format (String)
     * Method 4 formatDatetoString: (UI) return dateMillis to Normal Format (String)
     * Method 5 getCurrentDate: (Init) get current date in normal format
     * Method 6 getCurrentTime: (Init) get current time in normal format
     * Method 7 getFormattedDate: (UI) get current date from datePicker and set to normal format
     * Method 8 getFormattedTime: (UI) get current date from timePicker and set to normal format
     * Method 9 getMonthName : (UI) convert month number to name
     * Method 10 convertDateToMillis: (Database) convert date into timeMillis
     * Method 11 getNextMonthInterval : (Algorithm) get timeSeconds from this month to next
     * Method 12 calculateInterval:(Algorithm) get recurrence and calculate interval
     * Method 13 getNumberDaysInAMonth:(Algorithm) get number of days in a month
     * Method 14 getCurrentHour : get Current Hour
     * Method 15 getMonthInMillis : Used for getting last month in Milliseconds
     * Method 16 getNextMonthInMillis : Used for getting next month in Milliseconds
     * Method 17
     */

    private static final String TAG = "DateTimeUtils";


    /** Method 1
     * return today's time in millis
     * If user never set time, this time will be used as default
     * @return total
     */

    public static int getCurrentSecMillis(){
        Calendar calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        int mSec = calendar.get(Calendar.SECOND);

        int total = ((mHour * 3600) + (mMinute * 60) + (mSec))/1000;

        return total;
    }


    /** Method 2
     * format date and time to millis
     * Used in Add Activities after user picked their dates
     * @param time
     * @param date
     * @return
     */
    public static int formatDatetoSec(String time, String date){

        long timeMillis;
        int returnMillis;

        Calendar calendar = Calendar.getInstance();

        String strConcat = time + " " + date + ":" + calendar.get(Calendar.SECOND);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            calendar.setTime(dateFormat.parse(strConcat));
            timeMillis = calendar.getTimeInMillis()/1000;
            returnMillis = (int)timeMillis;

            return returnMillis;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }



    /** Method 3
     * return time in millis format to normal format
     *
     * @param time
     * @return
     */
    public static String formatTimetoString(long time){
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
        Date now = new Date(time);
        String strDate = sdfDate.format(now);

        return strDate;
    }

    /** Method 4
     * return Date in millis format to normal format
     * @param date
     * @return
     */
    public static String formatDatetoString(long date){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");//dd/MM/yyyy
        Date now = new Date(date*1000);
        String strDate = sdfDate.format(now);

        return strDate;
    }


    /** Method 5
     * return current date in dd/MM/YYYY format e.g 03/05/2018
     * Used to for widget.setText()
     * @return
     */
    public static String getCurrentDate(){
        Date date = new Date();
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateRes = outputFormat.format(date);

        return dateRes;
    }

    /** Method 6
     * return current date in HH:mm format e.g 06:55
     * Used to for widget.setText()
     * @return
     */
    public static String getCurrentTime(){
        Date date = new Date();
        DateFormat outputFormat = new SimpleDateFormat("HH:mm");
        String dateRes = outputFormat.format(date);

        return dateRes;
    }

    /** Method 7
     * format the date when individual values are given: Year, Month, Day
     * Used in DatePicker where year,month and day are individual values
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getFormattedDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        Date date;
        date = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String result= dateFormat.format(date);

        return result;
    }

    /** Method 8
     * format the date when individual values are given: Hour, Minute
     * Used in TimePicker where year, month and day are individual values
     * @param hour
     * @param minute
     * @return
     */
    public static String getFormattedTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);

        Date time;
        time = calendar.getTime();

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String result= timeFormat.format(time);

        return result;
    }

    /** Method 9
     * Used in TimeLine Recyclerview to convert month to name
     * e.g Month = 3 Output = Mar
     * @param month
     * @return
     */
    public static String getMonthName(int month){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);

        Date date = calendar.getTime();

        DateFormat df = new SimpleDateFormat("MMM");

        String result = df.format(date);

        return result;
    }

    /** Method 10
     * Used in Preset, converts date to secMillis
     * e.g 3/10/2018 --> 1520683713123
     */

    public static int convertDateToIntMillis(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        long timeMillis;
        int dateinTimeMillis;

        try {
            Date newDate = sdf.parse(date);
            timeMillis = newDate.getTime()/1000;
            dateinTimeMillis = (int)timeMillis + getCurrentSecMillis();

            return dateinTimeMillis;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    /** Method 10
     * Used in Preset, converts date to secMillis
     * e.g 3/10/2018 --> 1520683713123
     */
    public static long convertDateToLongMillis(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        long timeMillis;

        try {
            Date newDate = sdf.parse(date);
            timeMillis = newDate.getTime()/1000 + getCurrentSecMillis();

            return timeMillis;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    /** Method 11
     *  Used in Preset set notification
     *  e.g. This month = january
     */

    public static long getNextMonthInterval(){

        String strDate = getCurrentDate();

        int days = 0;
        long interval = 0L;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = sdf.parse(strDate);
            calendar.setTime(date);


            days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            interval = (days*(3600*24));


        }catch (Exception e){
            e.printStackTrace();
        }

        return interval;
    }

    /** Method 12
     *  Used in Preset set notification custom
     *  e.g. This month = january
     */

    public static long calculateInterval(String constant, int multiplier , String startDate){


        long varConstant,varInterval;

        if(constant.equals("Day")){
            varConstant = 86400L;
            varInterval = varConstant * (long)multiplier;
            return varInterval;

        }else if(constant.equals("Week")){
            varConstant = 86400L * 7 ;
            varInterval = varConstant * (long)multiplier;
            return varInterval;

        }else if(constant.equals("Month")){
            varConstant = getNumberDaysInAMonth(startDate,multiplier);
            varInterval = (varConstant * 86400L);
            return varInterval;
        }else {
            return 12345;
        }

    }

    /** Method 13
     *  Convert String to date
     *  e.g. This month = january
     */
    public static int getNumberDaysInAMonth(String strDate,int multiplier){

        int totalDays=0;
        int daysInMonth;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();


        try {
            Date date = sdf.parse(strDate);
            calendar.setTime(date);


            for(int i = 2  ; i < (multiplier + 2) ; i++){

                if(calendar.get(Calendar.MONTH) == 11){
                    calendar.add(Calendar.MONTH,-12);
                    calendar.add(Calendar.YEAR,1);
                }


                daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                totalDays = totalDays + daysInMonth;

                calendar.add(Calendar.MONTH,1);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return totalDays;
    }

    /**Method 14
     * get current Hour of the day
     * @return
     */
    public static int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);

        return mHour;
    }
    /** Method 15
     *  get Last month in milliseconds
     */
    public static int getLastMonthMillis(){

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)-1;
        int year = calendar.get(Calendar.YEAR);
        int day = 1;
        calendar.clear();
        calendar.set(year,month,day);
        int thisMonth = (int)(calendar.getTimeInMillis()/1000);

        return thisMonth;
    }
    /** Method 16
     *  get this month in milliseconds
     */
    public static int getThisMonthMillis(){

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) ;
        int year = calendar.get(Calendar.YEAR);
        int day = 1;
        calendar.clear();
        calendar.set(year,month,day);
        int thisMonth = (int)(calendar.getTimeInMillis()/1000);

        return thisMonth;
    }

    /** Method 16
     *  get this month in milliseconds
     */
    public static int getNextMonthMillis(){

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1 ;
        int year = calendar.get(Calendar.YEAR);
        int day = 1;
        calendar.clear();
        calendar.set(year,month,day);
        int thisMonth = (int)(calendar.getTimeInMillis()/1000);

        return thisMonth;
    }

    public static int getMonthMillis(int position){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + position ;
        int year = calendar.get(Calendar.YEAR);
        int day = 1;
        calendar.clear();
        calendar.set(year,month,day);
        int thisMonth = (int)(calendar.getTimeInMillis()/1000);

        return thisMonth;
    }


    /** Method 17
     *  get Month/Year in String e.g September 2018
     */
    public static String getMonthYearString(int position){
        String res;

        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int month = c.get(Calendar.MONTH ) + position;
        c.set(Calendar.MONTH,month);



        DateFormat df = new SimpleDateFormat("MMMM yyyy");
        res = df.format(c.getTime());

        return res;
    }
    /** Method 19
     * Convert Millis to Date
     * e.g 1520683713123 --> 3/10/2018
     */
    public static String convertMillisToDate(int millis){
        long timeStamp =(long)millis *1000;


        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        date.setTime(timeStamp);

        String result =df.format(date);

        return result;
    }




    /** Method 19
     * Used in Preset, converts date to secMillis
     * e.g 3/10/2018 --> 1520683713123
     */

    public static int convertStartMonthMillis(String date){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        long timeMillis;
        int dateinTimeMillis;

        try {
            Date currDate = sdf.parse(date);
            c.setTime(currDate);
            int month = c.get(Calendar.MONTH);

            c.set(Calendar.MONTH, month);

            Date newDate = c.getTime();
            timeMillis = newDate.getTime()/1000;
            dateinTimeMillis = (int)timeMillis + getCurrentSecMillis();

            return dateinTimeMillis;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public static int convertEndMonthMillis(String date){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        long timeMillis;
        int dateinTimeMillis;

        try {
            Date currDate = sdf.parse(date);
            c.setTime(currDate);
            int month = c.get(Calendar.MONTH) + 1;

            c.set(Calendar.MONTH, month);

            Date newDate = c.getTime();
            timeMillis = newDate.getTime()/1000;
            dateinTimeMillis = (int)timeMillis + getCurrentSecMillis();

            return dateinTimeMillis;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public static int getDayFromMillis(int timeSec){

        long intMillis = (long)timeSec*1000;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(intMillis);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return day;
    }

    public static int getDaysInThisMonth(){
        Calendar calendar = Calendar.getInstance();
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }


    public static int trendsDateConversion(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        long timeMillis;
        int dateinTimeMillis;

        try {
            Date newDate = sdf.parse(date);
            timeMillis = newDate.getTime()/1000;
            dateinTimeMillis = (int)timeMillis;

            return dateinTimeMillis;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public static int getMonthInterval(String startDate,int multiplier){

        int daysFromStartMonth = 0;
        Calendar calendar = Calendar.getInstance();
        DateFormat df =new SimpleDateFormat("dd/MM/yyyy");

        try {
            calendar.setTime(df.parse(startDate));

            int getMonth = calendar.get(Calendar.MONTH);

            for(int i = 0; i<multiplier;i++) {
                int setMonth = getMonth + i;
                calendar.set(Calendar.MONTH,setMonth);

                daysFromStartMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + daysFromStartMonth;

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return daysFromStartMonth;
    }


}
