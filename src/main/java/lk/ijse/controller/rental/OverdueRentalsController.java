package lk.ijse.controller.rental;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class OverdueRentalsController {

    @FXML
    private TableView<RentalDTO> tblOverdue;

    @FXML
    private TableColumn<RentalDTO, Long> colRentalId;
    @FXML
    private TableColumn<RentalDTO, Long> colBranchId;
    @FXML
    private TableColumn<RentalDTO, Long> colCustomerId;
    @FXML
    private TableColumn<RentalDTO, Long> colEquipmentId;
    @FXML
    private TableColumn<RentalDTO, LocalDate> colDueDate;
    @FXML
    private TableColumn<RentalDTO, Long> colDaysOverdue;

    private final RentalService rentalService =
            (RentalService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RENTAL);

    @FXML
    public void initialize() {
        colRentalId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        colBranchId.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEquipmentId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));

        colDaysOverdue.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() ->
                        ChronoUnit.DAYS.between(
                                cellData.getValue().getRentedTo(),
                                LocalDate.now()
                        )
                )
        );

        loadOverdueRentals();
    }

    private void loadOverdueRentals() {
        try {
            List<RentalDTO> overdueList =
                    rentalService.getOverdueRentals();

            tblOverdue.setItems(
                    FXCollections.observableArrayList(overdueList)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
