package com.hds.xquark.service.point.type

/**
 * @author wangxinhua.
 * @date 2018/12/12
 */
enum FunctionCodeType {

    /**
     * 增加德分
     */
    GRANT_POINT("1001"),

    /**
     * 消费德分
     */
    CONSUME_POINT("1002"),

    /**
     * 冻结德分
     */
    FREEZE_GRANT_POINT("1003"),

    /**
     * 取消德分
     */
    CANCEL_POINT("1004"),

    /**
     * 退回德分
     */
    RETURN_POINT("1005"),

    /**
     * 发放德分
     */
    RELEASE_POINT("1006"),

    /**
     * 消费红包德分, 消费类型
     */
    CONSUME_PACKET_POINT("1007"),

    /**
     * 返回红包德分, 回退类型
     */
    RETURN_PACKET_POINT("1008"),

    /**
     * 新增积分
     */
    GRANT_COMMISSION("2001"),

    /**
     * 消费积分
     */
    CONSUME_COMMISSION("2002"),

    /**
     * 新增积分并立即冻结
     */
    FREEZE_GRANT_COMMISSION("2003"),

    /**
     * 取消积分
     */
    CANCEL_COMMISSION("2004"),

    /**
     * 回退积分
     */
    RETURN_COMMISSION("2005"),

    /**
     * 发放冻结积分
     */
    RELEASE_COMMISSION("2006");

    private final String code

    FunctionCodeType(String code) {
        this.code = code
    }

    String getCode() {
        return code
    }
}