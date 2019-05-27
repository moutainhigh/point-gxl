package com.hds.xquark.dal.type;

/**
 * created by
 *
 * @author wangxinhua at 18-6-18 下午8:28
 */
public enum AuditType {
  INSERT(1),
  UPDATE(2);

  private final int code;

  AuditType(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
