package functions

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

object LoadFile {
  def load(spark: SparkSession, pathTable: String, schemaTable: StructType): DataFrame = {
    spark.read
      .options(Map("delimiter" -> "\\t",
      "dateFormat" -> "yyyy-MM-dd"))
      .schema(schemaTable)
      .csv(pathTable)
  }

}
