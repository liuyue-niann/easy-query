package com.nn.core;



/**
 * @author niann
 * @date 2024/2/2 10:51
 * @description 条件构造器
 **/
public class Wrapper<E> {
    private final BaseEntity baseEntity;

    public String getSql(){
        return this.baseEntity.getSql().toString();
    }
    public Wrapper(BaseEntity baseEntity){
        this.baseEntity = baseEntity;
    }


    public Wrapper<E> where(){
        this.baseEntity.appendSql("where");
        return new Wrapper<E>(this.baseEntity);
    }
    public Wrapper<E> eq(String field,Object val){
        this.baseEntity.appendSql("%s = ?".formatted(field));
        this.baseEntity.fieldValue.add(val);
        return new Wrapper<E>(this.baseEntity);
    }



    public Wrapper<E> and(){
        this.baseEntity.appendSql("and");
        return new Wrapper<E>(this.baseEntity);
    }
    public Execute<E> build(){
        return new Execute<E>(baseEntity);
    }


}
