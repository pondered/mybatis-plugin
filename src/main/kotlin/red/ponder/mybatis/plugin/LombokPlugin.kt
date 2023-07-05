package red.ponder.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.TopLevelClass
import java.lang.StringBuilder

/**
 * 添加 Lombok 注解
 * 用法  value 中的第一个参数必须为 boolean 值
 *      name=accessors,value=true|chain=true|fluent=true
 * 结果为
 *      @Accessors(chain=true,fluent=true)
 */
class LombokPlugin : PluginAdapter() {

    private fun populateLombokAnnotation(topLevelClass: TopLevelClass) {
        Annotations.values().forEach {
            val property = properties.getProperty(it.property, it.defaultValue)
            val split = property.split("|")
            if (split[0].toBoolean()) {
                topLevelClass.addImportedType(it.importType)

                val str = StringBuffer("(")
                for ((index, s) in split.withIndex()) {
                    if (index == 0) {
                        continue
                    }
                    str.append("$s,")
                }
                var removeRange = str.dropLast(1).toString()
                removeRange += ")"
                topLevelClass.addAnnotation(it.annotation + removeRange)
            }
        }
    }

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }


    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable
    ): Boolean {
        populateLombokAnnotation(topLevelClass)
        return true
    }

    override fun modelPrimaryKeyClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable
    ): Boolean {
        populateLombokAnnotation(topLevelClass)
        return true
    }

    override fun modelRecordWithBLOBsClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable
    ): Boolean {
        populateLombokAnnotation(topLevelClass)
        return true
    }

    override fun modelGetterMethodGenerated(
        method: Method,
        topLevelClass: TopLevelClass,
        introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable,
        modelClassType: Plugin.ModelClassType
    ): Boolean {
        return false
    }

    override fun modelSetterMethodGenerated(
        method: Method,
        topLevelClass: TopLevelClass,
        introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable,
        modelClassType: Plugin.ModelClassType
    ): Boolean {
        return false
    }


    private enum class Annotations(
        val property: String,
        val annotation: String,
        val importType: String,
        val defaultValue: String
    ) {
        DATA("data", "@Data", "lombok.Data", "true"),
        BUILDER("builder", "@Builder", "lombok.Builder", "false"),
        SUPER_BUILDER("superBuilder", "@SuperBuilder", "lombok.experimental.SuperBuilder", "false"),
        TO_STRING("toString", "@ToString", "lombok.ToString", "false"),
        ALL_CONSTRUCTOR("allConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor", "false"),
        NO_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor", "false"),
        REQUIRE_CONSTRUCTOR(
            "requiredArgsConstructor",
            "@RequiredArgsConstructor",
            "lombok.RequiredArgsConstructor",
            "false"
        ),
        ACCESSORS("accessors", "@Accessors", "lombok.experimental.Accessors", "false"),
        ;


    }
}