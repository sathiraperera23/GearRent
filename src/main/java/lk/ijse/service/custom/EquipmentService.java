package lk.ijse.service.custom;

import lk.ijse.service.SuperService;
import lk.ijse.dto.EquipmentDTO;
import java.util.List;

public interface EquipmentService extends SuperService {

    boolean saveEquipment(EquipmentDTO dto) throws Exception;

    boolean updateEquipment(EquipmentDTO dto) throws Exception;

    boolean deleteEquipment(long id) throws Exception;

    EquipmentDTO findEquipment(long id) throws Exception;

    List<EquipmentDTO> getAllEquipment() throws Exception;
}