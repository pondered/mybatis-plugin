package red.ponder.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.TopLevelClass

/**
 * Builder Factory
 */
class ModelBuilderPlugin : PluginAdapter(){
    override fun validate(warnings: MutableList<String>?): Boolean {
        return true
    }


    override fun modelRecordWithBLOBsClassGenerated(
        topLevelClass: TopLevelClass?,
        introspectedTable: IntrospectedTable?
    ): Boolean {
        return super.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable)
    }
}