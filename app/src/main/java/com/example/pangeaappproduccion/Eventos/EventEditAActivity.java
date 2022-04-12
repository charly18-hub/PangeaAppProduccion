package com.example.pangeaappproduccion.Eventos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pangeaappproduccion.Calendarios.CalendarUtils;
import com.example.pangeaappproduccion.R;

import java.time.LocalTime;

public class EventEditAActivity extends AppCompatActivity

    {
        private EditText eventNameET;
        private TextView eventDateTV, eventTimeTV;

        private LocalTime time;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_edit_aactivity);
            initWidgets();
            time = LocalTime.now();
            eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
            eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
        }

        private void initWidgets()
        {
            eventNameET = findViewById(R.id.eventNameET);
            eventDateTV = findViewById(R.id.eventDateTV);
            eventTimeTV = findViewById(R.id.eventTimeTV);
        }

        public void saveEventAction(View view)
        {
            String eventName = eventNameET.getText().toString();
            Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
            Event.eventsList.add(newEvent);
            finish();
        }
    }