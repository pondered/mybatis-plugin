package red.ponder.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.JavaVisibility
import org.mybatis.generator.api.dom.java.TopLevelClass

/**
 * 序列化接口
 */
class SerializablePlugin : PluginAdapter() {
    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    private fun implSerializable(topLevelClass: TopLevelClass) {
        Annotations.SERIALIZABLE.method(topLevelClass)
    }

    override fun modelPrimaryKeyClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable
    ): Boolean {
        implSerializable(topLevelClass)
        return true
    }

    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable
    ): Boolean {
        implSerializable(topLevelClass)
        return true
    }

    override fun modelRecordWithBLOBsClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable
    ): Boolean {
        implSerializable(topLevelClass)
        return true
    }

    private enum class Annotations(
        val property: String,
        val annotation: String,
        val importType: String,
        val defaultValue: String
    ) {
        SERIALIZABLE("serializable", "Serializable", "java.io.Serializable", "true") {
            override fun method(topLevelClass: TopLevelClass) {
                topLevelClass.addImportedType(SERIALIZABLE.importType)
                topLevelClass.addSuperInterface(FullyQualifiedJavaType(SERIALIZABLE.annotation))

                val field = Field("serialVersionUID", FullyQualifiedJavaType("long"))
                field.apply {
                    isFinal = true
                    isStatic = true
                    visibility = JavaVisibility.PRIVATE
                    setInitializationString("${System.nanoTime()}L".trim())
                }
                topLevelClass.addField(field)
            }
        };

        abstract fun method(topLevelClass: TopLevelClass);
    }
}