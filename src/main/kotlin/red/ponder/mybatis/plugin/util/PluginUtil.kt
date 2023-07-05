package red.ponder.mybatis.plugin.util

import jdk.internal.joptsimple.internal.Strings
import java.io.File

/**
 * 检查文件是否存在
 */

fun fileExistCheck(fullName: String, basePath: String, warnings: ArrayList<String>) {
    val path = listOf(basePath, fullName.replace(".", File.separator), ".java").joinToString { File.separator }
    if (File(path).exists()) {
        warnings.add("""java file ${fullName} already exists in ${basePath} and will be overridden.""")
    }
}

/**
 * 获取类名
 */
fun getClassName(recordType: String): String = recordType.substring(recordType.lastIndexOf(".") + 1)
