package lk.ijse.controller.category;

import lk.ijse.dto.CategoryDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.CategoryService;

import java.util.List;
import java.util.Scanner;

public class CategoryController {

    private final CategoryService categoryService =
            (CategoryService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.CATEGORY);

    private final Scanner scanner = new Scanner(System.in);

    public void createCategory() {
        try {
            System.out.print("Enter Category Name: ");
            String name = scanner.nextLine();

            System.out.print("Description: ");
            String desc = scanner.nextLine();

            // Match DTO constructor (3 fields only)
            CategoryDTO dto = new CategoryDTO(0, name, desc);

            boolean success = categoryService.addCategory(dto);
            System.out.println(success ? "Category Added!" : "Failed to Add Category.");

        } catch (Exception e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    public void listCategories() {
        try {
            List<CategoryDTO> list = categoryService.getAllCategories();

            System.out.println("\n--- CATEGORY LIST ---");
            for (CategoryDTO c : list) {
                System.out.println(c.getCategoryId() + " | " + c.getName() + " | " + c.getDescription());
            }

        } catch (Exception e) {
            System.out.println("Error listing categories: " + e.getMessage());
        }
    }

    public void updateCategory() {
        try {
            System.out.print("Enter Category ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("New Name: ");
            String name = scanner.nextLine();

            System.out.print("New Description: ");
            String desc = scanner.nextLine();

            // DTO takes only 3 arguments
            CategoryDTO dto = new CategoryDTO(id, name, desc);

            boolean success = categoryService.updateCategory(dto);
            System.out.println(success ? "Category Updated!" : "Update Failed.");

        } catch (Exception e) {
            System.out.println("Error updating category: " + e.getMessage());
        }
    }

    public void deleteCategory() {
        try {
            System.out.print("Enter Category ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean success = categoryService.deleteCategory(id);
            System.out.println(success ? "Category Deleted!" : "Delete Failed.");

        } catch (Exception e) {
            System.out.println("Error deleting category: " + e.getMessage());
        }
    }
}