public class SwingArray {
    public static void main(String[] args) {
        /**
         * 如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。
         * 第一个差（如果存在的话）可能是正数或负数。少于两个元素的序列也是摆动序列。
         *
         * 例如， [1,7,4,9,2,5] 是一个摆动序列，因为差值 (6,-3,5,-7,3) 是正负交替出现的。
         * 相反, [1,4,7,2,5] 和 [1,7,4,5,5] 不是摆动序列，第一个序列是因为它的前两个差值都是正数，
         * 第二个序列是因为它的最后一个差值为零。
         *
         * 给定一个整数序列，返回作为摆动序列的最长子序列的长度。
         * 通过从原始序列中删除一些（也可以不删除）元素来获得子序列，剩下的元素保持其原始顺序。
         *
         * 示例 1:
         *
         * 输入: [1,7,4,9,2,5]
         * 输出: 6
         * 解释: 整个序列均为摆动序列。
         * 示例 2:
         *
         * 输入: [1,17,5,10,13,15,10,5,16,8]
         * 输出: 7
         * 解释: 这个序列包含几个长度为 7 摆动序列，其中一个可为[1,17,10,13,10,16,8]。
         * 示例 3:
         *
         * 输入: [1,2,3,4,5,6,7,8,9]
         * 输出: 2
         *
         * */


    }

    //解法一：暴力搜索
    private int calcute(int[] nums, int index, boolean isUp){
        int maxCount= 0;
        for(int i = index + 1; i < nums.length; i++){
            if((isUp && nums[i] > nums[index]) || (!isUp && nums[i] < nums[index])){
                //从第1,2,3,4...个位置去找
                //找出最大的那个就是正确的结果
//                maxCount = Math.max(maxCount, 1+calcute(nums, i, !isUp));
                //等价写法
                int Count = 1 + calcute(nums, i, !isUp);
                if(maxCount < Count){
                    maxCount = Count;
                }
            }
        }
        return maxCount;
    }
    public int wiggleMaxLength1(int[] nums) {
        if(nums.length < 2) return nums.length;
        return 1 + Math.max(calcute(nums, 0, false), calcute(nums, 0, true));
    }

    //解法二：动态规划
    public int wiggleMaxLength2(int[] nums) {
        if(nums.length < 2) return nums.length;
        int[] up = new int[nums.length]; //up表示以i结尾的最长的上升摆动序列
        int[] down = new int[nums.length];//down表示以i结尾的最长的下降摆动序列
        for(int i= 0; i < nums.length; i++){
            for (int j = 0; j < i; j++){
                if(nums[i] > nums[j]){
                    up[i] = Math.max(up[i],down[j] + 1);
                }else if(nums[i] < nums[j]){
                    down[i] = Math.max(down[i], up[j] + 1);
                }
            }
        }
        return 1 + Math.max(up[nums.length-1],down[nums.length-1]);
    }
    //解法三:线性动态规划
    public int wiggleMaxLength3(int[] nums) {
        if(nums.length < 2) return nums.length;
        int[] up = new int[nums.length];
        int[] down = new int[nums.length];

        for(int i = 1; i < nums.length; i++){
            if(nums[i] > nums[i-1]){
                up[i] = down[i-1] + 1;
                down[i] = down[i-1];
            }else if(nums[i] < nums[i-1]){
                up[i] = up[i-1];
                down[i] = up[i-1] + 1;
            }else{
                up[i] = up[i-1];
                down[i] = down[i-1];
            }
        }
        return 1 + Math.max(up[nums.length-1],down[nums.length-1]);
    }
    //解法四：空间优化的动态规划
    public int wiggleMaxLength4(int[] nums) {
        if(nums.length < 2) return nums.length;
        int up = 0;
        int down = 0;
        for(int i = 1; i < nums.length; i++){
            if(nums[i] > nums[i - 1]){
                up = down + 1;
            }else if(nums[i] < nums[i - 1]){
                down = up + 1;
            }
        }
        return 1 + Math.max(up, down);
    }

    //解法五：贪心算法
    public int wiggleMaxLength5(int[] nums) {
        int diff = 0;
        int preDiff = nums[1] - nums[0];
        int count = preDiff != 0? 2:1;
        for(int i = 2; i < nums.length; i++){
            diff = nums[i] - nums[i-1];
            // 1 2 1 10 11 12 10
            // 1 2 1  ||  2 1 2 => count++
            // 3 2 3 3 2 3
            if((diff < 0 && preDiff >= 0) || (diff > 0 && preDiff <= 0)){
                count++;
                preDiff = diff;
            }
        }
        return count;
    }
}
