package cn.mdm.masterui.rewiget;

public class TestJava {
    public static void main(String[] args) {
        int[] nums = {1,4,2,7};
        countPairs(nums,2,6);
    }

    public static int countPairs(int[] nums, int low, int high) {
        int sum = 0;
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ;i < nums.length;i++){
            for(int j = i+1; j < nums.length ;j++){
                int temp = nums[i] ^ nums[j];
                if(low <= temp && temp <= high){
                    sb.append("("+i+","+j+"):nums["+i+"] XOR nums["+j+"] = " + temp + "\n");
                    sum++;
                }
            }
        }
        System.out.print(sb.toString());
        return sum;
    }
}
