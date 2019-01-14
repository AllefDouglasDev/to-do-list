package com.example.allef.tad.model;

/**
 * Created by Allef on 18/05/2018.
 */

public class Task
{
    private int id;

    private String name;

    private boolean status;

    private String comment;

    private String level;

    private boolean deleted;

    private String createdAt;

    private String updatedAt;

    /**
     *
     */
    public Task() {  }

    /**
     *
     * @param id
     * @param name
     * @param status
     * @param comment
     * @param level
     * @param deleted
     * @param createdAt
     * @param updatedAt
     */
    public Task(int id, String name, boolean status, String comment, String level, boolean deleted, String createdAt, String updatedAt)
    {
        this.id = id;
        this.name = name;
        this.status = status;
        this.comment = comment;
        this.level = level;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @param name
     * @param status
     * @param comment
     * @param level
     * @param deleted
     * @param createdAt
     * @param updatedAt
     */
    public Task(String name, boolean status, String comment, String level, boolean deleted, String createdAt, String updatedAt)
    {
        this.name = name;
        this.status = status;
        this.comment = comment;
        this.level = level;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public boolean getStatus()
    {
        return status;
    }

    public String getComment()
    {
        return comment;
    }

    public String getLevel()
    {
        return level;
    }

    public boolean getDeleted()
    {
        return deleted;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", level='" + level + '\'' +
                ", deleted=" + deleted +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
