package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.DamageRecordDAO;
import lk.ijse.entity.DamageRecord;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DamageRecordDAOImpl implements DamageRecordDAO {

    @Override
    public boolean save(DamageRecord d) throws Exception {
        return CrudUtil.executeUpdate(
                "INSERT INTO damage_records (rental_id, equipment_id, description, damage_cost) VALUES (?,?,?,?)",
                d.getRentalId(),
                d.getEquipmentId(),
                d.getDescription(),
                d.getDamageCost()
        );
    }

    @Override
    public boolean update(DamageRecord d) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE damage_records SET description=?, damage_cost=? WHERE damage_id=?",
                d.getDescription(),
                d.getDamageCost(),
                d.getDamageId()
        );
    }

    @Override
    public boolean delete(Long id) throws Exception {
        return CrudUtil.executeUpdate(
                "DELETE FROM damage_records WHERE damage_id=?",
                id
        );
    }

    @Override
    public DamageRecord find(Long id) throws Exception {
        ResultSet rs = CrudUtil.executeQuery(
                "SELECT * FROM damage_records WHERE damage_id=?",
                id
        );

        if (rs.next()) {
            return new DamageRecord(
                    rs.getLong("damage_id"),
                    rs.getLong("rental_id"),
                    rs.getLong("equipment_id"),
                    rs.getString("description"),
                    rs.getBigDecimal("damage_cost"),
                    rs.getTimestamp("assessed_at")
            );
        }
        return null;
    }

    @Override
    public List<DamageRecord> findAll() throws Exception {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM damage_records");
        List<DamageRecord> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new DamageRecord(
                    rs.getLong("damage_id"),
                    rs.getLong("rental_id"),
                    rs.getLong("equipment_id"),
                    rs.getString("description"),
                    rs.getBigDecimal("damage_cost"),
                    rs.getTimestamp("assessed_at")
            ));
        }
        return list;
    }
}
