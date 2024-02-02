package com.nn.core;

public class Wrapper {
    private BaseEntity baseEntity;

    public Wrapper(BaseEntity baseEntity){
        this.baseEntity = baseEntity;
    }


    public Wrapper where(){
        this.baseEntity.appendSql("where");
        return new Wrapper(this.baseEntity);
    }
    public Wrapper eq(String field,Object val){
        this.baseEntity.appendSql("%s = ?".formatted(field));
        this.baseEntity.fieldValue.add(val);
        return new Wrapper(this.baseEntity);
    }


}
