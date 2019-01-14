package com.example.allef.tad.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Allef on 18/05/2018.
 */

public class CreateDatabase
{
    /** Cria as tabelas no banco de dados */
    public void createTable()
    {
        SQLiteDatabase db = MainDB.getInstance().getWritableDatabase();

        String queryTarefa = "CREATE TABLE IF NOT EXISTS task "  + getColunasTask();

        String queryNotify = "CREATE TABLE IF NOT EXISTS notify "  + getColunasNotify();

        /** Criando tabela Task */
        db.execSQL(queryTarefa);
        /** Criando tabela Notify */
        db.execSQL(queryNotify);

        System.out.println("DATABASE CRIADO COM SUCESSO!");
    }

    /** Retorna as colunas da tabela TASK do DB */
    private String getColunasTask()
    {
        return "( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50) NOT NULL, " +
                "status INTEGER, " +
                "comment VARCHAR(145), " +
                "level VARCHAR(10), " +
                "deleted INTEGER, " +
                "created_at DATE NOT NULL, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL )";
    }

    /** Retorna as colunas da tabela NOTIFY do DB */
    private String getColunasNotify()
    {
        return "( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "task_id INTEGER, " +
                "notify TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                "notify_before TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (task_id) REFERENCES task (id) )";
    }
}
