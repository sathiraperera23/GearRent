package lk.ijse.dao.custom.impl;



import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.BranchDAO;
import lk.ijse.entity.Branch;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BranchDAOImpl implements BranchDAO {

    @Override
    public boolean save(Branch branch) throws Exception {
        return CrudUtil.executeUpdate(
                "INSERT INTO branches(code, name, address, contact) VALUES(?, ?, ?, ?)",
                branch.getCode(), branch.getName(), branch.getAddress(), branch.getContact()
        );
    }

    @Override
    public boolean update(Branch branch) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE branches SET code = ?, name = ?, address = ?, contact = ? WHERE branch_id = ?",
                branch.getCode(), branch.getName(), branch.getAddress(), branch.getContact(), branch.getBranchId()
        );
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        return CrudUtil.executeUpdate(
                "DELETE FROM branches WHERE branch_id = ?", id
        );
    }

    @Override
    public Branch find(Integer id) throws Exception {
        ResultSet rst = CrudUtil.executeQuery(
                "SELECT * FROM branches WHERE branch_id = ?", id
        );
        if (rst.next()) {
            return new Branch(
                    rst.getInt("branch_id"),
                    rst.getString("code"),
                    rst.getString("name"),
                    rst.getString("address"),
                    rst.getString("contact"),
                    rst.getTimestamp("created_at")
            );
        }
        return null;
    }

    @Override
    public List<Branch> findAll() throws Exception {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM branches");
        List<Branch> list = new ArrayList<>();
        while (rst.next()) {
            list.add(new Branch(
                    rst.getInt("branch_id"),
                    rst.getString("code"),
                    rst.getString("name"),
                    rst.getString("address"),
                    rst.getString("contact"),
                    rst.getTimestamp("created_at")
            ));
        }
        return list;
    }
}