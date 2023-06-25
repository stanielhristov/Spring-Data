import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Engine implements Runnable{
    private final EntityManager entityManager;
    private final Scanner scanner;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        System.out.println("Please, enter exercise number:");

        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 2 -> exerciseTwo();
            case 3 -> exerciseThree();
            case 4 -> exerciseFour();
            case 5 -> exerciseFive();
            case 6 -> exerciseSix();
            case 7 -> exerciseSeven();
            case 8 -> exerciseEight();
            case 9 -> exerciseNine();
            case 10 -> exerciseTen();
            case 11 -> exerciseEleven();
            case 12 -> exerciseTwelve();
            case 13 -> exerciseThirteen();
            default -> System.out.println("No such exercise!");
        }

    }

    private void exerciseThirteen() {
        System.out.println("Please, enter town: ");
        String townName = scanner.nextLine();
        Town town = entityManager.createQuery("SELECT t FROM Town t WHERE t.name=:p_town", Town.class)
                .setParameter("p_town", townName)
                .getSingleResult();

        setEmployeeAddress(townName);
        entityManager.getTransaction().begin();
        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a WHERE a.town.id=:p_id", Address.class)
                .setParameter("p_id", town.getId())
                .getResultList();
        addresses.forEach(entityManager::remove);

        entityManager.remove(town);
        entityManager.getTransaction().commit();
        System.out.printf("%d address in %s deleted", addresses.size(), townName);
    }

    private void setEmployeeAddress(String townName) {
        String query = "SELECT e FROM Employee AS e WHERE e.address.town.name = '" + townName + "'";

        List<Employee> employeeList =  entityManager.createQuery(query).getResultList();
        entityManager.getTransaction().begin();
        employeeList.forEach(employee -> {
            employee.setAddress(null);
            entityManager.persist(employee);
        });
        entityManager.getTransaction().commit();
    }

    private void exerciseTwelve() {
        List<Object[]> results = entityManager.createQuery("SELECT e.department.name, MAX (e.salary)  FROM Employee e GROUP BY e.department.name HAVING MAX (e.salary)  NOT BETWEEN 30000 AND 70000").getResultList();
        for (Object[] r : results) {
            System.out.println(r[0] + " " + r[1]);
        }
    }

    private void exerciseEleven() {
        System.out.println("Please, enter pattern: ");
        String pattern = scanner.nextLine();
        List<Employee> employeeList = entityManager.createQuery("SELECT e FROM Employee  e WHERE e.firstName LIKE :pattern", Employee.class)
                .setParameter("pattern",pattern.concat("%")).getResultList();

        employeeList.forEach(e -> System.out.printf("%s %s - %s - ($%.2f)%n",
                e.getFirstName(),
                e.getLastName(),
                e.getJobTitle(),
                e.getSalary()));
    }

    private void exerciseTen() {
        entityManager.getTransaction().begin();
        int count = entityManager.createQuery("UPDATE Employee e SET e.salary = e.salary * 1.12 WHERE e.department.id IN :ids")
                .setParameter("ids", Arrays.asList(1, 2, 4, 11)).executeUpdate();
        entityManager.getTransaction().commit();

        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee  e WHERE e.department.id IN :ids", Employee.class)
                .setParameter("ids", Arrays.asList(1, 2, 4, 11)).getResultList();
        employees.forEach(e -> System.out.printf("%s %s ($%.2f)%n", e.getFirstName(), e.getLastName(), e.getSalary()));
    }

    private void exerciseNine() {
        List<Project> projectList = entityManager.createQuery("SELECT p FROM Project p ORDER BY p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultList();

        projectList.stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> {
                    System.out.printf("Project name: %s%n", p.getName());
                    System.out.printf(" \tProject Description: %s%n", p.getDescription());
                    System.out.printf(" \tProject Start Date:%s%n", p.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")));
                    System.out.printf(" \tProject End Date:%s%n", p.getEndDate() == null ? "null" : p.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")));
                });
    }

    private void exerciseEight() {
        System.out.println("Please, enter employee id: ");
        int id = Integer.parseInt(scanner.nextLine());
        Employee employee = entityManager.find(Employee.class, id);

        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());

        employee.getProjects().stream()
                .map(Project::getName)
                .sorted(String::compareTo).forEach(p -> System.out.printf("\t%s%n", p));
    }

    private void exerciseSeven() {
        List<Address> addressesList = entityManager.createQuery("SELECT a FROM Address a ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10).getResultList();

        addressesList.forEach(a -> System.out.printf("%s, %s - %d employees%n",a.getText(),
                a.getTown() == null ? "Unknown": a.getTown().getName(),
                a.getEmployees().size()));
    }

    private void exerciseSix() {
        System.out.println("Please, enter employee last name: ");
        String employeeLastName = scanner.nextLine();
        Employee employee = entityManager.createQuery("SELECT e FROM Employee e WHERE e.lastName=:lName", Employee.class)
                .setParameter("lName", employeeLastName).getSingleResult();
        Address address = createAddress("Vitoshka 15");
        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private Address createAddress(String addressType) {
        Address address = new Address();
        address.setText(addressType);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void exerciseFive() {
        entityManager.createQuery("SELECT e FROM Employee e WHERE e.department.name=:dName ORDER BY e.salary, e.id", Employee.class)
                .setParameter("dName","Research and Development")
                .getResultList().forEach(e -> System.out.printf("%s %s from %s - $%.2f%n",e.getFirstName(),
                        e.getLastName(),
                        e.getDepartment().getName(),
                        e.getSalary()));
    }

    private void exerciseFour() {
        entityManager.createQuery("SELECT e FROM Employee e WHERE e.salary > :minimalSalary", Employee.class)
                .setParameter("minimalSalary", BigDecimal.valueOf(50000L)).getResultList().stream()
                .map(Employee::getFirstName).forEach(System.out::println);

    }

    private void exerciseThree() {
        System.out.println("Please enter employee name: ");
        String name = scanner.nextLine();
        String firstName = name.split("\\s+")[0];
        String lastName = name.split("\\s+")[1];

        List<Employee> employeeList = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.firstName=:fName AND e.lastName=:lName", Employee.class)
                .setParameter("fName", firstName)
                .setParameter("lName", lastName).getResultList();

        System.out.println(employeeList.isEmpty() ? "No" : "Yes");
    }

    private void exerciseTwo() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Query query = entityManager.createQuery("UPDATE Town  t SET t.name = UPPER(t.name) WHERE length(t.name) <=5 ");

        System.out.println("Affected rows: " + query.executeUpdate());
        transaction.commit();

    }
}
