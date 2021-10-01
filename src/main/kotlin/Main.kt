import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.EventHandler
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
import javafx.scene.text.Text
import javafx.stage.Stage
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths


class Main : Application()  {
    val home = ("${System.getProperty("user.dir")}/test/").replace('\\','/')
    //val home = "/"
    var curdir = home
    var showHidden:Boolean = false;

    private fun showHiddenFiles(viewHidden:CheckMenuItem, layout: BorderPane): ListView<String> {
        showHidden = viewHidden.isSelected()
        return loadDirectory( layout)
    }
    private fun addListenersToTree(tree:ListView<String>, layout: BorderPane) {
        tree.getSelectionModel().selectedItemProperty().addListener{ ov,  old_val,  new_val ->
            val selectedItem = tree.getSelectionModel().getSelectedItem();
            val index = tree.getSelectionModel().getSelectedIndex();
            if (selectedItem.endsWith(".txt") ||  selectedItem.endsWith(".md")) {
                val textroot = ScrollPane()
                println(selectedItem + ": is a file")
                val file = File(curdir + selectedItem)
                val path = curdir + selectedItem
                val absolutePathTextLabel = Label(path)
                layout.bottom = absolutePathTextLabel
                println(path)
                var content = Text(file.readText())
                textroot.setFitToHeight(true);
                textroot.setContent(content);
                layout.center = textroot
            }
            else if  ( selectedItem.endsWith(".png") || selectedItem.endsWith(".jpg") || selectedItem.endsWith(".bmp")) {
                println(selectedItem + ": is a picture")
                val picroot = ScrollPane()
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
                picroot.setFitToHeight(true);
                picroot.setContent(imageView);
                layout.center = picroot
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
    private fun Move(oldTree:ListView<String>, layout: BorderPane):ListView<String> {
        try {
            val td = TextInputDialog()
            td.title = "Where do you want to move this file to"
            td.headerText = "Enter a new location"
            td.showAndWait()
            // the app will block here until the user enters a value
            var result: String = td.editor.text
            if (result == ".") {
                //this means theyre moving it to the same direc it is in now\
                return oldTree;
            }
            val selectedItem = oldTree.getFocusModel().getFocusedItem()
            val file = File(curdir + selectedItem)
            var tree = ListView<String>()
            if (result == "") {
                val destPath = Paths.get(curdir + selectedItem)
                val srcPath = Paths.get(curdir + selectedItem)
                val temp = Files.move(srcPath, destPath)
                if (temp != null) {
                    println("File renamed and moved successfully");
                } else {
                    println("Failed to move the file");
                }
                tree = loadDirectory(layout)
                return tree
            }
            if (result.startsWith('/')) {
                val destPath = Paths.get(home + result + selectedItem)
                val srcPath = Paths.get(curdir + selectedItem)
                val temp = Files.move(srcPath, destPath)
                if (temp != null) {
                    println("File renamed and moved successfully");
                } else {
                    println("Failed to move the file");
                }
                tree = loadDirectory(layout)
                return tree
            } else {
                val destPath = Paths.get(curdir + result + selectedItem)
                val srcPath = Paths.get(curdir + selectedItem)
                val temp = Files.move(srcPath, destPath)
                if (temp != null) {
                    println("File renamed and moved successfully");
                } else {
                    println("Failed to move the file");
                }
                tree = loadDirectory(layout)
                return tree
            }
            return tree
        }catch (e:Exception) {
            val alert = Alert(AlertType.CONFIRMATION)
            alert.title = "Move Error"
            alert.headerText = "Moving file was not successful"
            alert.showAndWait()
            return oldTree
        }

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
                if (showHidden) {

                } else {
                    if (file.startsWith('.')) {
                        continue;
                    }
                }
                if (file.endsWith(".txt") ||  file.endsWith(".png") || file.endsWith(".jpg") ||  file.endsWith(".md") ||  file.endsWith(".bmp") || file.startsWith('.')  || file.endsWith(".bad")) {
                    if (file.endsWith(".bad")) {
                        file.replace(".bad", "" )
                    }
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
        tree.setOnMouseClicked {
            println(it.clickCount) // 2 on double click, 1 on single click
            if (it.clickCount == 2) {
                tree = Next(tree, layout)
                println("Next Double Click pressed")
            }
        }
        return tree;
    }
    private fun Previous(oldTree:ListView<String>, layout: BorderPane):ListView<String> {
        try {
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
                    if (showHidden) {

                    } else {
                        if (file.startsWith('.')) {
                            continue;
                        }
                    }
                    if (file.endsWith(".txt") || file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".md") || file.endsWith(".bmp") || file.startsWith('.') || file.endsWith(".bad")) {
                        if (file.endsWith(".bad")) {
                            file.replace(".bad", "" )
                        }
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
        tree.setOnMouseClicked {
            println(it.clickCount) // 2 on double click, 1 on single click
            if (it.clickCount == 2) {
                tree = Next(tree, layout)
                println("Next Double Click pressed")
            }
        }
        return tree;}
        catch (e:Exception) {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Previous Error"
        alert.headerText = "Going back was not successful"
        alert.showAndWait()
        return oldTree
    }
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
                        if (showHidden) {

                        } else {
                            if (file.startsWith('.')) {
                                continue;
                            }
                        }
                        if (file.endsWith(".txt") || file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".md")
                            || file.endsWith(".bmp") || file.startsWith('.') || file.endsWith(' ') || file.endsWith(".bad"))
                        {
                            if (file.endsWith(".bad")) {
                                println("LOAD HERE")
                                file.replace(".bad", "" )
                                println(file)
                            }
                            tree.items.add(file)
                            println(file + ": is a file <= LOAD")
                        } else {
                            tree.items.add(file + '/')
                            println(file + ": is a directory")
                        }
                    }
                    addListenersToTree(tree, layout)
                }
        layout.left = tree
        tree.setOnMouseClicked {
            println(it.clickCount) // 2 on double click, 1 on single click
            if (it.clickCount == 2) {
                tree = Next(tree, layout)
                println("Next Double Click pressed")
            }
        }
        return tree;
    }

    private fun Next(oldTree:ListView<String>, layout: BorderPane):ListView<String> {
            val selectedItem = oldTree.getFocusModel().getFocusedItem()
            println(selectedItem)
            var tree = ListView<String>()
            try {
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
                        if (showHidden) {

                        } else {
                            if (file.startsWith('.')) {
                                continue;
                            }
                        }
                        if (file.endsWith(".txt") || file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".md") || file.endsWith(
                                ".bmp") || file.startsWith('.') || file.endsWith(".bad")
                        ) {
                            if (file.endsWith(".bad")) {
                                println("HERE")
                                file.replace(".bad", "" )
                            }
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
                val alert = Alert(AlertType.CONFIRMATION)
                alert.title = "This is not a directory, you cannot enter it"
                 alert.showAndWait()
                return oldTree
            }
        layout.left = tree
        tree.setOnMouseClicked {
            println(it.clickCount) // 2 on double click, 1 on single click
            if (it.clickCount == 2) {
                tree = Next(tree, layout)
                println("Next Double Click pressed")
            }
        }
        return tree;} catch (e: Exception) {
            val alert = Alert(AlertType.CONFIRMATION)
                alert.title = "This is not a directory, you cannot enter it"
                alert.showAndWait()
                return oldTree
            return oldTree
        }
    }
    private fun Delete(oldTree: ListView<String>, layout: BorderPane):ListView<String> {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Deletion Confirmation"
        alert.headerText = "Are you sure you would like to delete this file?"

        val result = alert.showAndWait()
        var tree = ListView<String>()
        if (result.get() == ButtonType.OK) {
            try {
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
            }catch (e:Exception) {
                val alert = Alert(AlertType.CONFIRMATION)
                alert.title = "Deletion Error"
                alert.headerText = "Deletion of file was not successful"
                alert.showAndWait()
                return oldTree
            }
        } else {
            return oldTree
        }
        tree.setOnMouseClicked {
            println(it.clickCount) // 2 on double click, 1 on single click
            if (it.clickCount == 2) {
                tree = Next(tree, layout)
                println("Next Double Click pressed")
            }
        }
        return tree
    }
    private fun Rename(oldTree:ListView<String>, layout: BorderPane):ListView<String> {
        val td = TextInputDialog()
        td.title = "Title"
        td.headerText = "Enter new value:"
        td.showAndWait()
        // the app will block here until the user enters a value
        val result:String = td.editor.text
        try {
            if ('.' in result) {
                val selectedItem = oldTree.getFocusModel().getFocusedItem()
                val file = File(curdir + selectedItem)
                val f = File(curdir + result);
                file.renameTo(f);
            } else {
                val selectedItem = oldTree.getFocusModel().getFocusedItem()
                val file = File(curdir + selectedItem)
                val f = File(curdir + result);
                //val f = File(curdir + result + ".bad");
                file.renameTo(f);
            }
            var tree = ListView<String>()
            tree = loadDirectory(layout)
            return tree
        }  catch (e:Exception) {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Rename Error"
        alert.headerText = "Renaming of file was not successful"
        alert.showAndWait()
        return oldTree
    }

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
        val fileQuit = MenuItem("Quit")
        fileQuit.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN)
        fileMenu.items.add(fileQuit)
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

