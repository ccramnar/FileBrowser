import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class Main : Application()  {
    override fun start(stage: Stage) {

        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()

        // top: menubar
        val menuBar = MenuBar()
        val fileMenu = Menu("File")
        val fileNew = MenuItem("New")

        menuBar.menus.add(fileMenu)
        fileMenu.items.add(fileNew)

        // handle default user action aka press
        fileNew.setOnAction { event ->
            println("New pressed")
        }

        // left: tree
        val tree = ListView<String>()
        tree.items.add("First")
        tree.items.add("Second")

        // handle mouse clicked action
        tree.setOnMouseClicked { event ->
            println("Pressed ${event.button}")
        }

        // build the scene graph
        layout.top = menuBar
        layout.left = tree

        // create and show the scene
        val scene = Scene(layout)
        stage.width = 800.0
        stage.height = 500.0
        stage.scene = scene
        stage.show()
    }
}