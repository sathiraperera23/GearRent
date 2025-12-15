package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Config;

public interface ConfigDAO extends CrudDAO<Config, Integer> {
    // You can add custom methods later if needed
    Config findConfig() throws Exception; // <-- returns the single row

}
