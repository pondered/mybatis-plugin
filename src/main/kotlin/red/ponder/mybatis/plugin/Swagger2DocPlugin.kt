package red.ponder.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.TopLevelClass

class Swagger2DocPlugin : PluginAdapter() {
    private val modelAnnotationPackageStr = "apiModelAnnotationPackage"
    private val modelPropertyAnnotationPackageStr = "apiModelPropertyAnnotationPackage"

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
        val classAnnotation = """@ApiModel(value="${topLevelClass.type}"""".trim()

        val modelAnnotationPackage =
            properties.getProperty(modelAnnotationPackageStr, "io.swagger.annotations.ApiModel")
        val modelPropertyAnnotationPackage =
            properties.getProperty(modelPropertyAnnotationPackageStr, "io.swagger.annotations.ApiModelProperty")
        val javaDoc = properties.getProperty("generatorJavaDoc", "true").toBoolean()
        val swaggerDoc = properties.getProperty("generatorSwaggerDoc", "true").toBoolean()

        if (swaggerDoc) {
            topLevelClass.addImportedType(modelAnnotationPackage)
            topLevelClass.addImportedType(modelPropertyAnnotationPackage)
            field.addAnnotation(""" @ApiModelProperty(value="${introspectedColumn.javaProperty + introspectedColumn.remarks}" """.trim())
            if (!topLevelClass.annotations.contains(classAnnotation)) {
                topLevelClass.addAnnotation(classAnnotation)
            }
        }

        if (javaDoc) {
            field.apply {
                addJavaDocLine("/**")
                addJavaDocLine(introspectedColumn.remarks)
                addJavaDocLine("*/")
            }
        }


        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }

}