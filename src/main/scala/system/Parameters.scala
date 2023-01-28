package system

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import functions.LoadFile

object Parameters {
  val tables: Seq[String] = Seq("Customer", "Order", "Product")
  val pathCustomer = "src/main/resources/customer.csv"
  val pathOrder = "src/main/resources/order.csv"
  val pathProduct = "src/main/resources/product.csv"
  val pathTables: Map[String, String] = Map("Customer" -> pathCustomer,
                                            "Order" -> pathOrder,
                                            "Product" -> pathProduct)

  val schemaCustomer: StructType = StructType(
    StructField("ID", IntegerType, nullable = false) ::
      StructField("Name", StringType, nullable = true) ::
      StructField("Email", StringType, nullable = true) ::
      StructField("Date", DateType, nullable = true) ::
      StructField("Status", StringType, nullable = true) ::
      Nil)

  val schemaOrder: StructType = StructType(
    StructField("Customer_ID", IntegerType, nullable = false) ::
      StructField("Order_ID", IntegerType, nullable = false) ::
      StructField("Product_ID", IntegerType, nullable = false) ::
      StructField("Number_Of_Products", IntegerType, nullable = true) ::
      StructField("Order_Date", DateType, nullable = true) ::
      StructField("Status", StringType, nullable = true) ::
      Nil)

  val schemaProduct: StructType = StructType(
    StructField("ID", IntegerType, nullable = false) ::
      StructField("Name", StringType, nullable = true) ::
      StructField("Price", DoubleType, nullable = true) ::
      StructField("Number_Of_Products", IntegerType, nullable = true) ::
      Nil)

  val schemes: Map[String, StructType] = Map(
    "Customer" -> schemaCustomer,
    "Order" -> schemaOrder,
    "Product" -> schemaProduct
  )

  def initTables(implicit spark: SparkSession): Unit = {
    tables.foreach(table => LoadFile.load(table,
      pathTables.getOrElse(table, "No such table"),
      schemes(table))
    )
  }

}
