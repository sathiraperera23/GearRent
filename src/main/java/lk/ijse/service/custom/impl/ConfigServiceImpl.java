package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ConfigDAO;
import lk.ijse.dto.ConfigDTO;
import lk.ijse.entity.Config;
import lk.ijse.service.custom.ConfigService;

import java.util.ArrayList;
import java.util.List;

public class ConfigServiceImpl implements ConfigService {

    private final ConfigDAO configDAO =
            (ConfigDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CONFIG);

    @Override
    public boolean saveConfig(ConfigDTO dto) throws Exception {
        // Map DTO → Entity
        Config cfg = new Config(
                0, // assuming auto-increment ID
                dto.getLateFeePerDay(),
                dto.getMaxDeposit(),
                dto.getRegularDiscount(),
                dto.getSilverDiscount(),
                dto.getGoldDiscount()
        );
        return configDAO.save(cfg);
    }

    @Override
    public boolean updateConfig(ConfigDTO dto) throws Exception {
        // Map DTO → Entity
        Config cfg = new Config(
                dto.getConfigId(), // existing ID
                dto.getLateFeePerDay(),
                dto.getMaxDeposit(),
                dto.getRegularDiscount(),
                dto.getSilverDiscount(),
                dto.getGoldDiscount()
        );
        return configDAO.update(cfg);
    }

    @Override
    public boolean deleteConfig(int id) throws Exception {
        return configDAO.delete(id);
    }

    @Override
    public ConfigDTO searchConfig(int id) throws Exception {
        Config cfg = configDAO.find(id); // assuming DAO returns Config entity
        if (cfg == null) return null;

        // Map entity to DTO
        ConfigDTO dto = new ConfigDTO(
                cfg.getLateFeePerDay(),
                cfg.getMaxDeposit(),
                cfg.getRegularDiscount(),
                cfg.getSilverDiscount(),
                cfg.getGoldDiscount()
        );
        return dto;
    }

    @Override
    public List<ConfigDTO> getAllConfigs() throws Exception {
        List<Config> list = configDAO.findAll();
        List<ConfigDTO> dtoList = new ArrayList<>();
        for (Config cfg : list) {
            dtoList.add(new ConfigDTO(
                    cfg.getLateFeePerDay(),
                    cfg.getMaxDeposit(),
                    cfg.getRegularDiscount(),
                    cfg.getSilverDiscount(),
                    cfg.getGoldDiscount()
            ));
        }
        return dtoList;
    }

    @Override
    public ConfigDTO getConfig() throws Exception {
        Config cfg = configDAO.findConfig(); // retrieve the single config row
        if (cfg == null) return null;

        return new ConfigDTO(
                cfg.getLateFeePerDay(),
                cfg.getMaxDeposit(),
                cfg.getRegularDiscount(),
                cfg.getSilverDiscount(),
                cfg.getGoldDiscount()
        );
    }
}
