package com.resma.facma.db

import com.resma.facma.entity.Product
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class DatabaseSQL {

    object DatabaseConnection {
        private const val DB_URL = "jdbc:sqlite:products.db"

        fun connect(): Connection {
            return DriverManager.getConnection(DB_URL)
        }
    }

    fun initializeDatabase() {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                description TEXT,
                price REAL
            );
        """.trimIndent()

        DatabaseConnection.connect().use { connection ->
            connection.createStatement().use { statement ->
                statement.execute(createTableQuery)
            }
        }
    }

    fun addProduct(product: Product): Boolean {
        val insertQuery = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)"

        return try {
            DatabaseConnection.connect().use { connection ->
                connection.prepareStatement(insertQuery, java.sql.Statement.RETURN_GENERATED_KEYS).use { statement ->
                    statement.setString(1, product.name)
                    statement.setString(2, product.description)
                    statement.setDouble(3, product.price)

                    val affectedRows = statement.executeUpdate()

                    if (affectedRows > 0) {
                        val generatedKeys = statement.generatedKeys
                        if (generatedKeys.next()) {
                            product.id = generatedKeys.getInt(1)  // Asignar el id autoincrementado
                        }
                        true
                    } else {
                        false
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun checkIfNameAndDescriptionExist(name: String, description: String): Boolean {
        val query = "SELECT COUNT(*) FROM products WHERE name = ? AND description = ?"
        return try {
            DatabaseConnection.connect().use { connection ->
                connection.prepareStatement(query).use { statement ->
                    statement.setString(1, name)
                    statement.setString(2, description)
                    val resultSet = statement.executeQuery()
                    val count = if (resultSet.next()) resultSet.getInt(1) else 0
                    resultSet.close()
                    count > 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteProductByID(id: Int): Boolean {
        val deleteQuery = "DELETE FROM products WHERE id = ?"

        return try {
            DatabaseConnection.connect().use { connection ->
                connection.prepareStatement(deleteQuery).use { statement ->
                    statement.setInt(1, id)
                    statement.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun updateProduct(name: String, description: String, price: Double): Boolean {
        val updateQuery = """
            UPDATE products 
            SET description = ?, price = ?
            WHERE name = ?
        """.trimIndent()

        return try {
            DatabaseConnection.connect().use { connection ->
                connection.prepareStatement(updateQuery).use { statement ->
                    statement.setString(1, description)
                    statement.setDouble(2, price)
                    statement.setString(3, name)
                    statement.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val selectQuery = "SELECT * FROM products"

        try {
            DatabaseConnection.connect().use { connection ->
                connection.createStatement().use { statement ->
                    val resultSet = statement.executeQuery(selectQuery)
                    while (resultSet.next()) {
                        val product = Product.create(
                            name = resultSet.getString("name"),
                            description = resultSet.getString("description") ?: "",
                            price = resultSet.getDouble("price")
                        )
                        product.id = resultSet.getInt("id")
                        products.add(product)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return products
    }
}