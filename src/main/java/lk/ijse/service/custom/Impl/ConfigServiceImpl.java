package lk.ijse.service.custom.Impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ConfigDAO;
import lk.ijse.dto.ConfigDTO;
import lk.ijse.entity.Config;
import lk.ijse.service.custom.ConfigService;

import java.util.ArrayList;
import java.util.List;

public class ConfigServiceImpl implements ConfigService {

    private final ConfigDAO configDAO = (ConfigDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CONFIG);

    @Override
    public boolean saveConfig(ConfigDTO d) throws Exception {
        return configDAO.save(new Config(
                0,
                d.getLateFeePerDay(),
                d.getMaxDeposit(),
                d.getRegularDiscount(),
                d.getSilverDiscount(),
                d.getGoldDiscount()
        ));
    }

    @Override
    public boolean updateConfig(ConfigDTO d) throws Exception {
        return configDAO.update(new Config(
                d.getConfigId(),
                d.getLateFeePerDay(),
                d.getMaxDeposit(),
                d.getRegularDiscount(),
                d.getSilverDiscount(),
                d.getGoldDiscount()
        ));
    }

    @Override
    public boolean deleteConfig(int id) throws Exception {
        return configDAO.delete(id);
    }

    @Override
    public ConfigDTO searchConfig(int id) throws Exception {
        Config c = configDAO.find(id);
        if (c == null) return null;
        return new ConfigDTO(
                c.getConfigId(),
                c.getLateFeePerDay(),
                c.getMaxDeposit(),
                c.getRegularDiscount(),
                c.getSilverDiscount(),
                c.getGoldDiscount()
        );
    }

    @Override
    public List<ConfigDTO> getAllConfigs() throws Exception {
        List<ConfigDTO> list = new ArrayList<>();
        for (Config c : configDAO.findAll()) {
            list.add(new ConfigDTO(
                    c.getConfigId(),
                    c.getLateFeePerDay(),
                    c.getMaxDeposit(),
                    c.getRegularDiscount(),
                    c.getSilverDiscount(),
                    c.getGoldDiscount()
            ));
        }
        return list;
    }
}
