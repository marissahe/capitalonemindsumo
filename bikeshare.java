package bikeshare;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class bikeshare {
	public static void main(String[] args) {
		String filename = "metro-bike-share-trip-data.csv";
		BufferedReader br;
		String[][] total = new String[132427][14];
		try {
			br = new BufferedReader(new FileReader(filename));
			String[] top = br.readLine().split(",");
			String line = br.readLine();
			int index = 0;
			
			
			while (line != null) {
				total[index] = Arrays.copyOfRange(line.split(","), 0, 14);
				
				index++;
				line = br.readLine();
			}
			
			ArrayList<String> startStations = new ArrayList<String>();
			ArrayList<Integer> startStationsCount = new ArrayList<Integer>();
			ArrayList<String> endStations = new ArrayList<String>();
			ArrayList<Integer> endStationsCount = new ArrayList<Integer>();
			
			double totDist = 0.0;
			int missed = 0;
			int numWalkUp = 0;
			int numFlex = 0;
			int numMonthly = 0;
			int numStaff = 0;
			
			int[] usersByMonth = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			int[] usersByHour = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};		
					
			for (int i = 0; i < total.length; i++) {
				if (startStations.contains(total[i][4])) {
					int j = getIndex(startStations, total[i][4]);
					startStationsCount.set(j, startStationsCount.get(j)+1);
				}
				
				else {
					startStations.add(total[i][4]);
					startStationsCount.add(1);
				}
				
				
				
				if (endStations.contains(total[i][7])) {
					int j = getIndex(endStations, total[i][7]);
					endStationsCount.set(j, endStationsCount.get(j)+1);
				}
				
				else {
					endStations.add(total[i][7]);
					endStationsCount.add(1);
				}
				
				
				
				if (!(total[i][8].equals("")||total[i][9].equals("")||total[i][5].equals("")||total[i][6].equals(""))) {
					double earthRadius = 6371000; 
	    			double lat2 = Double.parseDouble(total[i][8]);
	    			double lat1 = Double.parseDouble(total[i][5]);
					double long2 = Double.parseDouble(total[i][9]);
					double long1 = Double.parseDouble(total[i][6]);
	    			
					
					double dLat = Math.toRadians(lat2-lat1);
					double dLong = Math.toRadians(long2-long1);
					double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *Math.sin(dLong/2) * Math.sin(dLong/2);
	    			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    			
					totDist+=(earthRadius*c);
					
				}
				
				else {
					missed++;
				}
				
				
				if (total[i][13].equals("Walk-up")) {
					numWalkUp++;
				}
				else if (total[i][13].equals("Flex Pass")) {
					numFlex++;
				}
				else if (total[i][13].equals("Monthly Pass")) {
					numMonthly++;
				}
				else {
					numStaff++;
				}
				
				
				int month = Integer.parseInt(total[i][2].substring(5, 7));
				usersByMonth[month-1] = usersByMonth[month-1]+1;
				
				int hour = Integer.parseInt(total[i][2].substring(11, 13));
				usersByHour[hour] = usersByHour[hour]+1;
			}
			
			int startMax = 0;
			int startMaxIndex = -1;
			for (int i = 0; i < startStationsCount.size(); i++) {
				if (startStationsCount.get(i)>startMax) {
					startMax = startStationsCount.get(i);
					startMaxIndex = i;
				}
			}
			System.out.println("The most used start station was "+startStations.get(startMaxIndex));
			
			int endMax = 0;
			int endMaxIndex = -1;
			for (int i = 0; i < endStationsCount.size(); i++) {
				if (endStationsCount.get(i)>endMax) {
					endMax = endStationsCount.get(i);
					endMaxIndex = i;
				}
			}
			System.out.println("The most used end station was "+endStations.get(endMaxIndex));
			
			System.out.println("The average distance travelled was "+totDist/(total.length-missed)+" meters.");
			
			int regulars = numFlex+numMonthly;
			System.out.println("The number of times a regular user biked was "+(regulars));
			
			System.out.println("Walk-up %:" + numWalkUp);
			System.out.println("Flex %:" + numFlex);
			System.out.println("Monthly %:" + numMonthly);
			System.out.println("Staff Annual %: " + numStaff);
			
			for (int i = 0 ; i < usersByMonth.length; i++) {
				System.out.println("[\'"+i+"\', "+usersByMonth[i]+"],");
			}
			System.out.println("");
			for (int i = 0; i < usersByHour.length; i++) {
				System.out.println("[\'"+i+"\', "+usersByHour[i]+"],");
			}
			
			
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getIndex(ArrayList<String> arr, String item) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).equals(item)) {
				return i;
			}
		}
		return -1;
	}
}
