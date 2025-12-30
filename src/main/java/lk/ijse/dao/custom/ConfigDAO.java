package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Config;

public interface ConfigDAO extends CrudDAO<Config, Integer> {
    Config findConfig() throws Exception;

}
