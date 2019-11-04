package com.itxiaoer.commons.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FixedSheets数据对象
 *
 * @author liuyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedSheets {

    /**
     * sheet页名称
     */
    private String name;

    /**
     * cell数据
     */
    private List<Cells> cells;


    @Data
    @SuppressWarnings("unused")
    public static class Cells {

        /**
         * 行
         */
        private int rows;

        /**
         * 列
         */
        private int cols;

        /**
         * 具体的值
         */
        private Object value;

        /**
         * 是否可画的
         */
        private boolean drawable;

        /**
         * 行
         */
        private int rows2;

        /**
         * 列
         */
        private int cols2;

        public Cells() {
        }

        public Cells(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        public Cells(int rows, int cols, int rows2, int cols2, boolean drawable) {
            this.rows = rows;
            this.cols = cols;
            this.drawable = drawable;
            this.rows2 = rows2;
            this.cols2 = cols2;
        }
    }
}
