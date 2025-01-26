package com.resma.facma.db

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

    // Crear tabla de productos
    fun initializeDatabase() {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS products (
                name TEXT PRIMARY KEY,
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

    // Añadir producto
    fun addProduct(name: String, description: String, price: Double): Boolean {
        val insertQuery = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)"

        return try {
            DatabaseConnection.connect().use { connection ->
                connection.prepareStatement(insertQuery).use { statement ->
                    statement.setString(1, name)
                    statement.setString(2, description)
                    statement.setDouble(3, price)
                    statement.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun checkIfProductExists(name: String): Boolean {
        val query = "SELECT COUNT(*) FROM products WHERE name = ?"
        return try {
            DatabaseConnection.connect().use { connection ->
                connection.prepareStatement(query).use { statement ->
                    statement.setString(1, name)
                    val resultSet = statement.executeQuery()
                    val count = resultSet.getInt(1)
                    resultSet.close()
                    count > 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Eliminar producto por nombre
    fun deleteProductByName(name: String): Boolean {
        val deleteQuery = "DELETE FROM products WHERE name = ?"

        return try {
            DatabaseConnection.connect().use { connection ->
                connection.prepareStatement(deleteQuery).use { statement ->
                    statement.setString(1, name)
                    statement.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Actualizar producto por nombre
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

    // Obtener todos los productos
    fun getAllProducts(): List<Map<String, String>> {
        val products = mutableListOf<Map<String, String>>()
        val selectQuery = "SELECT * FROM products"

        try {
            DatabaseConnection.connect().use { connection ->
                connection.createStatement().use { statement ->
                    val resultSet = statement.executeQuery(selectQuery)
                    while (resultSet.next()) {
                        val product = mapOf(
                            "name" to resultSet.getString("name"),
                            "description" to resultSet.getString("description"),
                            "price" to resultSet.getDouble("price").toString()
                        )
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
