package com.example.allef.tad;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.allef.tad.utils.Util;

import java.util.Calendar;

public class Notify extends AppCompatActivity
{
    /** Referente ao componente de hora e minuto que interage com o usuário */
    private TimePicker clock;
    /** Dia que a notificação será feita */
    private String currentDay;
    /** Dados da tarefa */
    private String[] dataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        init();
    }

    /**
     * Chama todos os métodos necessários para iniciar a aplicação
     * Esse método deve ser chamado no onCreate
     */
    private void init()
    {
        iniciarTimePicker();
        verifyDataByIntent();
        changeTxtCurrentDay();
    }

    /**
     * Verifica se algum dado foi passado por intent
     */
    private void verifyDataByIntent()
    {
        Intent i = getIntent();
        String extra = i.getStringExtra("class");
        currentDay = i.getStringExtra("currentDay");

        if (null == extra) return;

        switch (extra) {
            case "create":
                dataTask = i.getStringArrayExtra("dataTask");
                break;
            default:
                break;
        }
    }

    /** Altera o componente de texto da data atual */
    private void changeTxtCurrentDay()
    {
        TextView txtCurrentDay = (TextView) findViewById(R.id.txtCurrentDay);
        txtCurrentDay.setText(currentDay);
    }

    /**
     * Inicia o componente de tempo com a hora e minuto atual
     */
    private void iniciarTimePicker()
    {
        clock = (TimePicker) findViewById(R.id.clock);

        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);

        clock.setIs24HourView(true);

        clock.setHour(currentHour);
        clock.setMinute(currentMinute);
    }

    /**
     * Pega a hora e o minuto inseridos pelo usuário
     * @return
     */
    public int[] getTimeAtClock()
    {
        int hour = clock.getHour();
        int minute = clock.getMinute();

        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        int actualyDay = c.get(Calendar.DAY_OF_MONTH);

        int cDay = Integer.parseInt(currentDay.substring(0, 2));

        // Verifica se o dia da notificação é igual ao dia atual real
        if ( cDay == actualyDay ) {
            if (hour < currentHour) {
                return new int[]{};
            } else if (hour == currentHour && minute < currentMinute){
                return new int[]{};
            }
        } else if ( cDay < actualyDay ) { // Verifica se o dia já passou
            return new int[]{};
        }

        return new int[]{ hour, minute };
    }

    /**
     * Cria uma notificação
     *
     * (Não utilizado)
     */
    public void criarNotificacaoSimples()
    {
        int id = 1;
        String titulo = "Tarefa";
        String texto = "Texto da notificação Simples";
        int icone = android.R.drawable.ic_menu_day;

        Intent intent = new Intent(this, UpdateTask.class);
        PendingIntent p = getPendingIntent(id, intent, this);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(p);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }

    /**
     *
     * @param id
     * @param intent
     * @param context
     * @return
     */
    private PendingIntent getPendingIntent(int id, Intent intent, Context context)
    {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);

        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    /**
     * Retorna o ID da tarefa
     *
     * @return
     */
    private String getId()
    {
        Intent i = getIntent();
        return  i.getStringExtra("taskId");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notify, menu);
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
            int[] hourMinute = getTimeAtClock();
            // Verifica se o horário é valido
            if (0 >= hourMinute.length) {
                Toast.makeText(this, "Dia ou horário já ultrapassado.", Toast.LENGTH_SHORT).show();
                return true;
            }

            String taskId = getId();
            Intent i = null;

            if ( null == taskId ) {
                i = new Intent(this, CreateTask.class);
                i.putExtra("dataTask", dataTask);
            } else {
                i = new Intent(this, UpdateTask.class);
            }

            i.putExtra("class", "notify");
            i.putExtra("hourMinute", hourMinute);
            i.putExtra("taskId", getId());
            i.putExtra("currentDay", currentDay);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
