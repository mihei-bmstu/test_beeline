import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window
import system.Parameters
import functions.LoadFile

object Boot {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("com").setLevel(Level.ERROR)

    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("test_Beeline")
      .getOrCreate()

    import spark.implicits._

    val dfCustomer = LoadFile.load(spark, Parameters.pathCustomer, Parameters.schemaCustomer)
      .select('ID.as("Customer_ID"), 'Name.as("Customer_name"))

    val dfOrder = LoadFile.load(spark, Parameters.pathOrder, Parameters.schemaOrder)
      .filter("status = 'delivered' ")

    val dfProduct = LoadFile.load(spark, Parameters.pathProduct, Parameters.schemaProduct)
      .select('ID.as("Product_ID"), 'Name.as("Product_name"))

    val window = Window.partitionBy('Customer_ID).orderBy('SumProducts.desc)

    val dfGrouped = dfOrder.groupBy('Customer_ID, 'Product_ID)
      .agg(sum('Number_Of_Products).as("SumProducts"))
      .withColumn("rn", row_number.over(window))
      .filter('rn === 1)
      .join(dfCustomer, usingColumn = "Customer_ID")
      .join(dfProduct, usingColumn = "Product_ID")
      .select('Customer_name, 'Product_name)

    dfGrouped.show(50)

    dfGrouped.repartition(1)
      .write
      .option("header", value = true)
      .mode("overwrite")
      .csv("src/main/resources/result.csv")

  }

}
