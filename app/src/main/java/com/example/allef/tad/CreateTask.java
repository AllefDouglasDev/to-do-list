package com.example.allef.tad;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allef.tad.controller.TaskController;
import com.example.allef.tad.dao.TaskDAO;
import com.example.allef.tad.utils.Util;

public class CreateTask extends AppCompatActivity
{
    /** Componentes de input do xml */
    private EditText eTxtTarefa, eTxtDescricao;
    /** Componente de level de importância no xml */
    private Spinner dpdImportancia;
    /** Se deve criar dados de notificação */
    private boolean notify;
    /** Data para criação da tarefa */
    private String currentDay;
    /** Hora e minuto para notificar */
    private int[] hourMinuteToNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Criar Tarefa");

        init();
    }

    /**
     * Chama todos os métodos necessários para iniciar a aplicação
     * Esse método deve ser chamado no onCreate
     */
    private void init()
    {
        notify = false;

        addAdapterInSpinner();
        btnNotify();
        verifyDataByIntent();
    }

    /**
     * Verifica se algum dado foi passado pela intent
     */
    private void verifyDataByIntent()
    {
        Intent i = getIntent();
        String extra = i.getStringExtra("class");
        currentDay = i.getStringExtra("currentDay");

        if (null == extra) return;

        switch (extra) {
            case "notify":
                notify = true;
                hourMinuteToNotify = i.getIntArrayExtra("hourMinute");
                TextView txtNotify = (TextView) findViewById(R.id.txtNotify);
                String textNotify = (hourMinuteToNotify[1] >= 10 ) ? "Notificar de " + hourMinuteToNotify[0] + ":" + hourMinuteToNotify[1] : "Notificar de " + hourMinuteToNotify[0] + ":0" + hourMinuteToNotify[1];
                txtNotify.setText(textNotify);
                insertDataInput(i.getStringArrayExtra("dataTask"));
                break;
            default:
                break;
        }
    }

    /**
     * Preenche os campos de input após retornar da activity notify
     *
     * @param dataTask
     */
    private void insertDataInput(String[] dataTask)
    {
        eTxtTarefa = (EditText) findViewById(R.id.eTxtTarefa);
        eTxtDescricao = (EditText) findViewById(R.id.eTxtDescricao);
        dpdImportancia = (Spinner) findViewById(R.id.importancia);

        eTxtTarefa.setText(dataTask[0]);
        eTxtDescricao.setText(dataTask[1]);

        int importancia = dataTask[2].equals("Alta") ? 1 : 0;

        dpdImportancia.setSelection(importancia);
    }

    /** Adiciona os itens no Adapter, e o Adapter no Spinner */
    private void addAdapterInSpinner()
    {
        // Pegando o spinner do xml.
        dpdImportancia = findViewById(R.id.importancia);
        //  uma lista com os itens do spinner.
        String[] items = new String[]{"Normal","Alta"};
        // Criando um adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        // Alterando o adaptador
        dpdImportancia.setAdapter(adapter);
    }

    /** Botão direcionando para a Activity Notify */
    private void btnNotify()
    {
        Button btnNotify = (Button) findViewById(R.id.btnNotify);

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateTask.this, Notify.class);
                i.putExtra("class", "create");
                i.putExtra("currentDay", currentDay);
                i.putExtra("dataTask", getInputValueByXml());
                startActivity(i);
            }
        });
    }

    /** Retorna os valores inseridos pelo usuário */
    private String[] getInputValueByXml()
    {
        eTxtTarefa = (EditText) findViewById(R.id.eTxtTarefa);
        eTxtDescricao = (EditText) findViewById(R.id.eTxtDescricao);
        dpdImportancia = (Spinner) findViewById(R.id.importancia);

        String[] values = new String[3];

        values[0] = eTxtTarefa.getText().toString();
        values[1] = eTxtDescricao.getText().toString();
        values[2] = dpdImportancia.getSelectedItem().toString();

        return values;
    }

    /**
     * Cria uma tarefa e informa ao usuário com uma mensagem de sucesso
     * Retorna para a Activity MainActivity
     */
    private void createTask()
    {
        String[] values = getInputValueByXml();
        Intent i = new Intent(CreateTask.this, MainActivity.class);

        if (values[0].isEmpty()) {
            startActivity(i);
            return;
        }

        TaskController tc = new TaskController();

        int taskId = (int) tc.createTask(values[0], values[1], values[2], currentDay);

        if ( notify ) {
            String dayToNotify = currentDay + " " + hourMinuteToNotify[0] + ":" + hourMinuteToNotify[1];
            tc.createNotification(dayToNotify, taskId);
            Util util = new Util();
            util.notifyUser(this, taskId, hourMinuteToNotify[0], hourMinuteToNotify[1]);
        }

        if (-1 != taskId) {
            Toast.makeText(this, "Tarefa criada com sucesso", Toast.LENGTH_SHORT).show();

            i.putExtra("class", "create");
            i.putExtra("currentDay", currentDay);

            startActivity(i);
        } else {
            Toast.makeText(this, "Erro ao criar tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create) {
            createTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
