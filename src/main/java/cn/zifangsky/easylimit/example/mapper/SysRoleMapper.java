package cn.zifangsky.easylimit.example.mapper;

import cn.zifangsky.easylimit.example.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    Set<SysRole> selectByUserId(@Param("userId") Long userId);
}