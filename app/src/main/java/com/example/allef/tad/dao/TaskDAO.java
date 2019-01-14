package com.example.allef.tad.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.allef.tad.model.Notification;
import com.example.allef.tad.model.Task;

import java.util.ArrayList;

/**
 * Created by Allef on 18/05/2018.
 */

public class TaskDAO
{
    private SQLiteDatabase db;

    /**
     * Cadstra uma nova tarefa no banco de dados
     *
     * @param task
     * @return
     */
    public long addTask(Task task)
    {
        db = MainDB.getInstance().getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("name", task.getName());
        cv.put("status", task.getStatus());
        cv.put("comment", task.getComment());
        cv.put("level", task.getLevel());
        cv.put("deleted", task.getDeleted());
        cv.put("created_at", task.getCreatedAt());
        cv.put("updated_at", task.getUpdatedAt());

        return db.insert("task", null, cv);
    }

    /**
     * Cadastrar a notificação para uma tarefa
     *
     * @param notify
     * @param taskId
     * @return
     */
    public long addNotification(Notification notify, int taskId)
    {
        db = MainDB.getInstance().getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("task_id", taskId);
        cv.put("notify", notify.getDateToNotify());

        return db.insert("notify", null, cv);
    }

    /**
     * Altera os dados de uma tarefa
     *
     * @param task
     * @return
     */
    public boolean updateTask(Task task)
    {
        db = MainDB.getInstance().getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("name", task.getName());
        cv.put("comment", task.getComment());
        cv.put("level", task.getLevel());
        cv.put("updated_at", task.getUpdatedAt());

        String query = "id = " + task.getId();

        return db.update("task", cv, query, null) > 0;
    }

    /**
     * Altera o status de uma tarefa
     *
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(int id, boolean status)
    {
        db = MainDB.getInstance().getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("status", status);

        String query = "id = " + id;

        return db.update("task", cv, query, null) > 0;
    }

    /**
     * Altera a notificação no banco de uma tarefa
     *
     * @param notification
     * @param taskId
     * @return
     */
    public boolean updateNotification(Notification notification, int taskId)
    {
        db = MainDB.getInstance().getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("notify", notification.getDateToNotify());

        String query = "task_id = " + taskId;

        return db.update("notify", cv, query, null) > 0;
    }

    /**
     * Lista todas as tarefas já criadas
     *
     * @return
     */
    public ArrayList<Task> listTasks()
    {
        db = MainDB.getInstance().getReadableDatabase();

        String query = "SELECT * FROM task";

        ArrayList<Task> tasks = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Task t = new Task();

                t.setId(c.getInt(0));
                t.setName(c.getString(1));
                t.setStatus(c.getInt(2) == 1);
                t.setComment(c.getString(3));
                t.setLevel(c.getString(4));
                t.setDeleted(c.getInt(5) == 1);
                t.setCreatedAt(c.getString(6));
                t.setUpdatedAt(c.getString(7));

                tasks.add(t);
            } while (c.moveToNext());
        }

        c.close();

        return tasks;
    }

    /**
     * Busca uma tarefa
     *
     * @return
     */
    public ArrayList<Task> show(int id)
    {
        db = MainDB.getInstance().getReadableDatabase();

        String query = "SELECT * FROM task WHERE id = ?";

        ArrayList<Task> task = new ArrayList<>();

        String[] whereArgs = { String.valueOf(id) };

        Cursor c = db.rawQuery(query, whereArgs);

        if (c.moveToFirst()) {
            do {
                Task t = new Task();

                t.setId(c.getInt(0));
                t.setName(c.getString(1));
                t.setStatus(c.getInt(2) == 1);
                t.setComment(c.getString(3));
                t.setLevel(c.getString(4));
                t.setDeleted(c.getInt(5) == 1);
                t.setCreatedAt(c.getString(6));
                t.setUpdatedAt(c.getString(7));

                task.add(t);
            } while (c.moveToNext());
        }

        c.close();

        return task;
    }

    /**
     * Lista tarefas criadas no dia informado
     *
     * @return
     */
    public ArrayList<Task> showTasksByDate(String date)
    {
        db = MainDB.getInstance().getReadableDatabase();

        String query = "SELECT * FROM task WHERE created_at = ?";

        ArrayList<Task> tasks = new ArrayList<>();

        String[] whereArgs = { String.valueOf(date) };

        Cursor c = db.rawQuery(query, whereArgs);

        if (c.moveToFirst()) {
            do {
                Task t = new Task();

                t.setId(c.getInt(0));
                t.setName(c.getString(1));
                t.setStatus(c.getInt(2) == 1);
                t.setComment(c.getString(3));
                t.setLevel(c.getString(4));
                t.setDeleted(c.getInt(5) == 1);
                t.setCreatedAt(c.getString(6));
                t.setUpdatedAt(c.getString(7));

                tasks.add(t);
            } while (c.moveToNext());
        }

        c.close();

        return tasks;
    }

    /**
     * Lista as tarefas a partir de um filtro
     *
     * @return
     */
    public ArrayList<Task> filterTasks(String query)
    {
        db = MainDB.getInstance().getReadableDatabase();

        ArrayList<Task> tasks = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Task t = new Task();

                t.setId(c.getInt(0));
                t.setName(c.getString(1));
                t.setStatus(c.getInt(2) == 1);
                t.setComment(c.getString(3));
                t.setLevel(c.getString(4));
                t.setDeleted(c.getInt(5) == 1);
                t.setCreatedAt(c.getString(6));
                t.setUpdatedAt(c.getString(7));

                tasks.add(t);
            } while (c.moveToNext());
        }

        c.close();

        return tasks;
    }

    /**
     * Busca a notificação de uma tarefa
     *
     * @param taskId
     * @return
     */
    public Notification showNotification(int taskId)
    {
        db = MainDB.getInstance().getReadableDatabase();

        String query = "SELECT * FROM notify WHERE task_id = ?";

        String[] whereArgs = { String.valueOf(taskId) };

        Cursor c = db.rawQuery(query, whereArgs);

        Notification n = new Notification();

        if (c.moveToFirst()) {
            do {

                n.setId(c.getInt(0));
                n.setDateToNotify(c.getString(2));

            } while (c.moveToNext());
        }

        c.close();

        return n;
    }

    /**
     * Deleta uma tarefa por deleção lógica (true, false)
     *
     * @param id
     * @return
     */
    public boolean deleteTask(int id)
    {
        db = MainDB.getInstance().getWritableDatabase();

        String query = "id = ?";

        String[] whereArgs = { String.valueOf(id) };

        return db.delete("task", query, whereArgs) > 0;
    }

    /**
     * Remove uma tabela do banco de dados
     *
     * @param table
     */
    public void removeTable(String table)
    {
        db = MainDB.getInstance().getWritableDatabase();

        String query = "DROP TABLE IF EXISTS " + table;

        db.execSQL(query);

        System.out.println("# Tabela " + table + " removida com sucesso.");
    }
}
