package com.example.allef.tad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allef.tad.controller.TaskController;
import com.example.allef.tad.dao.CreateDatabase;
import com.example.allef.tad.dao.TaskDAO;
import com.example.allef.tad.model.Task;
import com.example.allef.tad.utils.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    /** lista das tarefas */
    private ArrayList<Task> taskList;
    /** Dia das tarefas */
    private String currentDay;
    /** Se existe algum filtro */
    private boolean hasFilter;
    /** Filtros para as tarefas */
    private boolean[] filters;

    private final ArrayList<String> selecionados = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o arquivo /layout/main.xml como layout principal da aplicação
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.v("minhaThread", "." + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /** Inicia variáveis e chama todos os métodos necessários */
    private void init()
    {
        //new TaskDAO().removeTable("task");
        new CreateDatabase().createTable();

        hasFilter = false;
        Util util = new Util();
        currentDay = util.getCurrentDate();

        verifyIntentExtras();

        TextView txtDate = (TextView) findViewById(R.id.txtDate);
        txtDate.setText(currentDay);

        inflateListTask();

        listView();

        floatingBtn();
    }

    /** Botão para adicionar uma nova tarefa */
    private void floatingBtn()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateTask.class);
                i.putExtra("currentDay", currentDay);
                startActivity(i);

                Snackbar.make(view, "Adicionar tarefa", Snackbar.LENGTH_SHORT)
                        .setAction("Adicionar", null).show();
            }
        });
    }

    /**
     * Busca as tarefas no banco e adiciona elas a lista
     */
    private void inflateListTask()
    {
        TaskController tc = new TaskController();
        if (!hasFilter) {
            taskList = tc.showByDate(currentDay);
        } else {
            taskList = tc.filterTasks(currentDay, filters);
        }

        System.out.println(taskList);

        if (null == taskList) taskList = new ArrayList<>();

        if (taskList.size() == 0) {
            RelativeLayout emptyList = (RelativeLayout) findViewById(R.id.emptyList);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Inicia a ListView
     */
    private void listView()
    {
        ListView listView = (ListView) findViewById(R.id.listTask);

        CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);
    }

    /** Verifica dados passados por outras Activity's */
    private void verifyIntentExtras()
    {
        Intent i = getIntent();
        String extra = i.getStringExtra("class");

        if (null == extra) return;


        switch (extra) {
            case "filter":
                currentDay = i.getStringExtra("currentDay");
                filters = i.getBooleanArrayExtra("filters");

                for (boolean f: filters) {
                    if (f) hasFilter = true;
                }
                break;
            case "day":
                currentDay = i.getStringExtra("currentDay");
                break;
            case "create":
                currentDay = i.getStringExtra("currentDay");
                break;
            case "update":
                currentDay = i.getStringExtra("currentDay");
                break;
            default:
                break;
        }
    }

    /**
     * Envia as tarefas da tela atual por e-mail
     */
    private void sendToEmail()
    {
        int i, len = taskList.size();

        if (len > 0) {
            StringBuilder strBuilder = new StringBuilder();

            for (i = 0; i < len; i++) {
                strBuilder.append("Tarefa: ").append(taskList.get(i).getName()).append("\n");
                if (!taskList.get(i).getComment().isEmpty()) strBuilder.append("Desc: ").append(taskList.get(i).getComment()).append("\n");
            }

            String tasks = strBuilder.toString();
            System.out.println("# Minhas tarefa" + tasks);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, tasks);
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        } else {
            Toast.makeText(this, "Nenhuma tarefa a ser enviada!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sendToEmail();
            return true;
        }

        if (id == R.id.change_day) {
            Intent i = new Intent(MainActivity.this, Day.class);
            startActivity(i);
            return true;
        }

        //Adiciona filtro
        if (id == R.id.action_filter) {
            Intent i = new Intent(MainActivity.this, Filter.class);
            i.putExtra("currentDay", currentDay);
            if (hasFilter)
                i.putExtra("filters", filters);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Utilizando padrão ViewHolder para injetar os dados e componentes na ListView
     */
    static class ViewHolder {
        CheckBox ckb;
        TextView text;
        Button btn;
    }

    /**
     * Classe do adaptador da ListView
     */
    class CustomAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            // View atual
            View rowView = view;

            ViewHolder viewHolder = new ViewHolder();

            if (null == rowView) {
                rowView = getLayoutInflater().inflate(R.layout.row_item, null);
                viewHolder.ckb = (CheckBox) rowView.findViewById(R.id.ckb);
                viewHolder.text = (TextView) rowView.findViewById(R.id.txtList);
                viewHolder.btn = (Button) rowView.findViewById(R.id.btnDescricao);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            // Verificando se está marcado ou desmarcado
            viewHolder.ckb.setChecked(taskList.get(i).getStatus());

            //Adicionando dados aos componentes
            viewHolder.ckb.setTag(i);
            viewHolder.text.setText(taskList.get(i).getName());

            //Adicionando os eventos aos componentes
            btnClick(viewHolder.btn, i);
            ckbClick(viewHolder.ckb, i);

            return rowView;
        }

        public void btnClick(Button btn, final int index)
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, UpdateTask.class);

                    intent.putExtra("taskId", Integer.toString(taskList.get(index).getId()));
                    intent.putExtra( "currentDay", currentDay );

                    startActivity(intent);
                    //System.out.println("A tarefa " + taskList.get(i).getName() + " foi selecionada para detalhes!");
                }
            });
        }

        public void ckbClick(final CheckBox ckb, final int i)
        {
            ckb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskController tc = new TaskController();

                    if (ckb.isChecked())
                        tc.updateStatus(taskList.get(i).getId(), true);
                    else
                        tc.updateStatus(taskList.get(i).getId(), false);
                    System.out.println("O checkbox com a tag " + taskList.get(i).getCreatedAt() + " foi clicado!");
                }
            });
        }
    }
}
