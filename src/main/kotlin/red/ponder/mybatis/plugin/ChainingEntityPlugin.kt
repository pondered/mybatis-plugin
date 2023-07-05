package red.ponder.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.TopLevelClass

/**
 * @author ponder
 * Whether or not setters should return themselves (chaining)
 * 对entity 的set 方法返回当前实例，用于链式调用
 */
class ChainingEntityPlugin : PluginAdapter() {
    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun modelSetterMethodGenerated(
        method: Method, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: Plugin.ModelClassType
    ): Boolean {
        method.setReturnType(topLevelClass.type)
        method.addBodyLine("return this")
        return super.modelSetterMethodGenerated(
            method, topLevelClass, introspectedColumn,
            introspectedTable, modelClassType
        )
    }
}