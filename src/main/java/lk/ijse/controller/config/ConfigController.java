package lk.ijse.controller.config;

import lk.ijse.dto.ConfigDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.ConfigService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConfigController {

    private final ConfigService configService =
            (ConfigService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.CONFIG);

    private final Scanner scanner = new Scanner(System.in);

    public void createConfig() {
        try {
            System.out.print("Late Fee per Day: ");
            BigDecimal lateFee = new BigDecimal(scanner.nextLine());

            System.out.print("Max Deposit: ");
            BigDecimal maxDeposit = new BigDecimal(scanner.nextLine());

            System.out.print("Regular Discount: ");
            BigDecimal regular = new BigDecimal(scanner.nextLine());

            System.out.print("Silver Discount: ");
            BigDecimal silver = new BigDecimal(scanner.nextLine());

            System.out.print("Gold Discount: ");
            BigDecimal gold = new BigDecimal(scanner.nextLine());

            ConfigDTO dto = new ConfigDTO(
                    lateFee,
                    maxDeposit,
                    regular,
                    silver,
                    gold
            );
// ID is auto-generated → DO NOT set it here
            boolean success = configService.saveConfig(dto);
            System.out.println(success ? "Config added!" : "Failed to add config.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listConfigs() {
        try {
            List<ConfigDTO> list = configService.getAllConfigs();
            list.forEach(c -> System.out.println(
                    c.getConfigId() + " | " + c.getLateFeePerDay() + " | " + c.getMaxDeposit() +
                            " | " + c.getRegularDiscount() + " | " + c.getSilverDiscount() +
                            " | " + c.getGoldDiscount()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateConfig() {
        try {
            System.out.print("Enter Config ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Late Fee per Day: ");
            BigDecimal lateFee = new BigDecimal(scanner.nextLine());

            System.out.print("Max Deposit: ");
            BigDecimal maxDeposit = new BigDecimal(scanner.nextLine());

            System.out.print("Regular Discount: ");
            BigDecimal regular = new BigDecimal(scanner.nextLine());

            System.out.print("Silver Discount: ");
            BigDecimal silver = new BigDecimal(scanner.nextLine());

            System.out.print("Gold Discount: ");
            BigDecimal gold = new BigDecimal(scanner.nextLine());

            ConfigDTO dto = new ConfigDTO(
                    lateFee,
                    maxDeposit,
                    regular,
                    silver,
                    gold
            );
            dto.setConfigId(id);   // set ID via setter

            boolean success = configService.updateConfig(dto);
            System.out.println(success ? "Config updated!" : "Update failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteConfig() {
        try {
            System.out.print("Enter Config ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean success = configService.deleteConfig(id);
            System.out.println(success ? "Config deleted!" : "Delete failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
