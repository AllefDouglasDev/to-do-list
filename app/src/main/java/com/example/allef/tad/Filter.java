package com.example.allef.tad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Filter extends AppCompatActivity
{
    /** Refere ao checkbox 'Concluído' */
    private CheckBox ckb1;
    /** Refere ao checkbox 'Não concluído' */
    private CheckBox ckb2;
    /** Refere ao checkbox 'Normal' */
    private CheckBox ckb3;
    /** Refere ao checkbox 'Alto' */
    private CheckBox ckb4;
    /** Dia passado pela activity para filtro */
    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        try {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Filtros");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verifyData();
        btnRemover();
    }

    /**  Remove todos os filtros */
    private void btnRemover()
    {
        Button btn = (Button) findViewById(R.id.btnRemover);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshValueCheckbox();

                ckb1.setChecked(false);
                ckb2.setChecked(false);
                ckb3.setChecked(false);
                ckb4.setChecked(false);
            }
        });
    }

    /** Retorna array com os filtros selecionados */
    private boolean[] getCheckedBoxes()
    {
        refreshValueCheckbox();

        boolean[] filter = new boolean[4];

        filter[0] = ckb1.isChecked();
        filter[1] = ckb2.isChecked();
        filter[2] = ckb3.isChecked();
        filter[3] = ckb4.isChecked();

        return filter;
    }

    /** Pega os dados passados pela Intent */
    private void verifyData()
    {
        Intent i = getIntent();
        //Recebendo dados da tela anterior
        Intent intent = getIntent();
        currentDay = intent.getStringExtra("currentDay");
        boolean[] filters = intent.getBooleanArrayExtra("filters");

        if (null != filters)
            addFiltersInCkb(filters);
    }

    /**
     * Marca como true os filtros enviados por parâmetros
     *
     * @param filters
     */
    private void addFiltersInCkb(boolean[] filters)
    {
        refreshValueCheckbox();

        ckb1.setChecked(filters[0]);
        ckb2.setChecked(filters[1]);
        ckb3.setChecked(filters[2]);
        ckb4.setChecked(filters[3]);
    }

    /** Pega os valores atuais dos checkbox e adiciona as variáveis */
    private void refreshValueCheckbox()
    {
        ckb1 = (CheckBox) findViewById(R.id.ckb1);
        ckb2 = (CheckBox) findViewById(R.id.ckb2);
        ckb3 = (CheckBox) findViewById(R.id.ckb3);
        ckb4 = (CheckBox) findViewById(R.id.ckb4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter_confirm) {
            Intent i = new Intent(Filter.this, MainActivity.class);
            i.putExtra("class", "filter");
            i.putExtra("currentDay", currentDay);
            i.putExtra("filters", getCheckedBoxes());
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
