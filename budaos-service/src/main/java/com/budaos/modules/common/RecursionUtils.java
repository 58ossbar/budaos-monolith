package com.budaos.modules.common;

import com.budaos.utils.tool.StrUtils;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RefreshScope
public class RecursionUtils {
	
	public List<Map<String, Object>> getTreeData(String parentId, List<Map<String, Object>> allList) {
		String curreIdKeyName = "id";
		String parentIdKeyName = "parentId";
		return buildBook(parentId, allList, 0, curreIdKeyName, parentIdKeyName);
	}
	
	/**
	 * 获取递归好的树形数据
	 * @param parentId
	 * @param allList
	 * @param configuration 配置选项
	 * @return
	 * @apiNote
	 * <p>
	 * configuration: {"curreIdKeyName": "id", "parentIdKeyName": "parentId", "children": "children"}<br>
	 * curreIdKeyName: 节点数据中保存唯一标识的属性名称，默认值id
	 * parentIdKeyName: 节点数据中保存其父节点唯一标识的属性名称，默认值parentId
	 * children: 节点数据中保存子节点数据的属性名称，默认值children，暂不支持更换
	 * </p>
	 */
	public List<Map<String, Object>> getTreeData(String parentId, List<Map<String, Object>> allList, Map<String, Object> configuration) {
		String curreIdKeyName = "id";
		String parentIdKeyName = "parentId";
		//String childrenKeyName = "children";
		if (configuration != null) {
			curreIdKeyName = StrUtils.isNull(configuration.get("curreIdKeyName")) ? curreIdKeyName : configuration.get("curreIdKeyName").toString();
			parentIdKeyName = StrUtils.isNull(configuration.get("parentIdKeyName")) ? parentIdKeyName : configuration.get("parentIdKeyName").toString();
			//childrenKeyName = StrUtils.isNull(configuration.get("childrenKeyName")) ? childrenKeyName : configuration.get("childrenKeyName").toString();
		}
		return buildBook(parentId, allList, 0, curreIdKeyName, parentIdKeyName);
	}
	
    /**
     * 获取递归好的树形数据
     * @param parentId 最顶级的id值
     * @param allList 需要处理的所有平级数据，确保此数据，有父子关系
     * @param curreIdKeyName 示例：id
     * @param parentIdKeyName 示例： parentId
     * @return
     */
    public List<Map<String, Object>> getTreeData(String parentId, List<Map<String, Object>> allList, String curreIdKeyName, String parentIdKeyName) {
       curreIdKeyName = StrUtils.isEmpty(curreIdKeyName) ? "id" : curreIdKeyName;
       parentIdKeyName = StrUtils.isEmpty(parentIdKeyName) ? "parentId" : parentIdKeyName;
       return buildBook(parentId, allList, 0, curreIdKeyName, parentIdKeyName);
    }

    /**
     * 递归构建树形数据
     * @param parentId
     * @param allList
     * @param level
     * @return
     */
    private List<Map<String, Object>> buildBook(String parentId, List<Map<String, Object>> allList, int level, String curreIdKeyName, String parentIdKeyName){
        if (allList == null || allList.size() == 0) {
            return null;
        }
        List<Map<String, Object>> nodeList = allList.stream().filter(a -> a.get(parentIdKeyName).equals(parentId)).collect(Collectors.toList());
        if (nodeList != null && nodeList.size() > 0) {
            level ++; // level计算当前处于第几级
            for (int i = 0; i < nodeList.size(); i++) {
                Map<String, Object> node = nodeList.get(i);
                node.put("level", level);
                List<Map<String, Object>> list = buildBook(node.get(curreIdKeyName).toString(), allList, level, curreIdKeyName, parentIdKeyName);
                node.put("children", list);
                if (list == null || list.size() == 0) {
                    node.put("children", null);
                    continue;
                }
            }
        }
        return nodeList;
    }

    /**
     * 过滤节点
     * @param allList
     * @param chapterName
     * @return
     */
    public List<Map<String, Object>> filter(List<Map<String, Object>> allList, String chapterName) {
        if (allList == null || allList.size() == 0) {
            return null;
        }
        List<Map<String, Object>> newArrayList = new ArrayList<Map<String,Object>>();
        allList.forEach(obj -> {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> children = (List<Map<String, Object>>) obj.get("children");
            List<Map<String, Object>> subs = filter(children, chapterName);
            if (isMatching(obj, chapterName)) {
                newArrayList.add(obj);
            } else if (subs != null && subs.size() > 0) {
                obj.put("children", subs);
                newArrayList.add(obj);
            }
        });
        return newArrayList;
    }

    /**
     * 精确匹配
     * @param obj
     * @param queryName
     * @return
     */
    private boolean isMatching(Map<String, Object> obj, String queryName) {
        String chapterName = (String)obj.get("chapterName");
        if (chapterName.indexOf(queryName) > -1) {
            return true;
        }
        return false;
    }

    /**
     * 处理序号
     * @param list
     */
    public void handleSortNum(List<Map<String, Object>> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        int index = 1;
        // 找到根节点
        for (Map<String, Object> node : list) {
            node.put("serial", index);
            deep(node, node);
            node.put("chapterName", +index+" "+node.get("chapterName"));
            index ++;
        }
    }

    /**
     * 递归处理序号
     * @param currNode 当前节点
     * @param parentNode 父节点
     */
    private void deep(Map<String, Object> currNode, Map<String, Object> parentNode) {
        int index = 0;
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> nodeList = (List<Map<String, Object>>) currNode.get("children");
        if (nodeList != null && nodeList.size() > 0) {
            for (Map<String, Object> node : nodeList) {
                index ++;
                node.put("serial", parentNode.get("serial")+"."+index);
                node.put("chapterName", parentNode.get("serial")+"."+index+" "+node.get("chapterName"));
                deep(node, node);

            }
        }
    }

}
