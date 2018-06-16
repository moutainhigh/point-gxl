package com.hds.xquark.service.point.helper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hds.xquark.dal.type.PlatformType.E;
import static com.hds.xquark.dal.type.PlatformType.H;
import static com.hds.xquark.dal.type.PlatformType.V;
import static org.springframework.util.ReflectionUtils.invokeMethod;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.PointRecordType;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

/**
 * created by
 *
 * @author wangxinhua at 18-6-14 下午4:43 积分/德分计算帮助类
 */
public class PointCommCalHelper {

  private final static Map<PlatformType, PointCommMinusChain> PLATFORM_CHAIN_MAP;

  static {
    // HDS扣减顺序 - HDS -> HV_MALL -> VIVI_LIFE
    PointCommMinusChain HDS_CHAIN = new PointCommMinusChain(H);
    HDS_CHAIN.setNext(new PointCommMinusChain(E))
        .setNext(new PointCommMinusChain(V));

    // HV 扣减顺序 - HV_MALL -> HDS -> VIVI_LIFE
    PointCommMinusChain HV_CHAIN = new PointCommMinusChain(E);
    HV_CHAIN.setNext(new PointCommMinusChain(H))
        .setNext(new PointCommMinusChain(V));

    // VIVI_LIFE 扣减顺序 - VIVI_LIFE -> HDS -> HV_MALL
    PointCommMinusChain VIVI_CHAIN = new PointCommMinusChain(V);
    VIVI_CHAIN.setNext(new PointCommMinusChain(H))
        .setNext(new PointCommMinusChain(E));

    // 配置扣减顺序map
    PLATFORM_CHAIN_MAP = ImmutableMap.of(
        H, HDS_CHAIN,
        E, HV_CHAIN,
        V, VIVI_CHAIN);
  }

  /**
   * 平台积分扣减顺序链
   */
  private static class PointCommMinusChain {

    final PlatformType type;
    PointCommMinusChain next = null;

    PointCommMinusChain(PlatformType type) {
      this.type = type;
    }

    public PlatformType getType() {
      return type;
    }

    public PointCommMinusChain getNext() {
      return next;
    }

    public PointCommMinusChain setNext(PointCommMinusChain next) {
      this.next = next;
      return next;
    }

    public boolean hasNext() {
      return next != null;
    }
  }

  /**
   * 增加积分
   *
   * @param pointComm 当前积分/德分
   * @param platform 当前操作平台
   * @param target 待增加积分
   */
  public static void plus(BasePointCommTotal pointComm, PlatformType platform,
      BigDecimal target) {
    BigDecimal oldVal = getUsable(pointComm, platform);
    setUsable(pointComm, platform, oldVal.add(target));
  }

  /**
   * 增加冻结积分
   *
   * @param pointComm 当前积分/德分
   * @param platform 当前操作平台
   * @param target 待增加积分
   */
  public static void plusFreeze(BasePointCommTotal pointComm, PlatformType platform,
      BigDecimal target) {
    BigDecimal oldVal = getFreezed(pointComm, platform);
    setFreezed(pointComm, platform, oldVal.add(target));
  }

  /**
   * 根据当前使用积分平台按顺序递归扣减积分/德分 该方法会直接修改源对象属性
   *
   * @param pointComm 当前积分/德分
   * @param platform 当前操作平台
   * @param target 待扣减积分
   * @param detailMap 保存积分扣减明细的map
   * @return 积分扣减正常则返回true, 若三网积分都不够扣则返回false
   * @throws com.hds.xquark.service.error.BizException 当前三个平台账户积分/德分不够都扣减
   * @throws RuntimeException 反射找不到方法名
   */
  public static boolean minus(BasePointCommTotal pointComm, PlatformType platform,
      BigDecimal target,
      Map<PlatformType, BigDecimal> detailMap) {
    checkNotNull(target);
    PointCommMinusChain chain = PLATFORM_CHAIN_MAP.get(platform);
    return minus(pointComm, chain, target, detailMap);
  }

  private static boolean minus(BasePointCommTotal pointComm, PointCommMinusChain chain,
      BigDecimal target, Map<PlatformType, BigDecimal> detailMap) {
    // 当前平台可用分
    PlatformType platform = chain.getType();
    BigDecimal currPlatformVal = getUsable(pointComm, platform);

    BigDecimal afterMinusVal = currPlatformVal.subtract(target);
    BigDecimal afterMinusAbs = afterMinusVal.abs();
    // 已经到最后的平台还是不够减
    if (afterMinusVal.signum() < 0 && !chain.hasNext()) {
      return false;
    }
    detailMap.put(platform, target);
    if (afterMinusVal.signum() < 0) {
      setUsable(pointComm, platform, BigDecimal.ZERO);
      // 只是当前平台不够减, 再由下一个平台继续扣减
      return minus(pointComm, chain.getNext(), afterMinusAbs, detailMap);
    }
    // 当前平台积分充足, 无需再扣减下一平台
    setUsable(pointComm, platform, afterMinusVal);
    return true;
  }

