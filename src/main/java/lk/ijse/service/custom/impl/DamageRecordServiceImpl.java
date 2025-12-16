package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.DamageRecordDAO;
import lk.ijse.dto.DamageRecordDTO;
import lk.ijse.entity.DamageRecord;
import lk.ijse.service.custom.DamageRecordService;

import java.util.ArrayList;
import java.util.List;

public class DamageRecordServiceImpl implements DamageRecordService {

    private final DamageRecordDAO damageDAO =
            (DamageRecordDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.DAMAGE);

    @Override
    public boolean addDamage(DamageRecordDTO d) throws Exception {
        return damageDAO.save(new DamageRecord(
                0,
                d.getRentalId(),
                d.getEquipmentId(),
                d.getDescription(),
                d.getDamageCost(),
                null
        ));
    }

    @Override
    public boolean updateDamage(DamageRecordDTO d) throws Exception {
        return damageDAO.update(new DamageRecord(
                d.getDamageId(),
                0,
                0,
                d.getDescription(),
                d.getDamageCost(),
                null
        ));
    }

    @Override
    public boolean deleteDamage(long id) throws Exception {
        return damageDAO.delete(id);
    }

    @Override
    public DamageRecordDTO searchDamage(long id) throws Exception {
        DamageRecord r = damageDAO.find(id);
        if (r == null) return null;

        return new DamageRecordDTO(
                r.getDamageId(),
                r.getRentalId(),
                r.getEquipmentId(),
                r.getDescription(),
                r.getDamageCost(),
                r.getAssessedAt()
        );
    }

    @Override
    public List<DamageRecordDTO> getAllDamages() throws Exception {
        List<DamageRecordDTO> list = new ArrayList<>();
        for (DamageRecord r : damageDAO.findAll()) {
            list.add(new DamageRecordDTO(
                    r.getDamageId(),
                    r.getRentalId(),
                    r.getEquipmentId(),
                    r.getDescription(),
                    r.getDamageCost(),
                    r.getAssessedAt()
            ));
        }
        return list;
    }
}
