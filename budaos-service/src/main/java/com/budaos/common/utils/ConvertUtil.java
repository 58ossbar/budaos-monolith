package com.budaos.common.utils;

import com.budaos.common.exception.BudaosException;
import com.budaos.modules.job.persistence.TsysOrgMapper;
import com.budaos.modules.job.persistence.TsysUserinfoMapper;
import com.budaos.modules.sys.domain.TsysOrg;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.tool.SpringContextUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 转换工具类(字典，参数，用户，机构等)
 * @author zhuq
 *
 */
@Component
public class ConvertUtil {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TsysUserinfoMapper tsysUserinfoMapper;
	@Autowired
	private TsysOrgMapper tsysOrgMapper;
	
	/**
	 * 将集合中的编码字段转换为显示值
	 * 
	 * @param        <T>
	 * @param datas  要转换的数据列表
	 * @param params 其实key为列表中的字段,必须与javabean中字段名一致.value为参数表中的参数类型
	 */
	public <T> void convertParam(List<T> datas, Map<String, String> params) {
		if ((datas == null) || (datas.size() == 0) || (params == null) || params.isEmpty() || datas.isEmpty()) {
			return;
		}
		// 增加map类型的转换
		if (datas.get(0) instanceof java.util.HashMap || datas.get(0) instanceof java.util.Map) {
			params.forEach((a, b) -> {
				datas.forEach(c -> {
					Map map = (Map) c;
					if (map.get(a) != null) {
						String key = SpringContextUtils.getBean(ParamUtil.class).getKeyByVal(b, map.get(a).toString());
						if (key != null) {
							map.put(a, key);
						}
					}
				});
			});
			return;
		}
		Set<String> keys = params.keySet();
		ParamUtil paramUtils = SpringContextUtils.getBean(ParamUtil.class);
		try {
			for (String field : keys) {
				Method setter = null;
				Method getter = null;
				String type = params.get(field);
				field = StrUtils.fistCapital(field);

				for (T t : datas) {
					if (getter == null) {
						getter = t.getClass().getDeclaredMethod("get" + field);
					}

					if (setter == null) {
						setter = t.getClass().getDeclaredMethod("set" + field, getter.getReturnType());
					}

					if ((getter == null) || (setter == null)) {
						break;
					}
					Object obj = getter.invoke(t);
					if(obj == null) {
						continue;
					}
					String val = (String)obj;
					String key = paramUtils.getKeyByVal(type, val);
					setter.invoke(t, key);
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 将集合中的编码字段转换为显示值
	 * 
	 * @param        <T>
	 * @param datas  要转换的数据列表
	 * @param params 其实key为列表中的字段,必须与javabean中字段名一致.value为参数表中的参数类型
	 */
	public <T> void convertDict(List<T> datas, Map<String, String> params) {
		if ((datas == null) || (datas.size() == 0) || (params == null) || params.isEmpty() || datas.isEmpty()) {
			return;
		}
		// 增加map类型的转换
		if (datas.get(0) instanceof java.util.HashMap || datas.get(0) instanceof java.util.Map) {
			params.forEach((a, b) -> {
				datas.forEach(c -> {
					Map map = (Map) c;
					if (map.get(a) != null) {
						String key = SpringContextUtils.getBean(DictUtil.class).getKeyByVal(b, map.get(a).toString());
						if (key != null) {
							map.put(a, key);
						}
					}
				});
			});
			return;
		}
		Set<String> keys = params.keySet();
		DictUtil dictUtil = SpringContextUtils.getBean(DictUtil.class);
		try {
			for (String field : keys) {
				Method setter = null;
				Method getter = null;
				String type = params.get(field);
				field = StrUtils.fistCapital(field);

				for (T t : datas) {
					if (getter == null) {
						getter = t.getClass().getDeclaredMethod("get" + field);
					}

					if (setter == null) {
						setter = t.getClass().getDeclaredMethod("set" + field, getter.getReturnType());
					}

					if ((getter == null) || (setter == null)) {
						break;
					}
					Object obj = getter.invoke(t);
					if(obj == null) {
						continue;
					}
					String val = (String)obj;
					String key = dictUtil.getKeyByVal(type, val);
					setter.invoke(t, key);
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 将集合中的编码字段转换为显示值
	 * 
	 * @param           <T>
	 * @param datas     要转换的数据列表
	 * @param field     对象中要转换的字段名，必须与javabean中字段名一致
	 * @param paramType 参数类型，对应数据库中的PARA_TYPE字段
	 */
	public <T> void convertParam(List<T> datas, String field, String paramType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(field, paramType);
		convertParam(datas, params);
	}

	/**
	 * 将集合中的编码字段转换为显示值
	 * 
	 * @param           <T>
	 * @param datas     要转换的数据列表
	 * @param field     对象中要转换的字段名，必须与javabean中字段名一致
	 * @param paramType 参数类型，对应数据库中的PARA_TYPE字段
	 */
	public <T> void convertDict(List<T> datas, String field, String paramType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(field, paramType);
		convertDict(datas, params);
	}

	/**
	 * 将单个对象中的编码字段转换为显示值
	 * 
	 * @param           <T>
	 * @param t         要转换的数据对象
	 * @param field     对象中要转换的字段名，必须与javabean中字段名一致
	 * @param paramType 参数类型，对应数据库中的PARA_TYPE字段
	 */
	public <T> void convertParam(T t, String field, String paramType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(field, paramType);

		List<T> datas = new ArrayList<T>();
		datas.add(t);
		convertParam(datas, params);
	}

	/**
	 * 将单个对象中的编码字段转换为显示值
	 * 
	 * @param           <T>
	 * @param t         要转换的数据对象
	 * @param field     对象中要转换的字段名，必须与javabean中字段名一致
	 * @param paramType 参数类型，对应数据库中的PARA_TYPE字段
	 */
	public <T> void convertDict(T t, String field, String paramType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(field, paramType);

		List<T> datas = new ArrayList<T>();
		datas.add(t);
		convertDict(datas, params);
	}

	public <T> void convertParam(T t, Map<String, String> map) {
		List<T> datas = new ArrayList<T>();
		datas.add(t);
		convertParam(datas, map);
	}

	public <T> void convertDict(T t, Map<String, String> map) {
		List<T> datas = new ArrayList<T>();
		datas.add(t);
		convertDict(datas, map);
	}

	/**
	 * 转换单个对象的指定属性
	 * @param request
	 * @param bean
	 * @param source
	 * @param fieldName
	 * @return
	 */
	public <T> T convertBean(T bean, Map<String, ?> source, String sourceField, String... fieldName) {
		List<T> list = new ArrayList<T>();
		list.add(bean);
		convertList(list, source, sourceField, fieldName);
		return bean;
	}
	/**
	 * 转换多个对象的指定属性
	 * @param request
	 * @param datas
	 * @param source
	 * @param fieldName
	 */
	public <T> void convertList(List<?> datas, Map<String, T> source, String sourceField, String... fieldNames) {
		if (datas == null || datas.size() == 0 || source == null || fieldNames == null || fieldNames.length == 0) return;
		//增加map类型的转换
		if(datas.get(0) instanceof java.util.HashMap || datas.get(0) instanceof java.util.Map){
			for (String fieldName : fieldNames) {
				for(int i=0; i<datas.size(); i++){
					Map map = (Map)datas.get(i);
					T t = source.get(map.get(fieldName));
					if(t == null){
						continue;
					}
					try {
						Method gt = t.getClass().getMethod("get" + StrUtils.fistCapital(sourceField));
						map.put(fieldName, gt.invoke(t));
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			return;
		}
		//增加map类型的转换结束
		for (String fieldName : fieldNames) {
			String mname = StrUtils.fistCapital(fieldName);
			Method setter = null;
			Method getter = null;
			for (Object o : datas) {
				try {
					if (setter == null) {
						try {
							setter = o.getClass().getMethod("set" + mname, String.class);
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
					if (getter == null) {
						try {
							getter = o.getClass().getMethod("get" + mname);
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
					if (setter != null && getter != null) {
						T t = null;
						try {
							t = (T) source.get(getter.invoke(o));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						String m = StrUtils.fistCapital(sourceField);
						if (t != null) {
							Method gt = null;
							try {
								gt = t.getClass().getMethod("get" + m);
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							}
							if (gt != null)
								try {
									setter.invoke(o, gt.invoke(t));
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
						}
					}
				} catch (BudaosException e) {
				}
			}
		}
	}
	/**
	 * 转换用户ID为用户名称
	 * @param request
	 * @param datas
	 * @param fieldName
	 * @author zhuq
	 * @since 2019-05-24
	 */
	public void convertUserId(List<?> datas, String... fieldName) {
		convertList(datas, getAllUsers(), "username", fieldName);
	}
	/**
	 * 转换用户ID为用户真实名称
	 * @param request
	 * @param datas
	 * @param fieldName
	 * @author zhuq
	 * @since 2019-05-24
	 */
	public void convertUserId2RealName(List<?> datas, String... fieldName) {
		convertList(datas, getAllUsers(), "userRealname", fieldName);
	}
	/**
	 * 转换某一个对象的用户ID为用户名称
	 * @param request
	 * @param bean
	 * @param fieldName
	 * @author zhuq
	 * @since 2019-05-24
	 */
	public <T> void convertUserId(T bean, String... fieldName) {
		List<T> list = new ArrayList<T>();
		list.add(bean);
		convertList(list, getAllUsers(), "username", fieldName);
	}
	/**
	 * 转换机构ID为机构名称
	 * @param request
	 * @param datas
	 * @param fieldName
	 * @author zhuq
	 * @since 2019-05-24
	 */
	public void convertOrgId(List<?> datas, String... fieldName) {
		convertList(datas, getAllOrgs(), "orgName", fieldName);
	}
	/**
	 * 转换某一个对象的机构号为机构名称
	 * @param request
	 * @param bean
	 * @param fieldName
	 * @author zhuq
	 * @since 2019-05-24
	 */
	public <T> void convertOrgId(T bean, String... fieldName) {
		List<T> list = new ArrayList<T>();
		list.add(bean);
		convertList(list, getAllOrgs(), "orgName", fieldName);
	}
	/**
	 * 取得所有的用户
	 * @param request
	 * @return
	 * @author zhuq
	 * @since 2019-05-24
	 */
	public Map<String, TsysUserinfo> getAllUsers() {
		List<TsysUserinfo> allusers = tsysUserinfoMapper.getAllUserinfo();
		return allusers.parallelStream().collect(Collectors.toMap(a -> ((TsysUserinfo)a).getUserId(), b -> b));
	}
	/**
	 * 取得所有的机构
	 * @param request
	 * @return
	 * @author zhuq
	 * @since 2019-05-24
	 */
	public Map<String, TsysOrg> getAllOrgs() {
		List<TsysOrg> allorgs = tsysOrgMapper.getAllOrgs();
		return allorgs.parallelStream().collect(Collectors.toMap(a -> ((TsysOrg)a).getOrgId(), b -> b));
	}
}
