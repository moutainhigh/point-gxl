package com.hds.xquark.dal.type;

/** @author wangxinhua on 2018/5/21. DESC: */
public enum LongOperation implements Operation<Long> {

  /** 加法 */
  PLUS("+") {
    @Override
    public Long apply(Long x1, Long x2) {
      assert x1 != null && x2 != null;
      return x1 + x2;
    }
  },

  /** 减法 */
  MINUS("-") {
    @Override
    public Long apply(Long x1, Long x2) {
      assert x1 != null && x2 != null;
      return x1 - x2;
    }
  };

  private final String symbol;

  LongOperation(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String getSymbol() {
    return symbol;
  }
}
