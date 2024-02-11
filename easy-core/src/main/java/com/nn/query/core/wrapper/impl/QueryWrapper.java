package com.nn.query.core.wrapper.impl;


import com.nn.query.annocation.Id;
import com.nn.query.annocation.ManyToOne;
import com.nn.query.annocation.OneToMany;
import com.nn.query.annocation.Table;
import com.nn.query.core.BaseEntity;
import com.nn.query.core.query.QueryExecute;
import com.nn.query.core.wrapper.Wrapper;
import com.nn.query.exception.QueryException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author niann
 * @date 2024/2/2 10:51
 * @description 条件构造器实现
 **/
public class QueryWrapper<E> implements Wrapper<E> {
    private final BaseEntity baseEntity;

    public QueryWrapper(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    public String getSql() {
        return this.baseEntity.getSql().toString();
    }

    @Override
    public QueryWrapper<E> where() {
        this.baseEntity.appendSql("where");
        return new QueryWrapper<E>(this.baseEntity);
    }


    @Override
    public QueryWrapper<E> join(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        String tableName;
        if (table == null || table.value() == null || table.value().isEmpty()) {
            String typeName = clazz.getTypeName();
            tableName = typeName.substring(typeName.lastIndexOf(".") + 1);
        } else {
            tableName = table.value();
        }
        this.baseEntity.appendSql("join %s ".formatted(tableName));
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> join(Class<?>... tables) {
        //TODO 连接查询待完成
        throw new RuntimeException("待完成");
    }


    @Override
    public QueryWrapper<E> eq(String field, Object val) {
        this.baseEntity.appendSql("%s = ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> eq(boolean bool, String field, Object val) {
        if (bool) {
            return eq(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> ne(String field, Object val) {
        this.baseEntity.appendSql("%s != ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> ne(boolean bool, String field, Object val) {
        if (bool) {
            return ne(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> ge(String field, Object val) {
        this.baseEntity.appendSql("%s >= ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> ge(boolean bool, String field, Object val) {
        if (bool) {
            return ge(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> gt(String field, Object val) {
        this.baseEntity.appendSql("%s > ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> gt(boolean bool, String field, Object val) {
        if (bool) {
            return gt(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> lt(String field, Object val) {
        this.baseEntity.appendSql("%s < ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> lt(boolean bool, String field, Object val) {
        if (bool) {
            return lt(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> le(String field, Object val) {
        this.baseEntity.appendSql("%s <= ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> le(boolean bool, String field, Object val) {
        if (bool) {
            return le(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> in(String field, Collection<Object> val) {
        StringBuilder sql = new StringBuilder("%s in (".formatted(field));
        for (Object o : val) {
            this.baseEntity.getFieldValue().add(o);
            sql.append("?,");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(")");
        this.baseEntity.appendSql(sql.toString());
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> in(String field, Object... val) {
        return this.in(field, List.of(val));
    }

    @Override
    public QueryWrapper<E> in(boolean bool, String field, Object... val) {
        if (bool) {
            return in(field, val);
        }
        return this.in(field, List.of(val));
    }

    @Override
    public QueryWrapper<E> in(boolean bool, String field, Object val) {
        if (bool) {
            return in(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> in(boolean bool, String field, Collection<Object> val) {
        if (bool) {
            return in(field, val);
        }
        return new QueryWrapper<>(this.baseEntity);
    }


    @Override
    public QueryWrapper<E> and() {
        this.baseEntity.appendSql("and");
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> or() {
        this.baseEntity.appendSql("or");
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> like(String field, Object var) {
        this.baseEntity.appendSql("%s like ?".formatted(field));
        this.baseEntity.getFieldValue().add("%" + var + "%");
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> like(boolean bool, String field, Object var) {
        if (bool) {
            return like(field, var);
        }
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> leftLike(String field, Object var) {
        this.baseEntity.appendSql("%s like ?".formatted(field));
        this.baseEntity.getFieldValue().add("%" + var);
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> leftLike(boolean bool, String field, Object var) {
        if (bool) {
            return leftLike(field, var);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> rightLike(String field, Object var) {
        this.baseEntity.appendSql("%s like ?".formatted(field));
        this.baseEntity.getFieldValue().add(var + "%");
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> rightLike(boolean bool, String field, Object var) {
        if (bool) {
            return rightLike(field, var);
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> order(Object... field) {
        List<Object> fields = List.of(field);
        if (fields.size() == 1) {
            this.baseEntity.appendSql("order by %s".formatted(field[0]));
        } else {
            this.baseEntity.appendSql("order by");
            for (Object var : fields) {
                this.baseEntity.appendSql("%s,".formatted(var));
            }
        }
        String sql = this.baseEntity.getSql().toString();
        int i = sql.lastIndexOf(",");
        if (i != -1) {
            this.baseEntity.getSql().delete(i, sql.length());
        }
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> orderDesc(Object... field) {
        List<Object> fields = List.of(field);
        if (fields.size() == 1) {
            this.baseEntity.appendSql("order by %s".formatted(field[0]));
        } else {
            this.baseEntity.appendSql("order by ");
            for (Object var : fields) {
                this.baseEntity.appendSql("%s,".formatted(var));
            }
        }
        String sql = this.baseEntity.getSql().toString();
        int i = sql.lastIndexOf(",");
        if (i != -1) {
            this.baseEntity.getSql().delete(i, sql.length());
        }
        this.baseEntity.appendSql("desc");
        return new QueryWrapper<>(this.baseEntity);
    }


    public QueryWrapper<E> on(String args) {
        this.baseEntity.appendSql("on %s".formatted(args));
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryExecute<E> build() {
        return new QueryExecute<>(baseEntity);
    }


}
