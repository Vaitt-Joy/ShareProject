package com.vz.share.exception;

/**
 * Created by VVz on 2017/3/16.
 *
 * @des 数据库异常类
 */
public class DaoException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DaoException() {
        super();
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