        //VIEW
        val viewMenu = Menu("View")
        val prev = MenuItem("Previous")
        prev.accelerator = KeyCodeCombination(KeyCode.BACK_SPACE)
        val next = MenuItem("Next")
        next.accelerator = KeyCodeCombination(KeyCode.ENTER)
        viewMenu.items.add(prev)
        viewMenu.items.add(next)

        //OPTIONS
        val optionsMenu = Menu("Options")
        val viewHidden = CheckMenuItem("Show Hidden Files")
        viewHidden.setSelected(false)
        optionsMenu.items.add(viewHidden)

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
        val moveButton = Button("Move")

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

        val moveImageView = ImageView((Image("moveicon.png")))
        moveButton.graphic = (moveImageView)
        moveImageView.fitWidthProperty().bind(homeButton.widthProperty().divide(5))
        moveImageView.isPreserveRatio = true
        moveButton.setMaxWidth(Double.MAX_VALUE)

        val hbox = HBox()
        hbox.children.addAll(homeButton, prevButton, nextButton, deleteButton, renameButton, moveButton)
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
            println("Delete pressed")
        }
        renameButton.setOnAction { event ->
            tree = Rename(tree, layout)
            println("Rename pressed")
        }
        move.setOnAction { event ->
            tree = Move(tree, layout)
            println("Move pressed")
        }
        moveButton.setOnAction { event ->
            tree = Move(tree, layout)
            println("Move pressed")
        }
        viewHidden.setOnAction { event ->
            tree = showHiddenFiles(viewHidden,layout)
            println("View Hidden clicked")
        }

        // build the scene graph
        layout.top = vbox
        //layout.center = label
        layout.left = tree
        //layout.bottom = absolutepath

        tree.setOnMouseClicked {
            println(it.clickCount) // 2 on double click, 1 on single click
            if (it.clickCount == 2) {
                tree = Next(tree, layout)
                println("Next Double Click pressed")
            }
        }


        // create and show the scene
        val scene = Scene(layout)
        stage.width = 800.0
        stage.height = 500.0
        stage.scene = scene
        stage.show()
    }
}