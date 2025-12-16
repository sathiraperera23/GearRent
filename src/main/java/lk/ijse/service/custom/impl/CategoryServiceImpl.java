package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CategoryDAO;
import lk.ijse.dto.CategoryDTO;
import lk.ijse.entity.Category;
import lk.ijse.service.custom.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO =
            (CategoryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CATEGORY);

    @Override
    public boolean addCategory(CategoryDTO dto) throws Exception {
        return categoryDAO.save(new Category(0, dto.getName(), dto.getDescription()));
    }

    @Override
    public boolean updateCategory(CategoryDTO dto) throws Exception {
        return categoryDAO.update(new Category(dto.getCategoryId(), dto.getName(), dto.getDescription()));
    }

    @Override
    public boolean deleteCategory(int categoryId) throws Exception {
        return categoryDAO.delete(categoryId);
    }

    @Override
    public CategoryDTO searchCategory(int categoryId) throws Exception {
        Category c = categoryDAO.find(categoryId);
        return (c != null) ? new CategoryDTO(c.getCategoryId(), c.getName(), c.getDescription()) : null;
    }

    @Override
    public List<CategoryDTO> getAllCategories() throws Exception {
        List<Category> list = categoryDAO.findAll();
        return list.stream()
                .map(c -> new CategoryDTO(c.getCategoryId(), c.getName(), c.getDescription()))
                .collect(Collectors.toList());
    }
}
