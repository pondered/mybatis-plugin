package red.ponder.mybatis.plugin

import org.mybatis.generator.api.JavaTypeResolver
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl
import java.sql.Types
import kotlin.reflect.full.staticProperties

/**
 * 类型转换
 * name 为mysql 类型 value 为 java类型
 * 如:
 *  name=TINYINT, value=Integer
 */
class TypeConvertResolver : JavaTypeResolverDefaultImpl, JavaTypeResolver {
    private val map: HashMap<String, Int> = hashMapOf()

    constructor() : super() {
        type2Int()
        properties.stringPropertyNames().forEach {
            var value = map[it.lowercase()]
            typeMap[value] = JdbcTypeInformation(it, FullyQualifiedJavaType(properties.getProperty(it)))
        }
    }


    private fun type2Int() {
        Types::class.staticProperties.forEach {
            map[it.name.lowercase()] = it.get().toString().toInt()
        }

    }

}
