import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.awt.BorderLayout
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.NullPointerException
import java.nio.charset.Charset


class Main : Application()  {
    val home = "${System.getProperty("user.dir")}/test/"
    var curdir = home

    private fun showHiddenFiles() {

    }
    private fun New() {

    }
    private fun addListenersToTree(tree:ListView<String>, layout: BorderPane) {
        tree.getSelectionModel().selectedItemProperty().addListener{ ov,  old_val,  new_val ->
            val selectedItem = tree.getSelectionModel().getSelectedItem();
            val index = tree.getSelectionModel().getSelectedIndex();
            if (selectedItem.endsWith(".txt") ||  selectedItem.endsWith(".md")) {
                println(selectedItem + ": is a file")
                val file = File(curdir + selectedItem)
                val path = curdir + selectedItem
                val absolutePathTextLabel = Label(path)
                layout.bottom = absolutePathTextLabel
                println(path)
                var content:String = file.readText()
                val label = Label(content)
                layout.center = label
            }
            else if  ( selectedItem.endsWith(".png") || selectedItem.endsWith(".jpg") || selectedItem.endsWith(".bmp")) {
                println(selectedItem + ": is a picture")
                val file = File(curdir + selectedItem)
                val path = curdir + selectedItem
                val absoluteImagePathLabel = Label(path)
                layout.bottom = absoluteImagePathLabel
                println(path)
                val stream: InputStream = FileInputStream(path)
                val image = Image(stream)
                stream.close()
                val imageView = ImageView()
                imageView.image = image
                imageView.x = 10.0
                imageView.y = 10.0
                imageView.fitWidth = 200.0
                imageView.isPreserveRatio = true
                layout.center = imageView
            }else {
                println(selectedItem + ": is a directory")
                val file = File(curdir + selectedItem)
                val path = curdir + selectedItem
                val absoluteImagePathLabel = Label(path)
                println(path)
                val label = Label()
                layout.center = label
                layout.bottom = absoluteImagePathLabel
            }
        }

    }
    private fun Move() {
        val td = TextInputDialog()
        td.title = "Move to a new directory"
        td.headerText = "Where do you want to move this file to?"
        td.showAndWait()
// the app will block here until the user enters a value
        val value:String = td.editor.text
    }
    private fun Home(home:String, layout: BorderPane):ListView<String> {
        var tree = ListView<String>()
        val f = File(home)
        val arr =   FXCollections.observableArrayList ((f.list()).sorted());
        //if directory exists,then
        if(f.exists())
        {
            var absolutePathLabel = Label(f.absolutePath)
            layout.bottom = absolutePathLabel

            for (file in arr) {
                val f1 = File(file)
                if (file.startsWith('.')) {
                    continue;
                }
                if (file.endsWith(".txt") ||  file.endsWith(".png") || file.endsWith(".jpg") ||  file.endsWith(".md") ||  f1.endsWith(".bmp")) {
                    tree.items.add(file)
                    println(file + ": is a file")
                } else {
                    tree.items.add(file + '/')
                    println(file + ": is a directory")
                }
            }
        }
        addListenersToTree(tree, layout)
        curdir = home
        layout.left = tree
        return tree;
    }
    private fun Previous(oldTree:ListView<String>, layout: BorderPane):ListView<String> {
        val selectedItem = oldTree.getFocusModel().getFocusedItem()
        println(selectedItem)
        var tree = ListView<String>()
        println("prevcurdir: " + curdir)
            curdir = curdir.trimEnd('/' )
        curdir = curdir.replaceAfterLast("/", "" )
        println("path1 " + curdir)
            println("path " + curdir)
            val f = File(curdir)
            val arr = FXCollections.observableArrayList((f.list()).sorted());
            //if directory exists,then
            if (f.exists()) {
                var absolutePathLabel = Label(curdir)
                layout.bottom = absolutePathLabel

                for (file in arr) {
                    val f1 = File(file)
                    if (file.startsWith('.')) {
                        continue;
                    }
                    if (file.endsWith(".txt") || file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".md") || f1.endsWith(
                            ".bmp"
                        )
                    ) {
                        tree.items.add(file)
                        println(file + ": is a file")
                    } else {
                        tree.items.add(file + '/')
                        println(file + ": is a directory")
                    }
                }
                addListenersToTree(tree, layout)
            }
        layout.left = tree
        return tree;
    }

    private fun loadDirectory( layout: BorderPane):ListView<String>{
        var tree = ListView<String>()
                val f = File(curdir)
                val arr = FXCollections.observableArrayList((f.list()).sorted());
                //if directory exists,then
                if (f.exists()) {
                    var absolutePathLabel = Label(curdir)
                    layout.bottom = absolutePathLabel

                    for (file in arr) {
                        val f1 = File(file)
                        if (file.startsWith('.')) {
                            continue;
                        }
                        if (file.endsWith(".txt") || file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".md") || f1.endsWith(
                                ".bmp"
                            )
                        ) {
                            tree.items.add(file)
                            println(file + ": is a file")
                        } else {
                            tree.items.add(file + '/')
                            println(file + ": is a directory")
                        }
                    }
                    addListenersToTree(tree, layout)
                }
        layout.left = tree
        return tree;
    }

    private fun Next(oldTree:ListView<String>, layout: BorderPane):ListView<String> {
            val selectedItem = oldTree.getFocusModel().getFocusedItem()
            println(selectedItem)
            var tree = ListView<String>()
            if (selectedItem.endsWith('/')) {
                curdir = curdir + selectedItem
                println("path " + curdir)
                val f = File(curdir)
                val arr = FXCollections.observableArrayList((f.list()).sorted());
                //if directory exists,then
                if (f.exists()) {
                    var absolutePathLabel = Label(curdir)
                    layout.bottom = absolutePathLabel

                    for (file in arr) {
                        val f1 = File(file)
                        if (file.startsWith('.')) {
                            continue;
                        }
                        if (file.endsWith(".txt") || file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".md") || f1.endsWith(
                                ".bmp"
                            )
                        ) {
                            tree.items.add(file)
                            println(file + ": is a file")
                        } else {
                            tree.items.add(file + '/')
                            println(file + ": is a directory")
                        }
                    }
                    addListenersToTree(tree, layout)
                }
            }else {
                throw Exception("This is not a directory")
            }
        layout.left = tree
        return tree;
    }
    private fun Delete(oldTree: ListView<String>, layout: BorderPane):ListView<String> {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Deletion Confirmation"
        alert.headerText = "Are you sure you would like to delete this file?"

        val result = alert.showAndWait()
        var tree = ListView<String>()
        if (result.get() == ButtonType.OK) {
            val selectedItem = oldTree.getFocusModel().getFocusedItem()
            if (selectedItem.endsWith('/')) {
                val file = File(curdir + selectedItem)
                println("path of directory to delete: " + curdir + selectedItem)
                val value = file.deleteRecursively();
                if (value) {
                    tree = loadDirectory(layout)
                    println("The Directory is deleted.");
                    layout.center = Label()
                } else {
                    println("The Directory is not deleted.");
                }
            } else {
                val file = File(curdir + selectedItem)
                println("path of file to delete: " + curdir + selectedItem)
                val value = file.delete();
                if (value) {
                    tree = loadDirectory(layout)
                    println("The File is deleted.");
                    layout.center = Label()
                } else {
                    println("The File is not deleted.");
                }
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        return tree
    }
    private fun Rename(file:File) {
        val td = TextInputDialog()
        td.title = "Title"
        td.headerText = "Enter new value:"
        td.showAndWait()
        // the app will block here until the user enters a value
        val result:String = td.editor.text
        val f = File(result);
        file.renameTo(f);
    }
    private fun Quit() {
        Platform.exit()
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
        fileNew.accelerator = KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN)
        val fileQuit = MenuItem("Quit")
        fileQuit.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN)
        fileMenu.items.add(fileNew)
        fileMenu.items.add(fileQuit)
        // handle default user action aka press
        fileNew.setOnAction { event ->
            New()
            println("New pressed")
        }
        fileQuit.setOnAction { event ->
            Quit()
            println("Quit pressed")
        }

        //ACTIONS
        val actionsMenu = Menu("Actions")
        val move = MenuItem("Move")
        val delete = MenuItem("Delete")
        actionsMenu.items.add(move)
        actionsMenu.items.add(delete)
        delete.accelerator = KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN)
        // handle default user action aka press
        move.setOnAction { event ->
            Move()
            println("Move pressed")
        }

        //VIEW
        val viewMenu = Menu("View")
        val prev = MenuItem("Previous")
        prev.accelerator = KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN)
        val next = MenuItem("Next")
        next.accelerator = KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN)
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
        // handle default user action aka press


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
        renameButton.setOnAction { event ->
            //Rename()
            println("New pressed")
        }

        val hbox = HBox()
        hbox.children.addAll(homeButton, prevButton, nextButton, deleteButton, renameButton)
        hbox.setSpacing(10.0);
        hbox.padding = Insets(10.0, 10.0, 10.0, 10.0)

        val vbox = VBox()
        vbox.children.addAll(menuBar,hbox)

        /*FILES */
        // left: tree
        var tree:ListView<String>;
        tree = Home(home, layout )

        homeButton.setOnAction { event ->
            tree= Home(home,layout)
            println("Home pressed")
        }
        next.setOnAction { event ->
            tree = Next(tree, layout)
            println("Next pressed")
        }
        nextButton.setOnAction { event ->
            tree = Next(tree, layout)
            println("Next Button pressed")
        }
        prevButton.setOnAction { event ->
            tree = Previous(tree, layout)
            println("Previous pressed")
        }
        prev.setOnAction { event ->
            tree = Previous(tree, layout)
            println("Previous pressed")
        }
        delete.setOnAction { event ->
            tree = Delete(tree, layout)
            println("Delete pressed")
        }
        deleteButton.setOnAction { event ->
            tree = Delete(tree, layout)
            println("New pressed")
        }

        // build the scene graph
        layout.top = vbox
        //layout.center = label
        layout.left = tree
        //layout.bottom = absolutepath

        // create and show the scene
        val scene = Scene(layout)
        stage.width = 800.0
        stage.height = 500.0
        stage.scene = scene
        stage.show()
    }
}