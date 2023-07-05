package red.ponder.mybatis.plugin

import org.mybatis.generator.api.FullyQualifiedTable
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.Interface
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.Parameter

/**
 * Add new methods with same name, which are appended with a RowBounds parameter
 * 在每个方法上添加 RowBounds 参数
 */
class RowBoundsPlugin : PluginAdapter() {

    private val rowBounds = FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds")

    private fun copyAndAddMethod(method: Method, interfaze: Interface) {
        val newMethod = Method(method)
        newMethod.addParameter(Parameter(rowBounds, "rowBounds"))
        interfaze.addMethod(newMethod)
        interfaze.addImportedType(rowBounds)
    }

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun clientSelectByExampleWithBLOBsMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable
    ): Boolean {
        copyAndAddMethod(method, interfaze)
        return true
    }

    override fun clientSelectByExampleWithoutBLOBsMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable
    ): Boolean {
        copyAndAddMethod(method, interfaze)
        return true
    }
}