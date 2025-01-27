package com.resma.facma.controller

import com.resma.facma.db.DatabaseSQL
import com.resma.facma.entity.Product
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.stage.Modality
import javafx.stage.Stage
import java.awt.Toolkit
//Toolkit.getDefaultToolkit().beep()

class ProductController {

    @FXML
    private lateinit var productSearch: TextField

    @FXML
    private lateinit var addButton: Button
    @FXML
    private lateinit var editButton: Button
    @FXML
    private lateinit var deleteButton: Button

    @FXML
    private lateinit var productTable: TableView<Product>
    @FXML
    private lateinit var nameColumn: TableColumn<Product, String>
    @FXML
    private lateinit var descriptionColumn: TableColumn<Product, String>
    @FXML
    private lateinit var priceColumn: TableColumn<Product, String>

    private val databaseSQL = DatabaseSQL()

    @FXML
    fun initialize() {
        nameColumn.cellValueFactory = PropertyValueFactory("name")
        descriptionColumn.cellValueFactory = PropertyValueFactory("description")
        priceColumn.cellValueFactory = PropertyValueFactory("price")

        databaseSQL.initializeDatabase()
        loadProducts()

        productTable.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            editButton.isDisable = newValue == null
            deleteButton.isDisable = newValue == null
        }
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
        dialog.isIconified = false
        dialog.isResizable = false
        dialog.initModality(Modality.APPLICATION_MODAL) // No permite salir o pulsar fuera

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
            } else { null }
        }
        priceField.textFormatter = numberFilter

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
            val newProduct = Product.create(
                nameField.text.trim(),
                descriptionField.text.trim(),
                priceField.text.trim().toDoubleOrNull() ?: 0.0
            )

            // Verificar si los campos son válidos
            if (newProduct.name.isNotEmpty() && !newProduct.description.isNullOrBlank() && newProduct.price > 0) {
                // Consultar en la base de datos si el nombre del producto ya existe
                // Aqui cambiar por listener que se actualice con cada char introducido
                val existingProduct = databaseSQL.checkIfProductExists(newProduct.name)

                if (existingProduct) {
                    // Si el producto ya existe, colorear el borde del TextField en rojo
                    nameField.style = "-fx-border-color: red; -fx-border-width: 2px;"
                    Toolkit.getDefaultToolkit().beep()
                    nameField.requestFocus()
                } else {
                    // Si no existe, agregar el producto a la base de datos
                    if (databaseSQL.addProduct(newProduct)) {
                        loadProducts()
                        dialog.close()
                    }
                }
            } else {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Error"
                alert.headerText = null
                alert.contentText = "Por favor, ingrese todos los campos correctamente."
                alert.showAndWait()
            }
        }
        dialog.showAndWait()
    }

    private fun showError(title: String, message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }

    @FXML
    fun deleteProduct() {
        val selectedProduct = productTable.selectionModel.selectedItem
        if (selectedProduct != null) {
            val name = selectedProduct.name  // Accede directamente a la propiedad `name` del objeto `Product`
            if (databaseSQL.deleteProductByName(name)) {
                loadProducts() // Recarga la tabla después de eliminar
            } else {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Error"
                alert.headerText = null
                alert.contentText = "No se pudo eliminar el producto."
                alert.showAndWait()
            }
        } else {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Advertencia"
            alert.headerText = null
            alert.contentText = "No se ha seleccionado ningún producto."
            alert.showAndWait()
        }
    }
}