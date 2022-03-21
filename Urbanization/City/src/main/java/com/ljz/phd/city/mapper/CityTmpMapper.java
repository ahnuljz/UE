package com.ljz.phd.city.mapper;

import com.ljz.phd.city.model.CityTmp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CityTmpMapper {
    @Insert({"<script>",
            "insert into city_tmp(fname,sn,datsize,columnsize,ensize,h1,h2,h3,h4,h5,h6,h7,h8,city,province,year,d01,d02,d03,d04,d05,d06,d07,d08) values ",
            "<foreach collection='list' item='rs' index='index' separator=','>",
            "(#{rs.filename},#{rs.sn},#{rs.datsize},#{rs.columnsize},#{rs.ensize},#{rs.h1},#{rs.h2},#{rs.h3},#{rs.h4},#{rs.h5},#{rs.h6},#{rs.h7},#{rs.h8},#{rs.city},#{rs.province},#{rs.year},",
            "#{rs.d01},#{rs.d02},#{rs.d03},#{rs.d04},#{rs.d05},#{rs.d06},#{rs.d07},#{rs.d08})",
            "</foreach>",
            "</script>"})
    int insert(@Param("list") List<CityTmp> list);
}