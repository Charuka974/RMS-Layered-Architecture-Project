package org.gourmetDelight.util;

import javafx.animation.AnimationTimer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

public class DateAndTime {
    public static String dateNow() {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        return format.format(new Date());
    }

    public static String yearNow() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date());
    }

    public static int getDays(int year, int month) {
        YearMonth yearMonthObject = YearMonth.of(year, month);
        return yearMonthObject.lengthOfMonth();

    }

    public static String monthNow() {
        LocalDate localDate = LocalDate.now();
        return String.valueOf(localDate.getMonth());
    }

    public static String timeNow() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(new Date()) ;

    }
    public static String timeNowForName() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH_mm");
        return dateFormat.format(new Date()) ;
    }

    public  void displayTime() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat time =  new SimpleDateFormat("HH:mm:ss");


            }
        };

        timer.start();

    }

    public String addDate(){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        return date.format(new Date());
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }


}
