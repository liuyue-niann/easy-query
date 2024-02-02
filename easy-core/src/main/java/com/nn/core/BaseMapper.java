package com.nn.core;

public class BaseMapper<E> {

    private final BaseEntity baseEntity;

    private final Wrapper wrapper;

    public BaseMapper(){
        this.baseEntity = new BaseEntity();
        this.wrapper = new Wrapper(baseEntity);
    }

    public Wrapper select(){
        this.baseEntity.setSql(new StringBuffer("select * from users"));
        return this.wrapper;
    }
    public Wrapper update(){
        this.baseEntity.setSql(new StringBuffer("update..."));
        return this.wrapper;
    }
    public Wrapper insert(){
        this.baseEntity.setSql(new StringBuffer("insert..."));
        return this.wrapper;
    }
    public Wrapper delete(){
        this.baseEntity.setSql(new StringBuffer("delete..."));
        return this.wrapper;
    }


}
