package com.hds.xquark.dal.type;

/** @author wangxinhua on 17-10-13. DESC: 符号运算接口 */
public interface Operation<T extends Number> {

  /**
   * 具体的运算逻辑实现
   *
   * @param t1 数字1
   * @param t2 数字2
   * @return 结果 <T extends Number>
   */
  T apply(T t1, T t2);

  /**
   * 获取运算符号的字符串表示
   *
   * @return 符号表示 如 + -
   */
  String getSymbol();
}
