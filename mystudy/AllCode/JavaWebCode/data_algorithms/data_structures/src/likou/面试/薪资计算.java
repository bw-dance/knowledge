package likou.面试;


public class 薪资计算 {
    public static void main(String[] args) {
        for (int i = 100000; i <= 400000; i++) {
            if (i % 10000 == 0) {
                //i:年包
                //month:多少薪资
                int month = 13;
                //公积金
                double accumulationFund = 0.12;
                //每月房租，燃气，电费
                int rent = 3000;
                //每月自己生活费
                int livingExpenses = 3000;
                //已交的金额
                int  hasSub =0;
                //工资计算
                computedWages(i, month, accumulationFund, livingExpenses, rent, hasSub);
            }
        }
    }

    private static double computedWages(int allSalary, int month, double accumulationFund, int livingExpenses, int rent, int hasSub) {
        int monthSalary = allSalary / month;
        double insuranceAndFoud = insuranceAndFoud(accumulationFund, monthSalary);
        double tax = tax(allSalary - insuranceAndFoud - (monthSalary * (month - 12)));
        double lifeAndRent = LifeAndRent(livingExpenses, rent);
//       double back = back(allSalary, hasSub);
      double back =0;
        double last = allSalary - insuranceAndFoud - tax - lifeAndRent - back;
        System.out.println("------------" + allSalary + "万-----------------");
        System.out.println("还钱：" + back);
        System.out.println("最后剩余：" + last);
        return last;
    }

    /**
     * 回款
     *
     * @param allSalary
     * @param hasSub
     * @return
     */
    private static double back(int allSalary, int hasSub) {
        int backSalary = allSalary - 180000;
        double sumBack = 48000+ (backSalary > 0 ? backSalary * 0.6 : 0);
        return sumBack < 150000 ? sumBack-hasSub : 150000-hasSub;
    }

    /**
     * 公积金
     *
     * @param accumulationFund
     * @param monthSalary
     * @return
     */
    private static double insuranceAndFoud(double accumulationFund, int monthSalary) {
        double unemploymentInsurance = 0.02;
        double endowmentInsurance = 0.08;
        double medicalInsurance = 0.002;
        double sumInsurance = (accumulationFund + unemploymentInsurance + endowmentInsurance + medicalInsurance) * monthSalary * 12;
        return sumInsurance;
    }


    /**
     * 生活成本和房租
     *
     * @param livingExpenses
     * @param rent
     * @return
     */
    private static double LifeAndRent(int livingExpenses, int rent) {
        return (livingExpenses + rent) * 12;
    }

    /**
     * 税务（工资-五险一金后-60000的部分算税，实际中还会扣除，这是按交最多税算的，税的话拿400000最高产生15,246税）
     * 另外 年薪40万，减完五险一金和60000的部分算税在25万左右，因此算的是0-300000之前的税收，之后的没有计算
     *
     * @param taxSalary
     * @return
     */
    private static double tax(double taxSalary) {
        double tax = 0;
        taxSalary -= 60000;
        if (taxSalary > 0 && taxSalary <= 36000) {
            tax += taxSalary * 0.03;
            return tax;
        } else if (taxSalary > 36000 && taxSalary <= 144000) {
            tax += 36000 * 0.03 + (taxSalary - 36000) * 0.1 - 2520;
            return tax;
        } else if (taxSalary > 144000 && taxSalary <= 300000) {
            tax += 36000 * 0.03 + (144000 - 36000) * 0.1 - 2520 + (taxSalary - 144000) * 0.2 - 16920;
            return tax;
        }
        return tax;
    }
}
