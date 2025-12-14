package lk.ijse.controller.branch;

import lk.ijse.dto.BranchDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.BranchService;

import java.util.List;
import java.util.Scanner;

public class BranchController {

    private final BranchService branchService =
            (BranchService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.BRANCH);

    private final Scanner scanner = new Scanner(System.in);

    public void createBranch() {
        try {
            System.out.print("Branch Code: ");
            String code = scanner.nextLine();

            System.out.print("Branch Name: ");
            String name = scanner.nextLine();

            System.out.print("Address: ");
            String address = scanner.nextLine();

            System.out.print("Contact: ");
            String contact = scanner.nextLine();

            BranchDTO branchDTO = new BranchDTO(0, code, name, address, contact, null);
            boolean success = branchService.addBranch(branchDTO);

            System.out.println(success ? "Branch Added!" : "Failed to Add Branch.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listBranches() {
        try {
            List<BranchDTO> list = branchService.getAllBranches();
            list.forEach(b -> System.out.println(
                    b.getBranchId() + " | " + b.getCode() + " | " + b.getName() + " | " + b.getAddress() + " | " + b.getContact()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBranch() {
        try {
            System.out.print("Enter Branch ID to Update: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("New Code: ");
            String code = scanner.nextLine();

            System.out.print("New Name: ");
            String name = scanner.nextLine();

            System.out.print("New Address: ");
            String address = scanner.nextLine();

            System.out.print("New Contact: ");
            String contact = scanner.nextLine();

            BranchDTO branchDTO = new BranchDTO(id, code, name, address, contact, null);
            boolean success = branchService.updateBranch(branchDTO);

            System.out.println(success ? "Branch Updated!" : "Update Failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBranch() {
        try {
            System.out.print("Enter Branch ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean success = branchService.deleteBranch(id);
            System.out.println(success ? "Branch Deleted!" : "Delete Failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}