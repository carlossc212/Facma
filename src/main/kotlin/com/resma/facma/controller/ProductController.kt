package com.resma.facma.controller

import com.resma.facma.db.DatabaseSQL
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.stage.Stage

class ProductController {

    @FXML
    private lateinit var productSearch: TextField

    @FXML
    private lateinit var productTable: TableView<Map<String, String>>
    @FXML
    private lateinit var nameColumn: TableColumn<Map<String, String>, String>
    @FXML
    private lateinit var descriptionColumn: TableColumn<Map<String, String>, String>
    @FXML
    private lateinit var priceColumn: TableColumn<Map<String, String>, String>

    private val databaseSQL = DatabaseSQL()

    @FXML
    fun initialize() {
        // Configurar columnas
        nameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value["name"] ?: "?") }
        descriptionColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value["description"] ?: "?") }
        priceColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value["price"] ?: "?") }

        // Inicializar base de datos
        databaseSQL.initializeDatabase()

        // Cargar productos
        loadProducts()
    }

    // Cargar productos en la tabla
    private fun loadProducts() {
        val products = databaseSQL.getAllProducts()
        productTable.items = FXCollections.observableArrayList(products)
    }

    // Añadir un producto
    @FXML
    fun addProduct() {
        // Crear un diálogo de entrada
        val dialog = Stage()
        dialog.title = "Añadir producto"

        // Crear un GridPane para el diseño
        val gridPane = GridPane()
        gridPane.hgap = 10.0
        gridPane.vgap = 10.0
        gridPane.padding = Insets(20.0)

        // Crear los campos de entrada
        val nameField = TextField()
        nameField.promptText = "Nombre del producto"
        val descriptionField = TextField()
        descriptionField.promptText = "Descripción del producto"
        val priceField = TextField()
        priceField.promptText = "Precio del producto"

        // Crear un TextFormatter para aceptar solo números
        val numberFilter = TextFormatter<Double> { change ->
            if (change.controlNewText.matches("^\\d*(\\.\\d*)?$".toRegex())) {
                change
            } else {
                null
            }
        }

        priceField.textFormatter = numberFilter


        // Crear las etiquetas
        gridPane.add(Label("Nombre:"), 0, 0)
        gridPane.add(nameField, 1, 0)
        gridPane.add(Label("Descripción:"), 0, 1)
        gridPane.add(descriptionField, 1, 1)
        gridPane.add(Label("Precio:"), 0, 2)
        gridPane.add(priceField, 1, 2)

        // Crear los botones
        val acceptButton = Button("Aceptar")
        val cancelButton = Button("Cancelar")
        acceptButton.style = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"
        cancelButton.style = "-fx-background-color: #B0BEC5; -fx-text-fill: white; -fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"
        acceptButton.minWidth = 100.0
        cancelButton.minWidth = 100.0

        // Colocar los botones dentro de un HBox para centrarlos
        val buttonBox = HBox(20.0, acceptButton, cancelButton)
        buttonBox.alignment = Pos.CENTER
        GridPane.setMargin(buttonBox, Insets(15.0, 0.0, 0.0, 0.0))

        gridPane.add(buttonBox, 0, 3, 2, 1)

        // Crear la escena y asignarla al diálogo
        val scene = Scene(gridPane)
        dialog.scene = scene

        // Definir las acciones de los botones
        cancelButton.setOnAction { dialog.close() }

        acceptButton.setOnAction {
            val name = nameField.text.trim()
            val description = descriptionField.text.trim()
            val price = priceField.text.trim().toDoubleOrNull()

            if (name.isNotEmpty() && description.isNotEmpty() && price != null && price > 0) {
                // Consultar en la base de datos si el nombre del producto ya existe
                val existingProduct = databaseSQL.checkIfProductExists(name)

                if (existingProduct) {
                    // Si el producto ya existe, colorear el borde del TextField en rojo
                    nameField.style = "-fx-border-color: red; -fx-border-width: 2px;"
                    val alert = Alert(Alert.AlertType.WARNING)
                    alert.title = "Producto ya existente"
                    alert.headerText = null
                    alert.contentText = "Ya existe un producto con el mismo nombre. Por favor, ingrese otro nombre."
                    alert.showAndWait()
                } else {
                    // Si no existe, agregar el producto a la base de datos
                    if (databaseSQL.addProduct(name, description, price)) {
                        println("Producto añadido: $name, $description, $price")
                        loadProducts()  // Recargar la lista de productos
                        dialog.close()
                    } else {
                        // Si hubo un error al agregar el producto
                        val alert = Alert(Alert.AlertType.ERROR)
                        alert.title = "Error"
                        alert.headerText = null
                        alert.contentText = "Hubo un problema al añadir el producto. Intenta nuevamente."
                        alert.showAndWait()
                    }
                }
            } else {
                // Mostrar un error si los datos no son válidos
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Error"
                alert.headerText = null
                alert.contentText = "Por favor, ingrese todos los campos correctamente."
                alert.showAndWait()
            }
        }

        // Mostrar el diálogo
        dialog.showAndWait()
    }

    private fun showError(title: String, message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }

    // Eliminar el producto seleccionado
    @FXML
    fun deleteProduct() {
        val selectedProduct = productTable.selectionModel.selectedItem
        if (selectedProduct != null) {
            val name = selectedProduct["name"]
            if (name != null && databaseSQL.deleteProductByName(name)) {
                loadProducts()
            }
        }
    }
}