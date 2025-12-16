package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.EquipmentDAO;
import lk.ijse.dto.EquipmentDTO;
import lk.ijse.entity.Equipment;
import lk.ijse.service.custom.EquipmentService;

import java.util.ArrayList;
import java.util.List;

public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentDAO equipmentDAO;

    public EquipmentServiceImpl() {
        this.equipmentDAO = (EquipmentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.EQUIPMENT);
    }

    // Convert DTO → Entity
    private Equipment toEntity(EquipmentDTO dto) {
        return new Equipment(
                dto.getEquipmentId(),
                dto.getCategoryId(),
                dto.getBranchId(),
                dto.getEquipmentCode(),
                dto.getBrand(),
                dto.getModel(),
                dto.getPurchaseYear(),
                dto.getBaseDailyPrice(),
                dto.getSecurityDeposit(),
                dto.getStatus(),
                null // created_at is DB timestamp
        );
    }

    // Convert Entity → DTO
    private EquipmentDTO toDTO(Equipment e) {
        return new EquipmentDTO(
                e.getEquipmentId(),
                e.getCategoryId(),
                e.getBranchId(),
                e.getEquipmentCode(),
                e.getBrand(),
                e.getModel(),
                e.getPurchaseYear(),
                e.getBaseDailyPrice(),
                e.getSecurityDeposit(),
                e.getStatus()
        );
    }

    @Override
    public boolean saveEquipment(EquipmentDTO dto) throws Exception {
        return equipmentDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateEquipment(EquipmentDTO dto) throws Exception {
        return equipmentDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteEquipment(long id) throws Exception {
        return equipmentDAO.delete(id);
    }

    @Override
    public EquipmentDTO findEquipment(long id) throws Exception {
        Equipment e = equipmentDAO.find(id);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<EquipmentDTO> getAllEquipment() throws Exception {
        List<Equipment> list = equipmentDAO.findAll();
        List<EquipmentDTO> dtoList = new ArrayList<>();

        for (Equipment e : list) {
            dtoList.add(toDTO(e));
        }

        return dtoList;
    }
}