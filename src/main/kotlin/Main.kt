import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.nio.file.Files


class Main : Application()  {
    private fun showHiddenFiles() {

    }

    override fun start(stage: Stage) {

        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()

        // top: menubar
        val menuBar = MenuBar()
        //FILE
        val fileMenu = Menu("File")
        val fileNew = MenuItem("New")
        fileMenu.items.add(fileNew)

        //ACTIONS
        val actionsMenu = Menu("Actions")
        val move = MenuItem("Move")
        val delete = MenuItem("Delete")
        actionsMenu.items.add(move)
        actionsMenu.items.add(delete)

        //VIEW
        val viewMenu = Menu("View")
        val prev = MenuItem("Previous")
        val next = MenuItem("Next")
        viewMenu.items.add(prev)
        viewMenu.items.add(next)

        //OPTIONS
        val optionsMenu = Menu("Options")
        val viewHidden = CheckMenuItem("Show Hidden Files")
        viewHidden.setSelected(false)
        optionsMenu.items.add(viewHidden)
        // handle default user action aka press
        viewHidden.setOnAction { event ->
            showHiddenFiles()
            println("View Hidden clicked")
        }

        menuBar.menus.add(fileMenu)
        menuBar.menus.add(viewMenu)
        menuBar.menus.add(actionsMenu)
        menuBar.menus.add(optionsMenu)

        //HBOX
        val homeButton = Button("Home")
        val prevButton = Button("Prev")
        val nextButton = Button("Next")
        val deleteButton = Button("Delete")
        val renameButton = Button("Rename")

        val homeImageView = ImageView((Image("homeicon.png")))
        homeButton.graphic = (homeImageView)
        homeImageView.fitWidthProperty().bind(homeButton.widthProperty().divide(5))
        homeImageView.isPreserveRatio = true
        homeButton.setMaxWidth(Double.MAX_VALUE)

        val prevImageView = ImageView((Image("previousicon.png")))
        prevButton.graphic = (prevImageView)
        prevImageView.fitWidthProperty().bind(homeButton.widthProperty().divide(5))
        prevImageView.isPreserveRatio = true
        prevButton.setMaxWidth(Double.MAX_VALUE)

        val nextImageView = ImageView((Image("nexticon.png")))
        nextButton.graphic = (nextImageView)
        nextImageView.fitWidthProperty().bind(homeButton.widthProperty().divide(5))
        nextImageView.isPreserveRatio = true
        nextButton.setMaxWidth(Double.MAX_VALUE)

        val deleteImageView = ImageView((Image("deleteicon.png")))
        deleteButton.graphic = (deleteImageView)
        deleteImageView.fitWidthProperty().bind(homeButton.widthProperty().divide(5))
        deleteImageView.isPreserveRatio = true
        deleteButton.setMaxWidth(Double.MAX_VALUE)

        val renameImageView = ImageView((Image("renameicon.png")))
        renameButton.graphic = (renameImageView)
        renameImageView.fitWidthProperty().bind(homeButton.widthProperty().divide(5))
        renameImageView.isPreserveRatio = true
        renameButton.setMaxWidth(Double.MAX_VALUE)

        val hbox = HBox()
        hbox.children.addAll(homeButton, prevButton, nextButton, deleteButton, renameButton)
        hbox.setSpacing(10.0);
        hbox.padding = Insets(10.0, 10.0, 10.0, 10.0)

        val vbox = VBox()
        vbox.children.addAll(menuBar,hbox)

        // handle default user action aka press
        fileNew.setOnAction { event ->
            println("New pressed")
        }

        // left: tree
        val f = File("${System.getProperty("user.dir")}/test/",)
        val tree = ListView<String>()
        //if directory exists,then
        if(f.exists())
        {
            //get the contents into arr[]
            //now arr[i] represent either a File or Directory
            val arr = (f.list()).sorted();

            //displaying the entries
            for (file in arr) {
                //create File object with the entry and test
                //if it is a file or directory
                val f1 = File(file)
                if (file.startsWith('.')) {
                    continue;
                }
                //tree.items.add(file)
                if (f1.isFile)
                    tree.items.add(file)
                    println(file + ": is a file")
                if (f1.isDirectory)
                    tree.items.add(file + '/')
                    println(file + ": is a directory")
            }
        }

        // handle mouse clicked action
        tree.setOnMouseClicked { event ->
            println("Pressed ${event.button}")
        }

        // build the scene graph
        layout.top = vbox
        layout.left = tree

        // create and show the scene
        val scene = Scene(layout)
        stage.width = 800.0
        stage.height = 500.0
        stage.scene = scene
        stage.show()
    }
}