  /**
   * 根据积分/类型获取异常信息
   *
   * @param clazz 积分/德分类型
   * @return 业务异常
   */
  public static BizException loadNotEnoughException(Class<? extends BasePointCommTotal> clazz) {
    if (clazz.isInstance(PointTotal.class)) {
      return new BizException(GlobalErrorCode.POINT_NOT_ENOUGH);
    } else if (clazz.isInstance(CommissionTotal.class)) {
      return new BizException(GlobalErrorCode.COMMISSION_NOT_ENOUGH);
    } else {
      return new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

  /**
   * 反射获取平台可用积分
   *
   * @param pointComm 当积分/德分对象
   * @param platform 平台类型
   * @return 平台对应积分
   */
  public static BigDecimal getUsable(BasePointCommTotal pointComm, PlatformType platform) {
    String methodPostfix = platform.getFullName();
    String getMethodName = "getUsable" + methodPostfix;
    return getVal(getMethodName, pointComm);
  }

  /**
   * 反射获取平台冻结积分
   *
   * @param pointComm 当积分/德分对象
   * @param platform 平台类型
   * @return 平台对应积分
   */
  public static BigDecimal getFreezed(BasePointCommTotal pointComm, PlatformType platform) {
    String methodPostfix = platform.getFullName();
    String getMethodName = "getFreezed" + methodPostfix;
    return getVal(getMethodName, pointComm);
  }

  /**
   * 反射获取值
   *
   * @param name 方法名
   * @param pointComm 积分对象
   * @return 积分值
   */
  private static BigDecimal getVal(String name, BasePointCommTotal pointComm) {
    Method getMethod;
    Class<? extends BasePointCommTotal> clazz = pointComm.getClass();
    try {
      // 反射获取方法
      getMethod = clazz.getMethod(name);
    } catch (NoSuchMethodException e) {
      // 方法名找不到
      throw new RuntimeException("对象: " + pointComm + "中没有方法:" + name, e);
    }
    return (BigDecimal) invokeMethod(getMethod, pointComm);
  }

  /**
   * 反射设置平台积分/德分
   *
   * @param pointComm 当积分/德分对象
   * @param platform 平台类型
   * @param val 设置值
   */
  public static void setUsable(BasePointCommTotal pointComm, PlatformType platform,
      BigDecimal val) {
    String methodPostfix = platform.getFullName();
    String setMethodName = "setUsable" + methodPostfix;
    setVal(setMethodName, pointComm, val);
  }

  /**
   * 反射设置冻结积分/德分
   *
   * @param pointComm 积分对象
   * @param platform 平台
   * @param val 新的值
   */
  public static void setFreezed(BasePointCommTotal pointComm, PlatformType platform,
      BigDecimal val) {
    String methodPostfix = platform.getFullName();
    String setMethodName = "setFreezed" + methodPostfix;
    setVal(setMethodName, pointComm, val);
  }

  /**
   * 反射设置积分
   *
   * @param name 方法名
   * @param pointComm 积分对象
   * @param val 新值
   */
  public static void setVal(String name, BasePointCommTotal pointComm, BigDecimal val) {
    Method setMethod;
    try {
      // 反射获取方法
      Class<? extends BasePointCommTotal> clazz = pointComm.getClass();
      setMethod = clazz.getMethod(name, BigDecimal.class);
    } catch (NoSuchMethodException e) {
      // 方法名找不到
      throw new RuntimeException("对象: " + pointComm + "中没有方法:" + name, e);
    }
    invokeMethod(setMethod, pointComm, val);
  }

  /**
   * 通过详细参数构造记录对象
   */
  public static <T extends BasePointCommRecord, S extends BasePointCommTotal> T buildRecord(
      Long cpId, String bizId, GradeCode grade,
      S infoBefore, S infoAfter, PlatformType platform, PointRecordType recordType,
      Class<T> clazz) {
    Preconditions.checkArgument(infoBefore != null && infoAfter != null,
        "积分计算错误");
    BigDecimal modified = getUsable(infoAfter, platform)
        .subtract(getUsable(infoBefore, platform));
    BigDecimal modifiedFreezed = getFreezed(infoAfter, platform)
        .subtract(getFreezed(infoBefore, platform));
    T record;
    try {
      record = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("积分记录构造失败, 请确保class不是是抽象父类"
          + "且有默认构造函数", e);
    }
    record.setBusinessId(bizId);
    record.setCurrent(modified);
    record.setCurrentFreezed(modifiedFreezed);
    record.setCodeNumber(grade.getCodeNumber());
    record.setSource(platform.getCode());
    record.setCpId(cpId);
    record.setGradeId(grade.getId());
    // TODO 类型需要修改
    record.setType(PointRecordType.PRBA);
    return record;
  }

  public static void main(String[] args) {
    PointTotal pointTotal = BasePointCommTotal.emptyInfo(0L, PointTotal.class);
    pointTotal.setUsablePointHds(BigDecimal.valueOf(1000));
    pointTotal.setUsablePointViviLife(BigDecimal.valueOf(1000));
    boolean ret = PointCommCalHelper.minus(pointTotal, H, BigDecimal.valueOf(1999), null);
    System.out.println(ret);

    System.out.println(pointTotal);
  }

}
