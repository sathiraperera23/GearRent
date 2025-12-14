package lk.ijse.dao.custom.impl;



import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.EquipmentDAO;
import lk.ijse.entity.Equipment;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAOImpl implements EquipmentDAO {

    @Override
    public boolean save(Equipment e) throws Exception {
        return CrudUtil.executeUpdate(
                "INSERT INTO equipment (category_id, branch_id, equipment_code, brand, model, purchase_year, base_daily_price, security_deposit, status) VALUES (?,?,?,?,?,?,?,?,?)",
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
    public boolean update(Equipment e) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE equipment SET category_id=?, branch_id=?, equipment_code=?, brand=?, model=?, purchase_year=?, base_daily_price=?, security_deposit=?, status=? WHERE equipment_id=?",
                e.getCategoryId(),
                e.getBranchId(),
                e.getEquipmentCode(),
                e.getBrand(),
                e.getModel(),
                e.getPurchaseYear(),
                e.getBaseDailyPrice(),
                e.getSecurityDeposit(),
                e.getStatus(),
                e.getEquipmentId()
        );
    }

    @Override
    public boolean delete(Long id) throws Exception {
        return CrudUtil.executeUpdate("DELETE FROM equipment WHERE equipment_id=?", id);
    }

    @Override
    public Equipment find(Long id) throws Exception {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM equipment WHERE equipment_id=?", id);

        if (rs.next()) {
            return new Equipment(
                    rs.getLong("equipment_id"),
                    rs.getInt("category_id"),
                    rs.getInt("branch_id"),
                    rs.getString("equipment_code"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getInt("purchase_year"),
                    rs.getDouble("base_daily_price"),
                    rs.getDouble("security_deposit"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
            );
        }
        return null;
    }

    @Override
    public List<Equipment> findAll() throws Exception {
        List<Equipment> list = new ArrayList<>();
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM equipment");

        while (rs.next()) {
            list.add(
                    new Equipment(
                            rs.getLong("equipment_id"),
                            rs.getInt("category_id"),
                            rs.getInt("branch_id"),
                            rs.getString("equipment_code"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getInt("purchase_year"),
                            rs.getDouble("base_daily_price"),
                            rs.getDouble("security_deposit"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    )
            );
        }
        return list;
    }
}