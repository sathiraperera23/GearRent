package lk.ijse.service.custom;

import lk.ijse.dto.ConfigDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface ConfigService extends SuperService {
    boolean saveConfig(ConfigDTO dto) throws Exception;
    boolean updateConfig(ConfigDTO dto) throws Exception;
    boolean deleteConfig(int id) throws Exception;
    ConfigDTO searchConfig(int id) throws Exception;
    List<ConfigDTO> getAllConfigs() throws Exception;
}
