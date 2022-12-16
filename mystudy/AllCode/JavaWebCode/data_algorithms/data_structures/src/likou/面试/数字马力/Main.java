package likou.面试.数字马力;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Department {
    private String id;
    private String pid;

    // 子部门列表
    private List<Department> subDeps;

    public Department(String id, String pid) {
        this.id = id;
        this.pid = pid;
    }

    public String getId() {
        return id;
    }


    public String getPid() {
        return pid;
    }





    public List<Department> getSubDeps() {
        return subDeps;
    }

    public void setSubDeps(List<Department> subDeps) {
        this.subDeps = subDeps;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", pid=" + pid +
                ", subDeps=" + subDeps +
                '}';
    }
}

public class Main {

    public static void main(String[] args) {
        List<Department> deps = new ArrayList<>(16);
        deps.add(new Department("1", "0"));
        deps.add(new Department("1.1", "1"));
        deps.add(new Department("1.2", "1"));
        deps.add(new Department("2", "0"));
        // 技术部的子部门
        deps.add(new Department("3", "0"));
        deps.add(new Department("1.3" ,   "1"));
        // 技术部-开发部门的子部门
        deps.add(new Department("1.3.1", "1.3"));
        deps.add(new Department("1.3.2", "1.3"));
        // 行政部门的子部门
        deps.add(new Department("1.4", "1.3"));
        deps.add(new Department("1.3.1.1", "1.3.1"));
        System.out.println(buildTree(deps));
    }

    /**
     * 构建树形结构并返回根
     *
     * @param deps 部门的列表
     * @return 根，将整棵树通过subDeps构建
     */
    public static Department buildTree(List<Department> deps) {
        Department department = null;
        for (Department dept : deps) {
            if (dept.getPid() .equals("0") ) {
                department = dept;
                department.setSubDeps(getSubDeps(dept, deps));
                break;
            }
        }
        return department;
    }

    private static List<Department> getSubDeps(Department dept, List<Department> deps) {
        List<Department> otherDeps = deps.stream().filter(item ->! dept.getId() .equals(item.getPid()) ).collect(Collectors.toList());
        List<Department> subDeps = deps.stream().filter(item -> dept.getId() .equals(item.getPid())).map(item -> {
            item.setSubDeps(getSubDeps(item, otherDeps));
            return item;
        }).collect(Collectors.toList());
        return subDeps;
    }

}
