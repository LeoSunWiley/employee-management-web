# Employee Management

## Problem Statement

Employee management provides functions to manage employees.

We need to implement the management system as follows:

- Implement REST web services to provide CRUD endpoints for employees.
- Design a database schema and employee table to store and fetch employee information.
- Provide a web page to interact with REST services via Ajax to manage employees in the UI.

Below is the main UI for employee management.



## Solution

The employee management should structure as follows. We use MVC and tiered design pattern to structure the system.

controller/EmployeeController provides functions as follows:

- Provide operations for employees.
- Delegate to EmployeeVew to display instructions.
- Delegate to EmployeeDao to operate on data-related tasks.

view/EmployeeView provides functions as follows:

- Prompt user to input information
- Display Instructions.

dao/EmployeeDao provides the actual operations on data-related tasks.

- Load from and write to employees to a file named employees.txt
- Use Map object to operate on employees in memory.

```shell
├───src
│   ├───main
│   │   └───java
│   │       └───com
│   │           └───example
│   │               │   Main.java
│   │               │
│   │               ├───controller
│   │               │       EmployeeController.java
│   │               │
│   │               ├───dao
│   │               │       EmployeeDao.java
│   │               │       EmployeeDaoImpl.java
│   │               │
│   │               ├───exception
│   │               │       EmployeeDaoException.java
│   │               │
│   │               ├───model
│   │               │       Employee.java
│   │               │
│   │               └───view
│   │                       EmployeeView.java
│   │                       UserIO.java
│   │                       UserIOConsoleImpl.java
```

### controller package

`EmployeeController` plays the controller role in MVC pattern which has `EmployeeView` and `EmployeeDao` as its dependencies.

```java
public class EmployeeController {

    private final EmployeeView view;
    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeController(EmployeeView view, EmployeeDao employeeDao) {
        this.view = view;
        this.employeeDao = employeeDao;
    }
}
```

Take **listAllEmployees** as an example, controller delegates to employee view to display banner and result info and delegates to employee dao to do query against backend (a file used to store employees).

```java
private void listEmployees() {
    view.listEmployeesBanner();
    List<Employee> employees = employeeDao.getAllEmployees();
    view.displayEmployees(employees);
}
```

### dao package

EmployeeDao as a interface defines CRUD apis that can be used to operate against employees.

```java
public interface EmployeeDao {
    
    List<Employee> getAllEmployees();

    Employee getEmployeeById(int id);

    Employee addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployeeById(int id);
}
```

EmployeeDaoImpl implements these apis which uses a file called employees.txt to do CRUD operations.

```java
@Override
    public Employee addEmployee(Employee employee) {
        count++;
        try {
            loadEmployees();
            employee.setId(count);
            employees.put(count, employee);
            writeEmployee();
            return employee;
        } catch (EmployeeDaoException e) {
            e.printStackTrace();
        }
        return null;
    }
```

Take this addEmployee as an example. First, load employees from file into memory stored as an Map. Then add the newly employee to the map. Finally, rewrite result to file.

### view package

`EmployeeView` has an instance of UserIO as its dependency. UserIO defines the operations related to receive input and display output.

```java
public class EmployeeView {

    private final UserIO io;

    @Autowired
    public EmployeeView(UserIO io) {
        this.io = io;
    }
}
```

`UserIOConsoleImpl` implements UserIO to use Scanner to receive user's input and use console to output info to user.

```java
public class UserIOConsoleImpl implements UserIO {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public int readInt(String prompt) {
        System.out.println(prompt);
        int returnValue = Integer.parseInt(scanner.nextLine());
        return returnValue;
    }
}
```
