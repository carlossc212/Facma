package com.resma.facma

import com.resma.facma.entity.Invoice
import com.resma.facma.util.PDFGenerator
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainApp : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        javaClass.getResource("/styles/styles.css")?.toExternalForm()?.let {
            scene.stylesheets.add(it)
        } ?: println("Advertencia: No se encontró el archivo styles.css")
        stage.title = "Facma"
        stage.scene = scene
        stage.centerOnScreen()
        stage.show()
    }
}

fun main() { Application.launch(MainApp::class.java) }