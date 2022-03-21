package com.ljz.phd.city.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Map;

public class Helper {
    public static final String NUMBERS = "一二三四五六七八九十";

    public static final String[] PROVINCES = {"河北省", "山西省", "吉林省", "辽宁省", "黑龙江省",
            "陕西省", "甘肃省", "青海省", "山东省", "福建省", "浙江省", "台湾省", "河南省", "湖北省",
            "湖南省", "江西省", "江苏省", "安徽省", "广东省", "海南省", "四川省", "贵州省", "云南省",
            "内蒙古自治区", "广西壮族自治区", "西藏自治区", "宁夏回族自治区", "新疆维吾尔自治区",
            "北京市", "上海市", "天津市", "重庆市", "香港特别行政区", "澳门特别行政区"};

    public static final String[] ANHUI = {"安庆市", "蚌埠市", "亳州市", "池州市", "滁州市", "阜阳市",
            "合肥市", "淮北市", "淮南市", "黄山市", "六安市", "马鞍山市", "宿州市", "铜陵市", "芜湖市", "宣城市"};
    public static final String[] ZHEJIANG = {"杭州市", "湖州市", "嘉兴市", "金华市", "丽水市", "宁波市",
            "衢州市", "绍兴市", "台州市", "温州市", "舟山市"};
    public static final String[] JIANGSU = {"常州市", "淮安市", "连云港市", "南京市", "南通市", "苏州市",
            "宿迁市", "泰州市", "无锡市", "徐州市", "盐城市", "扬州市", "镇江市"};

    public static final String[] CITYS = {"衡阳市", "宁波市", "咸阳市", "金华市", "周口市", "驻马店市",
            "赤峰市", "日照市", "桂林市", "林芝市", "中山市", "云浮市", "双鸭山市", "昭通市", "黑河市", "永州市",
            "自贡市", "安顺市", "保山市", "廊坊市", "黄山市", "娄底市", "晋城市", "荆门市", "南充市", "泰安市",
            "陇南市", "铁岭市", "连云港市", "丽水市", "巴彦淖尔市", "临沧市", "嘉兴市", "武汉市", "茂名市", "梅州市",
            "山南市", "泉州市", "潮州市", "遂宁市", "辽阳市", "铜陵市", "白城市", "昌都市", "崇左市", "沈阳市",
            "张家口市", "包头市", "黄石市", "马鞍山市", "三门峡市", "河池市", "渭南市", "石嘴山市", "苏州市", "商洛市",
            "定西市", "吕梁市", "台州市", "唐山市", "张掖市", "葫芦岛市", "宿迁市", "承德市", "衡水市", "龙岩市",
            "舟山市", "六盘水市", "九江市", "佛山市", "盐城市", "随州市", "广元市", "哈密市", "曲靖市", "东营市",
            "中卫市", "岳阳市", "湛江市", "晋中市", "松原市", "株洲市", "武威市", "遵义市", "广安市", "张家界市",
            "榆林市", "防城港市", "贵阳市", "玉林市", "哈尔滨市", "济南市", "常德市", "德阳市", "通化市", "泰州市",
            "成都市", "漳州市", "平顶山市", "锦州市", "亳州市", "鞍山市", "伊春市", "开封市", "四平市", "三亚市",
            "日喀则市", "沈阳市", "宿州市", "淮安市", "雅安市", "怀化市", "上饶市", "大连市", "芜湖市", "菏泽市",
            "朝阳市", "阳泉市", "鸡西市", "鹤岗市", "徐州市", "惠州市", "湖州市", "贺州市", "汉中市", "乌兰察布市",
            "河源市", "柳州市", "常州市", "阜阳市", "十堰市", "濮阳市", "乌鲁木齐市", "无锡市", "南昌市", "鹰潭市",
            "景德镇市", "潍坊市", "巴中市", "三明市", "黄冈市", "朔州市", "百色市", "邢台市", "忻州市", "商丘市",
            "佳木斯市", "烟台市", "襄阳市", "德州市", "鹤壁市", "嘉峪关市", "营口市", "南通市", "牡丹江市", "温州市",
            "眉山市", "宜宾市", "毕节市", "江门市", "普洱市", "本溪市", "资阳市", "池州市", "南平市", "铜仁市", "衢州市",
            "合肥市", "安阳市", "庆阳市", "淮南市", "汕尾市", "东莞市", "吐鲁番市", "邯郸市", "延安市", "平凉市", "珠海市",
            "长春市", "孝感市", "宜昌市", "邵阳市", "青岛市", "临沂市", "天津市", "辽源市", "鄂州市", "福州市", "保定市",
            "兰州市", "湘潭市", "吴忠市", "西安市", "扬州市", "北京市", "新余市", "洛阳市", "赣州市", "抚州市", "吉安市",
            "银川市", "南阳市", "清远市", "六安市", "深圳市", "石家庄市", "鄂尔多斯市", "克拉玛依市", "七台河市", "上海市",
            "金昌市", "乌海市", "齐齐哈尔市", "威海市", "吉林市", "绍兴市", "蚌埠市", "漯河市", "内江市", "沧州市", "贵港市",
            "玉溪市", "白银市", "西宁市", "达州市", "乐山市", "宣城市", "长沙市", "抚顺市", "梧州市", "萍乡市", "临汾市",
            "攀枝花市", "阜新市", "丽江市", "海东市", "呼伦贝尔市", "盘锦市", "太原市", "莆田市", "泸州市", "固原市", "绥化市",
            "滁州市", "郑州市", "昆明市", "焦作市", "天水市", "淮北市", "滨州市", "拉萨市", "大同市", "安庆市", "荆州市", "许昌市",
            "南宁市", "汕头市", "酒泉市", "重庆市", "儋州市", "杭州市", "揭阳市", "通辽市", "宁德市", "宜春市", "绵阳市",
            "广州市", "安康市", "新乡市", "宝鸡市", "秦皇岛市", "淄博市", "枣庄市", "济宁市", "聊城市", "咸宁市", "运城市",
            "韶关市", "那曲市", "铜川市", "丹东市", "大庆市", "来宾市", "厦门市", "肇庆市", "海口市", "白山市", "郴州市",
            "阳江市", "北海市", "南京市", "钦州市", "长治市", "信阳市", "三沙市", "益阳市", "呼和浩特市", "镇江市"};

