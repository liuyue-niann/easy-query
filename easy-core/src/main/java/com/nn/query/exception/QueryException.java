package com.nn.query.exception;

/**
 * @author niann
 * @date 2024/2/2 15:15
 * @description
 **/
public class QueryException extends RuntimeException {

    public QueryException(String msg) {
        super(msg);
    }

    public QueryException(Exception e) {
        super(e);
    }
}
