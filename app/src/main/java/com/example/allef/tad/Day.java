package com.example.allef.tad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.util.Calendar;

public class Day extends AppCompatActivity
{
    /** Referente ao componente de insersão de data que interage com o usuário */
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        init();
    }

    /**
     * Chama todos os métodos necessários para iniciar a aplicação
     * Esse método deve ser chamado no onCreate
     */
    private void init()
    {
        iniciarDatePicker();
    }

    /**
     * Inicia o dataPicker com a data atual
     */
    private void iniciarDatePicker()
    {
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePicker.OnDateChangedListener changedListener = new DatePicker.OnDateChangedListener() { public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {}};

        datePicker.init(year, month, day, changedListener);
    }

    /**
     * Pega a data selecionada pelo usuário
     *
     * @return
     */
    public String getData() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        StringBuilder stringBuilder = new StringBuilder();
        if (day < 10) {
            stringBuilder.append("0").append(day).append("-");
        } else {
            stringBuilder.append(day).append("-");
        }

        if (month < 10) {
            stringBuilder.append("0").append(month).append("-").append(year);
        } else {
            stringBuilder.append(month).append("-").append(year);
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_confirm) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("class", "day");
            i.putExtra("currentDay", getData());

            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