    public static String getProvinceName(String str) {
        if (StrUtil.isEmpty(str)) {
            return "";
        }
        str = replaceXXX(str);
        for (String province : PROVINCES) {
            if (province.contains(str) && !province.equals(str)) {
                return province;
            }
        }
        return str;
    }

    public static String getCityName(String str) {
        if (StrUtil.isEmpty(str)) {
            return "";
        }
        str = replaceXXX(str);
        for (String city : CITYS) {
            if (city.equals(str)) {
                return city;
            } else {
                if (city.equals(str + "市")) {
                    return city;
                }
            }
        }
        return str;
    }

    public static String getYZriverProvince(String str,String province) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }
        for (String city : ANHUI) {
            if (city.equals(str)) {
                return "安徽省";
            }
        }
        for (String city : ZHEJIANG) {
            if (city.equals(str)) {
                return "浙江省";
            }
        }
        for (String city : JIANGSU) {
            if (city.equals(str)) {
                return "江苏省";
            }
        }
        return province;
    }

    public static String replaceXXX(String str) {
        if (StrUtil.isEmpty(str)) {
            return "";
        }
        str = str.replace("·", "")
                .replace("=", "")
                .replace("‘", "")
                .replace("_", "")
                .replace("．", "")
                .replace(".", "")
                .replace("-", "")
                .replace("'", "")
                .replace("/", "")
                .replace("、", "")
                .replace("l", "")
                .replace("—", "");
        str = StringUtils.trimAllWhitespace(str);
        return str;
    }

    public static boolean hasChinese(String str) {
        if (str == null) {
            return false;
        }
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (c >= 0x4E00 && c <= 0x9FBF) {
                return true;
            }
        }
        return false;
    }

    public static void outputlog(String str) {
        File file = FileUtil.file("C:\\Users\\lee\\Desktop\\err.txt");
        FileAppender appender = new FileAppender(file, 16, true);
        appender.append(str);
        appender.flush();
    }
}
