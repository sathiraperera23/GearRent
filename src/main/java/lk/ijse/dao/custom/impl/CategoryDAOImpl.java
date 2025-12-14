package lk.ijse.dao.custom.impl;



import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.CategoryDAO;
import lk.ijse.entity.Category;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean save(Category category) throws Exception {
        return CrudUtil.executeUpdate(
                "INSERT INTO categories(name, description) VALUES(?, ?)",
                category.getName(), category.getDescription()
        );
    }

    @Override
    public boolean update(Category category) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE categories SET name = ?, description = ? WHERE category_id = ?",
                category.getName(), category.getDescription(), category.getCategoryId()
        );
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        return CrudUtil.executeUpdate(
                "DELETE FROM categories WHERE category_id = ?", id
        );
    }

    @Override
    public Category find(Integer id) throws Exception {
        ResultSet rst = CrudUtil.executeQuery(
                "SELECT * FROM categories WHERE category_id = ?", id
        );
        if (rst.next()) {
            return new Category(
                    rst.getInt("category_id"),
                    rst.getString("name"),
                    rst.getString("description")
            );
        }
        return null;
    }

    @Override
    public List<Category> findAll() throws Exception {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM categories");
        List<Category> list = new ArrayList<>();
        while (rst.next()) {
            list.add(new Category(
                    rst.getInt("category_id"),
                    rst.getString("name"),
                    rst.getString("description")
            ));
        }
        return list;
    }
}
