package com.example.allef.tad;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.example.allef.tad.model.Notification;
import com.example.allef.tad.model.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UpdateTask extends AppCompatActivity
{
    /** Componentes de input do xml */
    private EditText eTxtTarefa, eTxtDescricao;
    /** Componente de level de importância no xml */
    private Spinner dpdImportancia;
    /** Componente de notificação no xml */
    private TextView txtNotify;
    /** Se deve criar dados de notificação */
    private boolean notify;
    /** Atributo para apresentar o dialog de confirmação de deleção */
    private AlertDialog alerta;
    /** Hora e minuto para notificar */
    private int[] hourMinuteToNotify;
    /** Data para notificar */
    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Alterar Tarefa");

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
        insertDataInFields();
        verifyDataByIntent();
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
            case "notify":
                notify = true;
                hourMinuteToNotify = i.getIntArrayExtra("hourMinute");
                TextView txtNotify = (TextView) findViewById(R.id.txtNotify);
                String textNotify = (hourMinuteToNotify[1] >= 10 ) ? "Notificar às " + hourMinuteToNotify[0] + ":" + hourMinuteToNotify[1] : "Notificar às " + hourMinuteToNotify[0] + ":0" + hourMinuteToNotify[1];
                txtNotify.setText(textNotify);
                break;
            default:
                break;
        }
    }

    /** Adiciona os itens no Adapter, e o Adapter no Spinner */
    private void addAdapterInSpinner()
    {
        // Pegando o spinner do xml.
        dpdImportancia = findViewById(R.id.importancia);
        // Criando uma lista com os itens do spinner.
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
                Intent i = new Intent(UpdateTask.this, Notify.class);
                i.putExtra("taskId", Integer.toString(getId()));
                System.out.println("Passando a data: " + currentDay);
                i.putExtra("currentDay", currentDay);
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
     * Altera a tarefa escolhida
     * Retorna para a Activity MainActivity
     */
    private void updateTask()
    {
        String[] values = getInputValueByXml();

        Intent i = new Intent(UpdateTask.this, MainActivity.class);

        TaskController tc = new TaskController();
        if (values[0].isEmpty()) {
            startActivity(i);
            return;
        }

        if ( notify ) {
            String dayToNotify = currentDay + " " + hourMinuteToNotify[0] + ":" + hourMinuteToNotify[1];
            tc.updateNotification(dayToNotify, getId());
        }

        if ( tc.updateTask(getId(), values[0], values[1], values[2]) ) {
            i.putExtra( "class", "update" );
            i.putExtra( "currentDay", currentDay );
            startActivity(i);
        } else {
            Toast.makeText(this, "Erro ao alterar tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deleta a tarefa em questão
     * Retorna para a Activity MainActivity
     */
    private void deleteTask()
    {
        Intent i = new Intent(UpdateTask.this, MainActivity.class);

        TaskController tc = new TaskController();

        tc.deleteTask(getId());
        i.putExtra("class", "update" );
        i.putExtra( "currentDay", currentDay );
        startActivity(i);
    }

    /**
     * Dialog para deletar uma tarefa
     */
    private void showDialog()
    {
        // Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar tarefa");
        builder.setMessage("Deseja mesmo deletar essa tarefa?");
        // Define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                deleteTask();
                Toast.makeText(UpdateTask.this, "Tarefa deletada", Toast.LENGTH_SHORT).show();
            }
        });
        // Define um botão como negativo
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(UpdateTask.this, "Execução cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    /**
     * Retorna o ID da tarefa
     *
     * @return
     */
    private int getId()
    {
        Intent i = getIntent();
        return  Integer.parseInt(i.getStringExtra("taskId"));
    }

    /**
     * Preenche os campos com os dados da tarefa
     */
    private void insertDataInFields()
    {
        getInputValueByXml();

        TaskController tc = new TaskController();
        ArrayList<Task> task = tc.show(getId());

        Notification n = tc.showNotification(getId());

        eTxtTarefa.setText(task.get(0).getName());
        eTxtDescricao.setText(task.get(0).getComment());

        int importancia = task.get(0).getLevel().equals("Alta") ? 1 : 0;

        dpdImportancia.setSelection(importancia);

        if (null != n.getDateToNotify()) {
            txtNotify = (TextView) findViewById(R.id.txtNotify);
            String dateToNotify = "Notificar de: " + n.getDateToNotify().substring( n.getDateToNotify().length() - 5, n.getDateToNotify().length());
            txtNotify.setText(dateToNotify);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            updateTask();
            return true;
        }

        if (id == R.id.action_delete) {
            showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
