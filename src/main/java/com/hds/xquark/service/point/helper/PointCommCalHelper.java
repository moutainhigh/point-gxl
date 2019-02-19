package com.hds.xquark.service.point.helper;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.hds.xquark.dal.model.*;
import com.hds.xquark.dal.type.BelongintToType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hds.xquark.dal.type.PlatformType.*;
import static org.springframework.util.ReflectionUtils.invokeMethod;

/**
 * created by
 *
 * @author wangxinhua at 18-6-14 下午4:43 积分/德分计算帮助类
 */
public class PointCommCalHelper {

  private static final Map<PlatformType, PointCommMinusChain> POINT_PLATFORM_CHAIN_MAP;

  private static final Map<PlatformType, PointCommMinusChain> COMM_PLATFORM_CHAIN_MAP;

  static {
    // HDS扣减顺序 - HDS -> HV_MALL -> VIVI_LIFE
    PointCommMinusChain HDS_CHAIN = new PointCommMinusChain(H);
    HDS_CHAIN.setNext(new PointCommMinusChain(E)).setNext(new PointCommMinusChain(V));

    // HV 扣减顺序 - HV_MALL -> HDS -> VIVI_LIFE
    PointCommMinusChain HV_CHAIN = new PointCommMinusChain(E);
    HV_CHAIN.setNext(new PointCommMinusChain(H)).setNext(new PointCommMinusChain(V));

    // 德分 VIVI_LIFE 扣减顺序 - VIVI_LIFE -> HV_MALL -> HDS
    PointCommMinusChain VIVI_POINT_CHAIN = new PointCommMinusChain(V);
    VIVI_POINT_CHAIN.setNext(new PointCommMinusChain(E)).setNext(new PointCommMinusChain(H));

    // 积分 VIVI_LIFE 扣减顺序 - VIVI_LIFE -> HDS -> HV_MALL
    PointCommMinusChain VIVI_COMM_CHAIN = new PointCommMinusChain(V);
    VIVI_COMM_CHAIN.setNext(new PointCommMinusChain(H)).setNext(new PointCommMinusChain(E));

    // 配置德分扣减顺序map
    POINT_PLATFORM_CHAIN_MAP =
        ImmutableMap.of(
            H, HDS_CHAIN,
            E, HV_CHAIN,
            V, VIVI_POINT_CHAIN);

    // 配置积分扣减顺序map
    COMM_PLATFORM_CHAIN_MAP =
        ImmutableMap.of(
            H, HDS_CHAIN,
            E, HV_CHAIN,
            V, VIVI_COMM_CHAIN);
  }

