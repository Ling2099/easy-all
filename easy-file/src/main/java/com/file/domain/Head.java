package com.file.domain;

/**
 * Excel Head Data
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023/06/15
 */
public class Head<E> {

    /**
     * 行
     */
    private Integer row;

    /**
     * 列
     */
    private Integer col;

    /**
     * 数据
     */
    private E val;

    /**
     * 构造函数
     *
     * @param row {@link #row}
     * @param col {@link #col}
     * @param val {@link #val}
     */
    public Head(Integer row, Integer col, E val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }

    /**
     * getter function
     *
     * @return {@link #row}
     */
    public Integer getRow() {
        return row;
    }

    /**
     * setter function
     *
     * @param row {@link #row}
     */
    public void setRow(Integer row) {
        this.row = row;
    }

    /**
     * getter function
     *
     * @return {@link #col}
     */
    public Integer getCol() {
        return col;
    }

    /**
     * setter function
     *
     * @param col {@link #col}
     */
    public void setCol(Integer col) {
        this.col = col;
    }

    /**
     * getter function
     *
     * @return {@link #val}
     */
    public E getVal() {
        return val;
    }

    /**
     * setter function
     *
     * @param val {@link #val}
     */
    public void setVal(E val) {
        this.val = val;
    }
}
