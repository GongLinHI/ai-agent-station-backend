package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiMcpToolMapper {

    /**
     * 新增MCP工具配置
     *
     * @param aiMcpTool MCP工具配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiMcpTool aiMcpTool);

    /**
     * 根据数据库ID删除MCP工具配置
     *
     * @param aiMcpTool MCP工具配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiMcpTool aiMcpTool);

    /**
     * 根据MCP业务ID删除MCP工具配置
     *
     * @param aiMcpTool MCP工具配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByMcpId(AiMcpTool aiMcpTool);

    /**
     * 根据数据库ID更新MCP工具配置
     *
     * @param aiMcpTool MCP工具配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiMcpTool aiMcpTool);

    /**
     * 根据MCP业务ID更新MCP工具配置
     *
     * @param aiMcpTool MCP工具配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByMcpId(AiMcpTool aiMcpTool);

    /**
     * 根据数据库ID查询MCP工具配置
     *
     * @param id 主键ID
     * @return MCP工具配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiMcpTool queryById(@Param("id") Long id);

    /**
     * 根据MCP业务ID查询MCP工具配置
     *
     * @param mcpId MCP业务ID
     * @return MCP工具配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiMcpTool queryByMcpId(@Param("mcpId") String mcpId);

    /**
     * 通用查询MCP工具配置列表
     *
     * @param aiMcpTool 查询条件对象
     * @return MCP工具配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiMcpTool> query(AiMcpTool aiMcpTool);
}