  /**
   * 增加积分
   *
   * @param pointComm 当前积分/德分
   * @param platform 当前操作平台
   * @param target 待增加积分
   */
  public static void plus(BasePointCommTotal pointComm, PlatformType platform, BigDecimal target) {
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
  public static void plusFreeze(
      BasePointCommTotal pointComm, PlatformType platform, BigDecimal target) {
    BigDecimal oldVal = getFreezed(pointComm, platform);
    setFreezed(pointComm, platform, oldVal.add(target));
  }

  /**
   * 增加不可提现积分
   *
   * @param pointComm 当前积分/德分
   * @param platform 当前操作平台
   * @param target 待增加积分
   */
  public static void plusNoWithdrawal(
          BasePointCommTotal pointComm, PlatformType platform, BigDecimal target) {
    BigDecimal oldVal = getNoWithdrawal(pointComm, platform);
    setNoWithdrawal(pointComm, platform, oldVal.add(target));
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
  public static boolean minus(
      BasePointCommTotal pointComm,
      PlatformType platform,
      BigDecimal target,
      Map<PlatformType, BigDecimal> detailMap) {
    checkNotNull(target);
    PointCommMinusChain chain = getPlatFormChainMap(pointComm, platform);
    if (chain == null) {
      throw new RuntimeException("积分扣减关系未配置");
    }
    return minus(pointComm, chain, target, detailMap);
  }

  private static boolean minus(
      BasePointCommTotal pointComm,
      PointCommMinusChain chain,
      BigDecimal target,
      Map<PlatformType, BigDecimal> detailMap) {
    // 当前平台可用分
    PlatformType platform = chain.getType();
    BigDecimal currPlatformVal = getUsable(pointComm, platform);
    //当前平台可用不可提现积分
    BigDecimal noWithdrawal = getNoWithdrawal(pointComm, platform);

    if (noWithdrawal.signum() < 0){
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "不可提现积分为负数，请检查数据");
    }

    BigDecimal afterMinusVal;
    BigDecimal afterMinusAbs;

    if (noWithdrawal.signum() == 0){
      afterMinusVal = currPlatformVal.subtract(target);
      afterMinusAbs = afterMinusVal.abs();
    } else {
      BigDecimal after1MinusVal = noWithdrawal.subtract(target);
      if (after1MinusVal.signum() < 0){
        afterMinusVal = currPlatformVal.add(after1MinusVal);
        afterMinusAbs = afterMinusVal.abs();
        //当前平台的不可提现积分不足，先扣除掉所有的不可提现积分
        setNoWithdrawal(pointComm, platform, BigDecimal.ZERO);
      } else {
        afterMinusVal = after1MinusVal;
        //当前平台的不可提现积分充足，无需往下走
        setNoWithdrawal(pointComm, platform, afterMinusVal);
        if (currPlatformVal.signum() != 0) {
          detailMap.put(platform, target);
        }
        return true;
      }
    }

    // 已经到最后的平台还是不够减
    if (afterMinusVal.signum() < 0 && !chain.hasNext()) {
      return false;
    }
    if (currPlatformVal.signum() != 0) {
      detailMap.put(platform, target);
    }
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
   * 反射获取平台不可提现积分
   *
   * @param pointComm 当积分/德分对象
   * @param platform 平台类型
   * @return 平台对应积分
   */
  public static BigDecimal getNoWithdrawal(BasePointCommTotal pointComm, PlatformType platform) {
    String methodPostfix = platform.getFullName();
    String getMethodName = "getNoWithdrawal" + methodPostfix;
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
  public static void setUsable(
      BasePointCommTotal pointComm, PlatformType platform, BigDecimal val) {
    String methodPostfix = platform.getFullName();
    String setMethodName = "setUsable" + methodPostfix;
    setVal(setMethodName, pointComm, val);
  }

  /**
   * 反射设置平台不可提现积分
   *
   * @param pointComm 当积分对象
   * @param platform 平台类型
   * @param val 设置值
   */
  public static void setNoWithdrawal(
          BasePointCommTotal pointComm, PlatformType platform, BigDecimal val) {
    String methodPostfix = platform.getFullName();
    String setMethodName = "setNoWithdrawal" + methodPostfix;
    setVal(setMethodName, pointComm, val);
  }

  /**
   * 反射设置冻结积分/德分
   *
   * @param pointComm 积分对象
   * @param platform 平台
   * @param val 新的值
   */
  public static void setFreezed(
      BasePointCommTotal pointComm, PlatformType platform, BigDecimal val) {
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

  /** 积分、德分返回不同的扣减链 */
  private static PointCommMinusChain getPlatFormChainMap(
      BasePointCommTotal pointComm, PlatformType platformType) {
    if (pointComm == null) {
      return null;
    }
    if (pointComm instanceof PointTotal) {
      return POINT_PLATFORM_CHAIN_MAP.get(platformType);
    } else if (pointComm instanceof CommissionTotal) {
      return COMM_PLATFORM_CHAIN_MAP.get(platformType);
    }
    return null;
  }

  public static <T extends BasePointCommRecord, S extends BasePointCommTotal> T buildRecord(
      Long cpId,
      String bizId,
      GradeCode grade,
      S infoBefore,
      S infoAfter,
      PlatformType platform,
      Trancd recordType,
      Class<T> clazz) {
    return buildRecord(
        cpId,
        bizId,
        grade,
        infoBefore,
        infoAfter,
        platform,
        BelongintToType.NON,
        recordType,
        clazz);
  }

  /** 通过详细参数构造记录对象 */
  public static <T extends BasePointCommRecord, S extends BasePointCommTotal> T buildRecord(
      Long cpId,
      String bizId,
      GradeCode grade,
      S infoBefore,
      S infoAfter,
      PlatformType platform,
      BelongintToType belongingTo,
      Trancd recordType,
      Class<T> clazz) {
    Preconditions.checkArgument(infoBefore != null && infoAfter != null, "积分计算错误");
    PlatformType realPlatform =
        belongingTo == BelongintToType.NON
            ? platform
            : PlatformType.fromCode(belongingTo.getCode());
    BigDecimal modified =
        getUsable(infoAfter, realPlatform).subtract(getUsable(infoBefore, realPlatform));
    BigDecimal modifiedFreezed =
        getFreezed(infoAfter, realPlatform).subtract(getFreezed(infoBefore, realPlatform));
    BigDecimal modifiedNoWithdrawal =
        getNoWithdrawal(infoAfter, realPlatform).subtract(getNoWithdrawal(infoBefore, realPlatform));
    T record;
    try {
      record = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("积分记录构造失败, 请确保class不是是抽象父类" + "且有默认构造函数", e);
    }
    record.setBusinessId(bizId);
    record.setCurrent(modified);
    record.setCurrentFreezed(modifiedFreezed);
    record.setCurrentNoWithdrawal(modifiedNoWithdrawal);
    record.setCodeNumber(grade.getCodeNumber());
    record.setSource(platform.getCode());
    record.setBelongingTo(belongingTo.getCode());
    record.setCpId(cpId);
    record.setGradeId(grade.getId());
    // TODO 类型需要修改
    record.setType(recordType);
    int temp;
    if (modifiedNoWithdrawal.compareTo(BigDecimal.ZERO) != 0){
      temp = 2;
      if (modified.compareTo(BigDecimal.ZERO) != 0) {
        temp = 0;
      }
    } else {
      temp = 1;
    }
    record.setUsedType(temp);
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

  /** 平台积分扣减顺序链 */
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
}
