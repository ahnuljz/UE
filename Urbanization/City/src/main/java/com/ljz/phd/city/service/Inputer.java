package com.ljz.phd.city.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.EasyExcel;
import com.ljz.phd.city.dao.CityDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class Inputer {
    @Autowired
    CityDao cityDao;

    public void noModelRead(Integer year, String fileName) {
        String name = FileUtil.getPrefix(fileName);
        EasyExcel.read(fileName, new NoModelDataListener(year, name, cityDao))
                .sheet()
                .headRowNumber(0)
                .doRead();
    }

    public static void main(String[] args) {
        log.info("33.3{}",NumberUtil.isNumber("33.3"));
        log.info("—0.8{}",NumberUtil.isNumber("—0.8"));
        log.info("-1.0{}",NumberUtil.isNumber("-1.0"));
        log.info("-12{}",NumberUtil.isNumber("-12"));
        log.info("-0{}",NumberUtil.isNumber("-0"));
        log.info("0{}",NumberUtil.isNumber("0"));
        log.info("{}","123.".substring(0, "123.".length()-1));

        log.info("表08 人口 >>>{}",parseHeader("表08 人口"));
        log.info("表08 人 >>>{}",parseHeader("表08 人"));
        log.info("三十二、科技机构及人员 >>>{}",parseHeader("三十二、科技机构及人员"));
        log.info("(五)贸易、外经 >>>{}",parseHeader("(五)贸易、外经"));
        log.info("十五.邮电通讯(包括市辖县) >>>{}",parseHeader("十五.邮电通讯(包括市辖县)"));
        log.info("二十九、职工平均工资 >>>{}",parseHeader("二十九、职工平均工资"));
        log.info("2-10 地区生产总值构成 >>>{}",parseHeader("2-10 地区生产总值构成"));
        log.info("2．按经济类型分(按1980年不变价格计算) >>>{}",parseHeader("2．按经济类型分(按1980年不变价格计算)"));
        log.info("十六、固定资产投资 >>>{}",parseHeader("十六、固定资产投资"));
    }


    private static String parseHeader(String s) {
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
            if(c.length()>1){
                String c2 = c.substring(0, 1);
                if (NumberUtil.isNumber(c2)) {
                    return c.substring(1);
                }
                if (NumberUtil.isNumber(c1)) {
                    return c;
                }
            }else{
                if (NumberUtil.isNumber(c1)) {
                    return c;
                }
            }
            return content;
        }
        return s;
    }
}


