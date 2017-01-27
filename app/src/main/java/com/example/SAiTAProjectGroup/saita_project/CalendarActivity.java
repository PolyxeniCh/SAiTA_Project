package com.example.SAiTAProjectGroup.saita_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setFirstDayOfWeek(2);

        Calendar cal = Calendar.getInstance();
        calendar.setMinDate(cal.getTimeInMillis());

        int maxDay = cal.get(Calendar.DAY_OF_MONTH)-4;
        int maxMonth = cal.get(Calendar.MONTH)+1;
        int maxYear = cal.get(Calendar.YEAR)+1;
        try {
            String maxDateString = maxYear + "-" + maxMonth + "-" + maxDay;
            Date date = format.parse(maxDateString);
            long maxDate = date.getTime();
            calendar.setMaxDate(maxDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("DEPARTURE_DATE")) {
                String departureDate = (String) intent.getStringExtra("DEPARTURE_DATE");
                try {
                    long date = format.parse(departureDate).getTime();
                    calendar.setDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Intent intent = new Intent();
                String dateString = year + "-" + (month+1) + "-" + day;
                try {
                    Date date = format.parse(dateString);
                    dateString = format.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent.putExtra("DATE", dateString);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
