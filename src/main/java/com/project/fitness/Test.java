package com.project.fitness;

import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        int m = sc.nextInt();
        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        System.out.println(solve(arr, n-1, t, m));
    }

    private static int solve(int[] arr, int i, int t, int m) {
        if(i == 0) {
            if(t == 0 && m % arr[i] == 0) {
                return m / arr[i];
            }
        }

        int take = Integer.MAX_VALUE;
        if(t != 0 && m >= arr[i]) {
            int res = solve(arr, i, t-1, m-arr[i]);
            if(res != Integer.MAX_VALUE) {
                take = 1 + res;
            }
        }
        int not_take = solve(arr, i-1, t, m);

        return Math.min(take, not_take);
    }
}


// 10
// 20
// 3: {2, 2, 3}