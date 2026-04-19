package com.budaos.modules.evgl.pkg.service;

import com.budaos.modules.common.PkgPermissionUtils;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.pkg.api.ExportPackageService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResgroupMapper;
import com.budaos.modules.evgl.pkg.vo.ResVO;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huj
 * @create 2022-10-08 15:52
 * @email hujun@budaos.com
 */
@Service
public class ExportPackageServiceImpl implements ExportPackageService {

    private Logger log = LoggerFactory.getLogger(ExportPackageServiceImpl.class);

    @Autowired
    private TevglBookSubjectMapper tevglBookSubjectMapper;
    @Autowired
    private TevglBookChapterMapper tevglBookChapterMapper;
    @Autowired
    private TevglPkgInfoMapper tevglPkgInfoMapper;
    @Autowired
    private TevglPkgResgroupMapper tevglPkgResgroupMapper;

    @Value("${com.budaos.domain:}")
    private String domain;

    @Autowired
    private PkgPermissionUtils pkgPermissionUtils;
    
    /**
     * 根据课程，组织word文档内容
     * @param pkgId
     * @param subjectId
     * @param traineeId
     */
    @Override
    public String initWordDataBySubjectId(String pkgId, String subjectId, String ctId, String traineeId) {
        StringBuffer sb = new StringBuffer();
        if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId)) {
            return sb.toString();
        }
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (tevglPkgInfo == null) {
            log.error("无效的教学包");
            return sb.toString();
        }
        TevglBookSubject doc = tevglBookSubjectMapper.selectObjectById(subjectId);
        if (doc == null){
            return sb.toString();
        }
        // TODO 权限校验
        if (!checkHasPermission(pkgId, ctId, traineeId)) {
            log.info("没有权限导出word数据");
            sb.append("<h1 style=\"text-align: center;\"><strong>没有权限导出word数据</strong></h1>");
            return sb.toString();
        }
        // 文档标题(h1居中)
        sb.append("<h1 style=\"text-align: center;\"><strong>"+doc.getSubjectName()+"</strong></h1>");
        // 遍历章节
        Map<String, Object> map = new HashMap<>();
        map.put("state", "Y");
        map.put("subjectId", subjectId);
        map.put("sidx", "order_num");
        map.put("order", "asc");
        List<TevglBookChapter> allChapterList = tevglBookChapterMapper.selectListByMap(map);
        Map<String, Object> params = new HashMap<>();
        if (allChapterList != null && !allChapterList.isEmpty()) {
            List<TevglBookChapter> chapters = allChapterList.stream().filter(a -> a.getParentId().equals(subjectId)).collect(Collectors.toList());
            Stream.iterate(0, i -> i + 1).limit(chapters.size()).forEach(i -> {
                // 章节标题
                sb.append("<h1 style=\"text-align: center;\"><strong>第"+(i+1)+"章&nbsp;"+chapters.get(i).getChapterName()+"</strong></h1>");
                // 内容
                //sb.append(StrUtils.toString(chapters.get(i).getChapterContent()));
                handleContent(sb, pkgId, subjectId, chapters.get(i).getChapterId(), params);
                // 遍历2级章节
                List<TevglBookChapter> knowledges = allChapterList.stream().filter(a -> a.getParentId().equals(chapters.get(i).getChapterId())).collect(Collectors.toList());
                Stream.iterate(0, j -> j + 1).limit(knowledges.size()).forEach(j -> {
                    // 章节知识点标题
                    sb.append("<h2><strong>"+(i+1)+"."+(j+1)+"&nbsp;"+knowledges.get(j).getChapterName()+"</strong></h2>");
                    // 章节知识点内容（测试发现，这里可能导致章节顺序错乱，内容里面的h1-h6标签引起的）
                    handleContent(sb, pkgId, subjectId, knowledges.get(j).getChapterId(), params);
                    // 遍历3级章节
                    List<TevglBookChapter> chapters3 = allChapterList.stream().filter(a -> a.getParentId().equals(knowledges.get(j).getChapterId())).collect(Collectors.toList());
                    Stream.iterate(0, k -> k + 1).limit(chapters3.size()).forEach(k -> {
                        // 章节标题
                        sb.append("<h3><strong>"+(i+1)+"."+(j+1)+"."+(k+1)+"&nbsp;"+chapters3.get(k).getChapterName()+"</strong></h3>");
                        // 内容
                        handleContent(sb, pkgId, subjectId, chapters3.get(k).getChapterId(), params);
                    });
                });
            });
        }
        return sb.toString();
    }

    private void handleContent(StringBuffer sb, String pkgId, String subjectId, String chapterId, Map<String, Object> params) {
        params.clear();
        params.put("ctPkgId", pkgId);
        params.put("chapterId", chapterId);
        params.put("pkgId", pkgId);
        params.put("sidx", "t1.sort_num");
        params.put("order", "asc");
        params.put("subjectId", subjectId);
        List<Map<String, Object>> resList = tevglPkgResgroupMapper.selectSimpleListMap2(params);
        Stream.iterate(0, h -> h + 1).limit(resList.size()).forEach(h -> {
            if (!StrUtils.isNull(resList.get(h).get("resContent"))) {
                //sb.append("<strong>"+"&nbsp;"+resList.get(h).get("resgroupName")+"</strong>");
                sb.append(StrUtils.toString(resList.get(h).get("resContent")));
            }
        });
    }

    /**
     * 替换html中所有img标签中src的地址(拼接上完整的域名)
     * @param content
     * @return
     */
    private String replaceImgSrc(String content){
        if(StrUtils.isEmpty(content)){
            return content;
        }
        String regStr = "src\\s*=\\s*\"?(.*?)(\"|>|\\s+)";
        String result = new String(content);
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(content);
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile(regStr).matcher(img);
            while (m.find()) {
                String str = m.group(1);
                result = result.replace(str,domain+str);
            }
        }
        return result;
    }

    /**
     * 检验是否有权限
     * @param pkgId
     * @param ctId
     * @param traineeId
     * @return
     */
    private boolean checkHasPermission(String pkgId, String ctId, String traineeId){
        if (StrUtils.isEmpty(ctId)) {
            return pkgPermissionUtils.hasPermissionAv2(pkgId, traineeId, null);
        } else {
            return true;
        }
    }

    @Override
    public String getHtmlString(String pkgId, String subjectId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("content", "");
        // 遍历章节
        Map<String, Object> map = new HashMap<>();
        map.put("state", "Y");
        map.put("subjectId", subjectId);
        map.put("sidx", "order_num");
        map.put("order", "asc");
        List<TevglBookChapter> allChapterList = tevglBookChapterMapper.selectListByMap(map);
        // 所有资源
        Map<String, Object> params = new HashMap<>();
        params.put("ctPkgId", pkgId);
        params.put("pkgId", pkgId);
        params.put("sidx", "t1.sort_num");
        params.put("order", "asc");
        params.put("subjectId", subjectId);
        List<ResVO> allResList = tevglPkgResgroupMapper.findSimpleList(params);
        if (allChapterList != null && !allChapterList.isEmpty()) {
            List<TevglBookChapter> chapters = allChapterList.stream().filter(a -> a.getParentId().equals(subjectId)).collect(Collectors.toList());
            for (int i = 0; i < chapters.size(); i++) {
                List<TevglBookChapter> knowledges = new ArrayList<>();
                String htmlString = dataMap.get("content").toString();
                // 章节标题
                String title1 = "<h1 style=\"text-align: center;\"><strong>第"+(i+1)+"章&nbsp;"+chapters.get(i).getChapterName()+"</strong></h1>";
                // 内容
                String content1 = getContentText(chapters.get(i).getChapterId(), allResList);
                // 累加赋值
                dataMap.put("content", dataMap.get("content") + title1 + content1);
                // 得到2级章节
                for (TevglBookChapter a : allChapterList) {
                    if (a.getParentId().equals(chapters.get(i).getChapterId())) {
                        knowledges.add(a);
                    }
                }
                // 遍历2级章节
                for (int j = 0; j < knowledges.size(); j++) {
                    List<TevglBookChapter> chapters3 = new ArrayList<>();
                    // 章节知识点标题
                    String title2 = "<h2><strong>"+(i+1)+"."+(j+1)+"&nbsp;"+knowledges.get(j).getChapterName()+"</strong></h2>";
                    // 章节知识点内容（测试发现，这里可能导致章节顺序错乱，内容里面的h1-h6标签引起的）
                    String content2 = getContentText(knowledges.get(j).getChapterId(), allResList);
                    // 累加赋值
                    dataMap.put("content", dataMap.get("content") + title2 + content2);
                    for (TevglBookChapter a : allChapterList) {
                        if (a.getParentId().equals(knowledges.get(j).getChapterId())) {
                            chapters3.add(a);
                        }
                    }
                    // 遍历3级章节
                    for (int k = 0; k < chapters3.size(); k++) {
                        // 章节标题
                        String title3 = "<h3><strong>"+(i+1)+"."+(j+1)+"."+(k+1)+"&nbsp;"+chapters3.get(k).getChapterName()+"</strong></h3>";
                        // 内容
                        String content3 = getContentText(chapters3.get(k).getChapterId(), allResList);
                        // 累加赋值
                        dataMap.put("content", dataMap.get("content") + title3 + content3);
                    }
                }
            }
        }
        return dataMap.get("content").toString();
    }

    /**
     * 拼接内容
     * @param chapterId
     * @param allResList
     * @return
     */
    private String getContentText(String chapterId, List<ResVO> allResList) {
        String content = "";
        List<ResVO> resList = allResList.stream().filter(a -> a.getChapterId().equals(chapterId)).collect(Collectors.toList());
        for (int h = 0; h < resList.size(); h++) {
            if (!StrUtils.isNull(resList.get(h).getResContent())) {
                content += StrUtils.toString(resList.get(h).getResContent());
            }
        }
        return content;
    }
}
