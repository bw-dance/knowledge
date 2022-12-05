package likou.面试.数字马力;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Department {
    private long id;
    private long pid;
    private String name;
    // 子部门列表
    private List<Department> subDeps;

    public Department(long id, long pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public long getId() {
        return id;
    }


    public long getPid() {
        return pid;
    }


    public String getName() {
        return name;
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
                ", name='" + name + '\'' +
                ", subDeps=" + subDeps +
                '}';
    }
}

public class Main {

    public static void main(String[] args) {
        List<Department> deps = new ArrayList<>(16);
        deps.add(new Department(1, 0, "公司根"));
        deps.add(new Department(2, 1, "产品部"));
        deps.add(new Department(3, 1, "技术部"));
        deps.add(new Department(4, 1, "行政部门"));
        // 技术部的子部门
        deps.add(new Department(5, 3, "开发部门"));
        deps.add(new Department(6, 3, "测试部门"));
        // 技术部-开发部门的子部门
        deps.add(new Department(7, 5, "前端开发"));
        deps.add(new Department(8, 5, "后端开发"));
        // 行政部门的子部门
        deps.add(new Department(9, 4, "安保部门"));
        deps.add(new Department(10, 4, "后勤部门"));
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
            if (dept.getPid() == 0) {
                department = dept;
                department.setSubDeps(getSubDeps(dept, deps));
                break;
            }
        }
        return department;
    }

    private static List<Department> getSubDeps(Department dept, List<Department> deps) {
        List<Department> otherDeps = deps.stream().filter(item -> dept.getId() != item.getPid()).collect(Collectors.toList());
        List<Department> subDeps = deps.stream().filter(item -> dept.getId() == item.getPid()).map(item -> {
            item.setSubDeps(getSubDeps(item, otherDeps));
            return item;
        }).collect(Collectors.toList());
        return subDeps;
    }

}
