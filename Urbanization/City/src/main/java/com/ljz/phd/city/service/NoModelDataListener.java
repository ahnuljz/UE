package com.ljz.phd.city.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.ljz.phd.city.dao.CityDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {
    private Map<Integer, String> lineProvince = new HashMap<>();
    private List<Map<Integer, String>> headerList = ListUtils.newArrayListWithExpectedSize(8);
    private final CityDao cityDao;
    private Integer year = 0;
    private String filename = "";
    private Integer sn = 0;

    private static final int BATCH_COUNT = 50;
    private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public NoModelDataListener(Integer year, String filename, CityDao cityDao) {
        this.cityDao = cityDao;
        this.year = year;
        this.filename = filename;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        sn += 1;
        int columnSize = data.size();
        Map<String, Boolean> which = whichCell(sn, data);
        if (sn < 9 && which.get("isHeader")) {
            headerList.add(data);
        }
        //省信息
        if (which.get("isProvince")) {
            lineProvince = data;
        }
        //目标
        if (which.get("isCity")) {
            int ensize = year>=2012 ? 1 : 0;
            int datsize = columnSize - ensize - 1;
            data.put(900, String.valueOf(ensize));
            data.put(901, String.valueOf(year));
            data.put(902, getHeader(0));
            data.put(903, getHeader(1));
            data.put(904, getHeader(2));
            data.put(905, getHeader(3));
            data.put(906, getHeader(4));
            data.put(907, getHeader(5));
            data.put(908, getHeader(6));
            data.put(909, getHeader(7));
            data.put(910, getProvince());
            data.put(911, String.valueOf(datsize));
            data.put(912, filename);
            data.put(913, String.valueOf(sn));
            data.put(914, String.valueOf(columnSize));

            cachedDataList.add(data);
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("该文件解析完成！[{}]", getHeader(0));
    }

    private void saveData() {
        if (cachedDataList.size() == 0) {
            return;
        }
        int ensize = Integer.parseInt(cachedDataList.get(0).get(900));
        int columnSize = cachedDataList.get(0).size() - 15;
        int datsize = columnSize - ensize - 1;
        if (datsize < 1) {
            Helper.outputlog("err0:无效数据" + JSON.toJSONString(cachedDataList));
            return;
        }
        //cityDao.validate(cachedDataList);
        cityDao.save(cachedDataList);
    }

    private String getHeader(int index) {
        int size = headerList.size();
        if (size == 0 || index > size - 1) {
            return "";
        }

        Map<Integer, String> map = headerList.get(index);
        StringBuilder sb = new StringBuilder("");
        int notBlankSize = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            String value = entry.getValue();
            if (StrUtil.isNotBlank(value)) {
                notBlankSize++;
                sb.append(parseHeader(value));
            }
        }

        return notBlankSize == 1 ? sb.toString() : JSON.toJSONString(map);
    }

    private String getProvince() {
        return lineProvince.isEmpty() ? "" : Helper.getProvinceName(lineProvince.get(0));
    }

    private String parseHeader(String s) {
        if (s.length() <= 2) {
            return s;
        }
        s = StringUtils.trimAllWhitespace(s)
                .replaceFirst("－", "-")
                .replaceFirst("—", "-")
                .replaceFirst("．", "-1")
                .replaceFirst("\\.", "-1")
                .replaceAll("　", "");

        String left1 = s.substring(0, 1);
        //表18主要农产品产量   表07人口  -1-07人口
        if (left1.equals("表")) {
            s = s.replaceFirst("表", "1-");
        }
        //(五)贸易、外经
        if (left1.equals("(")) {
            s = s.replaceFirst("\\)", "-1");
        }
        if (s.length() >= 4) {
            String left2 = s.substring(1, 2);
            String left3 = s.substring(2, 3);
            String left4 = s.substring(3, 4);
            //三十二、科技机构及人员
            if (Helper.NUMBERS.contains(left1) && left2.equals("、")) {
                s = s.replaceFirst("、", "-1");
            }
            if (Helper.NUMBERS.contains(left2) && left3.equals("、")) {
                s = s.replaceFirst("、", "-1");
            }
            if (Helper.NUMBERS.contains(left3) && left4.equals("、")) {
                s = s.replaceFirst("、", "-1");
            }
        }

        return deleteHeaderPrefix(s);
    }

    //删除表头中的数字编号 如 2-10xxx >>xxx
    private static String deleteHeaderPrefix(String s) {
        String[] ss = s.split("-");
        if (ss.length == 2) {
            String content = ss[1];
            String c1 = content.substring(0, 1);
            String c = content.substring(1);
            if (c.length() > 1) {
                String c2 = c.substring(0, 1);
                if (NumberUtil.isNumber(c2)) {
                    return c.substring(1);
                }
                if (NumberUtil.isNumber(c1)) {
                    return c;
                }
            } else {
                if (NumberUtil.isNumber(c1)) {
                    return c;
                }
            }
            return content;
        }
        return s;
    }

    //
    private Map<String, Boolean> whichCell(int sn, Map<Integer, String> cell) {
        boolean isCity = false;
        boolean isProvince = false;
        boolean isHeader = false;

        int dataItemSize = 0;
        int chineseItemSize = 0;
        int notBlankItemSize = 0;
        String p0 = cell.get(0);
        String p1 = cell.get(1);
        p0 = Helper.replaceXXX(p0);
        p1 = Helper.replaceXXX(p1);

        for (Map.Entry<Integer, String> entry : cell.entrySet()) {
            Integer index = entry.getKey();
            String v = entry.getValue();
            v = StrUtil.isEmpty(v) ? "" : v;
            v = StringUtils.trimAllWhitespace(v).replace(",", "").replace("，", "").replace("　", "");
            dataItemSize += (index >= 1 && NumberUtil.isNumber(v)) ? 1 : 0;
            chineseItemSize += Helper.hasChinese(v) ? 1 : 0;
            notBlankItemSize += StrUtil.isNotBlank(v) ? 1 : 0;
        }

        //表头范围
        if (sn < 9 && dataItemSize == 0 && notBlankItemSize > 0 && chineseItemSize > 0) {
            isHeader = true;
        }

        //数据范围
        if (dataItemSize > 0 && chineseItemSize == 1) {
            isCity = true;
            isHeader = false;
        }
        if (chineseItemSize == 1 && StrUtil.isNotBlank(p0)) {
            for (String pro : Helper.PROVINCES) {
                if (pro.equals(p0) || pro.contains(p0)) {
                    isProvince = true;
                    break;
                }
            }
        }

        Map<String, Boolean> whats = new HashMap<>();
        whats.put("isCity", isCity);
        whats.put("isProvince", isProvince);
        whats.put("isHeader", isHeader);
        return whats;
    }
}
