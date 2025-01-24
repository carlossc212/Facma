package com.resma.facma

import com.resma.facma.entity.Product
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainApp : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "Facma"
        stage.scene = scene
        stage.show()
    }
}

fun main() { Application.launch(MainApp::class.java) }