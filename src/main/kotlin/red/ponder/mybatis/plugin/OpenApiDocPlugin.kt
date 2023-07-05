package red.ponder.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.TopLevelClass


/**
 * @author ponder
 * entity filed  add OpenAPI
 * 在实体类上添加 OpenAPI 注解
 */
class OpenApiDocPlugin : PluginAdapter() {
    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }


    override fun modelFieldGenerated(
        field: Field,
        topLevelClass: TopLevelClass,
        introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable,
        modelClassType: Plugin.ModelClassType
    ): Boolean {
        val useFullPathName = properties.getProperty("useFullPathName", "false").toBoolean()
        val classAnnotation = if (!useFullPathName) {
            "@Schema"
        } else {
            """@Schema@(description="${topLevelClass.type}"""".trim()
        }
        if (!topLevelClass.annotations.contains(classAnnotation)) {
            topLevelClass.addAnnotation(classAnnotation)
        }
        val schemaAnnotation = "io.swagger.v3.oss.annotations.media.Schema"

        topLevelClass.addImportedType(schemaAnnotation)

        val javaDoc = properties.getProperty("javaDoc", "false").toBooleanStrict()
        if (javaDoc) {
            field.addJavaDocLine("/**")
            field.addJavaDocLine(introspectedColumn.remarks)
            field.addJavaDocLine("*/")
        }
        field.addAnnotation("""@Schema(description="${introspectedColumn.remarks}"""".trim())

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }
}