package nl.amis.sdo.jpa.services;


import commonj.sdo.DataObject;
import commonj.sdo.helper.DataFactory;

import commonj.sdo.helper.XSDHelper;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;


import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import nl.amis.sdo.jpa.entities.Departments;
import nl.amis.sdo.jpa.entities.DepartmentsSDO;
import nl.amis.sdo.jpa.entities.Employees;
import nl.amis.sdo.jpa.entities.EmployeesSDO;

import oracle.webservices.annotations.PortableWebService;

@Stateless(name="HrSessionEJB", mappedName = "EjbSdoService-HrSessionEJB")
@PortableWebService(serviceName="HrSessionEJBBeanWS", targetNamespace="/nl.amis.sdo.jpa.services/",
    portName="HrSessionEJBBeanWSSoapHttpPort", endpointInterface="nl.amis.sdo.jpa.services.HrSessionEJB")
public class HrSessionEJBBean implements HrSessionEJB {
    @PersistenceContext(unitName="ModelSDO")
    private EntityManager em;

    public HrSessionEJBBean() {
    }

    public Object queryByRange(String jpqlStmt, int firstResult,
                               int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Departments persistDepartments(Departments departments) {
        em.persist(departments);
        return departments;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Departments mergeDepartments(Departments departments) {
        return em.merge(departments);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeDepartments(Departments departments) {
        departments = em.find(Departments.class, departments.getDepartmentId());
        em.remove(departments);
    }

    /** <code>select o from Departments o</code> */
    public List<Departments> getDepartmentsFindAll() {
        return em.createNamedQuery("Departments.findAll").getResultList();
    }

    /** <code>select o from Departments o where o.departmentId = :deptId</code> */
    public List<Departments> getDepartmentsFindOne(  Long deptId) {
        return em.createNamedQuery("Departments.findOne").setParameter("deptId", deptId).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Employees persistEmployees(Employees employees) {
        em.persist(employees);
        return employees;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Employees mergeEmployees(Employees employees) {
        return em.merge(employees);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeEmployees(Employees employees) {
        employees = em.find(Employees.class, employees.getEmployeeId());
        em.remove(employees);
    }

    /** <code>select o from Employees o</code> */
    public List<Employees> getEmployeesFindAll() {
        return em.createNamedQuery("Employees.findAll").getResultList();
    }

    /** <code>select o from Employees o where o.employeeId = :empId</code> */
    public  List<Employees> getEmployeesFindOne(  Long empId) {
        return em.createNamedQuery("Employees.findOne").setParameter("empId", empId).getResultList();
    }

    public DepartmentsSDO persistDepartmentsSDO(DepartmentsSDO departmentsSDO) throws RuntimeException {
        return persistDepartments(departmentsSDO.toDepartments()).toDepartmentsSDO();
    }

    public DepartmentsSDO mergeDepartmentsSDO(DepartmentsSDO departmentsSDO) throws RuntimeException {
        return mergeDepartments(departmentsSDO.toDepartments()).toDepartmentsSDO();
    }

    public void removeDepartmentsSDO(DepartmentsSDO departmentsSDO) throws RuntimeException {
        removeDepartments(departmentsSDO.toDepartments());
    }

    public List<DepartmentsSDO> getDepartmentsFindAllSDO() throws RuntimeException {
        List<Departments> departments = getDepartmentsFindAll();
        List<DepartmentsSDO> departmentsSDO = new ArrayList<DepartmentsSDO>(departments.size());
        for (Departments departments1 : departments) {
            departmentsSDO.add(departments1.toDepartmentsSDO());
        }
        return departmentsSDO;
    }

    public List<DepartmentsSDO> getDepartmentsFindOneSDO(Long deptId) throws RuntimeException {
        List<Departments> departments = getDepartmentsFindOne(deptId);
        List<DepartmentsSDO> departmentsSDO = new ArrayList<DepartmentsSDO>(departments.size());
        for (Departments departments1 : departments) {
            departmentsSDO.add(departments1.toDepartmentsSDO());
        }
        return departmentsSDO;
    }

    public EmployeesSDO persistEmployeesSDO(EmployeesSDO employeesSDO) throws RuntimeException {
        return persistEmployees(employeesSDO.toEmployees()).toEmployeesSDO();
    }

    public EmployeesSDO mergeEmployeesSDO(EmployeesSDO employeesSDO) throws RuntimeException {
        return mergeEmployees(employeesSDO.toEmployees()).toEmployeesSDO();
    }

    public void removeEmployeesSDO(EmployeesSDO employeesSDO) throws RuntimeException {
        removeEmployees(employeesSDO.toEmployees());
    }

    public List<EmployeesSDO> getEmployeesFindAllSDO() throws RuntimeException {
        List<Employees> employees = getEmployeesFindAll();
        List<EmployeesSDO> employeesSDO = new ArrayList<EmployeesSDO>(employees.size());
        for (Employees employees1 : employees) {
            employeesSDO.add(employees1.toEmployeesSDO());
        }
        return employeesSDO;
    }

    public List<EmployeesSDO> getEmployeesFindOneSDO(Long empId) throws RuntimeException {
        List<Employees> employees = getEmployeesFindOne(empId);
        List<EmployeesSDO> employeesSDO = new ArrayList<EmployeesSDO>(employees.size());
        for (Employees employees1 : employees) {
            employeesSDO.add(employees1.toEmployeesSDO());
        }
        return employeesSDO;
    }
    static {
        synchronized (HrSessionEJBBean.class) {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                XSDHelper.INSTANCE.define(loader.getResourceAsStream("nl/amis/sdo/jpa/entities/EmployeesSDO.xsd"), "nl/amis/sdo/jpa/entities/");
                XSDHelper.INSTANCE.define(loader.getResourceAsStream("nl/amis/sdo/jpa/entities/DepartmentsSDO.xsd"), "nl/amis/sdo/jpa/entities/");
                XSDHelper.INSTANCE.define(loader.getResourceAsStream("nl/amis/sdo/jpa/services/HrSessionEJBBeanWS.xsd"), "nl/amis/sdo/jpa/services/");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
