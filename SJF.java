package zte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SJF {

	public SJF() {
		int[] requestTimes= {0, 0, 4, 5, 6, 7};
		int[] dorations = {2, 3, 3, 2, 1, 2};
		
		int[] result = runSJF(requestTimes, dorations);
		
		for(int i = 0; i < result.length; i++)
			System.out.println(result[i]);
		
		double waitTime = getWaitTime(requestTimes, result);
		
		System.out.println("avlWaitTime:" + waitTime/requestTimes.length);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SJF();

	}
	
	private int[] runSJF(int[] requestTimes, int[] dorations)
	{
		HashMap<Integer, Integer> all = new HashMap();//设置ID
		for(int i = 0; i < requestTimes.length; i++)
			all.put(i, requestTimes[i]);
		
		HashMap<Integer, Integer> cur = new HashMap();//当前可运行的请求
		int[] startTimes = new int[requestTimes.length];
		int now = 0;
		
		int temp = -1;//正在执行的ID
		for(int i = 0; i < requestTimes.length; i++)
		{
			System.out.println("now:" + now + ",i:" + i + ",cursize:" + cur.size() + 
					",temp:" + temp);
			int countAdd;
			if(temp == -1)
			{
				countAdd = addCur(now, i, requestTimes, dorations, cur);
				temp = getRequest(cur,  dorations);
				startTimes[temp] = now;
				now = now + dorations[temp];
				System.out.println("\n\n");
				i = i + countAdd - 1;
				continue;
			}
			
			cur.remove(temp);
			System.out.println("remove:" + temp);
			if(cur.size() == 0)
				countAdd = addCur(now, i, requestTimes, dorations, cur);
			else
				countAdd = addCur(now, i, requestTimes, dorations, cur);
			temp = getRequest(cur,  dorations);
			startTimes[temp] = now;
			now = now + dorations[temp];//时间过去执行的时间
			i = i + countAdd - 1;
			System.out.println("\n\n");
		}
		cur.remove(temp);
		System.out.println("remove:" + temp);
		System.out.println("cursize:" + cur.size());
		runRest(now, cur, dorations, startTimes);
		
		return startTimes;
	}

	private int getRequest(HashMap<Integer, Integer> cur, int[] dorations)
	{
		int result = -1;
		int least = Integer.MAX_VALUE;
		for(Map.Entry<Integer, Integer> entry:cur.entrySet())
		{
			int key = entry.getKey();
			if(dorations[key] < least)
			{
				least = dorations[key];
				result = key;
			}
		}
		System.out.println("run " + result);
		return result;
	}
	
	private int addCur(int now, int i, int[] requestTimes, int[] dorations, HashMap<Integer, Integer> cur)
	{
		int countAdd = 0;
		
		for(int j = i; j < requestTimes.length; j++)
		{
			if(requestTimes[j] < now)
			{
				cur.put(j, requestTimes[j]);
				countAdd++;
				System.out.println("add " + j);
				continue;
				
			}
			
			if(requestTimes[j] == now)
			{
				cur.put(j, requestTimes[j]);
				countAdd++;
				System.out.println("add " + j);
				continue;
			}
			
			if(requestTimes[j] > now)
			{
				break;
			}
			
		}
		return countAdd;
	}
	
	int getWaitTime(int[] requestTimes, int[] startTimes)
	{
		int result = 0;
		for(int i = 0; i < requestTimes.length; i++)
			result = result + (startTimes[i] - requestTimes[i]);
		
		return result;
	}
	
	private void runRest(int now, HashMap<Integer, Integer> cur, int[] dorations, int[] startTimes)
	{
		if(cur.size() > 0)
		{
			int temp = getRequest(cur, dorations);
			startTimes[temp] = now;
			now = now + dorations[temp];
			System.out.println("now:" + now + ",run:" + temp);
			cur.remove(temp);
			runRest(now, cur, dorations, startTimes);
		}
	}
}
