package com.mckinsey.ckc.sf.algo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mckinsey.ckc.sf.utils.DistanceBetweenLongLat;

public class Kmeans {

	// Number of Clusters. This metric should be related to the number of points
	private static int NUM_CLUSTERS = 10;
	// Number of Points
	// private int NUM_POINTS = 15;t
	// Min and Max X and Y
	// private static final int MIN_COORDINATE = 0;
	// private static final int MAX_COORDINATE = 10;

	private List<Point> points;
	private List<Cluster> clusters;
	public static HashMap<String, Integer> pointHash = new HashMap<String, Integer>();

	public Kmeans() {
		this.points = new ArrayList<Point>();
		this.clusters = new ArrayList<Cluster>();
	}

	public static void main(String[] args) {

//		JDBCConnection.loadDeptTable();
//		DistanceBetweenLongLat.loadDistance();
//		System.out.println("dept size：" + DataPreparation.deptList.size());
		Kmeans kmeans = new Kmeans();
		kmeans.init();
		kmeans.calculate();
		
		List<Point> resultPoints = new ArrayList<Point>();
		for(Cluster c : kmeans.clusters){
			Point minPoint = null;
			double minDistance = Double.MAX_VALUE;
			Point center = c.centroid;
			for(Point eachPoint : c.points){
				double currentDistance = DistanceBetweenLongLat.GetDistance(eachPoint.getX(), eachPoint.getY(), center.getX(), center.getY());
				if(currentDistance < minDistance){
					minDistance = currentDistance;
					minPoint = eachPoint;
				}
			}
			System.out.println(minPoint.toString());
			resultPoints.add(minPoint);
		}
		

		// send to CSV
		// write to csv
		Writer fileWriter = null;
		// FileWriter fileWriter = null;
		try {
			fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./xuandian.csv"), "UTF-8"));
			// fileWriter = new FileWriter("./161205lingdanSendPosition.csv");
			// CSV file header
			String FILE_HEADER = "longitute,latitute";
			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			// Add a new line separator after the header
			fileWriter.append("\n");

			// Write a new student object list to the CSV file
			for (Point sendPoint : resultPoints) {
				fileWriter.append(String.valueOf(sendPoint.getX()));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(sendPoint.getY()));
				fileWriter.append("\n");
			}
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Initializes the process
	public void init() {
		// load 经纬度坐标
//		points = JDBCConnection.loadSendLongLat();
		// Create Points
		// points =
		// Point.createRandomPoints(MIN_COORDINATE,MAX_COORDINATE,NUM_POINTS);

		// Create Clusters
		// Set Random Centroids
		for (int i = 0; i < NUM_CLUSTERS; i++) {
			Cluster cluster = new Cluster(i);
//			Point centroid = DataPreparation.deptOutfieldPoints.get(i);
//			cluster.setCentroid(centroid);
			clusters.add(cluster);
		}

		// Print Initial state
		plotClusters();
	}

	private void plotClusters() {
		for (int i = 0; i < NUM_CLUSTERS; i++) {
			Cluster c = clusters.get(i);
			c.plotCluster();
		}
	}

	// The process to calculate the K Means, with iterating method.
	public void calculate() {
		boolean finish = false;
		int iteration = 0;

		List<Point> currentCentroids = null;
		;

		// Add in new data, one at a time, recalculating centroids with each new
		// one.
		while (!finish) {
			// Clear cluster state
			clearClusters();

			List<Point> lastCentroids = getCentroids();

			// Assign points to the closer cluster
			assignCluster();

			// Calculate new centroids.
			calculateCentroids();

			iteration++;
			currentCentroids = getCentroids();

			// Calculates total distance between new and old Centroids
			double distance = 0;
			for (int i = 0; i < lastCentroids.size(); i++) {
				distance += DistanceBetweenLongLat.GetDistance(lastCentroids.get(i).getX(), lastCentroids.get(i).getY(),
						currentCentroids.get(i).getX(), currentCentroids.get(i).getY());
				// distance +=
				// Point.distance(lastCentroids.get(i),currentCentroids.get(i));
			}
			System.out.println("#################");
			System.out.println("Iteration: " + iteration);
			System.out.println("Centroid distances: " + distance);
			plotClusters();

			if (distance == 0) {
				finish = true;
			}
		}
	}

	private void clearClusters() {
		for (Cluster cluster : clusters) {
			cluster.clear();
		}
	}

	private List<Point> getCentroids() {
		List<Point> centroids = new ArrayList<Point>(NUM_CLUSTERS);
		for (Cluster cluster : clusters) {
			Point aux = cluster.getCentroid();
			Point point = new Point(aux.getX(), aux.getY());
			centroids.add(point);
		}
		return centroids;
	}

	private void assignCluster() {
		double max = Double.MAX_VALUE;
		double min = max;
		int cluster = 0;
		double distance = 0.0;

		for (Point point : points) {
			if (pointHash.containsKey(point.toString())) {
				cluster = pointHash.get(point.toString());
				point.setCluster(cluster);
				clusters.get(cluster).addPoint(point);
				continue;
			} else {
				min = max;
				for (int i = 0; i < NUM_CLUSTERS; i++) {
					Cluster c = clusters.get(i);
					// distance = Point.distance(point, c.getCentroid());
					distance = DistanceBetweenLongLat.GetDistance(point.getX(), point.getY(), c.getCentroid().getX(),
							c.getCentroid().getY());
					if (distance < min) {
						min = distance;
						cluster = i;
					}
				}
				pointHash.put(point.toString(), cluster);
			}
			point.setCluster(cluster);
			clusters.get(cluster).addPoint(point);
		}
	}

//	private void calculateCentroids() {
//		for (Cluster cluster : clusters) {
//
//			List<Point> list = cluster.getPoints();
//			int n_points = list.size();
//
//			Point centroid = cluster.getCentroid();
//
//			Point minPoint = null;
//			double minDistance = Double.MAX_VALUE;
//			for (for in) {
//				double currentDistance = 0;
//				for (Point pp : list) {
//					currentDistance += DistanceBetweenLongLat.tripleList
//							.get(DistanceBetweenLongLat.calculateHash(p, pp));
//					System.out.println(currentDistance);
//				}
//				if (currentDistance < minDistance) {
//					minDistance = currentDistance;
//					minPoint = p;
//				}
//			}
//
//			if (n_points > 0) {
//				centroid.setX(minPoint.getX());
//				centroid.setY(minPoint.getY());
//			}
//		}
//	}
	
	private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            List<Point> list = cluster.getPoints();
            int n_points = list.size();
            
            for(Point point : list) {
            	sumX += point.getX();
                sumY += point.getY();
            }
            
            Point centroid = cluster.getCentroid();
            if(n_points > 0) {
            	double newX = sumX / n_points;
            	double newY = sumY / n_points;
                centroid.setX(newX);
                centroid.setY(newY);
            }
        }
    }
}
