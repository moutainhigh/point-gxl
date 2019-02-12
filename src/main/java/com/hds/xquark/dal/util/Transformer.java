package com.hds.xquark.dal.util;

import com.google.common.base.Function;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by
 *
 * @author wangxinhua on 17-11-19. DESC: 支持函数式接口的bean转换器
 */
public class Transformer {

  /**
   * 通过传入函数对象转换实例
   *
   * @param f from 源对象
   * @param function {@link Function} 函数接口引用
   * @return R result 转换对象
   */
  public static <F, R> R from(F f, Function<F, R> function) {
    return function.apply(f);
  }

  /**
   * 通过 {@link BeanUtils} 实现的实例转换
   *
   * @param f from 源对象
   * @param clazz 目标对象类型
   * @return 目标对象 R result 的实例
   * @throws IllegalStateException 实例转换失败
   */
  public static <F, R> R fromBean(F f, final Class<R> clazz) {
    return from(f, beanConvertFunction(clazz));
  }

  /**
   * 通过 {@link Function} 函数引用将一个集合转换ArrayList 用于前台展示 暂不支持转为其他类型
   *
   * @param input 输入集合
   * @param function 函数引用
   * @param <I> 输入集合Item类型
   * @param <E> 输出集合Item类型
   * @return 转换后的集合, 如果input为空则返回空集合
   */
  public static <I, E> List<E> fromList(Iterable<I> input, Function<I, E> function) {
    if (input == null || !input.iterator().hasNext()) {
      return Collections.emptyList();
    }
    ArrayList<E> list = new ArrayList<>();
    for (I t : input) {
      list.add(function.apply(t));
    }
    return list;
  }

  /**
   * 通过 {@code fromBean} 方法转换集合 暂不支持转为其他类型
   *
   * @param input 输入集合
   * @param clazz V的字节类型
   * @param <I> 输入集合Item类型
   * @param <E> 输出集合Item类型
   * @return 转换后的集合, 如果input为空则返回空集合
   */
  public static <I, E> List<E> fromList(Iterable<I> input, final Class<E> clazz) {
    if (input == null || !input.iterator().hasNext()) {
      return Collections.emptyList();
    }
    ArrayList<E> list = new ArrayList<>();
    Function<I, E> convertFunction = beanConvertFunction(clazz);
    for (I t : input) {
      list.add(from(t, convertFunction));
    }
    return list;
  }

  /**
   * 封装通过 {@link BeanUtils} 转换实例的 Function
   *
   * @param clazz 目标转换类型
   * @param <F> From 类型泛型
   * @param <R> Result 类型泛型
   * @return 实现bean转换的函数对象
   */
  private static <F, R> Function<F, R> beanConvertFunction(final Class<R> clazz) {
    return new Function<F, R>() {
      @Override
      public R apply(F t) {
        R ret;
        try {
          ret = clazz.newInstance();
          BeanUtils.copyProperties(t, ret);
        } catch (Exception e) {
          String msg = String.format("实例转换失败, %s -> %s", t.getClass().getName(), clazz.getName());
          throw new IllegalStateException(msg);
        }
        return ret;
      }
    };
  }
}
