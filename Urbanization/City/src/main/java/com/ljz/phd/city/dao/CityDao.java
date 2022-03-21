package com.ljz.phd.city.dao;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.ljz.phd.city.mapper.CityTmpMapper;
import com.ljz.phd.city.model.CityTmp;
import com.ljz.phd.city.service.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class CityDao {
    @Autowired
    CityTmpMapper cityTmpMapper;

    public void validate(List<Map<Integer, String>> dataList) {
        List<CityTmp> list = new ArrayList<>();
        for (Map<Integer, String> data : dataList) {
            String year = data.get(901);
            String file = data.get(912);
            String row = data.get(913);

            for (Map.Entry<Integer, String> entry : data.entrySet()) {
                Integer index = entry.getKey();
                String value = entry.getValue();
                if (index >= 1 && index <= 12 && StrUtil.isNotBlank(value) && !isData(value)) {
                    log.error("===year【{}】,file【{}】,row【{}】,index【{}】,value【{}】,data【{}】", year, file, row, index, value, data);
                }
            }
        }
    }

    public void save(List<Map<Integer, String>> dataList) {
        List<CityTmp> list = new ArrayList<>();
        for (Map<Integer, String> data : dataList) {
            String city = Helper.getCityName(data.get(0));
            String province = data.get(910);
            province = Helper.getYZriverProvince(city, province);
            CityTmp tmp = CityTmp.builder()
                    .ensize(Integer.parseInt(data.get(900)))
                    .year(Integer.parseInt(data.get(901)))
                    .h1(parseHeader(data.get(902)))
                    .h2(parseHeader(data.get(903)))
                    .h3(parseHeader(data.get(904)))
                    .h4(parseHeader(data.get(905)))
                    .h5(parseHeader(data.get(906)))
                    .h6(parseHeader(data.get(907)))
                    .h7(parseHeader(data.get(908)))
                    .h8(parseHeader(data.get(909)))
                    .datsize(Integer.parseInt(data.get(911)))
                    .filename(data.get(912))
                    .sn(Integer.parseInt(data.get(913)))
                    .columnsize(Integer.parseInt(data.get(914)))
                    .city(city)
                    .province(province)
                    .d01(parseData(data, 1))
                    .d02(parseData(data, 2))
                    .d03(parseData(data, 3))
                    .d04(parseData(data, 4))
                    .d05(parseData(data, 5))
                    .d06(parseData(data, 6))
                    .d07(parseData(data, 7))
                    .d08(parseData(data, 8))
                    .build();
            list.add(tmp);
        }
        cityTmpMapper.insert(list);
    }

    private boolean isData(String s) {
        s = replacex(s);
        return NumberUtil.isNumber(s);
    }

    private String parseData(Map<Integer, String> data, int key) {
        boolean hasPinyin = data.get(900).equals("1");
        int datsize = Integer.parseInt(data.get(911));
        if (datsize < key) {
            return null;//不应有数据
        }
        key = hasPinyin ? key + 1 : key;
        String s = StringUtils.trimAllWhitespace(data.get(key));
        s = replacex(s);

        if (StrUtil.isEmpty(s)) {
            return "0";//空白数据，空值会导致计算错误
        }
        if (!NumberUtil.isNumber(s)) {
            return "-8888";//错误数据
        }
        return s;//正常数据
    }

    private String parseHeader(String s) {
        if (StrUtil.isEmpty(s)) {
            s = "";
        }
        return StrUtil.trim(s);
    }

    private String replacex(String s) {
        if (StrUtil.isEmpty(s)) {
            s = "";
        }
        s = s.replace("．", ".")
                .replace(",", "")
                .replace("，", "")
                .replace("—", "-")
                .replace("　", "")
                .replace(" ", "")
                .replace("'", "")
                .replace("O", "0")
                .replace("I", "1")
                .replace("S", "5")
                .replace("\"", "")
                .replace(":", ".")
                .replace("C", "0")
                .replace("Q", "0")
                .replace("l", "1");
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        if (s.endsWith("00%")) {
            s = s.substring(0, s.length() - 3);
        }
        if (s.equals("-")) {
            s = "0";
        }

        return s;
    }

}
