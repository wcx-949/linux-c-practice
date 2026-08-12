
import java.util.Scanner;

public class lian_main99{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("请投币");
        int amount = in.nextInt();
        System.out.println(amount);
        System.out.println(amount>=180);
        if (amount>=180)
        {
            
        }
        else
        {
            System.out.println("金额不足,请重新投币");
            return;
        }
        
//         打印车票
        System.out.println("****************************");
        System.out.println("**    JAVA城际铁路客票    **");
        System.out.println("**       起点: 北京       **");
        System.out.println("**       终点: 上海       **");
        System.out.println("**         头等舱         **");
        System.out.println("**       票价:180元       **");
        System.out.println("****************************");
//      找零
        if(amount>180){
            System.out.println("找零: "+(amount-180)+"元");
            System.out.println("请收好找零,谢谢");
            System.out.println("金额刚好,请取票");
        }
    }
}
