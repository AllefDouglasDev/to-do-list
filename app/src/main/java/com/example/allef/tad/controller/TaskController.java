package com.example.allef.tad.controller;

import com.example.allef.tad.dao.TaskDAO;
import com.example.allef.tad.model.Notification;
import com.example.allef.tad.model.Task;
import com.example.allef.tad.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Allef on 22/05/2018.
 */

public class TaskController
{
    /** Variavel com funções de conexão de banco de dados */
    private TaskDAO tdao;
    /** Guarda funções uteis a mais de uma classe */
    private Util util;

    public TaskController()
    {
        tdao = new TaskDAO();
    }

    /**
     * Cadastra uma nova tarefa no banco
     *
     * @param tarefa
     * @param descricao
     * @param importancia
     * @return
     */
    public long createTask(String tarefa, String descricao, String importancia, String currentDay)
    {
        if (descricao.length() > 144) return -1;

        if (tarefa.length() > 50) return -1;

        if (null == util) util = new Util();

        String date = util.getCurrentDate();
        boolean status = false;
        Boolean deleted = false;

        Task task = new Task(tarefa, status, descricao, importancia, deleted, currentDay, date);

        return tdao.addTask(task);
    }

    /**
     * Cadastra uma notificação para uma tarefa
     *
     * @param dateToNotify
     * @param taskId
     * @return
     */
    public long createNotification(String dateToNotify, int taskId)
    {
        Notification notify = new Notification();

        notify.setDateToNotify(dateToNotify);

        return tdao.addNotification(notify, taskId);
    }

    /**
     * Busca todas as tarefas criadas
     *
     * @return AttayList<task>
     */
    public ArrayList<Task> index()
    {
        return tdao.listTasks();
    }

    /**
     * Busca uma tarefa pelo ID
     *
     * @param id
     * @return ArrayList<Task>
     */
    public ArrayList<Task> show(int id)
    {
        return tdao.show(id);
    }

    /**
     * Busca uma notificação de uma tarefa
     *
     * @param taskId
     * @return
     */
    public Notification showNotification(int taskId)
    {
        return tdao.showNotification(taskId);
    }

    /**
     * Busca lista de tarefas pela data selecionada
     *
     * @param date
     * @return Array com as tarefas
     */
    public ArrayList<Task> showByDate(String date)
    {
        return tdao.showTasksByDate(date);
    }

    /**
     * Filtra tarefas
     *
     * @param date
     * @param filters
     * @return
     */
    public ArrayList<Task> filterTasks(String date, boolean[] filters)
    {
        int i, len = filters.length;

        StringBuilder queryFilter = new StringBuilder();

        queryFilter.append("SELECT * FROM task WHERE created_at = '").append(date).append("' and (");

        if (filters[0] && filters[1])
            queryFilter.append(" (status = 1 or status = 0)");
        else if (filters[0])
            queryFilter.append(" status = 1");
        else if (filters[1])
            queryFilter.append(" status = 0");

        System.out.println("# Filtros: "+ filters[0] + " " + filters[1] + " " + filters[2] + " " + filters[3]);

        if (( filters[0] || filters[1] ) && ( filters[2] || filters[3] ))
            queryFilter.append(" and ");

        if (filters[2] && filters[3])
            queryFilter.append(" (level = 'Normal' or level = 'Alta')");
        else if (filters[2])
            queryFilter.append(" level = 'Normal'");
        else if (filters[3])
            queryFilter.append(" level = 'Alta'");

        queryFilter.append(") ");

        String query = queryFilter.toString();

        System.out.println("Minha query ficou assim: " + query);

        return tdao.filterTasks(query);
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
        return tdao.updateStatus(id, status);
    }

    /**
     * Altera o nome, a descrição e o status da tarefa
     *
     * @param
     * @return
     */
    public boolean updateTask(int id, String name, String comment, String level)
    {
        Task task = new Task();

        if (null == util) util = new Util();

        task.setId(id);
        task.setName(name);
        task.setComment(comment);
        task.setLevel(level);
        task.setUpdatedAt(util.getCurrentDate());

        return tdao.updateTask(task);
    }

    /**
     * Altera a data de notificação de uma tarefa
     *
     * @param
     * @return
     */
    public boolean updateNotification(String dataNotify, int id)
    {
        Notification noti = new Notification();

        noti.setDateToNotify(dataNotify);

        return tdao.updateNotification(noti, id);
    }

    /**
     * Deleta uma tarefa
     *
     * @param id
     * @return
     */
    public boolean deleteTask(int id)
    {
        return tdao.deleteTask(id);
    }

















}
