package com.hds.xquark.dal.type;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangxinhua on 2018/5/19. DESC: 积分平台类型
 */
public enum PlatformType {

  /**
   * 汉德森
   */
  H(1, "Hds", "汉德森"),

  /**
   * ViViLife
   */
  V(2, "ViviLife", "ViviLife"),

  /**
   * 汉薇商城
   */
  E(3, "Ecomm", "汉薇"),

  /**
   * 系统
   */
  S(4, "System", "系统");

  private static final Map<Integer, PlatformType> STATIC_MAP;

  public static final Function<PlatformType, Integer> GET_CODE_FUNC = new Function<PlatformType, Integer>() {
    @Override
    public Integer apply(PlatformType platformType) {
      return platformType.getCode();
    }
  };

  static {
    STATIC_MAP = new HashMap<>();
    for (PlatformType type : PlatformType.values()) {
      STATIC_MAP.put(type.getCode(), type);
    }
  }

  private final int code;

  private final String fullName;

  private final String nameCN;

  PlatformType(int code, String fullName, String nameCN) {
    this.code = code;
    this.fullName = fullName;
    this.nameCN = nameCN;
  }

  public int getCode() {
    return code;
  }

  public String getFullName() {
    return fullName;
  }

  public String getNameCN() {
    return nameCN;
  }

  public static Optional<PlatformType> fromCodeOp(int code) {
    return Optional.fromNullable(STATIC_MAP.get(code));
  }

  public static PlatformType fromCode(int code) {
    return STATIC_MAP.get(code);
  }

}
