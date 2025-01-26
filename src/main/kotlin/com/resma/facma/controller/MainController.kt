package com.resma.facma.controller

import com.resma.facma.MainApp
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import javafx.stage.Stage

class MainController {
    @FXML
    private lateinit var tabPane: TabPane

    @FXML
    private lateinit var tabInvoice: Tab
    @FXML
    private lateinit var tabProduct: Tab
    @FXML
    private lateinit var tabEmitter: Tab
    @FXML
    private lateinit var tabReceptor: Tab

    @FXML
    fun initialize() {
        loadView(tabInvoice, "invoice-view.fxml")
        loadView(tabProduct, "product-view.fxml")
        loadView(tabEmitter, "emitter-view.fxml")
        loadView(tabReceptor, "receptor-view.fxml")

        // Configurar el cambio dinámico de vistas en las pestañas
        tabInvoice.setOnSelectionChanged { if (tabInvoice.isSelected) loadView(tabInvoice, "invoice-view.fxml") }
        tabProduct.setOnSelectionChanged { if (tabProduct.isSelected) loadView(tabProduct, "product-view.fxml") }
        tabEmitter.setOnSelectionChanged { if (tabEmitter.isSelected) loadView(tabEmitter, "emitter-view.fxml") }
        tabReceptor.setOnSelectionChanged { if (tabReceptor.isSelected) loadView(tabReceptor, "receptor-view.fxml") }

        // Ajustar el tamaño del Stage cuando cambie la pestaña seleccionada
        tabPane.selectionModel.selectedItemProperty().addListener { _, _, _ -> adjustStageSize() }
    }

    private fun loadView(tab: Tab, fxmlFile: String) {
        try {
            val content: Pane = FXMLLoader(MainApp::class.java.getResource(fxmlFile)).load()
            tab.content = content
        } catch (_: Exception) { }
    }
    private fun adjustStageSize() {
        val stage = tabPane.scene?.window as? Stage ?: return
        val selectedTab = tabPane.selectionModel.selectedItem ?: return
        val content = selectedTab.content

        if (content != null) {
            // Calcular el tamaño preferido del contenido
            val prefWidth = content.prefWidth(-1.0)
            val prefHeight = content.prefHeight(-1.0)

            // Ajustar el tamaño del Stage sumando los bordes
            stage.width = prefWidth + tabPane.insets.left + tabPane.insets.right
            stage.height = prefHeight + tabPane.insets.top + tabPane.insets.bottom + tabPane.tabMaxHeight
        }
    }
}