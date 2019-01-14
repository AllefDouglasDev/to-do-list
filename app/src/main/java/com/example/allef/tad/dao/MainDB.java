package com.example.allef.tad.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.allef.tad.MyApp;

/**
 * Created by Allef on 18/05/2018.
 */

public class MainDB extends SQLiteOpenHelper
{
    /** Nome do banco de dados */
    private static String NOME_DB = "TODO";
    /** Versão do bando de dados */
    private static int VERSAO_DB = 1;
    /** Instancia da classe para padrão Singleton */
    private static MainDB INSTANCIA;

    /** Retorna instância da classe caso não exista, utilizando padrão Singleton */
    public static MainDB getInstance()
    {
        if (null == INSTANCIA) return INSTANCIA = new MainDB();
        return INSTANCIA;
    }

    private MainDB() { super(MyApp.getContext(), NOME_DB, null, VERSAO_DB); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {  }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {  }

    @Override
    public synchronized void close()
    {
        INSTANCIA = null;
        super.close();
    }
}
