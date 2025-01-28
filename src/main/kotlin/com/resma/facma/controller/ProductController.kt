package com.resma.facma.controller

import com.resma.facma.db.DatabaseSQL
import com.resma.facma.entity.Product
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
    private lateinit var idColumn: TableColumn<Product, String>
    @FXML
    private lateinit var nameColumn: TableColumn<Product, String>
    @FXML
    private lateinit var descriptionColumn: TableColumn<Product, String>
    @FXML
    private lateinit var priceColumn: TableColumn<Product, String>

    private val databaseSQL = DatabaseSQL()

    @FXML
    fun initialize() {
        idColumn.cellValueFactory = PropertyValueFactory("id")
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
        nameField.style = "-fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"
        val descriptionField = TextField()
        descriptionField.promptText = "Descripción del producto"
        descriptionField.style = "-fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"
        val priceField = TextField()
        priceField.promptText = "Precio del producto"
        priceField.style = "-fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"

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
        acceptButton.isDisable = true
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

        fun validateField(field: TextField): Boolean {
            return if (field.text.trim().isEmpty()) {
                field.style = "-fx-border-color: red; -fx-border-width: 2px; -fx-focus-color: transparent; -fx-padding: 3px 8px; -fx-font-size: 12px; -fx-background-radius: 10px; -fx-border-radius: 10px;"
                false
            } else {
                field.style = "-fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"
                true
            }
        }

        fun updateAcceptButtonState() {
            val isProductDuplicate = databaseSQL.checkIfNameAndDescriptionExist(
                nameField.text.trim(),
                descriptionField.text.trim()
            )

            acceptButton.isDisable = nameField.text.trim().isEmpty() ||
                    descriptionField.text.trim().isEmpty() ||
                    priceField.text.trim().isEmpty() || isProductDuplicate
        }

        // Verificar si el nombre y la descripción coinciden con un producto existente
        fun validateNameAndDescription(
            nameField: TextField,
            descriptionField: TextField,
            databaseSQL: DatabaseSQL
        ) {
            val name = nameField.text.trim()
            val description = descriptionField.text.trim()

            if (name.isNotEmpty() && description.isNotEmpty()) {
                val productExists = databaseSQL.checkIfNameAndDescriptionExist(name, description)

                if (productExists) {
                    Toolkit.getDefaultToolkit().beep()
                    // Cambiar el borde a azul si coinciden
                    nameField.style = "-fx-border-color: blue; -fx-border-width: 2px; -fx-focus-color: transparent; -fx-padding: 3px 8px; -fx-font-size: 12px; -fx-background-radius: 10px; -fx-border-radius: 10px;"
                    descriptionField.style = "-fx-border-color: blue; -fx-border-width: 2px; -fx-focus-color: transparent; -fx-padding: 3px 8px; -fx-font-size: 12px; -fx-background-radius: 10px; -fx-border-radius: 10px;"
                } else {
                    // Restablecer estilos si no coinciden
                    nameField.style = "-fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"
                    descriptionField.style = "-fx-padding: 5px 10px; -fx-font-size: 12px; -fx-background-radius: 10px;"
                }
            }
        }

        nameField.textProperty().addListener { _, _, _ ->
            validateField(nameField)
            validateNameAndDescription(nameField, descriptionField, databaseSQL)
            updateAcceptButtonState()
        }

        descriptionField.textProperty().addListener { _, _, _ ->
            validateField(descriptionField)
            validateNameAndDescription(nameField, descriptionField, databaseSQL)
            updateAcceptButtonState()
        }

        priceField.textProperty().addListener { _, _, _ ->
            validateField(priceField)
            updateAcceptButtonState()
        }

        cancelButton.setOnAction { dialog.close() }

        acceptButton.setOnAction {
            val newProduct = Product.create(
                nameField.text.trim(),
                descriptionField.text.trim(),
                priceField.text.trim().toDoubleOrNull() ?: 0.0
            )

            if (databaseSQL.addProduct(newProduct)) {
                loadProducts()
                dialog.close()
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
        val id = selectedProduct.id
        if (databaseSQL.deleteProductByID(id)) { loadProducts() }
    }
}