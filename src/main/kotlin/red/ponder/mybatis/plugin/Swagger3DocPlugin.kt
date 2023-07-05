package red.ponder.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.TopLevelClass

class Swagger3DocPlugin : PluginAdapter() {
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
        val javaDoc = properties.getProperty("generatorJavaDoc", "true").toBoolean()
        val swaggerDoc = properties.getProperty("generatorSwaggerDoc", "true").toBoolean()
        val modelAnnotationPackage =
            properties.getProperty("apiModelAnnotationPackage", "io.swagger.annotations.ApiModel")
        val modelPropertyAnnotationPackage =
            properties.getProperty("apiModelPropertyAnnotationPackage", "io.swagger.annotations.ApiModelProperty")

        val classAnnotation = if (useFullPathName) {
            """ @ApiModel(value="${topLevelClass.type}" """.trim()
        } else {
            "@ApiModel"
        }

        if (javaDoc) {
            field.apply {
                addJavaDocLine("/**")
                addJavaDocLine(introspectedColumn.remarks)
                addJavaDocLine("*/")
            }
        }

        if (swaggerDoc) {
            topLevelClass.addImportedType(modelAnnotationPackage)
            topLevelClass.addImportedType(modelPropertyAnnotationPackage)
            field.addAnnotation(""" @ApiModelProperty(value="${introspectedColumn.javaProperty + introspectedColumn.remarks}" """.trim())
            if (!topLevelClass.annotations.contains(classAnnotation)) {
                topLevelClass.addAnnotation(classAnnotation)
            }
        }

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }

}