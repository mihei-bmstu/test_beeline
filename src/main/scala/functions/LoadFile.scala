package functions

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

object LoadFile {
  def load(name: String, pathTable: String, schemaTable: StructType)(implicit spark: SparkSession): Unit = {
    spark.read
      .options(Map("delimiter" -> "\\t",
      "dateFormat" -> "yyyy-MM-dd"))
      .schema(schemaTable)
      .csv(pathTable)
      .createOrReplaceTempView(name)
  }

}
