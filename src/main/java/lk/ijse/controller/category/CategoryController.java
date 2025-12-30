package lk.ijse.controller.category;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dto.CategoryDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.CategoryService;

public class CategoryController {


    @FXML private TableView<CategoryDTO> tblCategories;
    @FXML private TableColumn<CategoryDTO, Integer> colId;
    @FXML private TableColumn<CategoryDTO, String> colName;
    @FXML private TableColumn<CategoryDTO, String> colDescription;


    @FXML private TextField txtCategoryId;
    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;

    private CategoryDTO selectedCategory;


    private final CategoryService categoryService =
            (CategoryService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.CATEGORY);


    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableListener();
        loadTable();
    }

    private void tableListener() {
        tblCategories.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, c) -> {
                    if (c != null) {
                        selectedCategory = c;
                        fillForm(c);
                    }
                });
    }

    private void fillForm(CategoryDTO c) {
        txtCategoryId.setText(String.valueOf(c.getCategoryId()));
        txtName.setText(c.getName());
        txtDescription.setText(c.getDescription());
    }

    private void loadTable() {
        try {
            ObservableList<CategoryDTO> list =
                    FXCollections.observableArrayList(
                            categoryService.getAllCategories()
                    );
            tblCategories.setItems(list);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    @FXML
    void btnAddOnAction() {
        try {
            CategoryDTO dto = new CategoryDTO(
                    0,
                    txtName.getText(),
                    txtDescription.getText()
            );
            categoryService.addCategory(dto);
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            if (selectedCategory == null) {
                showError("Select a category to update");
                return;
            }

            CategoryDTO dto = new CategoryDTO(
                    Integer.parseInt(txtCategoryId.getText()),
                    txtName.getText(),
                    txtDescription.getText()
            );

            categoryService.updateCategory(dto);
            loadTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            int id = Integer.parseInt(txtCategoryId.getText());
            categoryService.deleteCategory(id);
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnClearOnAction() {
        clearForm();
    }


    private void clearForm() {
        txtCategoryId.clear();
        txtName.clear();
        txtDescription.clear();
        selectedCategory = null;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
