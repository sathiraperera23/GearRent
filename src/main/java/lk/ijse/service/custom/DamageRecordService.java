package lk.ijse.service.custom;

import lk.ijse.dto.DamageRecordDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface DamageRecordService extends SuperService {

    boolean addDamage(DamageRecordDTO dto) throws Exception;
    boolean updateDamage(DamageRecordDTO dto) throws Exception;
    boolean deleteDamage(long id) throws Exception;
    DamageRecordDTO searchDamage(long id) throws Exception;
    List<DamageRecordDTO> getAllDamages() throws Exception;
}
