package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.ConfigDAO;
import lk.ijse.entity.Config;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConfigDAOImpl implements ConfigDAO {

    @Override
    public boolean save(Config config) throws Exception {
        String sql = "INSERT INTO config (late_fee_per_day, max_deposit, regular_discount, silver_discount, gold_discount) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.executeUpdate(sql,
                config.getLateFeePerDay(),
                config.getMaxDeposit(),
                config.getRegularDiscount(),
                config.getSilverDiscount(),
                config.getGoldDiscount()
        );
    }

    @Override
    public boolean update(Config config) throws Exception {
        String sql = "UPDATE config SET late_fee_per_day=?, max_deposit=?, regular_discount=?, silver_discount=?, gold_discount=? WHERE config_id=?";
        return CrudUtil.executeUpdate(sql,
                config.getLateFeePerDay(),
                config.getMaxDeposit(),
                config.getRegularDiscount(),
                config.getSilverDiscount(),
                config.getGoldDiscount(),
                config.getConfigId()
        );
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM config WHERE config_id=?";
        return CrudUtil.executeUpdate(sql, id);
    }

    @Override
    public Config find(Integer id) throws Exception {
        String sql = "SELECT * FROM config WHERE config_id=?";
        ResultSet rs = CrudUtil.executeQuery(sql, id);
        if (rs.next()) {
            return new Config(
                    rs.getInt("config_id"),
                    rs.getBigDecimal("late_fee_per_day"),
                    rs.getBigDecimal("max_deposit"),
                    rs.getBigDecimal("regular_discount"),
                    rs.getBigDecimal("silver_discount"),
                    rs.getBigDecimal("gold_discount")
            );
        }
        return null;
    }

    @Override
    public List<Config> findAll() throws Exception {
        String sql = "SELECT * FROM config";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<Config> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Config(
                    rs.getInt("config_id"),
                    rs.getBigDecimal("late_fee_per_day"),
                    rs.getBigDecimal("max_deposit"),
                    rs.getBigDecimal("regular_discount"),
                    rs.getBigDecimal("silver_discount"),
                    rs.getBigDecimal("gold_discount")
            ));
        }
        return list;
    }
}
