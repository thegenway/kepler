package com.hanqian.kepler.common.enums;

/**
 * 枚举
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 * @version 1.0 2016-07-13 。
 * ============================================================================
 */
public class BaseEnumManager {

    /**
     * 文档操作
     */
    public static enum MethodEnum {

        /**
         * 保存
         */
        Save("保存"),

        /**
         * 修改
         */
        Update("修改"),

        /**
         * 删除
         */
        Delete("删除"),

        /**
         * 启用
         */
        Enable("启用"),

        /**
         * 停用
         */
        Disenable("停用");

        private final String value;

        private MethodEnum(final String value) {
            this.value = value;
        }
        public String value() {
            return this.value;
        }

    }

    /**
     * 文档状态
     */
    public static enum StateEnum {

        /**
         * 删除
         */
        Delete("删除"),

        /**
         * 启用
         */
        Enable("启用"),

        /**
         * 停用
         */
        Disenable("停用"),

        /**
         * 历史
         */
        History("历史");

        private final String value;

        private StateEnum(final String value) {
            this.value = value;
        }
        public String value() {
            return this.value;
        }

    }

    /**
     * 性别
     */
    public static enum SexEnum {

        /**
         * 男
         */
        male("男"),

        /**
         * 女
         */
        female("女");

        private final String value;

        private SexEnum(final String value) {
            this.value = value;
        }
        public String value() {
            return this.value;
        }

    }

    /**
     * 账号类型
     */
    public enum AccountTypeEnum{

        Member("企业用户",1),
        CompanyManager("企业管理员",2),
        SystemManager("系统管理员",3);

        private final String value;
        private final int key;

        AccountTypeEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }

    }

    /**
     * 数据源
     */
    public enum DataSourceType{
        /**
         * 主库
         */
        MASTER,

        /**
         * 从库
         */
        SLAVE
    }

}
